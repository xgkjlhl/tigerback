/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.customer;

import org.junit.Test;
import tiger.web.api.base.AbstractIntegrationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hupeng
 * @version v0.1 2015/10/25 16:44
 */
public class CustomerControllerTest extends AbstractIntegrationTests {

    @Test
    public void testCustomersList() throws Exception {
        logger.info("测试获取客户列表");
        mockMvc.perform(get("/customers?scope=list").header("token", token))
                .andExpect(status().isOk());
//                 andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value(""));
//        mockMvc.perform(get("/customer/1")).andExpect(status().isOk());
//        mockMvc.perform(put("/customer/1")).andExpect(status().isOk());
        logger.info("完成测试获取客户列表");
    }

}
