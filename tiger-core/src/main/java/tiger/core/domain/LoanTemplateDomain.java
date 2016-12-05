/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain;

import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.extraDomain.ExtraField;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 贷款模板模型类
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 19, 2015 11:49:09 AM yiliang.gyl Exp $
 */
public class LoanTemplateDomain extends BaseDomain {

    private static final long serialVersionUID = -1846962638252958927L;

    private Long accountId;

    private Timestamp createTime;

    private Timestamp updateTime;

    /**
     * 业务类型
     */
    @CopyIgnore
    private BusinessTypeEnum businessType;

    /**
     * 抵押类型
     */
    @CopyIgnore
    private LoanPawnTypeEnum type;

    /**
     * 还款类型
     */
    @CopyIgnore
    private LoanPayWayEnum payWay;

    private Integer payCircle;

    private Integer payTotalCircle;

    private Double amount;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 提前结清违约金率（千分之）
     */
    private Double penaltyRate;

    /**
     * 利率
     */
    private Double interestRate;

    /**
     * 履约保证金
     */
    private Double bondPerform;

    /**
     * 服务费用
     */
    private Double costService;

    /**
     * 其他费用
     */
    private Double costOther;

    /**
     * 暂收费用
     */
    private Double costTemp;

    private Date startDate;

    private Date finishDate;

    @CopyIgnore
    private Map<String, String> extParams = new HashMap<>();

    private Long workspaceId;

    @CopyIgnore
    private ExtraField costSchedule;

    @CopyIgnore
    private ExtraField costOtherFirst;

    @CopyIgnore
    private ExtraField costOtherSecond;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    public LoanPawnTypeEnum getType() {
        return type;
    }

    public void setType(LoanPawnTypeEnum type) {
        this.type = type;
    }

    public LoanPayWayEnum getPayWay() {
        return payWay;
    }

    public void setPayWay(LoanPayWayEnum payWay) {
        this.payWay = payWay;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Double getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Getter for property 'costSchedule'.
     *
     * @return Value for property 'costSchedule'.
     */
    public ExtraField getCostSchedule() {
        return costSchedule;
    }

    /**
     * Setter for property 'costSchedule'.
     *
     * @param costSchedule Value to set for property 'costSchedule'.
     */
    public void setCostSchedule(ExtraField costSchedule) {
        this.costSchedule = costSchedule;
    }

    /**
     * Getter for property 'costOtherFirst'.
     *
     * @return Value for property 'costOtherFirst'.
     */
    public ExtraField getCostOtherFirst() {
        return costOtherFirst;
    }

    /**
     * Setter for property 'costOtherFirst'.
     *
     * @param costOtherFirst Value to set for property 'costOtherFirst'.
     */
    public void setCostOtherFirst(ExtraField costOtherFirst) {
        this.costOtherFirst = costOtherFirst;
    }

    /**
     * Getter for property 'costOtherSecond'.
     *
     * @return Value for property 'costOtherSecond'.
     */
    public ExtraField getCostOtherSecond() {
        return costOtherSecond;
    }

    /**
     * Setter for property 'costOtherSecond'.
     *
     * @param costOtherSecond Value to set for property 'costOtherSecond'.
     */
    public void setCostOtherSecond(ExtraField costOtherSecond) {
        this.costOtherSecond = costOtherSecond;
    }
}
