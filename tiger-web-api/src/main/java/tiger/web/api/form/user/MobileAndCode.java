/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.user;

import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;

/**
 * @author zhangbin
 * @version v0.1 2015/9/19 23:15
 */
public class MobileAndCode extends BaseForm {

    /**
     * 用户手机号码
     */
    @NotNull(message = "手机号码不能为空")
    private String phoneNum;

    /**
     * 短信验证码
     */
    @NotNull(message = "短信验证码不能为空")
    private String msgCode;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

}
