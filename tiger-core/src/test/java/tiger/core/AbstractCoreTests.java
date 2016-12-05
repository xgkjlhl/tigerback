/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import tiger.common.dal.DataConfig;

import javax.transaction.Transactional;

/**
 * @author yiliang.gyl
 * @version v 0.1 Sep 25, 2015 3:07:16 PM yiliang.gyl Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreInitilizerConfig.class, DataConfig.class})
@Transactional
public abstract class AbstractCoreTests {
    protected static Logger logger = Logger.getLogger(AbstractCoreTests.class);

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setupMockMvc() {
        logger.info("测试启动");
    }

    @After
    public void clearTests() {
        logger.info("测试关闭");
    }
}
