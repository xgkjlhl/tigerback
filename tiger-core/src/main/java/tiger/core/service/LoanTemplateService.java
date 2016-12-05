/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.LoanTemplateQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanTemplateDomain;

import java.util.List;

/**
 * 贷款模板处理服务
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 19, 2015 3:05:16 PM yiliang.gyl Exp $
 */
public interface LoanTemplateService {

    /**
     * 插入一条模板
     *
     * @param loanTemplateDomain the loan template domain
     * @return the int
     */
    LoanTemplateDomain insertLoanTemplate(LoanTemplateDomain loanTemplateDomain);

    /**
     * 删除一条模板
     *
     * @param id the id
     * @return the int
     */
    boolean deleteLoanTemplate(long id);

    /**
     * 通过id获取一个模板
     *
     * @param id the id
     * @return the loan template by id
     */
    LoanTemplateDomain getLoanTemplateById(long id);

    /**
     * 更新一个模板
     *
     * @param loanTemplateDomain the loan template domain
     * @return the int
     */
    boolean updateLoanTemplate(LoanTemplateDomain loanTemplateDomain);

    /**
     * List templates.
     *
     * @param query the query
     * @return the page result
     */
    PageResult<List<LoanTemplateDomain>> listTemplates(LoanTemplateQuery query);

    /**
     * All.
     *
     * @param query the query
     * @return the list
     */
    List<LoanTemplateDomain> all(LoanTemplateQuery query);

    /**
     * 根据查询条件获取贷款合同模版数量
     *
     * @param query
     * @return
     */
    int countLoanTemplate(LoanTemplateQuery query);

}
