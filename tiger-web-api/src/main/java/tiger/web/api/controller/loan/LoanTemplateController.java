/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.loan;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.loan.support.LoanTemplateManager;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.query.LoanTemplateQuery;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanTemplateDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.loan.LoanTemplateForm;

import javax.validation.Valid;
import java.util.List;

/**
 * 贷款/融资模板 API 接口.
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 12, 2015 6:25:18 PM yiliang.gyl Exp $
 */
@RestController
@RequestMapping(value = "loan")
@ResponseBody
public class LoanTemplateController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanTemplateController.class);

    @Autowired
    private LoanTemplateManager loanTemplateManager;

    /**
     * 获取贷款模板
     *
     * @param id
     * @return the loan template by id
     */
    @RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_TEMPLATE})
    public BaseResult<LoanTemplateDomain> getLoanTemplateById(@PathVariable("id") long id) {
        if (!loanTemplateManager.isInWorkspace(id, currentWorkspaceId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED);
        }

        return loanTemplateManager.findLoanTemplateById(id);
    }

    /**
     * 创建贷款模板.
     *
     * @param loanTemplateForm
     * @param bindingResult
     * @return the base result
     */
    @RequestMapping(value = "/template", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.CREATE_LOAN_TEMPLATE})
    public BaseResult<LoanTemplateDomain> createLoanTemplate(@RequestBody @Valid LoanTemplateForm loanTemplateForm,
                                                             BindingResult bindingResult) {
        LoanTemplateDomain domain = loanTemplateForm.convert2Domain();
        domain.setAccountId(currentAccount().getId());
        domain.setWorkspaceId(currentWorkspaceId());

        if (logger.isInfoEnabled()) {
            logger.info("创建一个模板 [" + domain + "]");
        }

        return loanTemplateManager.createLoanTemplate(domain);
    }

    /**
     * 删除贷款模板.
     *
     * @param id
     * @return the base result
     */
    @RequestMapping(value = "/template/{id}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.DELETE_LOAN_TEMPLATE})
    public BaseResult<Boolean> deleteLoanTemplate(@PathVariable("id") long id) {
        if (!loanTemplateManager.isInWorkspace(id, currentWorkspaceId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED, false);
        }

        if (logger.isInfoEnabled()) {
            logger.info("删除模板[" + id + "]");
        }
        return loanTemplateManager.deleteLoanTemplate(id);
    }

    /**
     * 更新贷款模板.
     *
     * @param loanTemplateForm
     * @param bindingResult
     * @param id
     * @return the base result
     */
    @RequestMapping(value = "/template/{id}", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_TEMPLATE})
    public BaseResult<Boolean> updateLoanTemplate(@RequestBody @Valid LoanTemplateForm loanTemplateForm,
                                                  BindingResult bindingResult,
                                                  @PathVariable("id") long id) {
        if (!loanTemplateManager.isInWorkspace(id, currentWorkspaceId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED, false);
        }
        LoanTemplateDomain domain = loanTemplateForm.convert2Domain();
        domain.setId(id);
        domain.setWorkspaceId(currentWorkspaceId());
        if (logger.isInfoEnabled()) {
            logger.info("更新模板 [" + domain + "]");
        }
        return loanTemplateManager.updateLoanTemplate(domain);
    }

    /**
     * 分页获取贷款模板
     *
     * @param query
     * @return the page result
     */
    @RequestMapping(value = "/templates", method = RequestMethod.GET, params = "scope=list")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_TEMPLATE})
    public PageResult<List<LoanTemplateDomain>> loanTemplates(LoanTemplateQuery query) {
        if (logger.isInfoEnabled()) {
            logger.info("分页列出模板,参数 [" + query + "]");
        }
        query.setWorkspaceId(currentWorkspaceId());
        return loanTemplateManager
                .listTemplates(query);
    }

    /**
     * 分页获取贷款模板
     *
     * @param query
     * @return the page result
     */
    @RequestMapping(value = "/templates", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_TEMPLATE})
    public BaseResult<List<LoanTemplateDomain>> allLoanTemplates(LoanTemplateQuery query) {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有模板,参数 [" + query + "]");
        }
        query.setWorkspaceId(currentWorkspaceId());
        return loanTemplateManager
                .findAllTemplates(query);
    }

    /**
     * 获取合同模版数量
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/templates", method = RequestMethod.GET, params = "scope=count")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_TEMPLATE})
    public BaseResult<Integer> countLoanTemplates(LoanTemplateQuery query) {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有模板的数量,参数 [" + query + "]");
        }

        query.setWorkspaceId(currentWorkspaceId());

        return loanTemplateManager
                .countLoanTemplate(query);
    }
}
