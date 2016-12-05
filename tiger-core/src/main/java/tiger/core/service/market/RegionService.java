/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.service.market;

import tiger.common.dal.query.RegionQuery;
import tiger.core.domain.market.RegionDomain;

import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 23:14 alfred_yuan Exp $
 */
public interface RegionService {

    /**
     * 根据query中的条件进行搜索
     * 返回结果已经按照层级结构组织好了
     * @param query
     * @return
     */
    List<RegionDomain> queryPackedRegions(RegionQuery query);

    /**
     * 获取regionCodes治下的且系统支持的
     * @param regionCodes
     * @return
     */
    List<RegionDomain> selectDominatedSupportedRegions(List<String> regionCodes);

    /**
     * 获取当前所有支持的地区
     * 返回结果已经按照层级结构组织好了
     * @return
     */
    List<RegionDomain> getSupportedRegions();

    /**
     * 获取系统所支持的regionCodes对应的regionDomain列表
     * 返回结果未处理
     * @param regionCodes
     * @return
     */
    List<RegionDomain> selectSupportedRegionByCodes(List<String> regionCodes);

    /**
     * 查询code所代表的地区是否存在 在系统配置的region_all表中
     * @param code
     * @return
     */
    boolean isExist(String code);
}
