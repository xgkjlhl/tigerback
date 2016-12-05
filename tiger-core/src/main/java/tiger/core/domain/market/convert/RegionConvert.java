/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.market.convert;

import tiger.common.dal.dataobject.RegionDO;
import tiger.core.domain.market.RegionDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 22:12 alfred_yuan Exp $
 */
public class RegionConvert {

    public static RegionDomain convert2Domain(RegionDO source) {
        if (source == null) {
            return null;
        }

        RegionDomain target = new RegionDomain();
        target.setCode(source.getCode());
        target.setId(source.getId());
        target.setName(source.getName());
        target.getFullName().append(source.getName());
        target.setParentId(source.getParentId());

        return target;
    }

    public static RegionDO convert2DO(RegionDomain source) {
        if (source == null) {
            return null;
        }

        RegionDO target = new RegionDO();
        target.setCode(source.getCode());
        target.setId(source.getId());
        target.setName(source.getName());
        target.setParentId(source.getParentId());

        return target;
    }

    public static List<RegionDomain> convert2Domains(List<RegionDO> sources) {
        if (sources == null) {
            return null;
        }

        List<RegionDomain> targets = new ArrayList<>(sources.size());

        for (RegionDO source : sources) {
            targets.add(convert2Domain(source));
        }

        return targets;
    }

}
