/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.sms;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import tiger.common.util.JsonUtil;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.customer.CustomerCreateForm;
import tiger.web.api.form.sms.SendSmsForm;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mi.li
 * @version v 0.1 15/12/4 下午11:23 mi.li Exp $
 */
public class SmsControllerTests extends AbstractIntegrationTests {
    private static boolean SMS_TEST_TRIGGER = true;

    @Test
    public void testSms() throws Exception {
        if (!SMS_TEST_TRIGGER) return;
        //创建客户并获取id
        CustomerCreateForm customerCreateForm = new CustomerCreateForm();
        customerCreateForm.setName("客户");
        customerCreateForm.setMobile("13764588973");
        MvcResult result = mockMvc.perform(post("/customer")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(customerCreateForm)))
                .andReturn();
        Integer custoemrId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("创建客户id为" + custoemrId);

        //获取消息模板
        logger.info("开始测试获取所有的短信模板");
        result = mockMvc.perform(get("/sms/templates")
                .header(TOKEN_KEY, token)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andReturn();
        logger.info("结束测试获取所有的短信模板");

        //获取template id(默认数据库里有template，取第一个)
        Integer templateId = JsonPath.read(result.getResponse().getContentAsString(), "$.data[0].msgTpls[0].id");
        logger.info("消息模板id为" + templateId);

        //发送即时短信
        logger.info("开始测试发送即时短信");
        SendSmsForm sendSmsForm = new SendSmsForm();
        sendSmsForm.setCustomerIds(new Long[]{Long.valueOf(custoemrId)});
        sendSmsForm.setMsgTplId(Long.valueOf(templateId));
        sendSmsForm.setAgreeLiscense(true);

        result = mockMvc.perform(post("/sms")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(sendSmsForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andReturn();
        logger.info("结束测试发送即时短信");

        //获取即时短信id
        logger.info("开始测试查看所有发送过的短信");
        result = mockMvc.perform(get("/sms/history")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andReturn();
        logger.info("结束测试查看所有发送过的短信");

        Integer smsId = JsonPath.read(result.getResponse().getContentAsString(), "$.data[0].id");
        logger.info("即时短信id为" + smsId);

//        //重新发送短信
//        logger.info("开始测试重新发送失败的短信");
//        result = mockMvc.perform(put("/sms/history/" + smsId)
//                .header(TOKEN_KEY, token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code", is("200")))
//                .andReturn();
//        logger.info("结束测试重新发送失败的短信");

        //发送定时短信
        logger.info("开始测试发送定时短信");
        String jsonContent = JsonUtil.toJson(sendSmsForm);
        //Map<String, String> test = JsonUtil.fromJson(jsonContent, HashMap.class);
        //test.put("dateToSendMsg", String.valueOf(System.currentTimeMillis()));
        //TODO:带数组的Map转JSON仍存在问题
        jsonContent = jsonContent.substring(0, jsonContent.length() - 1) + " ,\"dateToSendMsg\":\"" + System.currentTimeMillis() + "\"}";

        mockMvc.perform(post("/sms")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试发送定时短信");

        //获取所有的定时短信
        logger.info("开始测试获取所有的定时短信");
        result = mockMvc.perform(get("/sms/scheduler")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andReturn();
        logger.info("结束测试获取所有的定时短信");

        //获取定时短信id
        Integer schedulerSmsId = JsonPath.read(result.getResponse().getContentAsString(), "$.data[0].id");
        logger.info("定时短信id为" + schedulerSmsId);

        //立即发送定时短信
        logger.info("开始测试立即发送定时短信");
        mockMvc.perform(put("/sms/scheduler/" + schedulerSmsId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试立即发送定时短信");

        //取消定时短信
        logger.info("开始测试取消定时短信");
        mockMvc.perform(delete("/sms/scheduler/" + schedulerSmsId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试取消定时短信");
    }
}
