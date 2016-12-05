/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 ALL Rights Reserved
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.PaymentObjectDO;
import tiger.common.dal.enums.ObjectTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.PaymentObjectDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 23:47 mi.li Exp $
 */
public class PaymentObjectConvert {
    public static List<PaymentObjectDO> convertDomainsToDOs(List<PaymentObjectDomain> domains) {
        List<PaymentObjectDO> objectDOs = new ArrayList<>();
        if (!domains.isEmpty()) {
            for (PaymentObjectDomain domain : domains) {
                objectDOs.add(convertDomainToDO(domain));
            }
        }
        return objectDOs;
    }

    public static PaymentObjectDO convertDomainToDO(PaymentObjectDomain domain) {
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        PaymentObjectDO objectDO = new PaymentObjectDO();
        BeanUtil.copyPropertiesWithIgnores(domain, objectDO);
        objectDO.setType(domain.getType().getCode());
        return objectDO;
    }

    public static PaymentObjectDomain convertDOToDomain(PaymentObjectDO objectDO) {
        if (null == objectDO)
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        PaymentObjectDomain domain = new PaymentObjectDomain();
        BeanUtil.copyPropertiesWithIgnores(objectDO, domain);
        domain.setType(ObjectTypeEnum.getEnumByCode(objectDO.getType()));

        return domain;
    }
}
