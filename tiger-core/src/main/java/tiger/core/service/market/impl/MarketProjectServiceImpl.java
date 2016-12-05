/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.market.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.MarketProjectDO;
import tiger.common.dal.dataobject.example.MarketProjectExample;
import tiger.common.dal.persistence.MarketProjectMapper;
import tiger.common.dal.query.MarketProjectQuery;
import tiger.common.dal.query.TopQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.market.MarketProjectDomain;
import tiger.core.domain.market.MarketProjectPropertyDomain;
import tiger.core.domain.market.convert.MarketProjectConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.market.MarketProjectService;

import java.util.List;

/**
 * 市场项目实现类
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:09 PM yiliang.gyl Exp $
 */
@Service
public class MarketProjectServiceImpl implements MarketProjectService {

    @Autowired
    MarketProjectMapper marketProjectMapper;

    /**
     * @see MarketProjectService#create(MarketProjectDomain)
     */
    @Override
    public MarketProjectDomain create(MarketProjectDomain marketProjectDomain) {
        MarketProjectDO marketProjectDO = MarketProjectConvert.convert2DO(marketProjectDomain);
        int rc = marketProjectMapper.insert(marketProjectDO);
        if (rc > 0) {
            return read(marketProjectDO.getId());
        } else {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "新建项目失败，数据库异常");
        }
    }

    /**
     * @see MarketProjectService#delete(Long)
     */
    @Override
    public Boolean delete(Long id) {
        return marketProjectMapper.deleteByPrimaryKey(id) > 0;
    }

    /**
     * @see MarketProjectService#update(MarketProjectDomain)
     */
    @Override
    public Boolean update(MarketProjectDomain marketProject) {
        MarketProjectDO marketProjectDO = MarketProjectConvert.convert2DO(marketProject);
        return marketProjectMapper.updateByPrimaryKeySelective(marketProjectDO) > 0;
    }

    /**
     * @see MarketProjectService#read(Long)
     */
    @Override
    public MarketProjectDomain read(Long id) {
        return MarketProjectConvert.convert2Domain(marketProjectMapper.selectByPrimaryKey(id));
    }

    /**
     * @see MarketProjectService#isOwner(Long, Long)
     */
    @Override
    public Boolean isOwner(Long projectId, Long accountId) {
        MarketProjectExample example = new MarketProjectExample();
        example.createCriteria().andAccountIdEqualTo(accountId).andIdEqualTo(projectId);
        if (marketProjectMapper.countByExample(example) == 1) {
            return true;
        }
        return false;
    }

    /**
     * @see MarketProjectService#isExist(Long)
     */
    @Override
    public Boolean isExist(Long projectId) {
        MarketProjectDO marketProjectDO = marketProjectMapper.selectByPrimaryKey(projectId);
        if (marketProjectDO == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @see MarketProjectService#awesomeProject(Long, Long)
     */
    @Override
    public Boolean awesomeProject(Long accountId, Long projectId) {
        //1. 该方法应该支持一定的容错
        return marketProjectMapper.awesomeProject(accountId, projectId) > 0;
    }

    /**
     * @see MarketProjectService#deleteAwesomeProject(Long, Long)
     */
    @Override
    public Boolean deleteAwesomeProject(Long accountId, Long projectId) {
        return marketProjectMapper.removeProjectAwesome(accountId, projectId) > 0;
    }

    /**
     * @see MarketProjectService#countProjectAwesome(Long)
     */
    @Override
    public Integer countProjectAwesome(Long projectId) {
        return marketProjectMapper.countProjectAwesome(projectId);
    }

    /**
     * @see MarketProjectService#collectProject(Long, Long)
     */
    @Override
    public Boolean collectProject(Long accountId, Long projectId) {
        return marketProjectMapper.collectProject(accountId, projectId) > 0;
    }

    /**
     * @see MarketProjectService#collectedProjectIds(Long)
     */
    @Override
    public List<Long> collectedProjectIds(Long accountId) {
        return marketProjectMapper.getUserCollectedProjectIds(accountId);
    }

    /**
     * @see MarketProjectService#deleteCollectedProject(Long, Long)
     */
    @Override
    public Boolean deleteCollectedProject(Long accountId, Long projectId) {
        return marketProjectMapper.removeProjectCollected(accountId, projectId) > 0;
    }

    /**
     * @see MarketProjectService#findProjectByPage(MarketProjectQuery)
     */
    @Override
    public PageResult<List<MarketProjectDomain>> findProjectByPage(MarketProjectQuery query) {

        PageResult<List<MarketProjectDomain>> result = new PageResult<>();

        int totalItems = marketProjectMapper.countProjects(query);
        // 分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum()); // 目前选择的页码

        result.setData(MarketProjectConvert.convert2Domain(marketProjectMapper.queryProjects(query,
                paginator.getOffset(), paginator.getLength())));
        result.setPaginator(paginator);

        return result;
    }

    @Override
    public List<Long> getTopAccountIds(TopQuery query) {
        if (null == query) {
            return null;
        }

        return marketProjectMapper.selectTopAccountIds(query);
    }

    /**
     * @see  MarketProjectService#findProjectByPage(MarketProjectQuery, Long)
     */
    @Override
    public PageResult<List<MarketProjectDomain>> findProjectByPage(MarketProjectQuery query, Long accountId) {
        PageResult<List<MarketProjectDomain>> result = new PageResult<>();

        int totalItems = marketProjectMapper.countProjects(query);
        // 分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum()); // 目前选择的页码

        List<MarketProjectDomain> results = MarketProjectConvert.convert2Domain(marketProjectMapper.queryProjects(query,
                paginator.getOffset(), paginator.getLength()));

        for (MarketProjectDomain project : results){
            project.setProperty(getProjectProperty(project.getId(), accountId));
        }

        result.setData(results);
        result.setPaginator(paginator);

        return result;
    }

    /**
     * @see MarketProjectService#getProjectProperty(Long, Long)
     */
    @Override
    public MarketProjectPropertyDomain getProjectProperty(Long projectId, Long accountId) {
        MarketProjectPropertyDomain property = new MarketProjectPropertyDomain();
        if(accountId != null){
            property.setHasCollect(marketProjectMapper.getUserCollectedProjectIds(accountId).contains(projectId));
            property.setHasAwesome(marketProjectMapper.getAwesomedUserIds(projectId).contains(accountId));
        }
        property.setAwesomeCount(marketProjectMapper.countProjectAwesome(projectId));
        return property;
    }

    /**
     * @see MarketProjectService#getLatestProjects(TopQuery)
     */
    @Override
    public List<MarketProjectDomain> getLatestProjects(TopQuery topQuery) {
        if (topQuery == null) {
            return null;
        }

        return MarketProjectConvert.convert2Domain(marketProjectMapper.selectLatestProjects(topQuery));
    }
}
