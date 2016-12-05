/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.core.domain.LoanPawnDomain;

/**
 * 贷款抵押物服务类.
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 4:25:18 PM yiliang.gyl Exp $
 */
public interface LoanPawnService {


    /**
     * 获取一个抵押物信息
     *
     * @param loanPawnId
     * @return
     */
    LoanPawnDomain read(long loanPawnId);


    /**
     * 根据贷款id获取抵押物信息
     *
     * @param loanId
     * @return
     */
    LoanPawnDomain getLoanPawnByLoanId(long loanId);

    /**
     * 创建一个抵押物
     *
     * @param loanPawnDomain the loan pawn domain
     * @return the LoanPawnDomain
     */
    LoanPawnDomain create(LoanPawnDomain loanPawnDomain);


    /**
     * 更新一个抵押物
     *
     * @param loanPawnDomain the loan pawn domain
     * @return true, if successful
     */
    boolean updateByIdSelective(LoanPawnDomain loanPawnDomain);


    /**
     * 检查抵押物是否存在
     *
     * @param pawnId the pawn id
     * @return true, if is exist
     */
    boolean isExist(long pawnId);


    /**
     * 检查抵押物是否为accountId所有
     * ， 如果抵押物不存在则抛出NOT_FOUND异常
     *
     * @param pawnId
     * @param accountId
     * @return
     */
    boolean isOwner(long pawnId, long accountId);

}
