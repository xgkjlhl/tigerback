/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.service.market.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.RegionDO;
import tiger.common.dal.dataobject.example.RegionExample;
import tiger.common.dal.persistence.RegionMapper;
import tiger.common.dal.query.RegionQuery;
import tiger.common.util.StringUtil;
import tiger.core.domain.market.RegionDomain;
import tiger.core.domain.market.convert.RegionConvert;
import tiger.core.service.market.RegionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author alfred_yuan
 * @version v 0.1 23:17 alfred_yuan Exp $
 */
@Service
public class RegionServiceImpl implements RegionService {

    private Logger logger = Logger.getLogger(RegionServiceImpl.class);

    @Autowired
    private RegionMapper regionMapper;

    /**
     * @see RegionService#queryPackedRegions(RegionQuery)
     */
    @Override
    public List<RegionDomain> queryPackedRegions(RegionQuery query) {
        if (logger.isInfoEnabled()) {
            logger.info("开始 region 的搜索,接收的参数为 [" + query + "]");
        }

        List<RegionDomain> result;

        // 根据搜索方向的不同, 进行查找
        if (query.getIsUpward()) {
            result = packRegions(RegionConvert.convert2Domains(regionMapper.upwardQuery(query)));
        } else {
            result = packRegions(RegionConvert.convert2Domains(regionMapper.downwardQuery(query)));
        }

        return result;
    }

    public List<RegionDomain> selectDominatedSupportedRegions(List<String> regionCodes) {
        if (logger.isInfoEnabled()) {
            logger.info("开始查找 [" + regionCodes + "] 及其治下的系统支持地区");
        }

        if (CollectionUtils.isEmpty(regionCodes)) {
            return new ArrayList<>();
        }

        RegionQuery query = new RegionQuery();
        query.setCodes(regionCodes);

        return RegionConvert.convert2Domains(regionMapper.selectDominatedSupportedRegions(query));
    }

    /**
     * @see RegionService#getSupportedRegions()
     */
    @Override
    public List<RegionDomain> getSupportedRegions() {
        if (logger.isInfoEnabled()) {
            logger.info("开始查找系统支持的region");
        }
        return packRegions(RegionConvert.convert2Domains(regionMapper.getSupportedRegions()));
    }

    /**
     * @see RegionService#selectSupportedRegionByCodes(List)
     */
    @Override
    public List<RegionDomain> selectSupportedRegionByCodes(List<String> regionCodes) {
        if (logger.isInfoEnabled()) {
            logger.info("开始按地区代码搜索,接收的参数为 [" + regionCodes + "]");
        }
        if (CollectionUtils.isEmpty(regionCodes)) {
            return new ArrayList<>();
        }

        List<RegionDomain> result = RegionConvert.convert2Domains(regionMapper.selectSupportedRegionByCode(regionCodes));

        return result;
    }

    /**
     * @see RegionService#isExist(String)
     */
    @Override
    public boolean isExist(String code) {
        if (StringUtil.isBlank(code)) {
            return false;
        }

        RegionExample regionExample = new RegionExample();
        regionExample.createCriteria().andCodeEqualTo(code);

        List<RegionDO> regionDOs = regionMapper.selectByExample(regionExample);

        return CollectionUtils.isNotEmpty(regionDOs);
    }

    /**
     * 将 sources 的地区按照层级关系 映射 为树状结构
     *
     * @param sources
     * @return
     */
    private List<RegionDomain> packRegions(List<RegionDomain> sources) {
        if (sources == null) {
            return null;
        }

        Map<Long, RegionDomain> parentMap = new HashMap<>(sources.size());
        List<Long> idList = new ArrayList<>(sources.size());
        RegionDomain regionDomain, parentDomain;
        boolean mergeFlag;

        // 放入Map,方便查找
        sources.forEach(source -> parentMap.put(source.getId(), source));

        do {
            // 置继续合并flag为false
            mergeFlag = false;

            for (Map.Entry<Long, RegionDomain> entry : parentMap.entrySet()) {
                regionDomain = entry.getValue();
                parentDomain = parentMap.get(regionDomain.getParentId());
                if (parentDomain != null) {
                    // 设置regionDomain的fullName
                    regionDomain.getFullName().insert(0, parentDomain.getFullName() + "-");

                    parentDomain.getChildren().add(regionDomain);

                    idList.add(regionDomain.getId());
                    // 当前Map中存在父子关系
                    // 置继续合并flag为false
                    mergeFlag = true;
                }
            }

            // 清空map和list
            idList.forEach(parentMap::remove);
            idList.clear();
        } while (mergeFlag); // 如果之前的Map中存在父子关系,则继续合并

        return new ArrayList<>(parentMap.values());
    }

}
