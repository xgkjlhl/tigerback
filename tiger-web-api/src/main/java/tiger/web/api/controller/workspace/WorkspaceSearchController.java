/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.controller.workspace;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiger.biz.constants.BizConstants;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.RoleEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.domain.workspace.WorkspaceMemberListDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.web.api.controller.base.BaseController;

import java.util.Arrays;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:18 alfred_yuan Exp $
 */
@RestController
@ResponseBody
public class WorkspaceSearchController extends BaseController {
    private Logger logger = Logger.getLogger(WorkspaceSearchController.class);

    @Autowired
    private WorkspaceManager workspaceManager;

    /**
     * 获取用户所有的工作空间
     *
     * @return
     */
    @RequestMapping(value = "/workspaces", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<List<WorkspaceDomain>> getWorkspaces() {
        AccountDomain currentAccount = currentAccount();

        // 检验账户设置
        if (BizConstants.ACCOUNT_STATUS.equals(currentAccount.getStatus())) {
            throw new TigerException(ErrorCodeEnum.ACCOUNT_NO_LOGIN);
        }

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount.getId() + "] 获取他的工作空间列表");
        }

        return new BaseResult<>(workspaceManager.getUserWorkspaces(currentAccount));
    }

    /**
     * 获取工作空间成员列表
     * ~ 只要是团队成员都可以获取
     *
     * @return
     */
    @RequestMapping(value = "/workspace/members", method = RequestMethod.GET)
    @Permission
    public BaseResult<List<WorkspaceMemberListDomain>> getWorkspaceMembers() {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 获取工作空间 [" + currentWorkspaceId() + "] 成员列表");
        }

        workspaceManager.checkAndGetWorkspace(currentWorkspaceId());

        return workspaceManager.getWorkspaceMembers(currentWorkspaceId());
    }

    @RequestMapping(value = "/workspace/members", method = RequestMethod.GET, params = "operation=count")
    @Permission
    public BaseResult<Integer> countWorkspaceMembers() {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 获取工作空间 [" + currentWorkspaceId() + "] 成员数量");
        }

        workspaceManager.checkAndGetWorkspace(currentWorkspaceId());

        return workspaceManager.countWorkspaceMembers(currentWorkspaceId());
    }

    /**
     * 获取可用角色列表
     *
     * @return
     */
    @RequestMapping(value = "/workspace/roles", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<List<RoleEnum>> getWorkspaceRoles() {
        List<RoleEnum> roles = Arrays.asList(RoleEnum.values());
        return new BaseResult<>(roles);
    }

}
