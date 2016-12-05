package tiger.web.api.account;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import tiger.common.dal.enums.SmsVerifyCodeTypeEnum;
import tiger.common.util.EncryptUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.JsonUtil;
import tiger.common.util.RandomCodeUtil;
import tiger.core.constants.SystemConstants;
import tiger.core.service.SmsService;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.user.*;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Jaric Liao on 2015/11/23.
 */
public class AccountControllerTests extends AbstractIntegrationTests {

    private static final int CODE_LENGHT = 6;
    private static boolean SMS_TEST_TRIGGER = false;
    @Autowired
    SmsService smsService;

    @Test
    public void testSignUp() throws Exception {
        if (!SMS_TEST_TRIGGER) return;
        logger.info("开始测试用户注册");
        // 1 初始化注册信息
        String unique_mobile = "18801790702";
        String password = "gradletest";
        AccountAddForm form = new AccountAddForm();
        form.setMobile(unique_mobile);
        form.setUserName("testman");
        form.setPassword(password);
        form.setConfirmPassword(password);

        // 1.1 获取注册邀请码
        MvcResult mvcResult = mockMvc.perform(put("/account/invite")
                .header(TOKEN_KEY, token)
                .param("operation", "REGISTER")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        String inviteCode = JsonPath.read(response, "$.data.code");
        form.setInvitationCode(inviteCode);

        // 1.2 获取短信验证码
        String smsCode = RandomCodeUtil.getRandomNumer(CODE_LENGHT);
        smsService.addSmsVerifyCode(unique_mobile, smsCode, SmsVerifyCodeTypeEnum.REGISTER, 0l);
        form.setMsgCode(smsCode);

        // 2 调用注册API
        mockMvc.perform(post("/account")
                .param("operation", "register")
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", notNullValue()));

        logger.info("结束测试用户注册");
    }

    @Test
    public void testMsgCodeForRegister() throws Exception {
        if (!SMS_TEST_TRIGGER) return;
        logger.info("开始测试注册短信验证码");
        String unique_mobile = "18801790702";
        // 1 测试获取短信验证码
        mockMvc.perform(get("/account/sms")
                .param("operation", "register")
                .param("mobile", unique_mobile))
                .andExpect(status().isOk());

        // 2 测试提交短信验证码
        MobileAndCode form = new MobileAndCode();
        String smsCode = smsService.getMsgCodeFromDB(unique_mobile, SmsVerifyCodeTypeEnum.REGISTER);
        logger.info("短信验证码为: " + smsCode);
        form.setPhoneNum(unique_mobile);
        form.setMsgCode(smsCode);
        mockMvc.perform(post("/account/sms")
                .param("operation", "register")
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));

        logger.info("结束测试注册短信验证码");
    }

    @Test
    public void testChangePassword() throws Exception {
        logger.info("开始测试修改密码");
        String oldPassword = EncryptUtil.MD5("abc123");
        String newPassword = EncryptUtil.MD5("abc123abc123");
        SimpleResetPasswordForm form = new SimpleResetPasswordForm();
        form.setOldPassword(oldPassword);
        form.setPassword(newPassword);
        form.setConfirmPassword(newPassword);
        mockMvc.perform(put("/account/password")
                .param("operation", "reset")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试修改密码");
    }

    @Test
    public void testResetPassword() throws Exception {
        if (!SMS_TEST_TRIGGER) return;
        logger.info("开始测试忘记密码重置");
        // 1 获取重置短信验证码
        String currentMobile = "18516171260";
        mockMvc.perform(get("/account/password/sms")
                .param("operation", "reset")
                .param("mobile", currentMobile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));

        // 2 重置密码
        String verifycode = smsService.getMsgCodeFromDB(currentMobile,
                SmsVerifyCodeTypeEnum.RESET_PASSWORD);
        ResetPasswordForm form = new ResetPasswordForm();
        form.setMobile(currentMobile);
        form.setCode(verifycode);
        form.setPassword(EncryptUtil.MD5("abc123abc123"));
//        form.setConfirmPassword(EncryptUtil.MD5("abc123abc123"));
        mockMvc.perform(post("/account/password")
                .param("operation", "reset")
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        logger.info("结束测试忘记密码重置");
    }

    @Test
    public void testCreateRegisterInvitationCode() throws Exception {
        logger.info("开始测试创建注册邀请码");
        mockMvc.perform(put("/account/invite")
                .header(TOKEN_KEY, token)
                .param("operation", "REGISTER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.code", notNullValue()));
        logger.info("结束测试创建注册邀请码");
    }

    @Test
    public void testGetAvailableInvitation() throws Exception {
        logger.info("开始测试获取注册邀请码");
        MvcResult mvcResult = mockMvc.perform(put("/account/invite")
                .header(TOKEN_KEY, token)
                .param("operation", "REGISTER")).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        String inviteCode = JsonPath.read(response, "$.data.code");
        mockMvc.perform(get("/account/invite")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data[?(@.code=" + inviteCode + ")]", notNullValue()));
        logger.info("结束测试获取注册邀请码");
    }

    @Test
    public void testGetUserProfile() throws Exception {
        logger.info("开始测试获取用户信息");
        mockMvc.perform(get("/account/profile")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(currentId.intValue())));
        logger.info("结束测试获取用户信息");
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        logger.info("开始测试更新用户信息");
        String address = "新的地址";
        AccountUpdateForm form = new AccountUpdateForm();
        form.setAddress(address);
        mockMvc.perform(put("/account/profile")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试更新用户信息");
    }

    @Test
    public void testUpdateExtParam() throws Exception {
        logger.info("开始测试更新账户额外信息");
        Map<String, Object> extParam = new HashMap<String, Object>();
        int bad_loan_day = 91;
        int over_due_day = 31;
        extParam.put(SystemConstants.BAD_LOAN_DAY, bad_loan_day);
        extParam.put(SystemConstants.OVER_DUE_DAY, over_due_day);
        mockMvc.perform(put("/account")
                .header(TOKEN_KEY, token)
                .param("scope", "extParam")
                .contentType("application/json")
                .content(JsonConverterUtil.serialize(extParam)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/account/profile")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.extParams.badLoanDay", is(Integer.toString(bad_loan_day))))
                .andExpect(jsonPath("$.data.extParams.overDueDay", is(Integer.toString(over_due_day))));
        logger.info("结束测试更新账户额外信息");
    }

    @Test
    public void testAttachHeaderIcon() throws Exception {
        logger.info("开始测试更新用户头像");
        Long attachID = 95L;
        mockMvc.perform(put("/account/icon/" + attachID.toString())
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk());
        logger.info("结束测试更新用户头像");
    }

    @Test
    public void testIsMobileExist() throws Exception {
        logger.info("开始测试手机号是否存在");
        String testCase = "18800000000";
        mockMvc.perform(get("/account/mobile/" + testCase))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(false)));
        logger.info("结束测试手机号是否存在");
    }
}
