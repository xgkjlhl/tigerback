/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.AccountSocialTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 6:11 PM yiliang.gyl Exp $
 */
public class AccountBindDomain extends BaseDomain {

    private Date createTime;

    private Date updateTime;

    private Long accountId;

    @CopyIgnore
    private AccountSocialTypeEnum accountType;

    private String accountName;

    private String accessToken;

    private String openId;

    private String iconUrl;

    private Long iconId;

    private AttachDomain icon;

    private Boolean hasBind;

    private String unionId;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Boolean getHasBind() {
        return hasBind;
    }

    public void setHasBind(Boolean hasBind) {
        this.hasBind = hasBind;
    }

    public AttachDomain getIcon() {
        return icon;
    }

    public void setIcon(AttachDomain icon) {
        this.icon = icon;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountSocialTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountSocialTypeEnum accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getIconId() {
        return iconId;
    }

    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }

}
