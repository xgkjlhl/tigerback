/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPaymentStatusEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.Date;

/**
 * 还款列表模型类
 *
 * @author yiliang.gyl
 * @version v 0.1 Oct 4, 2015 8:28:24 PM yiliang.gyl Exp $
 */
public class LoanPaybackListDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 5150638516903861726L;

    private String process;

    private String keyId;

    private Long loanId;

    private Date createTime;

    private Date updateTime;

    @CopyIgnore
    private LoanActionEnum actionType;

    @CopyIgnore
    private LoanStatusEnum status;

    private Date deadLine;

    private Double amount;

    @CopyIgnore
    private Integer overdueDay = 0;

    @CopyIgnore
    private LoanPaymentStatusEnum paymentStatus;

    /**
     * Getter method for property <tt>process</tt>.
     *
     * @return property value of process
     */
    public String getProcess() {
        return process;
    }

    /**
     * Setter method for property <tt>process</tt>.
     *
     * @param process value to be assigned to property process
     */
    public void setProcess(String process) {
        this.process = process;
    }

    /**
     * Getter method for property <tt>keyId</tt>.
     *
     * @return property value of keyId
     */
    public String getKeyId() {
        return keyId;
    }

    /**
     * Setter method for property <tt>keyId</tt>.
     *
     * @param keyId value to be assigned to property keyId
     */
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }


    /**
     * Getter method for property <tt>loanId</tt>.
     *
     * @return property value of loanId
     */
    public Long getLoanId() {
        return loanId;
    }

    /**
     * Setter method for property <tt>loanId</tt>.
     *
     * @param loanId value to be assigned to property loanId
     */
    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    /**
     * Getter method for property <tt>actionType</tt>.
     *
     * @return property value of actionType
     */
    public LoanActionEnum getActionType() {
        return actionType;
    }

    /**
     * Setter method for property <tt>actionType</tt>.
     *
     * @param actionType value to be assigned to property actionType
     */
    public void setActionType(LoanActionEnum actionType) {
        this.actionType = actionType;
    }


    /**
     * Getter method for property <tt>deadLine</tt>.
     *
     * @return property value of deadLine
     */
    public Date getDeadLine() {
        return deadLine;
    }

    /**
     * Setter method for property <tt>deadLine</tt>.
     *
     * @param deadLine value to be assigned to property deadLine
     */
    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    /**
     * Getter method for property <tt>amount</tt>.
     *
     * @return property value of amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter method for property <tt>amount</tt>.
     *
     * @param amount value to be assigned to property amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter method for property <tt>createTime</tt>.
     *
     * @return property value of createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Setter method for property <tt>createTime</tt>.
     *
     * @param createTime value to be assigned to property createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Getter method for property <tt>updateTime</tt>.
     *
     * @return property value of updateTime
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Setter method for property <tt>updateTime</tt>.
     *
     * @param updateTime value to be assigned to property updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Getter method for property <tt>status</tt>.
     *
     * @return property value of status
     */
    public LoanStatusEnum getStatus() {
        return status;
    }

    /**
     * Setter method for property <tt>status</tt>.
     *
     * @param status value to be assigned to property status
     */
    public void setStatus(LoanStatusEnum status) {
        this.status = status;
    }

    /**
     * Getter method for property <tt>overdueDay</tt>.
     *
     * @return property value of overdueDay
     */
    public Integer getOverdueDay() {
        return overdueDay;
    }

    /**
     * Setter method for property <tt>overdueDay</tt>.
     *
     * @param overdueDay value to be assigned to property overdueDay
     */
    public void setOverdueDay(Integer overdueDay) {
        this.overdueDay = overdueDay;
    }

    public LoanPaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(LoanPaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
