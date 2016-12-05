/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 ALL Rights Reserved
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.PaymentOrderDO;
import tiger.common.dal.enums.OrderStatusEnum;
import tiger.common.dal.enums.PayTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.PaymentOrderDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

/**
 * @author mi.li
 * @version v 0.1 23:47 mi.li Exp $
 */
public class PaymentOrderConvert {
    public static PaymentOrderDO convertDomainToDO(PaymentOrderDomain domain) {
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        PaymentOrderDO paymentOrderDO = new PaymentOrderDO();
        BeanUtil.copyPropertiesWithIgnores(domain, paymentOrderDO);
        paymentOrderDO.setStatus(domain.getStatus().getCode());
        paymentOrderDO.setPayType(domain.getPayType().getCode());
        return paymentOrderDO;
    }

    public static PaymentOrderDomain convertDOToDomain(PaymentOrderDO orderDO) {
        if (null == orderDO)
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        PaymentOrderDomain domain = new PaymentOrderDomain();
        BeanUtil.copyPropertiesWithIgnores(orderDO, domain);
        domain.setPayType(PayTypeEnum.getEnumByCode(orderDO.getPayType()));
        domain.setStatus(OrderStatusEnum.getEnumByCode(orderDO.getStatus()));
        return domain;
    }
}
