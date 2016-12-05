/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.core.base.BaseDomain;

import java.util.Date;

/**
 * @author alfred_yuan
 * @version ${ID}: v 0.1 12:26 alfred_yuan Exp $
 */
public class LoanRefinementLimitDomain extends BaseDomain {

    private static final long serialVersionUID = -2155269014797925975L;

    private Double amount;

    private Integer payTotalCircle;

    private Integer payPassedTime;

    private Date startDate;

    private LoanPayWayEnum payWay;

    private Double interestRate;

    private Integer payCircle;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPayTotalCircle() {
        return payTotalCircle;
    }

    public void setPayTotalCircle(Integer payTotalCircle) {
        this.payTotalCircle = payTotalCircle;
    }

    public Integer getPayPassedTime() {
        return payPassedTime;
    }

    public void setPayPassedTime(Integer payPassedTime) {
        this.payPassedTime = payPassedTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public LoanPayWayEnum getPayWay() {
        return payWay;
    }

    public void setPayWay(LoanPayWayEnum payWay) {
        this.payWay = payWay;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getPayCircle() {
        return payCircle;
    }

    public void setPayCircle(Integer payCircle) {
        this.payCircle = payCircle;
    }
}
