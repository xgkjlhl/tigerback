package tiger.web.api.company;

import org.junit.Test;
import tiger.common.util.JsonUtil;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.company.CompanyForm;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jaric Liao on 2015/12/11.
 */
public class CompanyControllerTests extends AbstractIntegrationTests {

    @Test
    public void testUpdateCompany() throws Exception {
        logger.info("开始测试更新用户公司信息");
        String address = "上海市浦东区陆家嘴";
        CompanyForm companyForm = new CompanyForm();
        companyForm.setName("小锅集团");
        companyForm.setAddress(address);
        companyForm.setPhone("021-66666666");
        mockMvc.perform(put("/account/company")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(companyForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        mockMvc.perform(get("/account/company")
                .header(TOKEN_KEY, token))
                .andExpect(jsonPath("$.data.address", is(address)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("开始测试更新用户公司信息");
    }
}
