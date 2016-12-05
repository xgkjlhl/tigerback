/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.domain.LoanDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred.yx
 * @version v 0.1 Sep 30, 2015 9:23:19 PM alfred Exp $
 */
public class LoanServiceTest extends AbstractCoreTests {
    @Autowired
    LoanService loanService;

    @Test
    public void testGetExclusiveLoans() {
        List<LoanDomain> loanDomains = loanService.getExclusiveLoans(2l, null);
        int nullSize = loanDomains.size();
        List<LoanStatusEnum> statusList = new ArrayList<>();
        loanDomains = loanService.getExclusiveLoans(2l, statusList);
        int emptySize = loanDomains.size();
        Assert.assertEquals(nullSize, emptySize);

        statusList = getAllStatus();
        loanDomains = loanService.getExclusiveLoans(2l, statusList);
        int exclusiveSize = loanDomains.size();
        Assert.assertEquals(0, exclusiveSize);
    }

    @Test
    public void testGetInclusiveLoans() {
        List<LoanDomain> loanDomains = loanService.getInclusiveLoans(2l, null);
        int nullSize = loanDomains.size();
        Assert.assertEquals(0, nullSize);

        List<LoanStatusEnum> statusList = new ArrayList<LoanStatusEnum>();
        loanDomains = loanService.getInclusiveLoans(2l, statusList);
        int emptySize = loanDomains.size();
        Assert.assertEquals(0, emptySize);

        statusList = getAllStatus();
        loanDomains = loanService.getInclusiveLoans(2l, statusList);
    }

    private List<LoanStatusEnum> getAllStatus() {
        List<LoanStatusEnum> statusList = new ArrayList<LoanStatusEnum>();
        statusList.add(LoanStatusEnum.BAD_LOAN);
        statusList.add(LoanStatusEnum.CHECK_BLOCK);
        statusList.add(LoanStatusEnum.FINISH);
        statusList.add(LoanStatusEnum.IN_DRAFT);
        statusList.add(LoanStatusEnum.OVER_DUE);
        statusList.add(LoanStatusEnum.PAY_PROCESS);
        statusList.add(LoanStatusEnum.WAIT_LAUNCH);
        return statusList;
    }
}
