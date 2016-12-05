/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.springframework.stereotype.Service;
import tiger.common.dal.query.HelpQuery;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.UcenterHelpCatalogDomain;
import tiger.core.domain.UcenterHelpDomain;

import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 15/11/25 下午1:14 mi.li Exp $
 */
@Service
public interface UcenterHelpService {

    /**
     * 获取所有常用帮助
     *
     * @return
     */
    List<UcenterHelpDomain> getCommonHelps();

    /**
     * 获取所有helps，或指定catalog下地所有helps
     *
     * @param catalogId
     * @return
     */
    List<UcenterHelpDomain> getAllHelps(Long catalogId);

    /**
     * 分页获取所有helps，或指定catalog下地所有helps
     *
     * @param query
     * @return
     */
    PageResult<List<UcenterHelpDomain>> getListHelps(HelpQuery query);

    /**
     * 根据id获取帮助
     *
     * @param helpId
     * @return
     */
    UcenterHelpDomain getHelpById(Long helpId);


    /**
     * 获取所有catalogs
     *
     * @return
     */
    BaseResult<List<UcenterHelpCatalogDomain>> getAllCatalogs();

    /**
     * 获取某一catalog
     *
     * @return
     */
    UcenterHelpCatalogDomain getCatalogById(Long catalogId);

    /**
     * 搜索help
     *
     * @param keyword
     * @return
     */
    BaseResult<List<UcenterHelpDomain>> searchHelp(String keyword);

    // ~ manager

    /**
     * 新增帮助类别
     *
     * @param name
     * @return
     */
    Long addHelpCatalog(String name);

    /**
     * 新增帮助
     *
     * @param help
     * @return
     */
    UcenterHelpDomain addHelp(UcenterHelpDomain help);

    /**
     * 根据id更新帮助
     *
     * @param domain
     * @return
     */
    int updateHelp(UcenterHelpDomain domain);

    /**
     * 根据id删除帮助
     *
     * @param id
     * @return
     */
    int deleteHelp(long id);

}
