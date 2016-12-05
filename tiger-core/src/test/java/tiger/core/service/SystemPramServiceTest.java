/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.constants.SystemConstants;

/**
 * @author yiliang.gyl
 * @version v 0.1 Oct 3, 2015 5:21:25 PM yiliang.gyl Exp $
 */
public class SystemPramServiceTest extends AbstractCoreTests {

    @Autowired
    SystemParamService systemParamService;

    @Test
    public void testGetNormalParam() {
        systemParamService.getValueByTypeAndKey(SystemParamTypeEnum.DEFAULT,
                SystemConstants.DEFAULT);
    }
}
