/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.workspace;

import tiger.common.dal.enums.RoleEnum;
import tiger.core.domain.AccountDomain;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 17:17 alfred_yuan Exp $
 */
public class WorkspaceMemberDetailDomain {

    private AccountDomain account;

    private List<RoleEnum> roles = new ArrayList<>();

    /**
     * 加入时间
     */
    private Date joinDate;

    public AccountDomain getAccount() {
        return account;
    }

    public void setAccount(AccountDomain account) {
        this.account = account;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
