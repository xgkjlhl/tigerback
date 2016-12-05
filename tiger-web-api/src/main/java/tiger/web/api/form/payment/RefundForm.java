/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 ALL Rights Reserved
 */
package tiger.web.api.form.payment;

import tiger.common.util.BeanUtil;
import tiger.core.domain.PaymentRefundDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * @author mi.li
 * @version v 0.1 14:56 mi.li Exp $
 */
public class RefundForm extends BaseForm implements FormInterface {
    private static final long serialVersionUID = -22019035468902985L;

    @NotNull(message = "退款金额不能为空")
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
