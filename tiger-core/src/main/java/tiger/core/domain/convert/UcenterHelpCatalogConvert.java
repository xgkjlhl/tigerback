/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.UcenterHelpCatalogDO;
import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterHelpCatalogDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 2015年10月1日 上午11:11:10 mi.li Exp $
 */
public class UcenterHelpCatalogConvert {
    public static UcenterHelpCatalogDomain convertDOToDomain(UcenterHelpCatalogDO ucenterHelpCatalogDO) {
        if (null == ucenterHelpCatalogDO) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        UcenterHelpCatalogDomain domain = new UcenterHelpCatalogDomain();
        BeanUtil.copyPropertiesWithIgnores(ucenterHelpCatalogDO, domain);

        return domain;
    }

    public static List<UcenterHelpCatalogDomain> convertDOsToDomains(List<UcenterHelpCatalogDO> ucenterHelpCatalogDOs) {
        List<UcenterHelpCatalogDomain> domains = new ArrayList<>();
        if (!ucenterHelpCatalogDOs.isEmpty()) {
            for (UcenterHelpCatalogDO ucenterHelpCatalogDO : ucenterHelpCatalogDOs) {
                domains.add(convertDOToDomain(ucenterHelpCatalogDO));
            }
        }
        return domains;
    }
}
