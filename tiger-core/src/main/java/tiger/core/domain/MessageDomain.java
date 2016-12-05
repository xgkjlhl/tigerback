/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.sql.Timestamp;

/**
 * 站內消息Domain.
 *
 * @author alfred.yx
 * @version v 0.1 Sep 30, 2015 4:23:04 PM alfred Exp $
 */
public class MessageDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -3923833021322495838L;

    /**
     * The receiver id.
     */
    private Long receiverId;

    /**
     * The sender id.
     */
    private Long senderId;

    /**
     * The sender name.
     */
    private String senderName;

    /**
     * The is read.
     */
    @CopyIgnore
    private Boolean isRead;

    /**
     * The is archived.
     */
    @CopyIgnore
    private Boolean isArchived;

    /**
     * The is deleted.
     */
    @CopyIgnore
    private Boolean isDeleted;

    private String title;

    private String description;

    @CopyIgnore
    private String remark;

    /**
     * The content.
     */
    private String content;

    /**
     * The biz id.
     */
    private Long bizId;

    /**
     * The biz name.
     */
    private String bizName;

    /**
     * The biz type.
     */
    @CopyIgnore
    private BizObjectTypeEnum bizType;

    @CopyIgnore
    private MessageTypeEnum type;

    /**
     * The type params
     */
    private String[] typeParams;

    /**
     * The create time.
     */
    private Timestamp createTime;

    private Long workspaceId;

    private String workspaceName;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property 'remark'.
     *
     * @return Value for property 'remark'.
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Setter for property 'remark'.
     *
     * @param remark Value to set for property 'remark'.
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    public MessageTypeEnum getType() {
        return type;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
    }

    public BizObjectTypeEnum getBizType() {
        return bizType;
    }

    public void setBizType(BizObjectTypeEnum bizType) {
        this.bizType = bizType;
    }

    public String[] getTypeParams() {
        return typeParams;
    }

    public void setTypeParams(String[] typeParams) {
        this.typeParams = typeParams;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }
}
