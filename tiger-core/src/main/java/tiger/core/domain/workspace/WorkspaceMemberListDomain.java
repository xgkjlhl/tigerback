/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.workspace;

import tiger.common.dal.enums.RoleEnum;
import tiger.core.domain.AccountBaseDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 5:16 PM yiliang.gyl Exp $
 */
public class WorkspaceMemberListDomain {

    private AccountBaseDomain account;

    private List<RoleEnum> roles = new ArrayList<>();

    public AccountBaseDomain getAccount() {
        return account;
    }

    public void setAccount(AccountBaseDomain account) {
        this.account = account;
    }

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }
}
