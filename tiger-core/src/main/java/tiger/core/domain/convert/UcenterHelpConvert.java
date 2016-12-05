/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.UcenterHelpDO;
import tiger.common.dal.persistence.UcenterHelpCatalogMapper;
import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterHelpDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午9:26:08 mi.li Exp $
 */
public class UcenterHelpConvert {
    /**
     * @param domain
     * @return
     */
    public static UcenterHelpDO convertDomainToDO(UcenterHelpDomain domain) {
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        UcenterHelpDO ucenterHelpDO = new UcenterHelpDO();
        BeanUtil.copyPropertiesWithIgnores(domain, ucenterHelpDO);

        return ucenterHelpDO;
    }

    /**
     * @param ucenterHelpDO
     * @return
     */
    public static UcenterHelpDomain convertDOToDomain(UcenterHelpDO ucenterHelpDO) {
        if (null == ucenterHelpDO) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        UcenterHelpDomain domain = new UcenterHelpDomain();
        BeanUtil.copyPropertiesWithIgnores(ucenterHelpDO, domain);

        return domain;
    }

    public static List<UcenterHelpDomain> convertDOsToDomains(List<UcenterHelpDO> helpDOs, UcenterHelpCatalogMapper ucenterHelpCatalogMapper) {
        List<UcenterHelpDomain> domains = new ArrayList<>();
        if (!helpDOs.isEmpty()) {
            for (UcenterHelpDO helpDO : helpDOs) {
                UcenterHelpDomain domain = convertDOToDomain(helpDO);
                //get catalogName by catalogId
                domain.setCatalogName(ucenterHelpCatalogMapper.selectByPrimaryKey(domain.getCatalogId()).getName());
                domains.add(domain);
            }
        }
        return domains;
    }
}
