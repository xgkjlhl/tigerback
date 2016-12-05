/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.List;

/**
 * 贷款抵押物模型
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 3:41:07 PM yiliang.gyl Exp $
 */
public class LoanPawnDomain extends BaseDomain {

    private final long serialVersionUID = -7057138965955760345L;

    @CopyIgnore
    private LoanPawnTypeEnum loanPawnTypeEnum;

    private Integer order;

    private String remark;

    @CopyIgnore
    private List<LoanPawnParamDomain> loanPawnParamDomains;

    @CopyIgnore
    private List<AttachDomain> attachDomains;

    @CopyIgnore
    @JsonIgnore
    private List<AttachRelateDomain> loanPawnAttachDomains;

    private Long accountId;

    /**
     * 工作组id
     */
    private Long workspaceId;

    /**
     * Getter method for property <tt>loanPawnTypeEnum</tt>.
     *
     * @return property value of loanPawnTypeEnum
     */
    public LoanPawnTypeEnum getLoanPawnTypeEnum() {
        return loanPawnTypeEnum;
    }

    /**
     * Setter method for property <tt>loanPawnTypeEnum</tt>.
     *
     * @param loanPawnTypeEnum value to be assigned to property loanPawnTypeEnum
     */
    public void setLoanPawnTypeEnum(LoanPawnTypeEnum loanPawnTypeEnum) {
        this.loanPawnTypeEnum = loanPawnTypeEnum;
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
     * Getter method for property <tt>loanPawnParamDomains</tt>.
     *
     * @return property value of loanPawnParamDomains
     */
    public List<LoanPawnParamDomain> getLoanPawnParamDomains() {
        return loanPawnParamDomains;
    }

    /**
     * Setter method for property <tt>loanPawnParamDomains</tt>.
     *
     * @param loanPawnParamDomains value to be assigned to property loanPawnParamDomains
     */
    public void setLoanPawnParamDomains(List<LoanPawnParamDomain> loanPawnParamDomains) {
        this.loanPawnParamDomains = loanPawnParamDomains;
    }

    /**
     * Getter method for property <tt>attachDomains</tt>.
     *
     * @return property value of attachDomains
     */
    public List<AttachDomain> getAttachDomains() {
        return attachDomains;
    }

    /**
     * Setter method for property <tt>attachDomains</tt>.
     *
     * @param attachDomains value to be assigned to property attachDomains
     */
    public void setAttachDomains(List<AttachDomain> attachDomains) {
        this.attachDomains = attachDomains;
    }

    /**
     * Getter method for property <tt>remark</tt>.
     *
     * @return property value of remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Setter method for property <tt>remark</tt>.
     *
     * @param remark value to be assigned to property remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AttachRelateDomain> getLoanPawnAttachDomains() {
        return loanPawnAttachDomains;
    }

    public void setLoanPawnAttachDomains(List<AttachRelateDomain> loanPawnAttachDomains) {
        this.loanPawnAttachDomains = loanPawnAttachDomains;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}
