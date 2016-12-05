/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.mng.form.staff;

import tiger.core.base.BaseDomain;
import tiger.core.domain.StaffDomain;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author alfred_yuan
 * @version v 0.1 00:34 alfred_yuan Exp $
 */
public class StaffCreateForm extends BaseForm implements FormInterface {

    @NotNull
    @Size(min = 1, max = 32, message = "管理员名称应在1到32位之间")
    private String username;

    @NotNull
    @Size(min = 1, max = 128, message = "密码长度应在1到128之间")
    private String password;

    /**
     * Getter for property 'password'.
     *
     * @return Value for property 'password'.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for property 'password'.
     *
     * @param password Value to set for property 'password'.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for property 'username'.
     *
     * @return Value for property 'username'.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for property 'username'.
     *
     * @param username Value to set for property 'username'.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public StaffDomain convert2Domain() {
        StaffDomain staff = new StaffDomain();

        staff.setUsername(username);
        staff.setPassword(password);

        return staff;
    }
}
