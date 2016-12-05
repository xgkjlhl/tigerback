/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.core.AbstractCoreTests;

/**
 * @author yiliang.gyl
 * @version v 0.1 Sep 25, 2015 4:36:33 PM yiliang.gyl Exp $
 */
public class LoanTemplateServiceTest extends AbstractCoreTests {

    @Autowired
    LoanTemplateService loanTemplateService;

    @Test
    public void testGetLoanTemplateById() {
        logger.info(loanTemplateService.getLoanTemplateById(13l));

    }
}
