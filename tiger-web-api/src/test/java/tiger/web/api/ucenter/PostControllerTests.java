/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.ucenter;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import tiger.common.util.JsonUtil;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.ucenter.PostForm;
import tiger.web.api.form.ucenter.PostReplyForm;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mi.li
 * @version v 0.1 15/11/23 下午7:59 mi.li Exp $
 */
public class PostControllerTests extends AbstractIntegrationTests {

    @Test
    public void testPost() throws Exception {
        // ~ 增
        logger.info("开始测试新增一个主题帖");
        String title = "如何找回密码";
        String content = "忘记密码，如何找回";
        PostForm postForm = new PostForm();
        postForm.setTitle(title);
        postForm.setContent(content);
        MvcResult result = mockMvc.perform(post("/ucenter/post")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(postForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.title", is(title)))
                .andExpect(jsonPath("$.data.content", is(content)))
                .andReturn();
        logger.info("结束测试新增一个主题帖");

        //获取主题帖id
        Integer postId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增主题帖的id为" + postId);

        // ~ 查
        logger.info("开始测试获取一个主题帖");
        mockMvc.perform(get("/ucenter/post/" + postId)
                .header(TOKEN_KEY, ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(postId)));
        logger.info("结束测试获取一个主题帖");

        // ~ 改
        logger.info("开始测试修改一个主题帖");
        postForm.setTitle("[已解决]如何找回密码");
        postForm.setContent("已解决，谢谢！");
        mockMvc.perform(put("/ucenter/post/" + postId)
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(postForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试修改一个主题帖");

        // ~ 删
        logger.info("开始测试获取一个主题帖");
        mockMvc.perform(delete("/ucenter/post/" + postId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取一个主题帖");
    }

    @Test
    public void testStickyPosts() throws Exception {
        logger.info("开始测试获取所有置顶主题帖");

        mockMvc.perform(get("/ucenter/stickyPosts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        logger.info("结束测试获取所有置顶主题帖");
    }

    @Test
    public void testPostsAll() throws Exception {
        logger.info("开始测试获取所有非置顶主题帖");

        mockMvc.perform(get("/ucenter/posts").param("scope", "all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        logger.info("结束测试获取所有非置顶主题帖");
    }

    @Test
    public void testPostsList() throws Exception {
        logger.info("开始测试分页获取所有非置顶主题帖");

        mockMvc.perform(get("/ucenter/posts").param("scope", "list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        logger.info("结束测试分页获取所有非置顶主题帖");
    }

    @Test
    public void testReply() throws Exception {
        //新增主题帖
        String title = "如何找回密码";
        String content = "忘记密码，如何找回";
        PostForm postForm = new PostForm();
        postForm.setTitle(title);
        postForm.setContent(content);
        MvcResult result = mockMvc.perform(post("/ucenter/post")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(postForm)))
                .andReturn();

        //获取主题帖id
        Integer postId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增主题帖的id为" + postId);

        //获取all
        logger.info("开始测试获取某一主题帖的所有直接回复");
        mockMvc.perform(get("/ucenter/post/" + postId + "/replys").param("scope", "all")
                .header(TOKEN_KEY, ""))
                .andExpect(status().isOk());
        logger.info("结束测试获取某一主题帖的所有直接回复");

        //获取list
        logger.info("开始测试分页获取某一主题帖的所有直接回复");
        mockMvc.perform(get("/ucenter/post/" + postId + "/replys").param("scope", "list")
                .header(TOKEN_KEY, ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试分页获取某一主题帖的所有直接回复");

        // ~ 增
        logger.info("开始测试新增一个回复");
        PostReplyForm postReplyForm = new PostReplyForm();
        content = "如何找回密码";
        postReplyForm.setContent(content);
        result = mockMvc.perform(post("/ucenter/post/" + postId + "/reply")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(postReplyForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.content", is(content)))
                .andReturn();
        logger.info("结束测试新增一个回复");

        //获取回复id
        Integer replyId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增回复的id为" + replyId);

        // ~ 查
        logger.info("开始测试获取一个回复");
        mockMvc.perform(get("/ucenter/reply/" + replyId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(replyId)));
        logger.info("结束测试获取一个回复");

        // ~ 改
        logger.info("开始测试修改一个回复");
        postReplyForm = new PostReplyForm();
        postReplyForm.setContent("已解决，谢谢！");

        mockMvc.perform(put("/ucenter/reply/" + replyId)
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(postReplyForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试修改一个回复");

        // ~ 删
        logger.info("开始测试删除一个回复");
        mockMvc.perform(delete("/ucenter/reply/" + replyId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试删除一个回复");
    }

    @Test
    public void testSearchPost() throws Exception {
        logger.info("开始测试搜索主题帖");
        String keyword = "test";
        mockMvc.perform(get("/ucenter/search/").param("scope", "post").param("keyword", keyword)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试搜索主题帖");
    }

    @Test
    public void testSearchReply() throws Exception {
        logger.info("开始测试搜索回复");
        String keyword = "test";
        mockMvc.perform(get("/ucenter/search/").param("scope", "reply").param("keyword", keyword)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试搜索回复");
    }
}
