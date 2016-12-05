/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.ObjectTypeEnum;
import tiger.core.base.BaseDomain;

/**
 * @author mi.li
 * @version $ID: v 0.1 下午9:51 mi.li Exp $
 */
public class PaymentObjectDomain extends BaseDomain {

    private static final long serialVersionUID = -4226172463059406965L;

    private ObjectTypeEnum type;

    private Long idInType;

    private String subject;

    private Double price;

    private String showUrl;

    private String notes;

    private Integer quantity;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectTypeEnum getType() {
        return type;
    }

    public void setType(ObjectTypeEnum type) {
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

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
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

}
