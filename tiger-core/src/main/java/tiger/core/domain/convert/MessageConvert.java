/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.convert;

import tiger.common.dal.dataobject.MessageDO;
import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.ByteUtil;
import tiger.core.domain.MessageDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class MessageConvert.
 *
 * @author alfred.yx
 * @version v 0.1 Sep 23, 2015 10:43:25 AM alfred Exp $
 */
public class MessageConvert {

    public static final String MESSAGE_DELIMITER = " ";
    public static final String BUCKET_PREFIX = "[";
    public static final String BUCKET_SUFFIX = "]";

    /**
     * Convert Domain to DO.
     *
     * @param messageDomain the message Domain
     * @return the message DO
     */
    public static MessageDO convertDomainToDO(MessageDomain messageDomain) {
        if (null == messageDomain) {
            return null;
        }
        MessageDO messageDO = new MessageDO();
        BeanUtil.copyPropertiesWithIgnores(messageDomain, messageDO);
        // 为messageDO设置type和bizType
        if (null != messageDomain.getType()) {
            messageDO.setType(messageDomain.getType().getCode());
        }
        if (null != messageDomain.getBizType()) {
            messageDO.setBizType(messageDomain.getBizType().getCode());
        }
        // 为MessageDO设置is系列的值
        if (null != messageDomain.getIsArchived()) {
            messageDO.setIsArchived(ByteUtil.booleanToByte(messageDomain.getIsArchived()));
        } else {
            messageDO.setIsArchived(ByteUtil.BYTE_ZERO);
        }
        if (null != messageDomain.getIsDeleted()) {
            messageDO.setIsDeleted(ByteUtil.booleanToByte(messageDomain.getIsDeleted()));
        } else {
            messageDO.setIsDeleted(ByteUtil.BYTE_ZERO);
        }
        if (null != messageDomain.getIsRead()) {
            messageDO.setIsRead(ByteUtil.booleanToByte(messageDomain.getIsRead()));
        } else {
            messageDO.setIsRead(ByteUtil.BYTE_ZERO);
        }

        //解析动态类message的title
        if (null != messageDomain.getType()) {
            messageDO.setTitle(formatMessageTitle(messageDomain));
        }


        return messageDO;
    }

    /**
     * Convert Domains To DOs.
     *
     * @param messageDomains
     * @return
     */
    public static List<MessageDO> convertDomainsToDOs(List<MessageDomain> messageDomains) {
        if (messageDomains.isEmpty()) {
            return null;
        }
        List<MessageDO> messageDOs = new ArrayList<>(messageDomains.size());
        messageDomains.forEach(messageDomain -> messageDOs.add(convertDomainToDO(messageDomain)));
        return messageDOs;
    }

    /**
     * Convert DOs to Domain.
     *
     * @param messageDO the message DO
     * @return the message Domain
     */
    public static MessageDomain convertDOToDomain(MessageDO messageDO) {
        if (null == messageDO) {
            return null;
        }
        MessageDomain messageDomain = new MessageDomain();
        BeanUtil.copyPropertiesWithIgnores(messageDO, messageDomain);
        // 为messageDomain设置type和bizType
        messageDomain.setType(MessageTypeEnum.getEnumByCode(messageDO.getType()));
        messageDomain.setBizType(BizObjectTypeEnum.getEnumByCode(messageDO.getBizType()));
        messageDomain.setRemark(messageDO.getDescription());
        // 为messageDomain设置is系列的值
        if (null != messageDO.getIsArchived()) {
            messageDomain.setIsArchived(ByteUtil.toBoolean(messageDO.getIsArchived()));
        }
        if (null != messageDO.getIsDeleted()) {
            messageDomain.setIsDeleted(ByteUtil.toBoolean(messageDO.getIsDeleted()));
        }
        if (null != messageDO.getIsRead()) {
            messageDomain.setIsRead(ByteUtil.toBoolean(messageDO.getIsRead()));
        }
        return messageDomain;
    }


    /**
     * Convert DOs to Domains.
     *
     * @param messageDOs the message DOs
     * @return the list
     */
    public static List<MessageDomain> convertDOsToDomains(List<MessageDO> messageDOs) {
        if (messageDOs.isEmpty()) {
            return new ArrayList<>();
        }
        List<MessageDomain> messageDomains = new ArrayList<>(messageDOs.size());
        messageDOs.forEach(messageDO -> messageDomains.add(MessageConvert.convertDOToDomain(messageDO)));
        return messageDomains;
    }

    /**
     * 转换messageDomain为String
     *
     * @param message
     * @return
     */
    public static String convert2String(MessageDomain message) {
        String messageStr;

        switch (message.getType()) {
            case UPDATE_LOAN_SETTING:
                messageStr = String.join(MESSAGE_DELIMITER, message.getSenderName(), formatMessageTitle(message));
                break;
            case LOAN_NOTIFICATION:
                messageStr = String.join(MESSAGE_DELIMITER, formatMessageTitle(message), BUCKET_PREFIX, message.getBizName(), BUCKET_SUFFIX, message.getDescription());
                break;
            default:
                messageStr = String.join(MESSAGE_DELIMITER, message.getSenderName(), formatMessageTitle(message), BUCKET_PREFIX, message.getBizName(), BUCKET_SUFFIX);
        }

        return messageStr;
    }

    /**
     * 根据消息类型, format消息title
     *
     * @param messageDomain
     * @return
     */
    private static String formatMessageTitle(MessageDomain messageDomain) {
        MessageTypeEnum messageTypeEnum = messageDomain.getType();

        // 参数个数
        int paramNum;
        if (null == messageDomain.getTypeParams()) {
            paramNum = 0;
        } else {
            paramNum = messageDomain.getTypeParams().length;
        }

        // 判断个数是否满足
        if (!messageTypeEnum.checkParamNum(paramNum)) {
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "操作类型所需参数不够");
        }
        // format message title
        if (!messageDomain.getType().equals(MessageTypeEnum.LOAN_NOTIFICATION)) {
            if (null != messageDomain.getTypeParams())
                return MessageFormat.format(messageTypeEnum.getValue(), messageDomain.getTypeParams());
            else
                return messageTypeEnum.getValue();
        }
        // 否则返回默认title
        return messageDomain.getTitle();
    }
}
