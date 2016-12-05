/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.market;

import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.AccountDomain;

import java.util.Date;

/**
 * 基本贷款项目
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 1:46 PM yiliang.gyl Exp $
 */
public class MarketProjectDomain extends BaseDomain{

    private static final long serialVersionUID = -1859707102261633956L;

    private Integer period;

    private Long accountId;

    @CopyIgnore
    private AccountDomain account;

    private Date createTime;

    private Date updateTime;

    private Double amount;

    private String description;

    private String title;

    private Boolean hasBack;

    @CopyIgnore
    private LoanPawnTypeEnum pawnType;

    private Double minBackPercent;

    private Double maxBackPercent;

    private ContactInfoDomain contactInfo;

    private String address;

    private String addressKey;

    private MarketProjectPropertyDomain property = new MarketProjectPropertyDomain();

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountDomain getAccount() {
        return account;
    }

    public void setAccount(AccountDomain account) {
        this.account = account;
    }

    public Boolean getHasBack() {
        return hasBack;
    }

    public void setHasBack(Boolean hasBack) {
        this.hasBack = hasBack;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LoanPawnTypeEnum getPawnType() {
        return pawnType;
    }

    public void setPawnType(LoanPawnTypeEnum pawnType) {
        this.pawnType = pawnType;
    }

    public Double getMinBackPercent() {
        return minBackPercent;
    }

    public void setMinBackPercent(Double minBackPercent) {
        this.minBackPercent = minBackPercent;
    }

    public Double getMaxBackPercent() {
        return maxBackPercent;
    }

    public void setMaxBackPercent(Double maxBackPercent) {
        this.maxBackPercent = maxBackPercent;
    }

    public ContactInfoDomain getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoDomain contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressKey() {
        return addressKey;
    }

    public void setAddressKey(String addressKey) {
        this.addressKey = addressKey;
    }

    public MarketProjectPropertyDomain getProperty() {
        return property;
    }

    public void setProperty(MarketProjectPropertyDomain property) {
        this.property = property;
    }
}
