/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.workspace;

import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.Date;

/**
 * 用户操作类领域模型
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:37 AM yiliang.gyl Exp $
 */
public class ActivityDomain extends BaseDomain {

    /**
     * 操作者id
     **/
    private Long operatorId;

    /**
     * 时间
     */
    private Date createTime;

    /**
     * 操作类型
     */
    @CopyIgnore
    private MessageTypeEnum operation;

    /**
     * 操作类型参数
     */
    @CopyIgnore
    private String[] operationParams;

    /**
     * 操作类型和操作类型参数拼接而成的message，如"收了5笔款"
     */
    @CopyIgnore
    private String operationMessage;

    /**
     * 备操作对象
     */
    @CopyIgnore
    private BizObjectTypeEnum objectType;

    /**
     * 备操作对象id
     */
    private Long objectId;

    /**
     * 备操作对象名称
     */
    private String objectName;

    /**
     * workspace Id
     */
    private Long workspaceId;

    /**
     * 操作者用户名
     */
    private String operatorName;

    /**
     * 操作者头像附件id
     */
    private Long operatorAvatarId;

    /**
     * 操作者头像附件url
     */
    private String operatorAvatarUrl;

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public MessageTypeEnum getOperation() {
        return operation;
    }

    public void setOperation(MessageTypeEnum operation) {
        this.operation = operation;
    }

    public String[] getOperationParams() {
        return operationParams;
    }

    public void setOperationParams(String[] operationParams) {
        this.operationParams = operationParams;
    }

    public String getOperationMessage() {
        return operationMessage;
    }

    public void setOperationMessage(String operationMessage) {
        this.operationMessage = operationMessage;
    }

    public BizObjectTypeEnum getObjectType() {
        return objectType;
    }

    public void setObjectType(BizObjectTypeEnum objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorAvatarId() {
        return operatorAvatarId;
    }

    public void setOperatorAvatarId(Long operatorAvatarId) {
        this.operatorAvatarId = operatorAvatarId;
    }

    public String getOperatorAvatarUrl() {
        return operatorAvatarUrl;
    }

    public void setOperatorAvatarUrl(String operatorAvatarUrl) {
        this.operatorAvatarUrl = operatorAvatarUrl;
    }
}
