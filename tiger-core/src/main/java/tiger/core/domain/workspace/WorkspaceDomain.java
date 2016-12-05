/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.workspace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.dal.enums.WorkSpaceTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.AttachDomain;

import java.util.Map;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:15 AM yiliang.gyl Exp $
 */
public class WorkspaceDomain extends BaseDomain {

    /**
     * 工作空间名字
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 描述
     *  ~ 解决ios关键字冲突问题
     */
    private String remark;

    /**
     * 所有者 id
     */
    private Long ownerId;

    /**
     * 所有者
     */
    private AccountDomain owner;

    /**
     * 工作空间类型
     */
    @CopyIgnore
    private WorkSpaceTypeEnum type;

    /**
     * 工作空间配置字段
     */
    @CopyIgnore
    private Map<String, String> extParams;

    /**
     * 是否已经认证
     */
    private Boolean isVerified;

    /**
     * 是否是默认渲染的工作空间
     */
    private Boolean isDefault = false;

    @JsonIgnore
    private Long iconId;

    private AttachDomain icon;

    /**
     * 客户数
     */
    @CopyIgnore
    private Integer customerCount;

    /**
     * 合同数
     */
    @CopyIgnore
    private Integer loanCount;

    /**
     * 空间成员数
     */
    @CopyIgnore
    private Integer memberCount;

    /**
     * 合同模版数
     */
    @CopyIgnore
    private Integer loanTemplateCount;

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public AccountDomain getOwner() {
        return owner;
    }

    public void setOwner(AccountDomain owner) {
        this.owner = owner;
    }

    public WorkSpaceTypeEnum getType() {
        return type;
    }

    public void setType(WorkSpaceTypeEnum type) {
        this.type = type;
    }

    /**
     * Getter for property 'iconId'.
     *
     * @return Value for property 'iconId'.
     */
    public Long getIconId() {
        return iconId;
    }

    /**
     * Setter for property 'iconId'.
     *
     * @param iconId Value to set for property 'iconId'.
     */
    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }

    /**
     * Getter for property 'icon'.
     *
     * @return Value for property 'icon'.
     */
    public AttachDomain getIcon() {
        return icon;
    }

    /**
     * Setter for property 'icon'.
     *
     * @param icon Value to set for property 'icon'.
     */
    public void setIcon(AttachDomain icon) {
        this.icon = icon;
    }

    /**
     * Getter for property 'customerCount'.
     *
     * @return Value for property 'customerCount'.
     */
    public Integer getCustomerCount() {
        return customerCount;
    }

    /**
     * Setter for property 'customerCount'.
     *
     * @param customerCount Value to set for property 'customerCount'.
     */
    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    /**
     * Getter for property 'loanCount'.
     *
     * @return Value for property 'loanCount'.
     */
    public Integer getLoanCount() {
        return loanCount;
    }

    /**
     * Setter for property 'loanCount'.
     *
     * @param loanCount Value to set for property 'loanCount'.
     */
    public void setLoanCount(Integer loanCount) {
        this.loanCount = loanCount;
    }

    /**
     * Getter for property 'memberCount'.
     *
     * @return Value for property 'memberCount'.
     */
    public Integer getMemberCount() {
        return memberCount;
    }

    /**
     * Setter for property 'memberCount'.
     *
     * @param memberCount Value to set for property 'memberCount'.
     */
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * Getter for property 'loanTemplateCount'.
     *
     * @return Value for property 'loanTemplateCount'.
     */
    public Integer getLoanTemplateCount() {
        return loanTemplateCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Setter for property 'loanTemplateCount'.
     *
     * @param loanTemplateCount Value to set for property 'loanTemplateCount'.
     */
    public void setLoanTemplateCount(Integer loanTemplateCount) {
        this.loanTemplateCount = loanTemplateCount;
    }
}
