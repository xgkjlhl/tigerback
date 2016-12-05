/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.CustomerMsgBizTypeEnum;
import tiger.common.dal.enums.SmsStatusEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 客户消息
 *
 * @author HuPeng
 */
public class CustomerMsgDomain extends BaseDomain {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2909645170826342607L;

    /**
     * The content.
     */
    private String content;

    @CopyIgnore
    private SmsStatusEnum status;

    /**
     * The send time.
     */
    private Date sendTime;

    /**
     * The customer id.
     */
    private Long customerId;

    /**
     * The create time.
     */
    private Timestamp createTime;

    /**
     * The update time.
     */
    private Timestamp updateTime;

    private Long senderId;

    private String customerName;

    private String customerMobile;

    private String outBizId;

    @CopyIgnore
    private CustomerMsgBizTypeEnum bizType;

    public String getOutBizId() {
        return outBizId;
    }

    public void setOutBizId(String outBizId) {
        this.outBizId = outBizId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public CustomerMsgBizTypeEnum getBizType() {
        return bizType;
    }

    public void setBizType(CustomerMsgBizTypeEnum bizType) {
        this.bizType = bizType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content the new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    public SmsStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SmsStatusEnum status) {
        this.status = status;
    }

    /**
     * Gets the send time.
     *
     * @return the send time
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * Sets the send time.
     *
     * @param sendTime the new send time
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * Gets the customer id.
     *
     * @return the customer id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer id.
     *
     * @param customerId the new customer id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the creates the time.
     *
     * @return the creates the time
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * Sets the creates the time.
     *
     * @param createTime the new creates the time
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the update time.
     *
     * @return the update time
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the update time.
     *
     * @param updateTime the new update time
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
