/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.payment;

import tiger.web.api.form.BaseForm;

import java.io.Serializable;

/**
 * @author mi.li
 * @version v 0.1 15/10/22 下午8:56 mi.li Exp $
 */
public class OrderUpdateForm extends BaseForm implements Serializable {

    private static final long serialVersionUID = -5416028055377925290L;

    private String status;
    private Double totalFee;
    private Double discount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
