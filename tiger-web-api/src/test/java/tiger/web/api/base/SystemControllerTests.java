package tiger.web.api.base;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jaric Liao on 2015/12/11.
 */
public class SystemControllerTests extends AbstractIntegrationTests {

    @Test
    public void testGetSystemDateTime() throws Exception {
        logger.info("开始测试获取系统时间");
        mockMvc.perform(get("/system/dateTime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", notNullValue()));
        logger.info("结束测试获取系统时间");
    }

    @Test
    public void testGetSystemParams() throws Exception {
        logger.info("开始测试获取系统参数");
        mockMvc.perform(get("/system/systemparams")
                .header(TOKEN_KEY, token)
                .param("type", "LOAN_CONFIG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", notNullValue()));
        logger.info("结束测试获取系统参数");
    }
}
