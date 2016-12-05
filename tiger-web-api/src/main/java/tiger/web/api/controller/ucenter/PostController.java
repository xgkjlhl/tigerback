/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.ucenter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.ucenter.support.UcenterPostManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.query.PostQuery;
import tiger.common.dal.query.PostReplyQuery;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.UcenterPostDomain;
import tiger.core.domain.UcenterPostReplyDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.UcenterPostService;
import tiger.web.api.constants.APIConstants;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.ucenter.PostForm;
import tiger.web.api.form.ucenter.PostReplyForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 主题帖及回复接口
 *
 * @author mi.li
 * @version v 0.1 2015年9月27日 上午10:19:20 mi.li Exp $
 */
@RestController
@ResponseBody
public class PostController extends BaseController {

    public final static int DEFAULT_PAGE_SIZE = 10;
    private static Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    UcenterPostService ucenterPostService;

    @Autowired
    UcenterPostManager ucenterPostManager;

    /**
     * 新增一个主题帖
     *
     * @param postForm
     * @return
     */
    @RequestMapping(value = "ucenter/post", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<UcenterPostDomain> addPost(@Valid @RequestBody PostForm postForm, BindingResult bingdingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 创建主题帖 [" + postForm + "]");
        }
        UcenterPostDomain postDomain = postForm.convert2Domain();
        postDomain.setIsSticky(false);//普通用户发帖默认为不置顶
        postDomain.setAccountId(currentAccount().getId());

        postDomain = ucenterPostService.addPost(postDomain);

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 创建主题帖结果 [" + postDomain + "]");
        }
        return new BaseResult<>(postDomain);
    }

    /**
     * 获取所有置顶主题帖
     *
     * @return
     */
    @RequestMapping(value = "/ucenter/stickyPosts", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<UcenterPostDomain>> stickyPosts() {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有置顶主题帖");
        }
        return ucenterPostManager.getStickyPosts();
    }

    /**
     * 获取所有非置顶主题帖
     *
     * @return
     */
    @RequestMapping(value = "/ucenter/posts", method = RequestMethod.GET, params = "scope=all")
    @ResponseBody
    public BaseResult<List<UcenterPostDomain>> allPosts() {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有非置顶主题帖");
        }
        return ucenterPostManager.getAllPosts();
    }

    /**
     * 分页获取所有非置顶主题帖
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/posts", method = RequestMethod.GET, params = "scope=list")
    @ResponseBody
    public PageResult<List<UcenterPostDomain>> listPosts(HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("分页获取所有非置顶主题帖");
        }
        PostQuery pq = new PostQuery();
        pq.setPageSize(DEFAULT_PAGE_SIZE);
        if (StringUtil.isNotBlank(request.getParameter("pageNum"))) {
            pq.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
        }

        return ucenterPostManager.getListPosts(pq);
    }

    /**
     * 根据id获取主题帖
     *
     * @param postId
     * @param token
     * @return
     */
    @RequestMapping(value = "ucenter/post/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<UcenterPostDomain> getPostById(@PathVariable("id") Long postId, @RequestHeader(APIConstants.HEADER_TOKEN) String token) {
        if (logger.isInfoEnabled()) {
            logger.info("获取主题帖 [" + postId + "]");
        }
        return ucenterPostManager.getPostById(postId, token);
    }

    /**
     * 根据id修改主题帖（ 题目 | 内容）
     *
     * @param postForm
     * @param bindingResult
     * @param postId
     * @return
     */
    @RequestMapping(value = "ucenter/post/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> updatePostById(@RequestBody @Valid PostForm postForm,
                                             BindingResult bindingResult,
                                             @PathVariable("id") long postId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 修改主题帖 [" + postForm + "]");
        }

        if (bindingResult.hasErrors()) {
            return new BaseResult<Object>(ErrorCodeEnum.ILLEGAL_PARAMETER,
                    bindingResult.getAllErrors());
        }

        UcenterPostDomain newPostDomain = new UcenterPostDomain();
        newPostDomain.setTitle(postForm.getTitle());
        newPostDomain.setContent(postForm.getContent());

        return ucenterPostManager.updatePostById(postId, newPostDomain, currentAccount().getId());
    }

    /**
     * 根据id删除主题帖
     *
     * @param postId
     * @return
     */
    @RequestMapping(value = "ucenter/post/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> deletePostById(@PathVariable("id") long postId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 删除主题帖 [" + postId + "]");
        }
        return ucenterPostManager.deletePostById(postId, currentAccount().getId());
    }

    /**
     * 新增一个回复
     *
     * @param form
     * @param bindingResult
     * @param request
     * @param postId
     * @return
     */
    @RequestMapping(value = "ucenter/post/{id}/reply", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<UcenterPostReplyDomain> addReply(@Valid @RequestBody PostReplyForm form, BindingResult bindingResult, HttpServletRequest request, @PathVariable("id") long postId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 创建回复 [" + form + "]");
        }
        UcenterPostReplyDomain replyDomain = form.convert2Domain();

        replyDomain.setAccountId(currentAccount().getId());
        Long replyToId = null;
        if (StringUtil.isNotBlank(request.getParameter("replyToId"))) {
            replyToId = Long.parseLong(request.getParameter("replyToId"));
        }
        return ucenterPostManager.addReply(postId, replyDomain, replyToId);
    }

    /**
     * 获取给定post的所有直接回复，并设置回复的回复
     *
     * @param postId
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/post/{id}/replys", method = RequestMethod.GET, params = "scope=all")
    @ResponseBody
    public BaseResult<List<UcenterPostReplyDomain>> getAllReplys(@PathVariable("id") long postId, HttpServletRequest request, @RequestHeader(APIConstants.HEADER_TOKEN) String token) {
        if (logger.isInfoEnabled()) {
            logger.info("获取给定post的所有直接回复，并设置回复的回复");
        }
        return ucenterPostManager.getAllReplys(postId, token);
    }

    /**
     * 分页获取给定post的所有直接回复，并设置回复的回复
     *
     * @param postId
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/post/{id}/replys", method = RequestMethod.GET, params = "scope=list")
    @ResponseBody
    public PageResult<List<UcenterPostReplyDomain>> listReplys(@PathVariable("id") long postId, HttpServletRequest request, @RequestHeader(APIConstants.HEADER_TOKEN) String token) {
        if (logger.isInfoEnabled()) {
            logger.info("分页获取给定post的所有直接回复，并设置回复的回复");
        }
        PostReplyQuery prq = new PostReplyQuery();
        prq = new PostReplyQuery();
        prq.setPostId(postId);
        prq.setPageSize(DEFAULT_PAGE_SIZE);
        if (StringUtil.isNotBlank(request.getParameter("pageNum"))) {
            prq.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
        }

        return ucenterPostManager.getListReplys(prq, token);
    }

    /**
     * 根据id获取回复
     *
     * @param replyId
     * @return
     */
    @RequestMapping(value = "ucenter/reply/{id}", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<UcenterPostReplyDomain> getPostReplyById(@PathVariable("id") long replyId) {
        if (logger.isInfoEnabled()) {
            logger.info("获取回复 [" + replyId + "]");
        }
        UcenterPostReplyDomain domain = ucenterPostService.getPostReplyById(replyId);
        return new BaseResult<>(domain);
    }

    /**
     * 根据id更新回复(内容)
     *
     * @param form
     * @param bindingResult
     * @param replyId
     * @return
     */
    @RequestMapping(value = "ucenter/reply/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> updateReplyById(@RequestBody @Valid PostReplyForm form,
                                              BindingResult bindingResult,
                                              @PathVariable("id") long replyId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 更新回复 [" + form + "]");
        }
        if (bindingResult.hasErrors()) {
            return new BaseResult<Object>(ErrorCodeEnum.ILLEGAL_PARAMETER,
                    bindingResult.getAllErrors());
        }

        UcenterPostReplyDomain newPostReplyDomain = new UcenterPostReplyDomain();
        newPostReplyDomain.setContent(form.getContent());
        return ucenterPostManager.updateReplyById(replyId, newPostReplyDomain, currentAccount().getId());

    }

    /**
     * 根据id删除回复
     *
     * @param replyId
     * @return
     */
    @RequestMapping(value = "ucenter/reply/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> deletePostReplyById(@PathVariable("id") long replyId) {
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 删除回复 [" + replyId + "]");
        }
        return ucenterPostManager.deleteReplyById(replyId, currentAccount().getId());
    }

    /**
     * 搜索post
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/search", method = RequestMethod.GET, params = "scope=post")
    @ResponseBody
    @LoginRequired
    public BaseResult<List<UcenterPostDomain>> searchPost(HttpServletRequest request) {
        if (!StringUtil.isNotBlank(request.getParameter("keyword")))
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL);

        String keyword = request.getParameter("keyword");

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "搜索post [" + keyword + "]");
        }

        return new BaseResult<>(ucenterPostService.searchPost(keyword));
    }

    /**
     * 搜索reply
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/search", method = RequestMethod.GET, params = "scope=reply")
    @ResponseBody
    @LoginRequired
    public BaseResult<List<UcenterPostReplyDomain>> searchReply(HttpServletRequest request) {
        if (!StringUtil.isNotBlank(request.getParameter("keyword")))
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL);

        String keyword = request.getParameter("keyword");

        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "搜索reply [" + keyword + "]");
        }

        return new BaseResult<>(ucenterPostService.searchReply(keyword));
    }

    // ~ manager

    /**
     * 置顶主题帖 - 未校验管理员权限
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "ucenter/post/{id}/stick", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> stickPostById(@PathVariable("id") long id, HttpServletRequest request) {
        UcenterPostDomain postDomain = ucenterPostService.getPostById(id);
        if (!StringUtil.isNotBlank(request.getParameter("isSticky"))) {
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL);
        }
        postDomain.setIsSticky(Boolean.parseBoolean(request.getParameter("isSticky")));
        int rc = ucenterPostService.updatePost(postDomain);
        return checkReturnCode(rc);
    }


    // ~ private methods

    /**
     * Check return code.
     *
     * @param rc the rc
     * @return true, if successful
     */
    private BaseResult<Object> checkReturnCode(int rc) {
        if (rc > 0) {
            return new BaseResult<>(true);
        } else {
            return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL, false);
        }
    }

}
