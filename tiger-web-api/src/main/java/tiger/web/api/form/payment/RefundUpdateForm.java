/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.payment;

import tiger.common.util.BeanUtil;
import tiger.core.domain.PaymentRefundDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

/**
 * @author mi.li
 * @version v 0.1 15/10/23 下午10:46 mi.li Exp $
 */
public class RefundUpdateForm extends BaseForm implements FormInterface {

    private Double totalFee;

    private String reason;

    private String notes;

    private String status;

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public PaymentRefundDomain convert2Domain() {
        PaymentRefundDomain domain = new PaymentRefundDomain();

        BeanUtil.copyPropertiesWithIgnores(this, domain);

        return domain;
    }
}
