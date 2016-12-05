/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.PaymentRefundDO;
import tiger.common.dal.enums.RefundStatusEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.PaymentRefundDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 15/10/23 下午9:19 mi.li Exp $
 */
public class PaymentRefundConvert {
    public static PaymentRefundDO convertDomainToDO(PaymentRefundDomain domain) {
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        PaymentRefundDO paymentRefundDO = new PaymentRefundDO();
        BeanUtil.copyPropertiesWithIgnores(domain, paymentRefundDO);
        paymentRefundDO.setStatus(domain.getStatus().getCode());

        return paymentRefundDO;
    }

    public static PaymentRefundDomain convertDOToDomain(PaymentRefundDO refundDO) {
        if (null == refundDO) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        PaymentRefundDomain domain = new PaymentRefundDomain();
        BeanUtil.copyPropertiesWithIgnores(refundDO, domain);
        domain.setStatus(RefundStatusEnum.getEnumByCode(refundDO.getStatus()));

        return domain;
    }

    public static List<PaymentRefundDomain> convertDOsToDomains(List<PaymentRefundDO> refundDOs) {
        List<PaymentRefundDomain> domains = new ArrayList<>();
        if (!refundDOs.isEmpty()) {
            for (PaymentRefundDO refundDO : refundDOs) {
                domains.add(convertDOToDomain(refundDO));
            }
        }
        return domains;
    }
}
