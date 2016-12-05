/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.loan;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.attach.support.AttachRelateManager;
import tiger.biz.loan.support.LoanPawnManager;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.AttachRelateDomain;
import tiger.core.domain.LoanPawnDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.attach.AttachRelateForm;
import tiger.web.api.form.loan.LoanPawnCreateForm;
import tiger.web.api.form.loan.LoanPawnUpdateForm;

import javax.validation.Valid;

/**
 * 抵押物信息接口
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 1:13:22 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
public class LoanPawnController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanPawnController.class);

    @Autowired
    private LoanPawnManager loanPawnManager;

    @Autowired
    private AttachRelateManager attachRelateManager;

    /**
     * 录入一个抵押物信息
     * ~ 抵押物用于关联信息 + 附件
     *
     * @return 返回抵押物信息
     */
    @RequestMapping(value = "/loan/pawn", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    public BaseResult<LoanPawnDomain> createLoanPawn(@RequestBody @Valid LoanPawnCreateForm loanPawnCreateForm,
                                                     BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("创建抵押物，收到参数[" + loanPawnCreateForm + "]");
        }

        LoanPawnDomain loanPawnDomain = loanPawnCreateForm.convert2Domain();
        // 检查抵押物类型是否为空
        if (loanPawnDomain.getLoanPawnTypeEnum() == null) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
        }
        // 为抵押物增加account id
        loanPawnDomain.setAccountId(currentAccount().getId());
        // 为抵押物设置当前workspace id
        loanPawnDomain.setWorkspaceId(currentWorkspaceId());

        if (logger.isInfoEnabled()) {
            logger.info("获取到创建抵押物对象:[" + loanPawnDomain + "]");
        }
        return new BaseResult<>(loanPawnManager.create(loanPawnDomain));
    }

    /**
     * 通过id获取贷款抵押物信息, 包括 基本参数 及 附件信息
     *
     * @param pawnId
     * @return
     */
    @RequestMapping(value = "/loan/pawn/{pawnId}", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public BaseResult<LoanPawnDomain> readByPawnId(@PathVariable("pawnId") long pawnId) {
        if (logger.isInfoEnabled()) {
            logger.info("获取抵押物信息，收到参数:[" + pawnId + "]");
        }

        // 如果没有 VIEW_LOAN_ALL 权限, 则检查是否为贷款抵押物的所有者
        if (!isPermitted(PermissionEnum.VIEW_LOAN_ALL)) {
            checkIsPawnOwner(pawnId, currentAccount().getId());
        }

        return new BaseResult<>(loanPawnManager.readLoanPawnById(pawnId, currentAccount().getId()));
    }

    /**
     * 更新抵押物信息, 不支持在此更新附件信息
     */
    @RequestMapping(value = "/loan/pawn/{id}", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    public BaseResult<Boolean> updateLoanPawn(@PathVariable("id") long pawnId,
                                              @RequestBody @Valid LoanPawnUpdateForm loanPawnUpdateForm,
                                              BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("用户[" + currentAccount().getId() + "] 更新抵押物信息，收到参数:[" + loanPawnUpdateForm + "]");
        }

        // 如果没有 UPDATE_LOAN_ALL 权限, 则检查是否为贷款抵押物的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_ALL)) {
            checkIsPawnOwner(pawnId, currentAccount().getId());
        }

        LoanPawnDomain loanPawnDomain = loanPawnUpdateForm.convert2Domain();
        loanPawnDomain.setId(pawnId);
        loanPawnDomain.setAccountId(currentAccount().getId());
        loanPawnDomain.setWorkspaceId(currentWorkspaceId());

        return loanPawnManager.update(loanPawnDomain);
    }

    /*
     * 单独添加抵押物附件
     */
    @RequestMapping(value = "/loan/pawn/{id}/attach", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    public BaseResult<Boolean> addAttachToLoanPawn(@PathVariable("id") long pawnId,
                                                   @RequestBody @Valid AttachRelateForm loanPawnAttachForm,
                                                   BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("为抵押物[" + pawnId + "] 添加附件，收到参数:[" + loanPawnAttachForm + "]");
        }

        // 如果没有 UPDATE_LOAN_ALL 权限, 则检查是否为贷款抵押物的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_ALL)) {
            checkIsPawnOwner(pawnId, currentAccount().getId());
        }

        AttachRelateDomain attachDomain = loanPawnAttachForm.convert2Domain();
        attachDomain.setBizType(AttachBizTypeEnum.LOAN_PAWN);
        attachDomain.setSubjectId(pawnId);

        if (logger.isInfoEnabled()) {
            logger.info("为抵押物[" + pawnId + "] 添加附件，收到参数:[" + attachDomain + "]");
        }

        return attachRelateManager.relateAttach(attachDomain, currentAccount().getId());
    }

    /**
     * 单独删除抵押物附件
     */
    @RequestMapping(value = "/loan/pawn/{id}/attach/{attachId}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    public BaseResult<Boolean> deleteAttachFromLoanPawn(@PathVariable("id") long pawnId,
                                                        @PathVariable("attachId") long attachId) {
        if (logger.isInfoEnabled()) {
            logger.info("为抵押物[" + pawnId + "] 删除附件 [" + attachId + "]");
        }

        // 如果没有 UPDATE_LOAN_ALL 权限, 则检查是否为贷款抵押物的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_ALL)) {
            checkIsPawnOwner(pawnId, currentAccount().getId());
        }

        AttachRelateDomain attachDomain = new AttachRelateDomain();
        attachDomain.setBizType(AttachBizTypeEnum.LOAN_PAWN);
        attachDomain.setAttachId(attachId);
        attachDomain.setSubjectId(pawnId);

        if (logger.isInfoEnabled()) {
            logger.info("为抵押物[" + pawnId + "] 删除附件，收到参数:[" + attachDomain + "]");
        }

        return attachRelateManager.deRelateAttach(attachDomain);
    }

    // ~ private method

    /**
     * 检查accountId是否为pawnId的所有者
     *
     * @param pawnId
     * @param accountId
     */
    private void checkIsPawnOwner(long pawnId, long accountId) {
        if (!loanPawnManager.isOwner(pawnId, accountId)) {
            logger.error("[" + accountId + "] 不是贷款抵押物 [" + pawnId + "] 的所有者, 没有权限继续操作");
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }
}
