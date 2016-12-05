/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.ucenter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.core.domain.UcenterHelpDomain;
import tiger.core.service.UcenterHelpService;
import tiger.web.api.base.AbstractIntegrationTests;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mi.li
 * @version v 0.1 15/11/23 下午7:59 mi.li Exp $
 */
public class HelpControllerTests extends AbstractIntegrationTests {

    @Autowired
    UcenterHelpService ucenterHelpService;

    @Test
    public void testCommonHelps() throws Exception {
        logger.info("开始测试获取常用帮助列表");
        mockMvc.perform(get("/ucenter/commonHelps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取常用帮助列表");
    }

    @Test
    public void testHelpsAll() throws Exception {
        logger.info("开始测试获取所有帮助列表");
        mockMvc.perform(get("/ucenter/helps").param("scope", "all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取所有帮助列表");
    }

    @Test
    public void testHelpsList() throws Exception {
        logger.info("开始测试分页获取所有帮助列表");
        mockMvc.perform(get("/ucenter/helps").param("scope", "list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试分页获取所有帮助列表");
    }

    @Test
    public void testHelpsListWithCatalogId() throws Exception {
        //新增catalog
        Long id = ucenterHelpService.addHelpCatalog("default");

        logger.info("开始测试获取指定类别下所有帮助列表");
        mockMvc.perform(get("/ucenter/helps").param("scope", "list").param("catalogId", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取指定类别下所有帮助列表");
    }

    @Test
    public void testGetHelp() throws Exception {
        //新增catalog
        Long catalogId = ucenterHelpService.addHelpCatalog("default");
        //新增一个帮助
        UcenterHelpDomain helpDomain = new UcenterHelpDomain();
        helpDomain.setTitle("如何贷款");
        helpDomain.setContent("操作流程");
        helpDomain.setCatalogId(catalogId);
        helpDomain = ucenterHelpService.addHelp(helpDomain);

        //获取帮助id
        Long helpId = helpDomain.getId();
        logger.info("新增帮助的id为" + helpId);

        logger.info("开始测试获取一个帮助");
        mockMvc.perform(get("/ucenter/help/" + helpId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(helpId.intValue())));
        logger.info("结束测试获取一个帮助");
    }

    @Test
    public void testCatalogs() throws Exception {
        logger.info("开始测试获取帮助类别列表");
        mockMvc.perform(get("/ucenter/catalogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取帮助类别列表");
    }

    @Test
    public void testGetCatalog() throws Exception {
        //新增catalog
        Long id = ucenterHelpService.addHelpCatalog("default");

        logger.info("开始测试获取一个帮助类别");
        mockMvc.perform(get("/ucenter/catalog/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(id.intValue())));
        logger.info("结束测试获取一个帮助类别");
    }

    @Test
    public void testSearchHelp() throws Exception {
        logger.info("开始测试搜索帮助");
        String keyword = "test";
        mockMvc.perform(get("/ucenter/search/").param("scope", "help").param("keyword", keyword)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试搜索帮助");
    }

}
