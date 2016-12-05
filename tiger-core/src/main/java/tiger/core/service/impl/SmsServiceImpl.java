/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.CustomerMsgDO;
import tiger.common.dal.dataobject.CustomerMsgScheduleDO;
import tiger.common.dal.dataobject.MsgTplDO;
import tiger.common.dal.dataobject.SmsVerifyCodeDO;
import tiger.common.dal.dataobject.example.CustomerMsgScheduleExample;
import tiger.common.dal.dataobject.example.MsgTplExample;
import tiger.common.dal.dataobject.example.SmsVerifyCodeExample;
import tiger.common.dal.dto.MsgScheduleCustomerDTO;
import tiger.common.dal.enums.SmsStatusEnum;
import tiger.common.dal.enums.SmsVerifyCodeTypeEnum;
import tiger.common.dal.persistence.CustomerMsgMapper;
import tiger.common.dal.persistence.CustomerMsgScheduleMapper;
import tiger.common.dal.persistence.MsgTplMapper;
import tiger.common.dal.persistence.SmsVerifyCodeMapper;
import tiger.common.util.BeanUtil;
import tiger.common.util.DateUtil;
import tiger.common.util.component.sms.SmsComponent;
import tiger.common.util.component.sms.SmsResponse;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.CustomerMsgScheduleAddDomian;
import tiger.core.domain.CustomerMsgScheduleDomain;
import tiger.core.domain.convert.CustomerMsgConverter;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.SmsService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/6 17:38
 */
@Service
public class SmsServiceImpl implements SmsService {
    private static final int FIRST_INDEX = 0;
    @Autowired
    MsgTplMapper msgTplMapper;
    @Autowired
    SmsVerifyCodeMapper smsVerifyCodeMapper;
    @Autowired
    CustomerMsgScheduleMapper scheduleMapper;
    @Autowired
    CustomerMsgMapper customerMsgMapper;
    private Logger logger = Logger.getLogger(SmsServiceImpl.class);

    @Override
    public boolean addSmsVerifyCode(String mobile, String code, SmsVerifyCodeTypeEnum typeEnum, Long accountId) {
        SmsVerifyCodeDO sms = new SmsVerifyCodeDO();

        sms.setExpireTime(DateUtil.addMinutes(new Date(), 15));//15分钟
        sms.setMobile(mobile);
        sms.setCode(code);
        sms.setSmsType(typeEnum.getCode());
        sms.setIsActive(true);
        sms.setAccountId(accountId);

        return smsVerifyCodeMapper.insert(sms) > 0;
    }

    @Override
    public boolean sendVerifyCode(String mobile, String code) {
        boolean result = SmsComponent.sendVerifyCode(mobile, code);
        return result;
    }

    @Override
    public boolean sendSmsWithContent(String mobile, String content) {
        boolean result = SmsComponent.SendSms(mobile, content);
        return result;
    }

    @Override
    public boolean sendSms(String mobile, Long smsTplId) {
        String content = this.getSmsTplByTplId(smsTplId);
        if (content == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "根据短信模板ID查找失败");
        }
        return SmsComponent.SendSms(mobile, content);
    }


    /**
     * @see SmsService#insertCustomerMsgs(List)
     */
    @Override
    public List<CustomerMsgDomain> multiSendSms(List<CustomerMsgDomain> msgDomains) {
        StringBuilder mobileBuilder = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        for (CustomerMsgDomain msg : msgDomains) {
            contentBuilder.append(",");
            contentBuilder.append(msg.getContent());
            mobileBuilder.append(",");
            mobileBuilder.append(msg.getCustomerMobile());
        }
        List<SmsResponse> list = SmsComponent.multiSendSms(mobileBuilder.substring(1), contentBuilder.substring(1));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode() != 0) {
                //失败的
                msgDomains.get(i).setStatus(SmsStatusEnum.SENT_FAILED);
            } else {
                //成功的
                msgDomains.get(i).setStatus(SmsStatusEnum.SENT_SUCCESS);
            }
            msgDomains.get(i).setSendTime(new Timestamp(System.currentTimeMillis()));
        }
        return msgDomains;
    }

    /**
     * 根据手机号码取得短信验证码内同
     *
     * @param mobile
     * @return
     */
    @Override
    public String getMsgCodeFromDB(String mobile, SmsVerifyCodeTypeEnum typeEnum) {
        Date now = new Date();

        SmsVerifyCodeExample example = new SmsVerifyCodeExample();
        SmsVerifyCodeExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        criteria.andCreateTimeLessThan(now);
        criteria.andExpireTimeGreaterThan(now);
        criteria.andIsActiveEqualTo(true);
        criteria.andSmsTypeEqualTo(typeEnum.getCode());
        example.setOrderByClause("expire_time desc");

        List<SmsVerifyCodeDO> verifyCodeList = smsVerifyCodeMapper.selectByExample(example);


        if (CollectionUtils.isEmpty(verifyCodeList)) {
            throw new TigerException(ErrorCodeEnum.BIZ_VERY_CODE, "验证码已过期");
        }
        String verifyCode = verifyCodeList.get(FIRST_INDEX).getCode();

        return verifyCode;
    }

    /**
     * 把一个号码的一个类型的所有短信设为失效
     *
     * @param mobile
     * @param typeEnum
     * @return
     */
    @Override
    public boolean setExpired(String mobile, SmsVerifyCodeTypeEnum typeEnum) {
        SmsVerifyCodeDO verifyCodeDO = new SmsVerifyCodeDO();
        verifyCodeDO.setIsActive(false);

        SmsVerifyCodeExample example = new SmsVerifyCodeExample();
        SmsVerifyCodeExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile.trim());
        criteria.andSmsTypeEqualTo(typeEnum.getCode());

        int count = smsVerifyCodeMapper.updateByExampleSelective(verifyCodeDO, example);
        return (count > 0);
    }

    /**
     * 获取所有的模板
     *
     * @return
     */
    @Override
    public List<CustomerMsgScheduleDomain> getAllSchedule(Long senderId) {
        CustomerMsgScheduleExample example = new CustomerMsgScheduleExample();
        CustomerMsgScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andSenderIdEqualTo(senderId).andStatusEqualTo(SmsStatusEnum.TO_SENT.getCode());


        List<MsgScheduleCustomerDTO> dtos = scheduleMapper.selectScheduleByExample(example);
        List<CustomerMsgScheduleDomain> domains = new ArrayList<>(dtos.size());
        for (MsgScheduleCustomerDTO dto : dtos) {
            CustomerMsgScheduleDomain domain = new CustomerMsgScheduleDomain();
            BeanUtil.copyPropertiesWithIgnores(dto, domain);
            domains.add(domain);
        }

        return domains;
    }

    @Override
    public boolean deleteMsgSchedule(Long id, Long senderId) {
        CustomerMsgScheduleExample example = new CustomerMsgScheduleExample();
        CustomerMsgScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andSenderIdEqualTo(senderId);
        criteria.andIdEqualTo(id);
        //只有未发送状态的才能转化为取消状态
        criteria.andStatusEqualTo(SmsStatusEnum.TO_SENT.getCode());

        CustomerMsgScheduleDO msgScheduleDO = new CustomerMsgScheduleDO();
        msgScheduleDO.setStatus(SmsStatusEnum.CANCELED.getCode());
        msgScheduleDO.setId(id);

        int count = scheduleMapper.updateByExampleSelective(msgScheduleDO, example);

        return (count == 1);
    }

    /**
     * 插入一条新纪录
     *
     * @param domian
     * @return
     */
    @Override
    public int addSchedule(CustomerMsgScheduleAddDomian domian) {
        CustomerMsgScheduleDO msgScheduleDO = new CustomerMsgScheduleDO();
        BeanUtil.copyPropertiesWithIgnores(domian, msgScheduleDO);

        int count = scheduleMapper.insert(msgScheduleDO);
        return count;
    }

    @Override
    public int addSchedules(List<CustomerMsgScheduleAddDomian> domians) {
        List<CustomerMsgScheduleDO> msgSchedules = new ArrayList<>(domians.size());

        for (CustomerMsgScheduleAddDomian domian : domians) {
            CustomerMsgScheduleDO msgScheduleDO = new CustomerMsgScheduleDO();
            BeanUtil.copyPropertiesWithIgnores(domian, msgScheduleDO);
            msgSchedules.add(msgScheduleDO);
        }
        return scheduleMapper.insertList(msgSchedules);
    }

    /**
     * @see SmsService#insertCustomerMsgs(List)
     */
    @Override
    public int insertCustomerMsgs(List<CustomerMsgDomain> msgDomains) {
        List<CustomerMsgDO> customerMsgDOs = CustomerMsgConverter.conver2Do(msgDomains);
        if (customerMsgDOs.size() == 0) {
            return 0;
        }
        return customerMsgMapper.insertList(customerMsgDOs);
    }

    /**
     * 根基短信模板的Id查找模板内容（注意默认是静态模板，即直接把该内容发送给客户）
     *
     * @param tplId
     * @return
     */
    private String getSmsTplByTplId(Long tplId) {
        MsgTplExample example = new MsgTplExample();
        MsgTplExample.Criteria criteria = example.createCriteria();
        criteria.andTplIdEqualTo(tplId);

        List<MsgTplDO> list = msgTplMapper.selectByExample(example);
        if (list.size() == 1) {
            String content = list.get(0).getContent();
            return content;
        }
        return null;
    }

    /**
     * 获取指定时间段内未发送的定时短信
     *
     * @see SmsService#getTosendScheduleSmsBetweenTime(Date, Date)
     */
    @Override
    public List<CustomerMsgScheduleDomain> getTosendScheduleSmsBetweenTime(Date fromTime, Date toTime) {
        List<CustomerMsgScheduleDomain> msgScheduleDomains = new ArrayList<CustomerMsgScheduleDomain>();
        ;
        CustomerMsgScheduleExample example = new CustomerMsgScheduleExample();
        CustomerMsgScheduleExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(SmsStatusEnum.TO_SENT.getCode());
        criteria.andSendTimeBetween(fromTime, toTime);
        List<CustomerMsgScheduleDO> msgScheduleDOs = scheduleMapper.selectByExample(example);

        for (CustomerMsgScheduleDO msgScheduleDO : msgScheduleDOs) {
            CustomerMsgScheduleDomain domain = new CustomerMsgScheduleDomain();
            BeanUtil.copyPropertiesWithIgnores(msgScheduleDO, domain);
            msgScheduleDomains.add(domain);
        }

        return msgScheduleDomains;
    }

    @Override
    public int countVerifySmsesInOneDayByAccountId(Long accountId, SmsVerifyCodeTypeEnum smsVerifyCodeTypeEnum, Date day) {
        if (accountId == null || smsVerifyCodeTypeEnum == null || day == null) {
            return -1;
        }

        Date begin = DateUtil.getDayBegin(day);
        Date end = DateUtil.getDayEnd(day);

        return smsVerifyCodeMapper.countVerifySmses(accountId, smsVerifyCodeTypeEnum.getCode(), begin, end);
    }

    @Override
    public int countVerifySmsesInOneDayByMobile(String mobile, SmsVerifyCodeTypeEnum smsVerifyCodeTypeEnum, Date day) {
        if (mobile == null || smsVerifyCodeTypeEnum == null || day == null) {
            return -1;
        }

        SmsVerifyCodeExample example = new SmsVerifyCodeExample();
        SmsVerifyCodeExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile).andSmsTypeEqualTo(smsVerifyCodeTypeEnum.getCode())
                .andCreateTimeBetween(DateUtil.getDayBegin(day), DateUtil.getDayEnd(day));

        return smsVerifyCodeMapper.countByExample(example);
    }
}
