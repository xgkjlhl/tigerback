/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.market;

import tiger.common.dal.query.MarketProjectQuery;
import tiger.common.dal.query.TopQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.market.MarketProjectDomain;
import tiger.core.domain.market.MarketProjectPropertyDomain;

import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:08 PM yiliang.gyl Exp $
 */
public interface MarketProjectService {

    /**
     * 新建需要市场项目
     *
     * @param marketProjectDomain
     * @return
     */
    MarketProjectDomain create(MarketProjectDomain marketProjectDomain);

    /**
     * 删除一个项目
     *
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 更新项目
     *
     * @param marketProject
     * @return
     */
    Boolean update(MarketProjectDomain marketProject);

    /**
     * 通过id获取一个项目
     *
     * @param id
     * @return
     */
    MarketProjectDomain read(Long id);

    /**
     * 判断是否是本人的项目
     *
     * @param projectId
     * @param accountId
     * @return
     */
    Boolean isOwner(Long projectId, Long accountId);

    /**
     * 判断项目是否存在
     *
     * @param projectId
     * @return
     */
    Boolean isExist(Long projectId);

    /**
     * 添加一个赞
     *
     * @param accountId
     * @param projectId
     * @return
     */
    Boolean awesomeProject(Long accountId, Long projectId);

    /**
     * 删除一个赞
     *
     * @param accountId
     * @param projectId
     * @return
     */
    Boolean deleteAwesomeProject(Long accountId, Long projectId);

    /**
     * 计算project的赞数目
     *
     * @param projectId
     * @return
     */
    Integer countProjectAwesome(Long projectId);

    /**
     * 收藏一个项目
     *
     * @param accountId
     * @param projectId
     * @return
     */
    Boolean collectProject(Long accountId, Long projectId);

    /**
     * 获取用户收藏项目的id
     *
     * @param accountId
     * @return
     */
    List<Long> collectedProjectIds(Long accountId);

    /**
     * 取消收藏
     * @param accountId
     * @param projectId
     * @return
     */
    Boolean deleteCollectedProject(Long accountId, Long projectId);

    /**
     * 分页获取项目
     *
     * @param query
     * @return
     */
    PageResult<List<MarketProjectDomain>> findProjectByPage(MarketProjectQuery query);

    /**
     * 获取活跃用户的is
     *
     * @return
     */
    List<Long> getTopAccountIds(TopQuery query);

    /**
     * 分页获取项目
     *
     * @param query
     * @return
     */
    PageResult<List<MarketProjectDomain>> findProjectByPage(MarketProjectQuery query, Long accountId);

    /**
     * 获取项目的属性信息
     *
     * @param projectId
     * @param accountId
     * @return
     */
    MarketProjectPropertyDomain getProjectProperty(Long projectId, Long accountId);

    /**
     *
     * @param topQuery
     * @return
     */
    List<MarketProjectDomain> getLatestProjects(TopQuery topQuery);
}
