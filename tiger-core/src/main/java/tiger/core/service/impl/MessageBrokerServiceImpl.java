package tiger.core.service.impl;

import org.springframework.stereotype.Service;
import tiger.core.domain.LoanDomain;
import tiger.core.service.MessageBrokerService;

/**
 * Created by Jaric Liao on 2016/1/16.
 */
@Service
public class MessageBrokerServiceImpl implements MessageBrokerService {

    /**
     * @see tiger.core.service.MessageBrokerService#backupLoanInfo(LoanDomain)
     */
    @Override
    public boolean backupLoanInfo(LoanDomain loanDomain) {
        return true;
    }
}
