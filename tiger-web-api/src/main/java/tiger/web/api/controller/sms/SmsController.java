/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.sms.support.SmsManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.enums.SmsVerifyCodeTypeEnum;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.dal.query.SmsQuery;
import tiger.common.util.PhoneUtil;
import tiger.core.base.BaseResult;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.CustomerMsgScheduleDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.CustomerService;
import tiger.core.service.SmsService;
import tiger.core.service.SystemParamService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.sms.SendSmsForm;
import tiger.web.api.form.sms.SmsVerifyForm;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @version v0.1 2015/10/6 1:58
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/sms")
public class SmsController extends BaseController {

    @Autowired
    SystemParamService systemParamService;

    @Autowired
    SmsManager smsManager;

    @Autowired
    SmsService smsService;

    @Autowired
    CustomerService customerService;

    /**
     * 发短信（包括定时短信和即时短信）
     *
     * @param form
     * @param result
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<?> sendMsg(@RequestBody @Valid SendSmsForm form, BindingResult result) {
        if (!form.getAgreeLiscense()) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
        //即时发送短信
        if (form.getDateToSendMsg() == null) {
            List<CustomerMsgDomain> domains = smsManager.multiSendMsg(this.currentAccount(), form.getCustomerIds(), form.getMsgTplId());
            return new BaseResult<>(domains);
        } else {
            int count = smsManager.addSchedule(this.currentAccount(), form.getMsgTplId(), form.getCustomerIds(), form.getDateToSendMsg());
            return new BaseResult<>(count);
        }

    }

    /**
     * 获取所有短信模板并分类
     *
     * @return
     */
    @RequestMapping(value = "/templates", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<List> getMsgTemplate() {

        return new BaseResult<>(smsManager.getAllMsgTpl());
    }

    /**
     * 查看所有发过的短信，包括失败的
     *
     * @param query
     * @param result
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult showHistory(@Valid SmsQuery query, BindingResult result) {
        if (result.hasErrors()) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL);
        }
        AccountDomain account = this.currentAccount();
        query.setSenderId(account.getId());

        return smsManager.getAllCustomerMsg(query);
    }

    /**
     * 重新发送失败的短信
     *
     * @return
     */
    @RequestMapping(value = "/history/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> sendAgain(@PathVariable("id") long id) {
        AccountDomain domain = this.currentAccount();
        boolean result = smsManager.sendMsgAgain(domain.getId(), id);

        return new BaseResult<>(result);
    }

    /**
     * 获取所有的定时短信
     *
     * @return
     */
    @RequestMapping(value = "/scheduler", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<List> getallSheduler() {
        AccountDomain domain = this.currentAccount();
        if (domain == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL);
        }
        List<CustomerMsgScheduleDomain> domains = smsService.getAllSchedule(domain.getId());

        return new BaseResult<>(domains);
    }

    /**
     * 立即发送定时短信
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/scheduler/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> snedSheduler(@PathVariable("id") Long id) {
        AccountDomain domain = this.currentAccount();
        boolean result = smsManager.sendScheduler(domain.getId(), id);
        return new BaseResult<>(result);
    }

    /**
     * 取消定时短信
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/scheduler/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> deleteSheduler(@PathVariable Long id) {
        AccountDomain domain = this.currentAccount();
        if (domain == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_EXCEPTION);
        }

        boolean result = smsService.deleteMsgSchedule(id, domain.getId());
        return new BaseResult<>(result);
    }

    /**
     * 发送验证码给指定手机
     */
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    @LoginRequired
    @ResponseBody
    public BaseResult<Boolean> sendVerifyCode(SmsVerifyForm verifyForm,
                                              BindingResult bindingResult) {
        if (!PhoneUtil.isValidMobile(verifyForm.getPhoneNum())) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE.getCode(), "手机号不合法");
        }

        final SmsVerifyCodeTypeEnum codeType = SmsVerifyCodeTypeEnum.getEnumByCode(verifyForm.getSmsBiz());

        if (codeType == null) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE.getCode(), "不支持的短信关联业务");
        }
        switch (codeType) {
            case REGISTER:
                checkSmsOption(SmsVerifyCodeTypeEnum.REGISTER, SystemConstants.SMS_REGISTER_OPTION,
                        SystemConstants.SMS_REGISTER_LIMIT, "您今日注册验证码短信额度已经用光,请明日再次尝试");
                break;
            case RESET_MOBILE:
                checkSmsOption(SmsVerifyCodeTypeEnum.RESET_MOBILE, SystemConstants.SMS_RESET_MOBILE_OPTION,
                        SystemConstants.SMS_RESET_MOBILE_LIMIT, "您今日绑定新手机号注册验证码短信额度已经用光,请明日再次尝试");
                break;
            case RESET_PASSWORD:
                checkSmsOption(SmsVerifyCodeTypeEnum.RESET_PASSWORD, SystemConstants.SMS_RESET_PASSWORD_OPTION,
                        SystemConstants.SMS_RESET_PASSWORD_OPTION, "您今日重置密码验证码短信额度已经用光,请明日再次尝试");
                break;
            default:
                return new  BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE.getCode(), "不支持的短信关联业务");
        }
        //发送短信到指定的号码
        return new BaseResult<>(smsManager.sendVerifyCode(verifyForm.getPhoneNum(),
                codeType.getCode(), currentAccount().getId()));
    }

    /**
     * 检查Sms的系统限制条件
     * @param type
     * @param optionSystemParam
     * @param limitSystemParam
     * @param message
     */
    private void checkSmsOption(SmsVerifyCodeTypeEnum type, String optionSystemParam, String limitSystemParam, String message) {
        int smsCount = smsService.countVerifySmsesInOneDayByAccountId(currentAccount().getId(), type, new Date());
        Map<String, String> smsOption = systemParamService.getParamsByType(SystemParamTypeEnum.ACCOUNT_SMS_OPTION);
        if (Boolean.valueOf(smsOption.get(optionSystemParam))) {
            if (smsCount >= Integer.valueOf(smsOption.get(limitSystemParam))) {
                throw new TigerException(ErrorCodeEnum.BIZ_REQUEST_EXCEEDED, message);
            }
        }
    }
}
