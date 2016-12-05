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
import tiger.biz.loan.support.LoanNotificationSettingManager;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.PermissionEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.LoanSmsSettingDomain;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.loan.LoanSmsSettingForm;

import javax.validation.Valid;
import java.util.List;

/**
 * 贷款通知服务接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 6:59 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
public class LoanNotificationController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanNotificationController.class);

    @Autowired
    private LoanNotificationSettingManager loanNotificationSettingManager;

    @Autowired
    private LoanManager loanManager;

    /**
     * 获取所有短信历史记录
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/loan/{id}/sms", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<List<CustomerMsgDomain>> getHistorySms(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        if (!isPermitted(PermissionEnum.VIEW_LOAN_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        return loanNotificationSettingManager.listHistorySms(id);
    }

    /**
     * 获取短信策略
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/loan/{id}/sms/setting", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<LoanSmsSettingDomain> getSmsSetting(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        if (!isPermitted(PermissionEnum.VIEW_LOAN_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        return loanNotificationSettingManager.getSmsSetting(id);
    }

    /**
     * 设置短信策略
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/loan/{id}/sms/setting", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    @LoanValidCheck
    public BaseResult<LoanSmsSettingDomain> settingSms(@PathVariable("id") Long id,
                                                       @RequestBody @Valid LoanSmsSettingForm smsSettingForm,
                                                       BindingResult bindingResult) {
        // 如果当前用户没有 UPDATE_LOAN_ALL 权限, 则检查是否为所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }

        LoanSmsSettingDomain smsSettingDomain = smsSettingForm.convert2Domain();
        smsSettingDomain.setLoanId(id);

        return loanNotificationSettingManager.addSmsSetting(smsSettingDomain);
    }

}
