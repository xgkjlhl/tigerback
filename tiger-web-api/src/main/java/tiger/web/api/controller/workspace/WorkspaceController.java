/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.controller.workspace;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.*;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.workspace.WorkspaceForm;
import tiger.web.api.form.workspace.WorkspaceLoanSettingForm;

import javax.validation.Valid;

/**
 * 工作空间管理API
 *
 * @author alfred_yuan
 * @version v 0.1 15:48 alfred_yuan Exp $
 */
@RestController
@RequestMapping("/workspace")
@ResponseBody
public class WorkspaceController extends BaseController {

    private Logger logger = Logger.getLogger(WorkspaceController.class);

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 创建一个团队工作空间
     *
     * @param createForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @LoginRequired
    public BaseResult<WorkspaceDomain> createGroupWorkspace(@RequestBody @Valid WorkspaceForm createForm,
                                                            BindingResult bindingResult) {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 创建工作空间, 参数为 [" + createForm + "]");
        }

        // 设置参数
        WorkspaceDomain workspaceDomain = createForm.convert2Domain();
        workspaceDomain.setOwnerId(currentAccountId);
        workspaceDomain.setType(WorkSpaceTypeEnum.TEAM);
        workspaceDomain.setIsVerified(false);

        return workspaceManager.create(workspaceDomain);
    }

    /**
     * 更新一个团队工作空间
     *
     * @param updateForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_WORKSPACE_INFO})
    public BaseResult<WorkspaceDomain> updateWorkspace(@RequestBody @Valid WorkspaceForm updateForm,
                                                       BindingResult bindingResult) {
        logger.info(currentAccount().getAccountWorkspaceDomains());
        Long workspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 更新工作空间 [" + workspaceId + "], 参数为 [" + workspaceId + "]");
        }

        // 检查workspaceId是否存在
        workspaceManager.checkAndGetWorkspace(workspaceId);

        WorkspaceDomain workspaceDomain = updateForm.convert2Domain();
        workspaceDomain.setId(workspaceId);

        return workspaceManager.update(workspaceDomain);
    }

    /**
     * 删除团队工作空间
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @Permission(role = {RoleEnum.OWNER}, permission = {PermissionEnum.DELETE_WORKSPACE})
    public BaseResult<Boolean> deleteGroupWorkspace() {

        Long workspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 删除工作空间, 参数为 [" + workspaceId + "]");
        }

        workspaceManager.checkAndGetGroupWorkspace(workspaceId);

        return workspaceManager.delete(workspaceId);
    }

    /**
     * 团队工作空间转让
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/transfer/{accountId}", method = RequestMethod.PUT)
    @Permission(role = {RoleEnum.OWNER}, permission = {PermissionEnum.UPDATE_WORKSPACE_TRANSFER_OWNER})
    public BaseResult<Boolean> transferGroupWorkspace(@PathVariable("accountId") Long accountId) {
        Long workspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 转让工作空间 [" + workspaceId + "] 给 [" + accountId + "]");
        }

        workspaceManager.checkAndGetGroupWorkspace(workspaceId);

        return workspaceManager.transferGroupWorkspace(workspaceId, currentAccount().getId(), accountId);
    }

    /**
     * 更新团队贷款设置
     *
     * @return
     */
    @RequestMapping(value = "/setting/loan", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_WORKSPACE_CONFIGURATION})
    public BaseResult<Boolean> updateWorkspaceLoanSetting(@Valid @RequestBody WorkspaceLoanSettingForm settingForm,
                                                          BindingResult bindingResult) {
        Long workspaceId = currentWorkspaceId();
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "]更新工作空间 [" + workspaceId + "]的贷款设置, 收到的参数为: [" + settingForm + "]");
        }

        workspaceManager.checkAndGetWorkspace(workspaceId);
        if (!(isValidSetting(settingForm))) {
            logger.error("Workspace " + workspaceId + "提交的贷款设置有误: [ " + settingForm + " ]");
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }
        WorkspaceDomain workspaceDomain = settingForm.convert2Domain();
        workspaceDomain.setId(workspaceId);

        Boolean result = workspaceManager.updateLoanSetting(workspaceDomain);
        if (result) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.WORKSPACE, new NotificationBasicDomain(workspaceId, currentAccount().getId(), null, MessageTypeEnum.UPDATE_LOAN_SETTING, null));
        }

        return new BaseResult<>(result);
    }

    /**
     * 获取工作组基本信息
     *
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<WorkspaceDomain> getWorkspace(@PathVariable("id") Long workspaceId) {
        AccountDomain currentAccount = currentAccount();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount.getId() + "] 获取工作空间列表 [" + workspaceId + "] 的基本信息");
        }

        return workspaceManager.getJoinedWorkspaceById(workspaceId, currentAccount);
    }

    /**
     * 工作组成员退出团队
     *
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @LoginRequired
    public BaseResult<Boolean> quitWorkspace(@PathVariable("id") Long workspaceId) {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 退出工作空间 [" + workspaceId + "]");
        }

        return workspaceManager.quit(workspaceId, currentAccountId);
    }

    /**
     * 检查当前用户是否能够新建团队工作空间
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<Boolean> checkCreateWorkspaceAbility() {
        return new BaseResult<>(workspaceManager.canCreateWorkspace(currentAccount().getId()));
    }

    /**
     * 判断是否是正确的设置
     *
     * @param settingForm
     * @return
     */
    private boolean isValidSetting(WorkspaceLoanSettingForm settingForm) {
        if (settingForm.getBadLoanDay() < settingForm.getOverDueDay()) {
            return false;
        }
        return true;
    }
}
