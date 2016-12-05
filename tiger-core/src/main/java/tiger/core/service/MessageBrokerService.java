package tiger.core.service;

import tiger.core.domain.LoanDomain;

/**
 * Created by Jaric Liao on 2016/1/16.
 */
public interface MessageBrokerService {

    /**
     *
     * @param loanDomain the loan info
     * @return the boolean
     */
    boolean backupLoanInfo(LoanDomain loanDomain);
}
