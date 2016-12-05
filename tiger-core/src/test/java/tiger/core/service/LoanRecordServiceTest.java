/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.query.LoanPaybackQuery;
import tiger.core.AbstractCoreTests;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanPaybackListDomain;

import java.util.List;

/**
 * @author yiliang.gyl
 * @version v 0.1 Oct 4, 2015 6:30:29 PM yiliang.gyl Exp $
 */
public class LoanRecordServiceTest extends AbstractCoreTests {

    @Autowired
    private LoanRecordService loanRecordService;

    @Test
    public void testGetPaybacks() {
        LoanPaybackQuery loanPaybackQuery = new LoanPaybackQuery();

        loanPaybackQuery.setBusinessType(BusinessTypeEnum.LOAN.getCode());
        PageResult<List<LoanPaybackListDomain>> items = loanRecordService
                .getRecordsByPage(loanPaybackQuery);
        System.out.println(items);
    }
}
