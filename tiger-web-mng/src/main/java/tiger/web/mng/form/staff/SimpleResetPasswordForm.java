/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.mng.form.staff;

import tiger.common.util.StringUtil;
import tiger.core.domain.StaffDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 4:31 PM yiliang.gyl Exp $
 */
public class SimpleResetPasswordForm extends BaseForm implements FormInterface {

    @NotNull(message = "原密码不能为空")
    @Size(min = 1, max = 128, message = "原密码长度应在1到128之间")
    private String oldPassword;

    /**
     * 忘记密码后的，重置的密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 1, max = 128, message = "密码长度应在1到128之间")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    @Size(min = 1, max = 128, message = "确认密码长度应在1到128之间")
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public StaffDomain convert2Domain() {
        if (!StringUtil.equals(this.password, this.confirmPassword)) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
        }
        StaffDomain domain = new StaffDomain();
        domain.setPassword(this.password);
        // 设置旧密码
        domain.setOldPassword(this.oldPassword);
        return domain;
    }
}
