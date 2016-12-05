/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.PostQuery;
import tiger.common.dal.query.PostReplyQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.UcenterPostDomain;
import tiger.core.domain.UcenterPostReplyDomain;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 2015年9月27日 上午9:55:52 mi.li Exp $
 */
public interface UcenterPostService {
    /**
     * 新增一个主题帖
     *
     * @param post the post
     * @return the post
     */
    UcenterPostDomain addPost(UcenterPostDomain post);

    /**
     * 获取post的回复数
     *
     * @param postId
     * @return
     */
    Integer getReplyCount(Long postId);

    /**
     * 获取所有置顶主题帖
     *
     * @return
     */
    List<UcenterPostDomain> getStickyPosts();

    /**
     * 获取给定post的最后回复时间
     *
     * @param postId
     * @return
     */
    Timestamp getLastReplyTime(Long postId);

    /**
     * 获取所有非置顶post
     *
     * @param
     * @return
     */
    List<UcenterPostDomain> getAllPosts();

    /**
     * 分页获取所有非置顶post
     *
     * @param query
     * @return
     */
    PageResult<List<UcenterPostDomain>> listPosts(PostQuery query);

    /**
     * 根据id获取主题贴
     *
     * @param id the post id
     * @return the post domain
     */
    UcenterPostDomain getPostById(Long id);

    /**
     * 根据id修改主题帖（ 题目 | 内容）
     *
     * @param postDomain
     * @return
     */
    int updatePost(UcenterPostDomain postDomain);

    /**
     * 删除主题帖下的所有回复
     *
     * @param postId
     * @return
     */
    int deleteReplysByPostId(Long postId);

    /**
     * 根据id删除主题帖
     *
     * @param id
     * @return
     */
    int deletePost(Long id);

    /**
     * 新增一个回复
     *
     * @param replyDomain
     * @return
     */
    UcenterPostReplyDomain addReply(UcenterPostReplyDomain replyDomain);

    /**
     * 获取某一回复下的所有回复
     *
     * @param replyId
     * @return
     */
    List<UcenterPostReplyDomain> getReplysToReply(Long replyId);

    /**
     * 获取给定post的所有直接回复
     *
     * @return
     */
    List<UcenterPostReplyDomain> getAllReplys(Long replyId);

    /**
     * 分页获取给定post的所有直接回复
     *
     * @param query
     * @return
     */
    PageResult<List<UcenterPostReplyDomain>> getListReplys(PostReplyQuery query);

    /**
     * 根据id获取回复
     *
     * @param id
     * @return
     */
    UcenterPostReplyDomain getPostReplyById(Long id);

    /**
     * 根据id更新回复
     *
     * @param domain
     * @return
     */
    int updatePostReply(UcenterPostReplyDomain domain);

    /**
     * 删除所有回复给定reply的replys
     *
     * @param replyToId
     * @return
     */
    int deleteReplysByReplyToId(Long replyToId);

    /**
     * 根据id删除回复
     *
     * @param id
     * @return
     */
    int deletePostReply(Long id);

    /**
     * 搜索post
     *
     * @param keyword
     * @return
     */
    List<UcenterPostDomain> searchPost(String keyword);

    /**
     * 搜索reply
     *
     * @param keyword
     * @return
     */
    List<UcenterPostReplyDomain> searchReply(String keyword);
}
