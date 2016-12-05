/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.workspace.AccountWorkspaceDomain;
import tiger.core.domain.workspace.WorkspaceDomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The Class AccountDomain.
 *
 * @author zhangbin
 * @version v0.1 2015/10/1 22:41
 */
public class AccountDomain extends BaseDomain {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6803210431910905404L;

    /**
     * The company id.
     */
    @JsonIgnore
    private Long companyId;

    /**
     * The user name.
     */
    private String userName;

    /**
     * The password.
     */
    @JsonIgnore
    private String password;

    /**
     * The gender.
     */
    @CopyIgnore
    private GenderEnum gender;

    /**
     * The birthday.
     */
    private Date birthday;

    /**
     * The id card.
     */
    private String idCard;

    /**
     * The business scope.
     */
    private String businessScope;

    /**
     * The address.
     */
    private String address;

    /**
     * The regionCode
     */
    private String regionCode;

    /**
     * The mobile.
     */
    private String mobile;

    /**
     * The self-introduction
     */

    private String selfIntroduction;
    /**
     * 头像
     */
    @CopyIgnore
    private AttachDomain icon;

    /**
     * 头像id
     */
    @JsonIgnore
    private Long iconId;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;

    /**
     * 公司信息
     */
    @CopyIgnore
    private CompanyDomain companyDomain;

    /**
     * 用户额外参数
     */
    @CopyIgnore
    private HashMap<String, String> extParams;


    /**
     * 用户团队空间
     */
    @CopyIgnore
    private List<AccountWorkspaceDomain> accountWorkspaceDomains = new ArrayList<>();

    @CopyIgnore
    private List<WorkspaceDomain> workspaces;

    private String status;

    public String getIdCard() {
        return idCard;
    }

    /**
     * Sets the id card.
     *
     * @param idCard the new id card
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public AttachDomain getIcon() {
        return icon;
    }

    public void setIcon(AttachDomain icon) {
        this.icon = icon;
    }

    public Long getIconId() {
        return iconId;
    }

    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }

    public CompanyDomain getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(CompanyDomain companyDomain) {
        this.companyDomain = companyDomain;
    }

    /**
     * Gets the company id.
     *
     * @return the company id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * Sets the company id.
     *
     * @param companyId the new company id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public GenderEnum getGender() {
        return gender;
    }

    /**
     * Sets the gender.
     *
     * @param gender the new gender
     */
    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    /**
     * Gets the birthday.
     *
     * @return the birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Sets the birthday.
     *
     * @param birthday the new birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Gets the business scope.
     *
     * @return the business scope
     */
    public String getBusinessScope() {
        return businessScope;
    }

    /**
     * Sets the business scope.
     *
     * @param businessScope the new business scope
     */
    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address.
     *
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    /**
     * Gets the mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile.
     *
     * @param mobile the new mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public HashMap<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(HashMap<String, String> extParams) {
        this.extParams = extParams;
    }

    public List<AccountWorkspaceDomain> getAccountWorkspaceDomains() {
        return accountWorkspaceDomains;
    }

    public void setAccountWorkspaceDomains(List<AccountWorkspaceDomain> accountWorkspaceDomains) {
        this.accountWorkspaceDomains = accountWorkspaceDomains;
    }

    public List<WorkspaceDomain> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<WorkspaceDomain> workspaces) {
        this.workspaces = workspaces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
