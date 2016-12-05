/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午6:31:40 mi.li Exp $
 */
public class UcenterPostReplyDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -3231099659907488429L;

    private Long postId;

    private Long replyToId;

    private Long accountId;

    private String content;

    private Timestamp createTime;

    private Timestamp updateTime;

    @CopyIgnore
    private String photoUrl;

    @CopyIgnore
    private String userName;

    @CopyIgnore
    private List<UcenterPostReplyDomain> replysToReply;

    @CopyIgnore
    private boolean isMine;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(Long replyToId) {
        this.replyToId = replyToId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UcenterPostReplyDomain> getReplysToReply() {
        return replysToReply;
    }

    public void setReplysToReply(List<UcenterPostReplyDomain> replysToReply) {
        this.replysToReply = replysToReply;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }
}
