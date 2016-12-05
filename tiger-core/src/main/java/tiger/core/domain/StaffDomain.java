/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.List;

/**
 * 后台用户模型
 *
 * @author HuPeng
 * @version v 0.1 2015年10月19日 下午4:04:01 HuPeng Exp $
 */
public class StaffDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 6192379115981548061L;

    private Long accountId;

    private String username;

    @JsonIgnore
    private String password;

    // 旧密码, 仅用于重置密码
    @CopyIgnore
    @JsonIgnore
    private String oldPassword;

    private Boolean isDel = false;

    private List<RoleDomain> roles;

    private List<PermissionDomain> permissions;

    /**
     * Getter method for property <tt>accountId</tt>.
     *
     * @return property value of accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Setter method for property <tt>accountId</tt>.
     *
     * @param accountId value to be assigned to property accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Getter method for property <tt>username</tt>.
     *
     * @return property value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for property <tt>username</tt>.
     *
     * @param username value to be assigned to property username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for property <tt>password</tt>.
     *
     * @return property value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for property <tt>password</tt>.
     *
     * @param password value to be assigned to property password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for property <tt>roles</tt>.
     *
     * @return property value of roles
     */
    public List<RoleDomain> getRoles() {
        return roles;
    }

    /**
     * Setter method for property <tt>roles</tt>.
     *
     * @param roles value to be assigned to property roles
     */
    public void setRoles(List<RoleDomain> roles) {
        this.roles = roles;
    }

    /**
     * Getter method for property <tt>permissions</tt>.
     *
     * @return property value of permissions
     */
    public List<PermissionDomain> getPermissions() {
        return permissions;
    }

    /**
     * Setter method for property <tt>permissions</tt>.
     *
     * @param permissions value to be assigned to property permissions
     */
    public void setPermissions(List<PermissionDomain> permissions) {
        this.permissions = permissions;
    }

    /**
     * Getter for property 'oldPassword'.
     *
     * @return Value for property 'oldPassword'.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Setter for property 'oldPassword'.
     *
     * @param oldPassword Value to set for property 'oldPassword'.
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Getter for property 'del'.
     *
     * @return Value for property 'del'.
     */
    public Boolean getIsDel() {
        return isDel;
    }

    /**
     * Setter for property 'del'.
     *
     * @param del Value to set for property 'del'.
     */
    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
}
