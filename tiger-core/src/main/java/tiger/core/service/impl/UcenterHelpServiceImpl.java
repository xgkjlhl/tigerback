/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.UcenterHelpCatalogDO;
import tiger.common.dal.dataobject.UcenterHelpDO;
import tiger.common.dal.dataobject.example.UcenterHelpCatalogExample;
import tiger.common.dal.dataobject.example.UcenterHelpExample;
import tiger.common.dal.persistence.UcenterHelpCatalogMapper;
import tiger.common.dal.persistence.UcenterHelpMapper;
import tiger.common.dal.persistence.UcenterPostMapper;
import tiger.common.dal.persistence.UcenterPostReplyMapper;
import tiger.common.dal.query.HelpQuery;
import tiger.common.util.Paginator;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.UcenterHelpCatalogDomain;
import tiger.core.domain.UcenterHelpDomain;
import tiger.core.domain.convert.UcenterHelpCatalogConvert;
import tiger.core.domain.convert.UcenterHelpConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.UcenterHelpService;

import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 15/11/25 下午1:16 mi.li Exp $
 */
@Service
public class UcenterHelpServiceImpl implements UcenterHelpService {
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
     * The help mapper
     */
    @Autowired
    UcenterHelpMapper ucenterHelpMapper;

    /**
     * The catalog mapper
     */
    @Autowired
    UcenterHelpCatalogMapper ucenterHelpCatalogMapper;

    /**
     * 获取所有常用帮助
     *
     * @see UcenterHelpService#getCommonHelps()
     */
    @Override
    public List<UcenterHelpDomain> getCommonHelps() {
        UcenterHelpExample example = new UcenterHelpExample();
        example.createCriteria().andIsCommonEqualTo(true);

        List<UcenterHelpDomain> list = UcenterHelpConvert.convertDOsToDomains(ucenterHelpMapper.selectByExample(example), ucenterHelpCatalogMapper);

        return list;
    }

    /**
     * 获取所有helps，或指定catalog下地所有helps
     *
     * @see UcenterHelpService#getAllHelps(java.lang.Long)
     */
    @Override
    public List<UcenterHelpDomain> getAllHelps(Long catalogId) {
        UcenterHelpExample example = new UcenterHelpExample();
        if (null != catalogId)
            example.createCriteria().andCatalogIdEqualTo(catalogId);

        List<UcenterHelpDomain> list = UcenterHelpConvert.convertDOsToDomains(ucenterHelpMapper.selectByExample(example), ucenterHelpCatalogMapper);

        if (null == list)
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);

        return list;
    }

    /**
     * 分页获取所有helps，或指定catalog下地所有helps
     *
     * @see UcenterHelpService#getListHelps(tiger.common.dal.query.HelpQuery)
     */
    @Override
    public PageResult<List<UcenterHelpDomain>> getListHelps(HelpQuery query) {
        PageResult<List<UcenterHelpDomain>> results = new PageResult<>();

        UcenterHelpExample example = new UcenterHelpExample();
        if (null != query.getCatalogId())
            example.createCriteria().andCatalogIdEqualTo(query.getCatalogId());

        int totalItems = ucenterHelpMapper.countByExample(example);

        //分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setPage(query.getPageNum());
        paginator.setItemsPerPage(query.getPageSize());

        RowBounds rowBounds = new RowBounds(paginator.getBeginIndex() - 1,
                paginator.getItemsPerPage());
        results.setData(UcenterHelpConvert
                .convertDOsToDomains(ucenterHelpMapper.selectByExampleAndPage(example, rowBounds), ucenterHelpCatalogMapper));
        //results.setData(UcenterPostConvert
        //    .convertDOsToDomains(ucenterPostMapper.selectByExampleWithBLOBsAndPage(example, rowBounds)));
        results.setPaginator(paginator);
        return results;
    }

    /**
     * 根据id获取帮助
     *
     * @see UcenterHelpService#getHelpById(java.lang.Long)
     */
    @Override
    public UcenterHelpDomain getHelpById(Long helpId) {
        UcenterHelpDO ucenterHelpDO = ucenterHelpMapper.selectByPrimaryKey(helpId);
        if (null == ucenterHelpDO) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        return UcenterHelpConvert.convertDOToDomain(ucenterHelpDO);
    }

    /**
     * 获取所有catalogs
     *
     * @see UcenterHelpService#getAllCatalogs()
     */
    @Override
    public BaseResult<List<UcenterHelpCatalogDomain>> getAllCatalogs() {
        UcenterHelpCatalogExample example = new UcenterHelpCatalogExample();

        List<UcenterHelpCatalogDomain> list = UcenterHelpCatalogConvert.convertDOsToDomains(ucenterHelpCatalogMapper.selectByExample(example));

        if (null == list)
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, null);

        return new BaseResult<List<UcenterHelpCatalogDomain>>(list);
    }

    /**
     * 获取某一catalog
     *
     * @see UcenterHelpService#getCatalogById(Long)
     */
    @Override
    public UcenterHelpCatalogDomain getCatalogById(Long catalogId) {
        UcenterHelpCatalogDO catalogDO = ucenterHelpCatalogMapper.selectByPrimaryKey(catalogId);
        UcenterHelpCatalogDomain domain = UcenterHelpCatalogConvert.convertDOToDomain(catalogDO);
        return domain;
    }

    /**
     * 搜索help
     *
     * @see UcenterHelpService#searchHelp(java.lang.String)
     */
    @Override
    public BaseResult<List<UcenterHelpDomain>> searchHelp(String keyword) {
        List<UcenterHelpDO> helpDOList = ucenterHelpMapper.search("%" + keyword + "%");
        List<UcenterHelpDomain> helpDomainList = UcenterHelpConvert.convertDOsToDomains(helpDOList, ucenterHelpCatalogMapper);
        return new BaseResult<>(helpDomainList);
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

    // ~ manager

    /**
     * 新增帮助类别
     *
     * @see UcenterHelpService#addHelpCatalog(String)
     */
    public Long addHelpCatalog(String name) {
        UcenterHelpCatalogDO ucenterHelpCatalogDO = new UcenterHelpCatalogDO();
        ucenterHelpCatalogDO.setName(name);
        int rc = ucenterHelpCatalogMapper.insertSelective(ucenterHelpCatalogDO);
        return ucenterHelpCatalogDO.getId();
    }

    /**
     * 新增帮助
     *
     * @see UcenterHelpService#addHelp(tiger.core.domain.UcenterHelpDomain)
     */
    @Override
    public UcenterHelpDomain addHelp(UcenterHelpDomain help) {
        UcenterHelpDO ucenterHelpDO = UcenterHelpConvert.convertDomainToDO(help);
        int rc = ucenterHelpMapper.insertSelective(ucenterHelpDO);
        return getHelpById(ucenterHelpDO.getId());
    }

    /**
     * 根据id更新帮助
     *
     * @see UcenterHelpService#updateHelp(tiger.core.domain.UcenterHelpDomain)
     */
    @Override
    public int updateHelp(UcenterHelpDomain domain) {
        UcenterHelpDO ucenterHelpDO = UcenterHelpConvert.convertDomainToDO(domain);
        return ucenterHelpMapper.updateByPrimaryKeySelective(ucenterHelpDO);
    }

    /**
     * 根据id删除帮助
     *
     * @see UcenterHelpService#deleteHelp(long)
     */
    @Override
    public int deleteHelp(long id) {
        //检查help是否存在
        getHelpById(id);
        return ucenterHelpMapper.deleteByPrimaryKey(id);
    }

}
