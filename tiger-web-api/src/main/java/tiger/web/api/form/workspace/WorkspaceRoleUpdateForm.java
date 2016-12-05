/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.form.workspace;

import tiger.common.dal.enums.RoleEnum;
import tiger.core.domain.workspace.AccountWorkspaceDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 17:13 alfred_yuan Exp $
 */
public class WorkspaceRoleUpdateForm extends BaseForm implements FormInterface {

    @Size(min = 1, message = "角色列表不能为空")
    @NotNull(message = "角色列表不能为空")
    private List<RoleEnum> roles;

    public List<RoleEnum> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEnum> roles) {
        this.roles = roles;
    }

    @Override
    public AccountWorkspaceDomain convert2Domain() {
        AccountWorkspaceDomain accountWorkspaceDomain = new AccountWorkspaceDomain();
        accountWorkspaceDomain.setRoles(roles);
        return accountWorkspaceDomain;
    }
}
