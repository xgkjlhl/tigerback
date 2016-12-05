/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import javax.persistence.Transient;
import java.util.*;

/**
 * 贷款调整记录领域模型
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:38 PM yiliang.gyl Exp $
 */
public class LoanRefineDomain extends BaseDomain {

    private static final long serialVersionUID = -8645665869138400875L;

    private Long loanId;

    /**
     * 调整日期
     */
    private Date refineDate;

    /**
     * 原合同开始日期
     */
    private Date startDate;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 利率
     */
    private Double interestRate;

    /**
     * 还款周期
     */
    private Integer payCircle;

    /**
     * 还款次数
     */
    private Integer payTotalCircle;

    /**
     * 已还款次数
     */
    private Integer paidTotalCircle;

    /**
     * 还款方式
     */
    @CopyIgnore
    private LoanPayWayEnum payWay;

    @CopyIgnore
    private List<LoanRecordDomain> recordDomains = new ArrayList<>();

    // 临时字段,存放调整合同总期限(单位:月)
    @Transient
    @CopyIgnore
    private Integer timeLimit;

    // 违约金利率(‰/天)
    private Double penaltyRate;

    // 履约保证金
    private Double bondPerform;

    // 手续费
    private Double costService;

    // 其他费用
    private Double costOther;

    // 暂收金额
    private Double costTemp;

    // 额外信息
    @CopyIgnore
    private Map<String, String> extParams = new HashMap<>();

    @Transient
    @CopyIgnore
    private Boolean isRefined;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Date getRefineDate() {
        return refineDate;
    }

    public void setRefineDate(Date refineDate) {
        this.refineDate = refineDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Integer getPayTotalCircle() {
        return payTotalCircle;
    }

    public void setPayTotalCircle(Integer payTotalCircle) {
        this.payTotalCircle = payTotalCircle;
    }

    public LoanPayWayEnum getPayWay() {
        return payWay;
    }

    public void setPayWay(LoanPayWayEnum payWay) {
        this.payWay = payWay;
    }

    public Integer getPaidTotalCircle() {
        return paidTotalCircle;
    }

    public void setPaidTotalCircle(Integer paidTotalCircle) {
        this.paidTotalCircle = paidTotalCircle;
    }

    public List<LoanRecordDomain> getRecordDomains() {
        return recordDomains;
    }

    public void setRecordDomains(List<LoanRecordDomain> recordDomains) {
        this.recordDomains = recordDomains;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Double getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public Double getBondPerform() {
        return bondPerform;
    }

    public void setBondPerform(Double bondPerform) {
        this.bondPerform = bondPerform;
    }

    public Double getCostService() {
        return costService;
    }

    public void setCostService(Double costService) {
        this.costService = costService;
    }

    public Double getCostOther() {
        return costOther;
    }

    public void setCostOther(Double costOther) {
        this.costOther = costOther;
    }

    public Double getCostTemp() {
        return costTemp;
    }

    public void setCostTemp(Double costTemp) {
        this.costTemp = costTemp;
    }

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }

    public Boolean getIsRefined() {
        return isRefined;
    }

    public void setIsRefined(Boolean isRefined) {
        this.isRefined = isRefined;
    }
}
