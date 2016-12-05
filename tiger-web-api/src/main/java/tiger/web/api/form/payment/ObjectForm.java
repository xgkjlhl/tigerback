/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.payment;

import tiger.common.dal.enums.ObjectTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.PaymentObjectDomain;
import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author mi.li
 * @version : v 0.1 2015年10月19日 下午8:55 mi.li Exp $
 */
public class ObjectForm extends BaseForm implements Serializable {

    private static final long serialVersionUID = -4005249316930840541L;

    @CopyIgnore
    @NotNull(message = "商品类型不能为空")
    private String type;

    @NotNull(message = "商品在类型中的id不能为空")
    private Long idInType;

    @NotNull(message = "商品名称不能为空")
    private String subject;

    @NotNull(message = "商品单价不能为空")
    private Double price;

    private String showUrl;

    private String notes;

    @NotNull(message = "商品数量不能为空")
    private Integer quantity;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getIdInType() {
        return idInType;
    }

    public void setIdInType(Long idInType) {
        this.idInType = idInType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String show_url) {
        this.showUrl = show_url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PaymentObjectDomain convert2Domain() {
        PaymentObjectDomain domain = new PaymentObjectDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);
        domain.setType(ObjectTypeEnum.getEnumByCode(this.getType()));
        return domain;
    }
}
