/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.Notification;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import tiger.common.dal.enums.MessageTypeEnum;

import java.io.Serializable;

/**
 * @author alfred_yuan
 * @version v 0.1 22:35 alfred_yuan Exp $
 */
public class NotificationBasicDomain implements Serializable {

    private static final long serialVersionUID = 8401577580784235615L;

    private Long workspaceId;

    private Long accountId;

    private Long subjectId;

    private MessageTypeEnum typeEnum;

    private String[] operationParams;

    public NotificationBasicDomain(Long workspaceId, Long accountId, Long subjectId, MessageTypeEnum typeEnum, String[] operationParams) {
        this.workspaceId = workspaceId;
        this.accountId = accountId;
        this.subjectId = subjectId;
        this.typeEnum = typeEnum;
        this.operationParams = operationParams;
    }

    public NotificationBasicDomain() {
    }


    /**
     * Getter for property 'workspaceId'.
     *
     * @return Value for property 'workspaceId'.
     */
    public Long getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Setter for property 'workspaceId'.
     *
     * @param workspaceId Value to set for property 'workspaceId'.
     */
    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    /**
     * Getter for property 'accountId'.
     *
     * @return Value for property 'accountId'.
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Setter for property 'accountId'.
     *
     * @param accountId Value to set for property 'accountId'.
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Getter for property 'subjectId'.
     *
     * @return Value for property 'subjectId'.
     */
    public Long getSubjectId() {
        return subjectId;
    }

    /**
     * Setter for property 'subjectId'.
     *
     * @param subjectId Value to set for property 'subjectId'.
     */
    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Getter for property 'typeEnum'.
     *
     * @return Value for property 'typeEnum'.
     */
    public MessageTypeEnum getTypeEnum() {
        return typeEnum;
    }

    /**
     * Setter for property 'typeEnum'.
     *
     * @param typeEnum Value to set for property 'typeEnum'.
     */
    public void setTypeEnum(MessageTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    /**
     * Getter for property 'operationParams'.
     *
     * @return Value for property 'operationParams'.
     */
    public String[] getOperationParams() {
        return operationParams;
    }

    /**
     * Setter for property 'operationParams'.
     *
     * @param operationParams Value to set for property 'operationParams'.
     */
    public void setOperationParams(String[] operationParams) {
        this.operationParams = operationParams;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
