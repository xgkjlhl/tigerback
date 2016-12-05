/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.user;

import tiger.core.domain.AccountResetPwdDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhangbin
 * @version v0.1 2015/9/20 9:49
 */
public class ResetPasswordForm extends BaseForm implements FormInterface {

    /**
     * 手机号码
     */
    @NotNull(message = "手机号码不能为空")
    @Size(min = 1, max = 32, message = "手机长度应在1到32之间")
    private String mobile;

    /**
     * 短信验证码
     */
    @NotNull
    private String code;

    /**
     * 忘记密码后的，重置的密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 1, max = 128, message = "密码长度应在1到128之间")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public AccountResetPwdDomain convert2Domain() {

        AccountResetPwdDomain account = new AccountResetPwdDomain();
        account.setMobile(this.mobile);
        account.setPassword(this.password);

        return account;
    }
}
