/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.InterestCalTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 合并过后的账单
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 9:26 AM yiliang.gyl Exp $
 */
public class LoanMergedBillDomain implements Serializable {

    private Long loanId;

    private List<LoanRecordDomain> bills;

    /**
     * 计算后的总额
     */
    private Double amount;

    /**
     * 是否结清
     */
    private boolean isEnd;

    /**
     * 计息方式
     */
    private InterestCalTypeEnum calTypeEnum;


    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public List<LoanRecordDomain> getBills() {
        return bills;
    }

    public void setBills(List<LoanRecordDomain> bills) {
        this.bills = bills;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public InterestCalTypeEnum getCalTypeEnum() {
        return calTypeEnum;
    }

    public void setCalTypeEnum(InterestCalTypeEnum calTypeEnum) {
        this.calTypeEnum = calTypeEnum;
    }
}
