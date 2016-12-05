/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.listDomain;

import tiger.core.domain.AccountBaseDomain;

import java.util.Date;

/**
 * 反馈列表的Base Domain
 *
 * @author alfred_yuan
 * @version v 0.1 22:14 alfred_yuan Exp $
 */
public class FeedbackListDomain {

    private Long id;

    private Long accountId;

    private AccountBaseDomain account;

    private String content;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
