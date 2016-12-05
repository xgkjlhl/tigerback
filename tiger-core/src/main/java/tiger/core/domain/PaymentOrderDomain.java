/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.OrderStatusEnum;
import tiger.common.dal.enums.PayMethodEnum;
import tiger.common.dal.enums.PayTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mi.li
 * @version : v 0.1 2015年10月19日 下午9:47 mi.li Exp $
 */
public class PaymentOrderDomain extends BaseDomain {

    private static final long serialVersionUID = -1581719911037495727L;

    private long accountId;

    private Double totalFee;

    private Double discount;

    private String notes;

    private PayTypeEnum payType;

    private List<PaymentObjectDomain> objects;

    @CopyIgnore
    private Timestamp createTime;

    @CopyIgnore
    private Timestamp updateTime;

    private OrderStatusEnum status;

    private Boolean isDeleted;

    private PayMethodEnum payMethod;

    private Long tradeNo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PaymentObjectDomain> getObjects() {
        return objects;
    }

    public void setObjects(List<PaymentObjectDomain> objects) {
        this.objects = objects;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public PayTypeEnum getPayType() {
        return payType;
    }

    public void setPayType(PayTypeEnum payType) {
        this.payType = payType;
    }

    public PayMethodEnum getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethodEnum payMethod) {
        this.payMethod = payMethod;
    }

    public Long getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Long tradeNo) {
        this.tradeNo = tradeNo;
    }
}
