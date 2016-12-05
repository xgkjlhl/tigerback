/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.core.domain.LoanRefineDomain;

import java.util.List;

/**
 * @author alfred_yuan
 * @version ${ID}: v 0.1 20:29 alfred_yuan Exp $
 */
public interface LoanRefineService {

    LoanRefineDomain insert(LoanRefineDomain domain);

    LoanRefineDomain read(Long id);

    // 根据贷款合同id获取与之相关的合同修正项目
    List<LoanRefineDomain> getLoanRefineByLoanId(Long loanId);
}
