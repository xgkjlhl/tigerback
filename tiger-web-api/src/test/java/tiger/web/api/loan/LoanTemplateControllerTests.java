package tiger.web.api.loan;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import tiger.common.util.JsonUtil;
import tiger.core.domain.ModifyLoanRecordDomain;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.loan.LoanCreateForm;
import tiger.web.api.form.loan.LoanTemplateForm;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jaric Liao on 2015/12/11.
 */
public class LoanTemplateControllerTests extends AbstractIntegrationTests {

    LoanTemplateForm form;

    @Before
    public void createSampleTemplate() {
        form = new LoanTemplateForm();
        String name = "template name";
        double interestRate = 1;
        double penaltyRate = 1;
        form.setTemplateName(name);
        form.setBusinessType("LOAN");
        form.setType("HOUSE_PAWN");
        form.setPayWay("1001");
        form.setInterestRate(interestRate);
        form.setPenaltyRate(penaltyRate);
    }

    @Test
    public void testCreateLoanTemplate() throws Exception {
        logger.info("开始测试创建贷款模板");
        mockMvc.perform(post("/loan/template")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", notNullValue()));
        logger.info("结束测试创建贷款模板");
    }

    @Test
    public void testGetLoanTemplateById() throws Exception {
        logger.info("开始测试获取贷款模板");
        MvcResult mvcResult = mockMvc.perform(post("/loan/template")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form))).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        int templateID = JsonPath.read(response, "$.data.id");
        mockMvc.perform(get("/loan/template/" + templateID)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", equalTo(templateID)));
        logger.info("结束测试获取贷款模板");
    }

    @Test
    public void testUpdateLoanTemplate() throws Exception {
        logger.info("开始测试更新贷款模板");
        MvcResult mvcResult = mockMvc.perform(post("/loan/template")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form))).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        int templateID = JsonPath.read(response, "$.data.id");
        double newPenaltyRate = 2;
        form.setPenaltyRate(newPenaltyRate);
        mockMvc.perform(put("/loan/template/" + templateID)
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试更新贷款模板");
    }

    @Test
    public void testDeleteLoanTemplate() throws Exception {
        logger.info("开始测试删除贷款模板");
        MvcResult mvcResult = mockMvc.perform(post("/loan/template")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form))).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        int templateID = JsonPath.read(response, "$.data.id");
        mockMvc.perform(delete("/loan/template/" + templateID)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试删除贷款模板");
    }

    @Test
    public void testAllLoanTemplates() throws Exception {
        logger.info("开始测试获取所有贷款模板");
        MvcResult mvcResult = mockMvc.perform(post("/loan/template")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form))).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        int templateID = JsonPath.read(response, "$.data.id");
        mockMvc.perform(get("/loan/templates")
                .header(TOKEN_KEY, token)
                .param("scope", "all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data[?(@.id=" + templateID + ")]", notNullValue()));
        logger.info("结束测试获取所有贷款模板");
    }


    public void createLoanWithAll(){
        LoanCreateForm loanCreateForm=new LoanCreateForm();
        List<ModifyLoanRecordDomain> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            ModifyLoanRecordDomain modifyLoanRecordDomain=new ModifyLoanRecordDomain();
            modifyLoanRecordDomain.setAmount(200.0);
            modifyLoanRecordDomain.setInterest(50.0);
           // modifyLoanRecordDomain.setOtherCost(100.0);
            list.add(modifyLoanRecordDomain);
        }
        ModifyLoanRecordDomain lastLoan=new ModifyLoanRecordDomain();
        lastLoan.setAmount(0.0);
        lastLoan.setInterest(0.0);
       // lastLoan.setOtherCost(0.0);
        lastLoan.setBondPerformMoney(500.0);
        lastLoan.setTempMoney(200.0);
        list.add(lastLoan);
       String json=JsonUtil.toJson(list);
        System.out.println(json);
    }
}
