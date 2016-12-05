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
import tiger.biz.loan.support.LoanPayModifyManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.loan.LoanModificationForm;

import javax.validation.Valid;

/**
 * 贷款异常修正类接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:06 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
public class LoanPayModifyController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanPayModifyController.class);

    @Autowired
    private LoanManager loanManager;

    @Autowired
    private LoanPayModifyManager loanPayModifyManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 提交贷款履约记录修正
     *
     * @return
     */
    @RequestMapping(value = "/loan/{id}/bill/modification", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MODIFICATION_MEMBER, PermissionEnum.UPDATE_LOAN_MODIFICATION_ALL})
    @LoanValidCheck
    public BaseResult<LoanRecordDomain> modifyLoanBill(@RequestBody @Valid LoanModificationForm loanModificationForm,
                                                       BindingResult bindingResult,
                                                       @PathVariable("id") Long loanId) {
        Long currentAccountId = currentAccount().getId();
        Long currentWrokspaceId = currentWorkspaceId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 创建一个对贷款[" +
                    loanId + " ]异常修正 [" + loanModificationForm + "]");
        }

        checkIsOwner(loanId);

        LoanRecordDomain loanRecordDomain = loanModificationForm.convert2Domain();
        loanRecordDomain.setLoanId(loanId);
        loanRecordDomain.setWorkspaceId(currentWrokspaceId);

        BaseResult<LoanRecordDomain> modifyResult = loanPayModifyManager.createModification(loanRecordDomain);

        // 发送消息到kafka
        kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWrokspaceId, currentAccountId, loanId, MessageTypeEnum.ADD_MODIFICATION, null));

        return modifyResult;
    }

    /**
     * 删除一个修正记录
     *
     * @return
     */
    @RequestMapping(value = "/loan/{id}/bill/modification/{modificationId}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MODIFICATION_MEMBER, PermissionEnum.UPDATE_LOAN_MODIFICATION_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> deleteModification(@PathVariable("id") Long loanId,
                                                  @PathVariable("modificationId") Long modificationId) {
        Long currentAccountId = currentAccount().getId();
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 删除一个贷款相关修正 [" + modificationId + "]");
        }

        checkIsOwner(loanId);

        BaseResult<Boolean> deleteResult = loanPayModifyManager.deleteModification(loanId, modificationId);

        // 如果删除成功, 则添加动态
        if (deleteResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, loanId, MessageTypeEnum.DELETE_MODIFICATION, null));
        }

        return deleteResult;
    }

    /**
     * 更新修正记录
     *
     * @return
     */
    @RequestMapping(value = "/loan/{id}/bill/modification/{modificationId}", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MODIFICATION_MEMBER, PermissionEnum.UPDATE_LOAN_MODIFICATION_ALL})
    @LoanValidCheck
    public BaseResult<LoanRecordDomain> updateModification(@PathVariable("id") Long loanId,
                                                           @PathVariable("modificationId") Long modificationId,
                                                           @RequestBody @Valid LoanModificationForm loanModificationForm,
                                                           BindingResult bindingResult) {
        Long currentAccountId = currentAccount().getId();

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccountId + "] 更新一个对贷款[" +
                    loanId + " ]异常修正 [" + loanModificationForm + "]");
        }

        checkIsOwner(loanId);

        LoanRecordDomain recordDomain = loanModificationForm.convert2Domain();
        recordDomain.setLoanId(loanId);
        recordDomain.setId(modificationId);
        recordDomain.setWorkspaceId(currentWorkspaceId());

        BaseResult<LoanRecordDomain> updateResult = loanPayModifyManager.updateModification(recordDomain);

        // 发送消息到kafka
        kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, loanId, MessageTypeEnum.UPDATE_MODIFICATION, null));

        return updateResult;
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
