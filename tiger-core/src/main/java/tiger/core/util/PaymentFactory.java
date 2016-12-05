/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.util;

import tiger.common.util.component.payment.Base.Payment;
import tiger.common.util.component.payment.Base.Refund;
import tiger.common.util.component.payment.alipay.PaymentAlipay;
import tiger.common.util.component.payment.alipay.RefundAlipay;

/**
 * @author mi.li
 * @version v 0.1 15/10/27 下午9:04 mi.li Exp $
 */
public class PaymentFactory {
    public static Payment createPayment(String payMethod) {
        Payment payment = null;
        switch (payMethod) {
            case "ALIPAY":
                payment = new PaymentAlipay();
                break;
            case "WECHATPAY":
                break;
        }
        return payment;
    }

    public static Refund createRefund(String payMethod) {
        Refund refund = null;
        switch (payMethod) {
            case "ALIPAY":
                refund = new RefundAlipay();
                break;
            case "WECHATPAY":
                break;
        }
        return refund;
    }
}
