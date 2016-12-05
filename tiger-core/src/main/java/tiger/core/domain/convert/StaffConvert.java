/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import org.apache.commons.collections.CollectionUtils;
import tiger.common.dal.dataobject.StaffDO;
import tiger.core.domain.StaffDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuPeng
 * @version v 0.1 2015年10月19日 下午4:05:40 HuPeng Exp $
 */
public class StaffConvert {

    /**
     * staff数据对象转换成模型
     *
     * @param source
     * @return
     */
    public static StaffDomain convert2Domain(StaffDO source) {
        if (source == null) {
            return null;
        }

        StaffDomain target = new StaffDomain();

        target.setId(source.getId());
        target.setIsDel(source.getIsDel());
        target.setUsername(source.getUsername());
        target.setPassword(source.getPassword());
        target.setAccountId(source.getAccountId());

        return target;
    }

    /**
     * 转换模型为staff数据
     *
     * @param source
     * @return
     */
    public static StaffDO convert2DO(StaffDomain source) {
        if (source == null) {
            return null;
        }

        StaffDO target = new StaffDO();

        target.setId(source.getId());
        target.setIsDel(source.getIsDel());
        target.setAccountId(source.getAccountId());
        target.setPassword(source.getPassword());
        target.setUsername(source.getUsername());

        return target;
    }

    /**
     * 转换一组staff数据为模型列表
     *
     * @param sources
     * @return
     */
    public static List<StaffDomain> convert2Domains(List<StaffDO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return new ArrayList<>();
        }

        List<StaffDomain> targets = new ArrayList<>(sources.size());

        sources.forEach(source -> targets.add(convert2Domain(source)));

        return targets;
    }

}
