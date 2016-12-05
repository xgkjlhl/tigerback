/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.UcenterPostDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterPostDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert Domain to DO.
 *
 * @author mi.li
 * @version v 0.1 2015年9月27日 上午9:33:25 mi.li Exp $
 */
public class UcenterPostConvert {
    /**
     * @param postDomain
     * @return
     */
    public static UcenterPostDO convertDomainToDO(UcenterPostDomain postDomain) {
        if (null == postDomain) {
            return null;
        }
        UcenterPostDO ucenterPostDO = new UcenterPostDO();
        BeanUtil.copyPropertiesWithIgnores(postDomain, ucenterPostDO);

        return ucenterPostDO;
    }

    /**
     * @param ucenterPostDO
     * @return
     */
    public static UcenterPostDomain convertDOToDomain(UcenterPostDO ucenterPostDO) {
        if (null == ucenterPostDO) {
            return null;
        }
        UcenterPostDomain postDomain = new UcenterPostDomain();
        BeanUtil.copyPropertiesWithIgnores(ucenterPostDO, postDomain);

        return postDomain;
    }

    /**
     * @param postDOs
     * @return
     */
    public static List<UcenterPostDomain> convertDOsToDomains(List<UcenterPostDO> postDOs) {
        List<UcenterPostDomain> domains = new ArrayList<>();
        if (!postDOs.isEmpty()) {
            for (UcenterPostDO postDO : postDOs) {
                domains.add(convertDOToDomain(postDO));
            }
        }
        return domains;
    }
}
