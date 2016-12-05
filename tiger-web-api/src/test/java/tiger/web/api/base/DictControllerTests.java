/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.base;

import org.junit.Test;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author yiliang.gyl
 * @version v 0.1 Sep 25, 2015 2:50:29 PM yiliang.gyl Exp $
 */
public class DictControllerTests extends AbstractIntegrationTests {

    @Test
    public void testLoanDict200() throws Exception {
        logger.info("测试获取贷款字典数据");
        mockMvc.perform(get("/system/dict?scope=loan&dictType=bizType")).andExpect(status().isOk());
        mockMvc.perform(get("/system/dict?scope=loan&dictType=payWay")).andExpect(status().isOk());
        mockMvc.perform(get("/system/dict?scope=loan&dictType=loanPawnType"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/system/dict?scope=loan&dictType=loanStatus"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/system/dict?scope=loan&dictType=customerType"))
                .andExpect(status().isOk());
        logger.info("完成测试获取贷款字典数据");
    }

    @Test
    public void testName() throws Exception {
        logger.info(new Date());
        logger.info(new java.sql.Date(System.currentTimeMillis()));

    }
}
