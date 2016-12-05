/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanPayWayEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.extraDomain.ExtraField;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Transient;

/**
 * 借贷项目模型
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 13, 2015 7:28:26 AM yiliang.gyl Exp $
 */
public class LoanDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -186727117163268956L;

    @CopyIgnore
    private Long id;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Long accountId;

    @CopyIgnore
    private BusinessTypeEnum businessType;

    @CopyIgnore
    private LoanPawnTypeEnum type;

    @CopyIgnore
    private LoanPayWayEnum payWay;

    @CopyIgnore
    private LoanStatusEnum loanStatus;

    private Double amount;

    private Integer monthsLimit;

    private Date startDate;

    private Date startPayDate;

    private Date finishDate;

    /**
     * 最近需要操作时间
     */
    private Date operateDate;

    /**
     * 项目正式开始时间：一般的放款时间
     */
    private Date launchDate;

    private Double interestRate;

    private Double penaltyRate;

    private Double bondPerform;

    private Double costService;

    private Double costOther;

    private Double costTemp;

    private Boolean hasHolder;

    @CopyIgnore
    private Map<String, String> extParams = new HashMap<String, String>();

    private Integer payCircle;

    private Integer payTotalCircle;

    private String keyId;

    /**
     * 贷款人
     */
    @CopyIgnore
    private CustomerDomain loaner;

    /**
     * 担保人
     */
    @CopyIgnore
    private CustomerDomain holder;

    /**
     * 抵押物
     */
    @CopyIgnore
    private LoanPawnDomain loanPawnDomain;

    /**
     * 创建者
     */
    @CopyIgnore
    private AccountBaseDomain ownerInfo;

    @CopyIgnore
    @JsonIgnore
    private List<AttachRelateDomain> attachRelateDomains;

    @CopyIgnore
    private List<AttachDomain> attachDomains;


    @Transient
    @CopyIgnore
    private Integer overDueNumbers = 0;

    @Transient
    @CopyIgnore
    private Integer badLoanNumbers = 0;

    @Transient
    @CopyIgnore
    private Integer currentPayment = 0;

    @CopyIgnore
    private String costOtherDesc;

    @CopyIgnore
    private String costTempDesc;

    @CopyIgnore
    private String customerDesc;

    private Long workspaceId;

    @CopyIgnore
    private ExtraField costSchedule;

    @CopyIgnore
    private ExtraField costOtherFirst;

    @CopyIgnore
    private ExtraField costOtherSecond;

    private List<LoanLaunchRecordDomain> loanRecordDomains;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public List<AttachDomain> getAttachDomains() {
        return attachDomains;
    }

    public void setAttachDomains(List<AttachDomain> attachDomains) {
        this.attachDomains = attachDomains;
    }

    public List<AttachRelateDomain> getAttachRelateDomains() {
        return attachRelateDomains;
    }

    public void setAttachRelateDomains(List<AttachRelateDomain> attachRelateDomains) {
        this.attachRelateDomains = attachRelateDomains;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Boolean getHasHolder() {
        return hasHolder;
    }

    public void setHasHolder(Boolean hasHolder) {
        this.hasHolder = hasHolder;
    }

    /**
     * Getter method for property <tt>loanStatus</tt>.
     *
     * @return property value of loanStatus
     */
    public LoanStatusEnum getLoanStatus() {
        return loanStatus;
    }

    /**
     * Setter method for property <tt>loanStatus</tt>.
     *
     * @param loanStatus value to be assigned to property loanStatus
     */
    public void setLoanStatus(LoanStatusEnum loanStatus) {
        this.loanStatus = loanStatus;
    }

    /**
     * Getter method for property <tt>accountId</tt>.
     *
     * @return property value of accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Setter method for property <tt>accountId</tt>.
     *
     * @param accountId value to be assigned to property accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public LoanPawnTypeEnum getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(LoanPawnTypeEnum type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>payWay</tt>.
     *
     * @return property value of payWay
     */
    public LoanPayWayEnum getPayWay() {
        return payWay;
    }

    /**
     * Setter method for property <tt>payWay</tt>.
     *
     * @param payWay value to be assigned to property payWay
     */
    public void setPayWay(LoanPayWayEnum payWay) {
        this.payWay = payWay;
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
     * Getter method for property <tt>monthsLimit</tt>.
     *
     * @return property value of monthsLimit
     */
    public Integer getMonthsLimit() {
        return monthsLimit;
    }

    /**
     * Setter method for property <tt>monthsLimit</tt>.
     *
     * @param monthsLimit value to be assigned to property monthsLimit
     */
    public void setMonthsLimit(Integer monthsLimit) {
        this.monthsLimit = monthsLimit;
    }

    /**
     * Getter method for property <tt>startDate</tt>.
     *
     * @return property value of startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter method for property <tt>startDate</tt>.
     *
     * @param startDate value to be assigned to property startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Getter method for property <tt>finishDate</tt>.
     *
     * @return property value of finishDate
     */
    public Date getFinishDate() {
        return finishDate;
    }

    /**
     * Setter method for property <tt>finishDate</tt>.
     *
     * @param finishDate value to be assigned to property finishDate
     */
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * Getter method for property <tt>interestRate</tt>.
     *
     * @return property value of interestRate
     */
    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * Setter method for property <tt>interestRate</tt>.
     *
     * @param interestRate value to be assigned to property interestRate
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Getter method for property <tt>penaltyRate</tt>.
     *
     * @return property value of penaltyRate
     */
    public Double getPenaltyRate() {
        return penaltyRate;
    }

    /**
     * Setter method for property <tt>penaltyRate</tt>.
     *
     * @param penaltyRate value to be assigned to property penaltyRate
     */
    public void setPenaltyRate(Double penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    /**
     * Getter method for property <tt>bondPerform</tt>.
     *
     * @return property value of bondPerform
     */
    public Double getBondPerform() {
        return bondPerform;
    }

    /**
     * Setter method for property <tt>bondPerform</tt>.
     *
     * @param bondPerform value to be assigned to property bondPerform
     */
    public void setBondPerform(Double bondPerform) {
        this.bondPerform = bondPerform;
    }

    /**
     * Getter method for property <tt>costService</tt>.
     *
     * @return property value of costService
     */
    public Double getCostService() {
        return costService;
    }

    /**
     * Setter method for property <tt>costService</tt>.
     *
     * @param costService value to be assigned to property costService
     */
    public void setCostService(Double costService) {
        this.costService = costService;
    }

    /**
     * Getter method for property <tt>costOther</tt>.
     *
     * @return property value of costOther
     */
    public Double getCostOther() {
        return costOther;
    }

    /**
     * Setter method for property <tt>costOther</tt>.
     *
     * @param costOther value to be assigned to property costOther
     */
    public void setCostOther(Double costOther) {
        this.costOther = costOther;
    }

    /**
     * Getter method for property <tt>costTemp</tt>.
     *
     * @return property value of costTemp
     */
    public Double getCostTemp() {
        return costTemp;
    }

    /**
     * Setter method for property <tt>costTemp</tt>.
     *
     * @param costTemp value to be assigned to property costTemp
     */
    public void setCostTemp(Double costTemp) {
        this.costTemp = costTemp;
    }

    /**
     * Getter method for property <tt>还款周期 月/次</tt>.
     *
     * @return property value of payCircle
     */
    public Integer getPayCircle() {
        return payCircle;
    }

    /**
     * Setter method for property <tt>还款周期 月/次</tt>.
     *
     * @param payCircle value to be assigned to property payCircle
     */
    public void setPayCircle(Integer payCircle) {
        this.payCircle = payCircle;
    }

    /**
     * Getter method for property <tt>还款次数 次</tt>.
     *
     * @return property value of payTotalCircle
     */
    public Integer getPayTotalCircle() {
        return payTotalCircle;
    }

    /**
     * Setter method for property <tt>还款次数 次</tt>.
     *
     * @param payTotalCircle value to be assigned to property payTotalCircle
     */
    public void setPayTotalCircle(Integer payTotalCircle) {
        this.payTotalCircle = payTotalCircle;
    }

    /**
     * Getter method for property <tt>businessType</tt>.
     *
     * @return property value of businessType
     */
    public BusinessTypeEnum getBusinessType() {
        return businessType;
    }

    /**
     * Setter method for property <tt>businessType</tt>.
     *
     * @param businessType value to be assigned to property businessType
     */
    public void setBusinessType(BusinessTypeEnum businessType) {
        this.businessType = businessType;
    }

    /**
     * Getter method for property <tt>id</tt>.
     *
     * @return property value of id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     *
     * @param id value to be assigned to property id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>loaner</tt>.
     *
     * @return property value of loaner
     */
    public CustomerDomain getLoaner() {
        return loaner;
    }

    /**
     * Setter method for property <tt>loaner</tt>.
     *
     * @param loaner value to be assigned to property loaner
     */
    public void setLoaner(CustomerDomain loaner) {
        this.loaner = loaner;
    }

    /**
     * Getter method for property <tt>holder</tt>.
     *
     * @return property value of holder
     */
    public CustomerDomain getHolder() {
        return holder;
    }

    /**
     * Setter method for property <tt>holder</tt>.
     *
     * @param holder value to be assigned to property holder
     */
    public void setHolder(CustomerDomain holder) {
        this.holder = holder;
    }

    /**
     * Getter method for property <tt>loanPawnDomain</tt>.
     *
     * @return property value of loanPawnDomain
     */
    public LoanPawnDomain getLoanPawnDomain() {
        return loanPawnDomain;
    }

    /**
     * Setter method for property <tt>loanPawnDomain</tt>.
     *
     * @param loanPawnDomain value to be assigned to property loanPawnDomain
     */
    public void setLoanPawnDomain(LoanPawnDomain loanPawnDomain) {
        this.loanPawnDomain = loanPawnDomain;
    }


    /**
     * Gets the key id.
     *
     * @return the key id
     */
    public String getKeyId() {
        return keyId;
    }

    /**
     * Sets the key id.
     *
     * @param keyId the new key id
     */
    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    /**
     * Getter method for property <tt>extParams</tt>.
     *
     * @return property value of extParams
     */
    public Map<String, String> getExtParams() {
        return extParams;
    }

    /**
     * Setter method for property <tt>extParams</tt>.
     *
     * @param extParams value to be assigned to property extParams
     */
    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

    public Integer getOverDueNumbers() {
        return overDueNumbers;
    }

    public void setOverDueNumbers(Integer overDueNumbers) {
        this.overDueNumbers = overDueNumbers;
    }

    public Integer getBadLoanNumbers() {
        return badLoanNumbers;
    }

    public void setBadLoanNumbers(Integer badLoanNumbers) {
        this.badLoanNumbers = badLoanNumbers;
    }

    public Integer getCurrentPayment() {
        return currentPayment;
    }

    public void setCurrentPayment(Integer currentPayment) {
        this.currentPayment = currentPayment;
    }

    public Date getStartPayDate() {
        return startPayDate;
    }

    public void setStartPayDate(Date startPayDate) {
        this.startPayDate = startPayDate;
    }

    public String getCostOtherDesc() {
        return costOtherDesc;
    }

    public void setCostOtherDesc(String costOtherDesc) {
        this.costOtherDesc = costOtherDesc;
    }

    public String getCostTempDesc() {
        return costTempDesc;
    }

    public void setCostTempDesc(String costTempDesc) {
        this.costTempDesc = costTempDesc;
    }

    public String getCustomerDesc() {
        return customerDesc;
    }

    public void setCustomerDesc(String customerDesc) {
        this.customerDesc = customerDesc;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public AccountBaseDomain getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(AccountBaseDomain ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public ExtraField getCostSchedule() {
        return costSchedule;
    }

    public void setCostSchedule(ExtraField costSchedule) {
        this.costSchedule = costSchedule;
    }

    public ExtraField getCostOtherFirst() {
        return costOtherFirst;
    }

    public void setCostOtherFirst(ExtraField costOtherFirst) {
        this.costOtherFirst = costOtherFirst;
    }

    public ExtraField getCostOtherSecond() {
        return costOtherSecond;
    }

    public void setCostOtherSecond(ExtraField costOtherSecond) {
        this.costOtherSecond = costOtherSecond;
    }

    public List<LoanLaunchRecordDomain> getLoanRecordDomains() {
        return loanRecordDomains;
    }

    public void setLoanRecordDomains(List<LoanLaunchRecordDomain> loanRecordDomains) {
        this.loanRecordDomains = loanRecordDomains;
    }
}
