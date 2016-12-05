/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.LoanCustomerTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 4:38 PM yiliang.gyl Exp $
 */
public interface LoanCustomerService {
    /**
     * 获取一个贷款合同的所有客户 id
     *
     * @param loanId
     * @return
     */
    List<Long> getLoanCustomerIds(Long loanId);

    /**
     * 获取多个贷款合同的所有客户id(去重)
     *
     * @param loanIds
     * @return
     */
    List<Long> getLoansCustomerIds(List<Long> loanIds);

    /**
     * 根据贷款id和客户类型获取客户数量
     *
     * @param loanId
     * @param customerType
     * @return
     */
    int countCustomerByLoanType(Long loanId, LoanCustomerTypeEnum customerType);

    /**
     * 解除合同关联用户
     *
     * @param loanId
     * @return
     */
    boolean deleteCustomerByLoanId(Long loanId);

    /**
     * 解除合同关联用户
     *
     * @param loanId
     * @param customerType
     * @return
     */
    boolean deleteCustomerByLoanType(Long loanId, LoanCustomerTypeEnum customerType);

    /**
     * 根据客户类型, 计算客户相关联的合同数量
     *
     * @param customerIds
     * @param type
     * @return
     */
    Map<Long, Integer> countCustomersByType(List<Long> customerIds, LoanCustomerTypeEnum type);

    /**
     * 删除与loanIds相关联的客户
     *
     * @param loanIds
     * @return
     */
    boolean deleteCustomerByLoanIds(List<Long> loanIds);
}
