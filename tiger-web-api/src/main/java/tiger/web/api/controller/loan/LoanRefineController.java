/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.loan;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.loan.support.LoanManager;
import tiger.biz.loan.support.LoanRefineManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRefinementLimitDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.loan.LoanRefineForm;

import javax.validation.Valid;
import java.util.List;

/**
 * 合同调整接口
 *
 * @author alfred_yuan
 * @version ${ID}: v 0.1 13:06 alfred_yuan Exp $
 */
@RestController
@ResponseBody
public class LoanRefineController extends BaseController {
    private final Logger logger = Logger.getLogger(LoanRefineController.class);

    @Autowired
    private LoanRefineManager loanRefineManager;

    @Autowired
    private LoanManager loanManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 获取贷款合同调整的限制条件
     *
     * @param loanId
     * @return
     */
    @RequestMapping(value = "loan/{id}/refinement", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_REFINEMENT_MEMBER, PermissionEnum.UPDATE_LOAN_REFINEMENT_ALL})
    @LoanValidCheck
    public BaseResult<LoanRefinementLimitDomain> getLoanRefinementLimit(@PathVariable("id") long loanId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 获取合同调整限制,参数为: [" + loanId + "]");
        }

        checkIsOwner(loanId);

        LoanRefinementLimitDomain limitDomain = loanRefineManager.getLoanRefinementLimit(loanId);

        if (logger.isInfoEnabled()) {
            logger.info("获取到合同调整限制: [" + limitDomain + "]");
        }

        return new BaseResult<>(limitDomain);
    }

    /**
     * 贷款合同调整
     *
     * @param loanId
     * @param refineForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "loan/{id}/refinement", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_REFINEMENT_MEMBER, PermissionEnum.UPDATE_LOAN_REFINEMENT_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> refineLoan(@PathVariable("id") long loanId,
                                          @Valid @RequestBody LoanRefineForm refineForm,
                                          BindingResult bindingResult) {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 进行合同调整,参数为: [ loanId: [" + loanId + "], refineForm: [" + refineForm + "]]");
        }

        checkIsOwner(loanId);

        LoanDomain refineLoan = refineForm.convert2Domain();
        refineLoan.setId(loanId);
        refineLoan.setWorkspaceId(currentWorkspaceId());

        Boolean result = loanRefineManager.refineLoan(refineLoan);

        if (result) {
            if (logger.isInfoEnabled()) {
                logger.info("用户 [" + currentAccount().getId() + "] 进行合同调整成功");
                // 发送消息到kafka
                kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, loanId, MessageTypeEnum.LOAN_REFINE, null));
            }
        } else {
            logger.error("用户 [" + currentAccount().getId() + "] 进行合同调整失败,参数为: [ loanId: [" + loanId + "], refineForm: [" + refineForm + "]]");
        }

        return new BaseResult<>(result);
    }

    /**
     * 获取合同调整后还款明细（预览）
     *
     * @param loanId
     * @param refineForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "loan/{id}/refinement/payItems", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_REFINEMENT_MEMBER, PermissionEnum.UPDATE_LOAN_REFINEMENT_ALL})
    @LoanValidCheck
    public BaseResult<List<LoanRecordDomain>> previewLoanRefinePayItems(@PathVariable("id") long loanId,
                                                                        @Valid @RequestBody LoanRefineForm refineForm,
                                                                        BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 查看合同调整后还款账单,参数为: [ loanId: [" + loanId + "], refineForm: [" + refineForm + "]]");
        }

        checkIsOwner(loanId);

        LoanDomain refineLoan = refineForm.convert2Domain();
        refineLoan.setId(loanId);

        List<LoanRecordDomain> payItems = loanRefineManager.getPreviewRefinePayItems(refineLoan);

        if (logger.isInfoEnabled()) {
            logger.info("获取到合同调整后还款账单: [" + payItems + "]");
        }

        return new BaseResult<>(payItems);
    }

    // ~ Private Method

    /**
     * 当前用户是否能够操作贷款项目:
     * 1. 拥有UPDATE_LOAN_MODIFICATION_ALL 权限 -> 切面控制
     * 2. 是loanId的所有者
     *
     * @param loanId
     */
    public void checkIsOwner(Long loanId) {
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_MODIFICATION_ALL)) {
            loanManager.checkIsOwner(loanId, currentAccount().getId());
        }
    }
}
