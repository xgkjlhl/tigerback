/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.sms;

import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;

/**
 * 发送sms给指定手机的表单
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:35 PM yiliang.gyl Exp $
 */
public class SmsVerifyForm extends BaseForm {

    @NotNull(message = "业务参数不能为空")
    private String smsBiz;

    @NotNull(message = "手机号不能为空")
    private String phoneNum;

    public String getSmsBiz() {
        return smsBiz;
    }

    public void setSmsBiz(String smsBiz) {
        this.smsBiz = smsBiz;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
