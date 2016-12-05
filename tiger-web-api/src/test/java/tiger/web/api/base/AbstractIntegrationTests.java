/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.base;

import com.jayway.jsonpath.JsonPath;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tiger.common.util.StringUtil;
import tiger.web.api.IntegrationTestsConfig;
import tiger.web.api.ShiroConfig;
import tiger.web.api.constants.APIConstants;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 集成测试启动器
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 25, 2015 1:46:54 PM yiliang.gyl Exp $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {IntegrationTestsConfig.class})
@Transactional
@PropertySource(value = {"api-test.properties"})
public abstract class AbstractIntegrationTests {

    protected final static String TOKEN_KEY = "X-Auth-Token";
    protected static Logger logger = Logger.getLogger(AbstractIntegrationTests.class);
    @Autowired
    protected WebApplicationContext wac;
    protected Long currentId;
    protected String token = "";
    protected MockMvc mockMvc;
    @Autowired
    Environment environment;
    private String testUserName;
    private String testPassword;

    @Before
    public void setupMockMvc() throws Exception {
        logger.info("测试启动");
        this.testUserName = StringUtil.defaultIfBlank(environment.getProperty("testUserName"),
                "18516171260");
        this.testPassword = StringUtil.defaultIfBlank(environment.getProperty("testPassword"),
                "3ba01b7f80fccc857741024c2e653bd8");

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        MvcResult mvcResult = mockMvc.perform(post("/account/authentication")
                .header(APIConstants.HEADER_USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36")
                .header(APIConstants.HEADER_USERNAME, testUserName)
                .header(APIConstants.HEADER_PASSWORD, testPassword))
                .andReturn();
        String token = mvcResult.getResponse().getHeader(TOKEN_KEY);
        String response = mvcResult.getResponse().getContentAsString();
        currentId = Long.parseLong(JsonPath.read(response, "$.data.id").toString());
        if (StringUtil.isBlank(token)) {
            throw new Exception("登录失败");
        } else {
            logger.info("登录成功，获取token [" + token + "]");
            this.token = token;
            SecurityUtils.setSecurityManager(new ShiroConfig().securityManager());
        }

    }

    @After
    public void clearTests() throws Exception {
        mockMvc.perform(delete("/account/authentication")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk());
        logger.info("测试关闭");
    }
}
