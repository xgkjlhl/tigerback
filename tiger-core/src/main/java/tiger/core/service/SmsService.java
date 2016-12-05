/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.SmsVerifyCodeTypeEnum;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.CustomerMsgScheduleAddDomian;
import tiger.core.domain.CustomerMsgScheduleDomain;

import java.util.Date;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/6 17:27
 */
public interface SmsService {

    /**
     * 插入要发送到mobile类型为type的验证码code到数据库
     *
     * @param mobile
     * @param code
     * @param typeEnum
     * @param accountId
     * @return
     */
    boolean addSmsVerifyCode(String mobile, String code, SmsVerifyCodeTypeEnum typeEnum, Long accountId);

    /**
     * 发送验证码code到手机mobile
     *
     * @param mobile
     * @param code
     * @return
     */
    boolean sendVerifyCode(String mobile, String code);

    boolean sendSmsWithContent(String mobile, String content);

    boolean sendSms(String mobile, Long smsTplId);


    /**
     * 从数据库中获取最后一次生成的验证码
     */
    String getMsgCodeFromDB(String mobile, SmsVerifyCodeTypeEnum typeEnum);

    /**
     * 将发送到mobile的code验证码设置为过期
     *
     * @param mobile
     * @param typeEnum
     * @return
     */
    boolean setExpired(String mobile, SmsVerifyCodeTypeEnum typeEnum);

    List<CustomerMsgScheduleDomain> getAllSchedule(Long senderId);

    boolean deleteMsgSchedule(Long id, Long senderId);

    int addSchedule(CustomerMsgScheduleAddDomian domian);

    int addSchedules(List<CustomerMsgScheduleAddDomian> domians);

    /**
     * 给客户发送短信
     *
     * @param msgDomains
     * @return
     */
    List<CustomerMsgDomain> multiSendSms(final List<CustomerMsgDomain> msgDomains);

    /**
     * 插入给客户发送的短信记录
     *
     * @param msgDomains
     * @return
     */
    int insertCustomerMsgs(List<CustomerMsgDomain> msgDomains);

    /**
     * 获取指定时间段内未发送的定时短信
     *
     * @param fromTime
     * @param toTime
     * @return
     */
    List<CustomerMsgScheduleDomain> getTosendScheduleSmsBetweenTime(Date fromTime, Date toTime);

    /**
     * 获取accountId在day这一天获取到的类型为smsVerifyCodeTypeEnum的验证码个数
     * @param accountId
     * @param smsVerifyCodeTypeEnum
     * @param day
     * @return
     */
    int countVerifySmsesInOneDayByAccountId(Long accountId, SmsVerifyCodeTypeEnum smsVerifyCodeTypeEnum, Date day);

    /**
     * 获取mobil在day这一天获取到的类型为smsVerifyCodeTypeEnum的验证码个数
     * @param mobil
     * @param smsVerifyCodeTypeEnum
     * @param day
     * @return
     */
    int countVerifySmsesInOneDayByMobile(String mobile, SmsVerifyCodeTypeEnum smsVerifyCodeTypeEnum, Date day);
}
