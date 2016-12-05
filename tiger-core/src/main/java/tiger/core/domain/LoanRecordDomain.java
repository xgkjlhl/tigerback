/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 贷款每期还款模型类
 * ~ 存放每期还款的计算结果
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 10:44:33 AM yiliang.gyl Exp $
 */
public class LoanRecordDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 8469467022693060654L;

    private Long recordNo;

    private Double amount;

    private Date deadLine;

    @CopyIgnore
    private LoanStatusEnum state;

    private Long loanId;

    /**
     * 合同调整id
     */
    private Long loanRefineId;

    private Double actualAmount;

    private Date actualDate;

    private Double actualInterest;

    @CopyIgnore
    private LoanActionEnum type;

    private Double theoryAmount;

    private Date theoryDeadLine;

    private String process;

    private Double theoryInterest;

    @CopyIgnore
    private List<LoanRecordItemDomain> recordItemDomains = new ArrayList<>();

    private Integer order;

    @CopyIgnore
    private LoanPayItemModeEnum loanPayItemModeEnum;

    private String remark;

    /**
     * 工作组id
     */
    private Long workspaceId;

    @CopyIgnore
    private long overdueDay;

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
     * Getter method for property <tt>recordNo</tt>.
     *
     * @return property value of recordNo
     */
    public Long getRecordNo() {
        return recordNo;
    }

    /**
     * Setter method for property <tt>recordNo</tt>.
     *
     * @param recordNo value to be assigned to property recordNo
     */
    public void setRecordNo(Long recordNo) {
        this.recordNo = recordNo;
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
     * Getter method for property <tt>state</tt>.
     *
     * @return property value of state
     */
    public LoanStatusEnum getState() {
        return state;
    }

    /**
     * Setter method for property <tt>state</tt>.
     *
     * @param state value to be assigned to property state
     */
    public void setState(LoanStatusEnum state) {
        this.state = state;
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

    public Long getLoanRefineId() {
        return loanRefineId;
    }

    public void setLoanRefineId(Long loanRefineId) {
        this.loanRefineId = loanRefineId;
    }

    /**
     * Getter method for property <tt>actualAmount</tt>.
     *
     * @return property value of actualAmount
     */
    public Double getActualAmount() {
        return actualAmount;
    }

    /**
     * Setter method for property <tt>actualAmount</tt>.
     *
     * @param actualAmount value to be assigned to property actualAmount
     */
    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    /**
     * Getter method for property <tt>actualDate</tt>.
     *
     * @return property value of actualDate
     */
    public Date getActualDate() {
        return actualDate;
    }

    /**
     * Setter method for property <tt>actualDate</tt>.
     *
     * @param actualDate value to be assigned to property actualDate
     */
    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    /**
     * Getter method for property <tt>actualInterest</tt>.
     *
     * @return property value of actualInterest
     */
    public Double getActualInterest() {
        return actualInterest;
    }

    /**
     * Setter method for property <tt>actualInterest</tt>.
     *
     * @param actualInterest value to be assigned to property actualInterest
     */
    public void setActualInterest(Double actualInterest) {
        this.actualInterest = actualInterest;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public LoanActionEnum getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(LoanActionEnum type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>theoryAmount</tt>.
     *
     * @return property value of theoryAmount
     */
    public Double getTheoryAmount() {
        return theoryAmount;
    }

    /**
     * Setter method for property <tt>theoryAmount</tt>.
     *
     * @param theoryAmount value to be assigned to property theoryAmount
     */
    public void setTheoryAmount(Double theoryAmount) {
        this.theoryAmount = theoryAmount;
    }

    /**
     * Getter method for property <tt>theoryDeadLine</tt>.
     *
     * @return property value of theoryDeadLine
     */
    public Date getTheoryDeadLine() {
        return theoryDeadLine;
    }

    /**
     * Setter method for property <tt>theoryDeadLine</tt>.
     *
     * @param theoryDeadLine value to be assigned to property theoryDeadLine
     */
    public void setTheoryDeadLine(Date theoryDeadLine) {
        this.theoryDeadLine = theoryDeadLine;
    }

    /**
     * Getter method for property <tt>theoryInterest</tt>.
     *
     * @return property value of theoryInterest
     */
    public Double getTheoryInterest() {
        return theoryInterest;
    }

    /**
     * Setter method for property <tt>theoryInterest</tt>.
     *
     * @param theoryInterest value to be assigned to property theoryInterest
     */
    public void setTheoryInterest(Double theoryInterest) {
        this.theoryInterest = theoryInterest;
    }

    /**
     * Getter method for property <tt>recordItemDomains</tt>.
     *
     * @return property value of recordItemDomains
     */
    public List<LoanRecordItemDomain> getRecordItemDomains() {
        return recordItemDomains;
    }

    /**
     * Setter method for property <tt>recordItemDomains</tt>.
     *
     * @param recordItemDomains value to be assigned to property recordItemDomains
     */
    public void setRecordItemDomains(List<LoanRecordItemDomain> recordItemDomains) {
        this.recordItemDomains = recordItemDomains;
    }

    /**
     * Adds the item.
     *
     * @param itemDomain the item domain
     */
    public void addItem(LoanRecordItemDomain itemDomain) {
        itemDomain.setOrder(this.recordItemDomains.size() + 1);
        this.recordItemDomains.add(itemDomain);
    }

    /**
     * Getter method for property <tt>order</tt>.
     *
     * @return property value of order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Setter method for property <tt>order</tt>.
     *
     * @param order value to be assigned to property order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * Getter method for property <tt>loanPayItemModeEnum</tt>.
     *
     * @return property value of loanPayItemModeEnum
     */
    public LoanPayItemModeEnum getLoanPayItemModeEnum() {
        return loanPayItemModeEnum;
    }

    /**
     * Setter method for property <tt>loanPayItemModeEnum</tt>.
     *
     * @param loanPayItemModeEnum value to be assigned to property loanPayItemModeEnum
     */
    public void setLoanPayItemModeEnum(LoanPayItemModeEnum loanPayItemModeEnum) {
        this.loanPayItemModeEnum = loanPayItemModeEnum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public long getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(long overdueDay) {
        this.overdueDay = overdueDay;
    }
}
