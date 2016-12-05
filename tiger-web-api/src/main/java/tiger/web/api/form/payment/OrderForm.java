/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.payment;

import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.PaymentObjectDomain;
import tiger.core.domain.PaymentOrderDomain;
import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mi.li
 * @version : v 0.1 2015年10月19日 下午8:10 mi.li Exp $
 */
public class OrderForm extends BaseForm implements Serializable {

    private static final long serialVersionUID = 1548293349124543201L;

    @NotNull(message = "订单总金额不能为空")
    private Double totalFee;

    private Double discount;

    private String notes;

    @CopyIgnore
    private String payType;

    @CopyIgnore
    @NotNull(message = "订单必须有关联商品")
    private List<ObjectForm> objects;

    private String status;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public List<ObjectForm> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectForm> objects) {
        this.objects = objects;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentOrderDomain convert2Domain() {
        PaymentOrderDomain orderDomain = new PaymentOrderDomain();
        BeanUtil.copyPropertiesWithIgnores(this, orderDomain);

        //转换objects
        List<PaymentObjectDomain> objects = new ArrayList<PaymentObjectDomain>();
        for (ObjectForm objectForm : this.getObjects()) {
            PaymentObjectDomain objectDomain = objectForm.convert2Domain();
            objects.add(objectDomain);
        }
        orderDomain.setObjects(objects);

        return orderDomain;
    }
}
