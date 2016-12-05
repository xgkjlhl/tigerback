/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.controller.workspace;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.component.support.CachingManager;
import tiger.biz.workspace.support.WorkspaceInvitationManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.enums.RoleEnum;
import tiger.common.dal.query.WorkspaceVerifyQuery;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.workspace.AccountWorkspaceDomain;
import tiger.core.domain.workspace.WorkspaceInvitationDomain;
import tiger.core.domain.workspace.WorkspaceMemberDetailDomain;
import tiger.core.domain.workspace.WorkspaceMemberVerifyDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.workspace.WorkspaceRoleUpdateForm;
import tiger.web.api.form.workspace.WorkspaceVerifyForm;

import javax.validation.Valid;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 13:40 alfred_yuan Exp $
 */
@RestController
@RequestMapping("/workspace")
@ResponseBody
public class WorkspaceMemberController extends BaseController {

    private Logger logger = Logger.getLogger(WorkspaceMemberController.class);

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private WorkspaceInvitationManager workspaceInvitationManager;

    @Autowired
    private CachingManager cachingManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 生成用户邀请链接
     *
     * @return
     */
    @RequestMapping(value = "/member/invitation", method = RequestMethod.GET)
    @Permission(role = {RoleEnum.ADMIN, RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_INVITE_MEMBER})
    public BaseResult<WorkspaceInvitationDomain> generateInvitation() {
        return new BaseResult<>(workspaceManager.inviteUser(currentAccount().getId(), currentWorkspaceId()));
    }

    /**
     * 通过分享的 code 提交加入团队申请
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/member/invitation/joinByCode", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<?> joinWorkspaceByInvitationCode(@RequestParam("code") String code) {
        Boolean result = workspaceInvitationManager.applyJoinWorkSpace(code, currentAccount().getId());
        if (result) {
            return new BaseResult<>(result);
        } else {
            return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL.getCode(), "加入团队失败");
        }

    }

    /**
     * 获取用户邀请具体信息
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/member/invitation/info", method = RequestMethod.GET)
    public BaseResult<WorkspaceInvitationDomain> getInvitationInfo(@RequestParam("code") String code) {
        return new BaseResult<>(workspaceManager.getInvitationInfo(code));
    }

    /**
     * 获取团队成员单人信息
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/member/{accountId}", method = RequestMethod.GET)
    @Permission
    public BaseResult<WorkspaceMemberDetailDomain> getWorkspaceMember(@PathVariable("accountId") Long accountId) {
        Long workspaceId = currentWorkspaceId();
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 获取工作空间 [" + workspaceId + "] 的成员 [" + accountId + "] 信息");
        }

        return workspaceManager.getWorkspaceMember(workspaceId, accountId);
    }

    /**
     * 添加 accountId 加入团队工作空间 workspaceId
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/member/{accountId}", method = RequestMethod.PUT)
    @Permission(role = {RoleEnum.ADMIN, RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_INVITE_MEMBER})
    public BaseResult<Boolean> addWorkspaceMember(@PathVariable("accountId") Long accountId) {
        Long workspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 添加 [" + accountId + "] 到团队工作空间 [" + workspaceId + "] 的成员列表中");
        }

        workspaceManager.checkAndGetGroupWorkspace(workspaceId);

        return workspaceManager.addGroupWorkspaceMember(workspaceId, accountId);
    }

    /**
     * 从团队工作空间 workspaceId 中移除 accountId 成员
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/member/{accountId}", method = RequestMethod.DELETE)
    @Permission(role = {RoleEnum.ADMIN, RoleEnum.OWNER}, permission = {PermissionEnum.DELETE_WORKSPACE_MEMBER})
    public BaseResult<Boolean> deleteWorkspaceMember(@PathVariable("accountId") Long accountId) {
        Long workspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 删除 [" + accountId + "] 在团队工作空间 [" + workspaceId + "] 中的成员角色");
        }

        workspaceManager.checkAndGetGroupWorkspace(workspaceId);

        BaseResult<Boolean> deleteWorkspaceMember = workspaceManager.deleteGroupWorkspaceMember(workspaceId, accountId);

        if (deleteWorkspaceMember.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.WORKSPACE, new NotificationBasicDomain(workspaceId, accountId, null, MessageTypeEnum.UNFOLLOW_WORKSPACE, null));
        }

        return deleteWorkspaceMember;
    }

    /**
     * 获取用户在一个空间的角色列表
     *
     * @return
     */
    @RequestMapping(value = "/member/roles", method = RequestMethod.GET)
    @Permission
    public BaseResult<AccountWorkspaceDomain> getUserAuthInWorkspace() {
        Long workspaceId = currentWorkspaceId();
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("[" + currentAccountId + "] 开始查找其在 [" + workspaceId + "] 下的角色列表及权限信息");
        }

        return new BaseResult<>(workspaceManager.getUserAuthInWorkspace(currentAccountId, workspaceId));
    }


    /**
     * 从团队工作空间 workspaceId 中更新 accountId 的角色
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/member/{accountId}/role", method = RequestMethod.PUT)
    @Permission(role = {RoleEnum.OWNER, RoleEnum.ADMIN}, permission = {PermissionEnum.UPDATE_WORKSPACE_MEMBER_ROLE})
    public BaseResult<Boolean> updateWorkspaceRoles(@PathVariable("accountId") Long accountId,
                                                    @RequestBody @Valid WorkspaceRoleUpdateForm roleUpdateForm,
                                                    BindingResult bindingResult) {
        Long workspaceId = currentWorkspaceId();
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 更新 [" + accountId + "] 在团队工作空间 [" + workspaceId + "] 中的成员角色");
        }

        workspaceManager.checkAndGetGroupWorkspace(workspaceId);
        AccountWorkspaceDomain accountWorkspaceDomain = roleUpdateForm.convert2Domain();
        accountWorkspaceDomain.setWorkspaceId(workspaceId);
        accountWorkspaceDomain.setAccountId(accountId);
        return new BaseResult(workspaceManager.updateMemberRole(accountWorkspaceDomain));
    }

    /**
     * 获取工作空间中邀请成员审核信息
     *
     * @return
     */
    @RequestMapping(value = "/member/verifications", method = RequestMethod.GET, params = "scope=count")
    @Permission(role = {RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_INVITE_MEMBER})
    public BaseResult<Integer> countVerificationsOfWorkspace() {
        return new BaseResult<>(workspaceInvitationManager.countVerificationsOfWorkspace(currentWorkspaceId()));
    }

    /**
     * 获取工作空间中成员加入审核列表
     *
     * @return
     */
    @RequestMapping(value = "/member/verifications", method = RequestMethod.GET)
    @Permission(role = {RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_INVITE_MEMBER})
    public PageResult<List<WorkspaceMemberVerifyDomain>> getVerificationsOfWorkspace(@Valid WorkspaceVerifyQuery query,
                                                                                     BindingResult bindingResult) {
        query.setWorkspaceId(currentWorkspaceId());
        return workspaceInvitationManager.getWorkspaceVerifications(query);
    }

    /**
     * 审核邀请成员添加信息
     *
     * @return
     */
    @RequestMapping(value = "/member/verification", method = RequestMethod.PUT)
    @Permission(role = {RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_INVITE_MEMBER})
    public BaseResult<Boolean> countVerificationOfWorkspace(@RequestBody @Valid WorkspaceVerifyForm verifyForm,
                                                            BindingResult bindingResult) {
        return workspaceInvitationManager.verifyMemberInvitation(verifyForm.getVerifyId(), verifyForm.getIsPermit());
    }


}
