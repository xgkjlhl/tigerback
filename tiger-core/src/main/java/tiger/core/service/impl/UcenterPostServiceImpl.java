/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.UcenterPostDO;
import tiger.common.dal.dataobject.UcenterPostReplyDO;
import tiger.common.dal.dataobject.example.UcenterPostExample;
import tiger.common.dal.dataobject.example.UcenterPostReplyExample;
import tiger.common.dal.persistence.UcenterPostMapper;
import tiger.common.dal.persistence.UcenterPostReplyMapper;
import tiger.common.dal.query.PostQuery;
import tiger.common.dal.query.PostReplyQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.UcenterPostDomain;
import tiger.core.domain.UcenterPostReplyDomain;
import tiger.core.domain.convert.UcenterPostConvert;
import tiger.core.domain.convert.UcenterPostReplyConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.UcenterPostService;

import java.sql.Timestamp;
import java.util.List;

/**
 * The Class UcenterServiceImpl
 *
 * @author mi.li
 * @version v 0.1 2015年9月27日 上午9:56:21 mi.li Exp $
 */
@Service
public class UcenterPostServiceImpl implements UcenterPostService {

    /**
     * The post mapper.
     */
    @Autowired
    UcenterPostMapper ucenterPostMapper;

    /**
     * The reply mapper.
     */
    @Autowired
    UcenterPostReplyMapper ucenterPostReplyMapper;

    /**
     * 新增一个主题帖
     *
     * @see UcenterPostService#addPost(tiger.core.domain.UcenterPostDomain)
     */
    @Override
    public UcenterPostDomain addPost(UcenterPostDomain postDomain) {
        UcenterPostDO ucenterPostDO = UcenterPostConvert.convertDomainToDO(postDomain);
        int rc = ucenterPostMapper.insertSelective(ucenterPostDO);
        if (!checkReturnCode(rc))
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        return getPostById(ucenterPostDO.getId());
    }

    /**
     * 获取post的回复数
     *
     * @param postId
     * @return
     */
    @Override
    public Integer getReplyCount(Long postId) {
        UcenterPostReplyExample example = new UcenterPostReplyExample();

        UcenterPostReplyExample.Criteria criteria = example.createCriteria();
        criteria.andReplyToIdEqualTo((long) -1);
        criteria.andPostIdEqualTo(postId);

        return ucenterPostReplyMapper.countByExample(example);
    }

    /**
     * 获取所有置顶post
     *
     * @see UcenterPostService#getStickyPosts()
     */
    @Override
    public List<UcenterPostDomain> getStickyPosts() {
        UcenterPostExample example = new UcenterPostExample();
        example.createCriteria().andIsStickyEqualTo(true);

        List<UcenterPostDomain> list = UcenterPostConvert.convertDOsToDomains(ucenterPostMapper.selectByExample(example));

        return list;
    }

    /**
     * 获取给定post的最后回复时间
     *
     * @see UcenterPostService#getLastReplyTime(Long)
     */
    @Override
    public Timestamp getLastReplyTime(Long postId) {
        Timestamp lastReplyTime = new Timestamp(0);
        UcenterPostReplyExample example = new UcenterPostReplyExample();
        example.createCriteria().andPostIdEqualTo(postId);

        List<UcenterPostReplyDomain> list = UcenterPostReplyConvert.convertDOsToDomains(ucenterPostReplyMapper.selectByExample(example));

        for (UcenterPostReplyDomain domain : list) {
            if (domain.getUpdateTime().after(lastReplyTime)) {
                lastReplyTime = domain.getUpdateTime();
            }
        }
        UcenterPostDO postDO = ucenterPostMapper.selectByPrimaryKey(postId);
        if (lastReplyTime.equals(new Timestamp(0)))
            return postDO.getUpdateTime();
        return lastReplyTime;
    }

    /**
     * 获取所有非置顶post
     *
     * @see UcenterPostService#getAllPosts()
     */
    @Override
    public List<UcenterPostDomain> getAllPosts() {
        UcenterPostExample example = new UcenterPostExample();
        example.createCriteria().andIsStickyEqualTo(false);
        List<UcenterPostDomain> list = UcenterPostConvert.convertDOsToDomains(ucenterPostMapper.selectByExample(example));
        return list;
    }

    /**
     * 分页获取所有非置顶post
     *
     * @see UcenterPostService#listPosts(tiger.common.dal.query.PostQuery)
     */
    @Override
    public PageResult<List<UcenterPostDomain>> listPosts(PostQuery query) {
        PageResult<List<UcenterPostDomain>> results = new PageResult<>();
        UcenterPostExample example = new UcenterPostExample();
        example.createCriteria().andIsStickyEqualTo(false);
        example.setOrderByClause(SystemConstants.CREATE_TIME_DESC);

        int totleItems = ucenterPostMapper.countByExample(example);

        //分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totleItems);
        paginator.setPage(query.getPageNum());
        paginator.setItemsPerPage(query.getPageSize());

        RowBounds rowBounds = new RowBounds(paginator.getBeginIndex() - 1,
                paginator.getItemsPerPage());
        results.setData(UcenterPostConvert
                .convertDOsToDomains(ucenterPostMapper.selectByExampleAndPage(example, rowBounds)));
        //results.setData(UcenterPostConvert
        //    .convertDOsToDomains(ucenterPostMapper.selectByExampleWithBLOBsAndPage(example, rowBounds)));

        for (UcenterPostDomain domain : results.getData()) {
            domain.setLastReplyTime(getLastReplyTime(domain.getId()));
        }

        results.setPaginator(paginator);
        return results;
    }

    /**
     * 根据id获取主题贴
     *
     * @see UcenterPostService#getPostById(java.lang.Long)
     */
    @Override
    public UcenterPostDomain getPostById(Long id) {
        UcenterPostDO ucenterPostDO = ucenterPostMapper.selectByPrimaryKey(id);
        if (null == ucenterPostDO)
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        return UcenterPostConvert.convertDOToDomain(ucenterPostDO);
    }

    /**
     * 根据id修改主题帖（ 题目 | 内容）
     *
     * @see UcenterPostService#updatePost(tiger.core.domain.UcenterPostDomain)
     */
    @Override
    public int updatePost(UcenterPostDomain postDomain) {
        UcenterPostDO ucenterPostDO = UcenterPostConvert.convertDomainToDO(postDomain);
        return ucenterPostMapper.updateByPrimaryKeySelective(ucenterPostDO);
    }

    /**
     * 删除主题帖下的所有回复
     *
     * @param postId
     * @return
     */
    @Override
    public int deleteReplysByPostId(Long postId) {

        UcenterPostReplyExample example = new UcenterPostReplyExample();
        example.createCriteria().andPostIdEqualTo(postId);

        List<UcenterPostReplyDO> replysToDelete = ucenterPostReplyMapper.selectByExample(example);
        if (replysToDelete.size() != 0)
            return ucenterPostReplyMapper.batchDelete(replysToDelete);
        return 1;
    }

    /**
     * 根据id删除主题帖
     *
     * @see UcenterPostService#deletePost(Long)
     */
    @Override
    public int deletePost(Long id) {
        return ucenterPostMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增一个回复
     *
     * @see UcenterPostService#addReply(tiger.core.domain.UcenterPostReplyDomain)
     */
    @Override
    public UcenterPostReplyDomain addReply(UcenterPostReplyDomain replyDomain) {
        UcenterPostReplyDO ucenterPostReplyDO = UcenterPostReplyConvert.convertDomainToDO(replyDomain);
        int rc = ucenterPostReplyMapper.insertSelective(ucenterPostReplyDO);
        return getPostReplyById(ucenterPostReplyDO.getId());
    }

    /**
     * 获取某一回复下的所有回复
     *
     * @see UcenterPostService#getReplysToReply(Long)
     */
    @Override
    public List<UcenterPostReplyDomain> getReplysToReply(Long replyId) {
        UcenterPostReplyExample example = new UcenterPostReplyExample();
        example.createCriteria().andReplyToIdEqualTo(replyId);

        List<UcenterPostReplyDomain> list = UcenterPostReplyConvert.convertDOsToDomains(ucenterPostReplyMapper.selectByExample(example));

        return list;
    }

    /**
     * 获取给定post的所有直接回复
     *
     * @see UcenterPostService#getAllReplys(java.lang.Long)
     */
    @Override
    public List<UcenterPostReplyDomain> getAllReplys(Long id) {
        UcenterPostReplyExample example = new UcenterPostReplyExample();
        example.createCriteria().andPostIdEqualTo(id);

        List<UcenterPostReplyDomain> list = UcenterPostReplyConvert.convertDOsToDomains(ucenterPostReplyMapper.selectByExample(example));

        return list;
    }

    /**
     * 分页获取给定post的所有直接回复
     *
     * @see UcenterPostService#getListReplys(tiger.common.dal.query.PostReplyQuery)
     */
    @Override
    public PageResult<List<UcenterPostReplyDomain>> getListReplys(PostReplyQuery prq) {
        PageResult<List<UcenterPostReplyDomain>> results = new PageResult<>();

        UcenterPostReplyExample example = new UcenterPostReplyExample();

        UcenterPostReplyExample.Criteria criteria = example.createCriteria();
        criteria.andReplyToIdEqualTo((long) -1);
        criteria.andPostIdEqualTo(prq.getPostId());

        int totleItems = ucenterPostReplyMapper.countByExample(example);

        //分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totleItems);
        paginator.setPage(prq.getPageNum());
        paginator.setItemsPerPage(prq.getPageSize());

        RowBounds rowBounds = new RowBounds(paginator.getBeginIndex() - 1,
                paginator.getItemsPerPage());
        results.setData(UcenterPostReplyConvert
                .convertDOsToDomains(ucenterPostReplyMapper.selectByExampleAndPage(example, rowBounds)));
        //results.setData(UcenterPostConvert
        //    .convertDOsToDomains(ucenterPostMapper.selectByExampleWithBLOBsAndPage(example, rowBounds)));
        results.setPaginator(paginator);
        return results;
    }

    /**
     * 根据id获取回复
     *
     * @see UcenterPostService#getPostReplyById(java.lang.Long)
     */
    @Override
    public UcenterPostReplyDomain getPostReplyById(Long id) {
        UcenterPostReplyDO ucenterPostReplyDO = ucenterPostReplyMapper.selectByPrimaryKey(id);
        if (null != ucenterPostReplyDO) {
            return UcenterPostReplyConvert.convertDOToDomain(ucenterPostReplyDO);
        }
        throw new TigerException(ErrorCodeEnum.NOT_FOUND);
    }

    /**
     * 根据id更新回复
     *
     * @see UcenterPostService#updatePostReply(tiger.core.domain.UcenterPostReplyDomain)
     */
    @Override
    public int updatePostReply(UcenterPostReplyDomain domain) {
        UcenterPostReplyDO ucenterPostReplyDO = UcenterPostReplyConvert.convertDomainToDO(domain);
        return ucenterPostReplyMapper.updateByPrimaryKeySelective(ucenterPostReplyDO);
    }

    /**
     * 删除所有回复给定reply的replys
     *
     * @see UcenterPostService#deleteReplysByReplyToId(Long)
     */
    @Override
    public int deleteReplysByReplyToId(Long replyToId) {

        UcenterPostReplyExample example = new UcenterPostReplyExample();
        example.createCriteria().andReplyToIdEqualTo(replyToId);

        List<UcenterPostReplyDO> replysToDelete = ucenterPostReplyMapper.selectByExample(example);
        if (replysToDelete.size() != 0)
            return ucenterPostReplyMapper.batchDelete(replysToDelete);
        return 1;
    }

    /**
     * 根据id删除回复
     *
     * @see UcenterPostService#deletePostReply(Long)
     */
    @Override
    public int deletePostReply(Long id) {
        return ucenterPostReplyMapper.deleteByPrimaryKey(id);
    }

    /**
     * 搜索post
     *
     * @see UcenterPostService#searchPost(java.lang.String)
     */
    @Override
    public List<UcenterPostDomain> searchPost(String keyword) {
        List<UcenterPostDO> postDOList = ucenterPostMapper.search("%" + keyword + "%");
        List<UcenterPostDomain> postDomainList = UcenterPostConvert.convertDOsToDomains(postDOList);
        return postDomainList;
    }

    /**
     * 搜索reply
     *
     * @see UcenterPostService#searchReply(java.lang.String)
     */
    @Override
    public List<UcenterPostReplyDomain> searchReply(String keyword) {
        List<UcenterPostReplyDO> postReplyDOList = ucenterPostReplyMapper.search("%" + keyword + "%");
        List<UcenterPostReplyDomain> postReplyDomainList = UcenterPostReplyConvert.convertDOsToDomains(postReplyDOList);
        return postReplyDomainList;
    }

    // ~ private methods

    /**
     * Check return code.
     *
     * @param rc the rc
     * @return true, if successful
     */
    private boolean checkReturnCode(int rc) {
        if (rc > 0) {
            return true;
        } else {
            return false;
        }
    }

}
