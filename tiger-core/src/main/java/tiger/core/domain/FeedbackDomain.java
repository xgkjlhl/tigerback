/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.Date;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:07 alfred_yuan Exp $
 */
public class FeedbackDomain extends BaseDomain {

    private static final long serialVersionUID = 4758043101233188192L;

    private Long accountId;

    @CopyIgnore
    private AccountBaseDomain account;

    private String title;

    private String email;

    private String mobile;

    private String content;

    @JsonIgnore
    @CopyIgnore
    private List<AttachRelateDomain> attachRelateDomains;

    @CopyIgnore
    private List<AttachDomain> attaches;

    private Date createTime;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountBaseDomain getAccount() {
        return account;
    }

    public void setAccount(AccountBaseDomain account) {
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<AttachRelateDomain> getAttachRelateDomains() {
        return attachRelateDomains;
    }

    public void setAttachRelateDomains(List<AttachRelateDomain> attachRelateDomains) {
        this.attachRelateDomains = attachRelateDomains;
    }

    public List<AttachDomain> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<AttachDomain> attaches) {
        this.attaches = attaches;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
