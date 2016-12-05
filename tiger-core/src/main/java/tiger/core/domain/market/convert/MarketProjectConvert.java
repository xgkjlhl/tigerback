/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.market.convert;

import tiger.common.dal.dataobject.MarketProjectDO;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.market.ContactInfoDomain;
import tiger.core.domain.market.MarketProjectDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * 贷款项目相互转化工具
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 1:54 PM yiliang.gyl Exp $
 */
public class MarketProjectConvert {
    /**
     * Convert 2 domain
     *
     * @param source
     * @return
     */
    public static MarketProjectDomain convert2Domain(MarketProjectDO source) {
        if (null == source) {
            return null;
        }
        MarketProjectDomain target = new MarketProjectDomain();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        if (StringUtil.isNotBlank(source.getPawnType())) {
            target.setPawnType(LoanPawnTypeEnum.getEnumByCode(source.getPawnType()));
        }
        if (StringUtil.isNotBlank(source.getContactInfo())) {
            target.setContactInfo(ContactInfoDomain.deserilized(source.getContactInfo()));
        }
        return target;
    }

    /**
     * Convert 2 domain.
     *
     * @param sourceList the source list
     * @return the list
     */
    public static List<MarketProjectDomain> convert2Domain(List<MarketProjectDO> sourceList) {
        List<MarketProjectDomain> targetList = new ArrayList<>();
        for (MarketProjectDO source : sourceList) {
            targetList.add(convert2Domain(source));
        }
        return targetList;
    }

    /**
     * Convert 2 do.
     *
     * @param source the source
     * @return the customer do
     */
    public static MarketProjectDO convert2DO(MarketProjectDomain source) {
        MarketProjectDO target = new MarketProjectDO();
        BeanUtil.copyPropertiesWithIgnores(source, target);
        if (source.getPawnType() != null) {
            target.setPawnType(source.getPawnType().getCode());
        }
        if (source.getContactInfo() != null) {
            target.setContactInfo(source.getContactInfo().serilized());
        }
        return target;
    }

    /**
     * Convert 2 domain.
     *
     * @param sourceList the source list
     * @return the list
     */
    public static List<MarketProjectDO> convert2Dos(List<MarketProjectDomain> sourceList) {
        List<MarketProjectDO> targetList = new ArrayList<>();
        for (MarketProjectDomain source : sourceList) {
            targetList.add(convert2DO(source));
        }
        return targetList;
    }
}
