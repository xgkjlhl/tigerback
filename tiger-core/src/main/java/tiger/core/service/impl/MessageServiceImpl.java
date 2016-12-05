/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tiger.common.dal.dataobject.MessageDO;
import tiger.common.dal.dataobject.example.MessageExample;
import tiger.common.dal.persistence.MessageMapper;
import tiger.common.dal.query.MessageQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.MessageDomain;
import tiger.core.domain.convert.MessageConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AccountService;
import tiger.core.service.CustomerService;
import tiger.core.service.LoanService;
import tiger.core.service.MessageService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class MessageServiceImpl.
 *
 * @author alfred.yx
 * @version v 0.1 Sep 23, 2015 10:32:27 AM alfred Exp $
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final Logger logger = Logger.getLogger(MessageServiceImpl.class);

    /**
     * The message mapper.
     */
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    /**
     * Gets the message by id.
     *
     * @param id the id
     * @return the message by id
     * @see tiger.core.service.MessageService#getMessageById(Long)
     */
    @Override
    public MessageDomain getMessageById(Long id) {
        MessageDO messageDO = messageMapper.selectByPrimaryKey(id);
        if (null != messageDO) {
            return MessageConvert.convertDOToDomain(messageDO);
        }
        return null;
    }

    /**
     * 检查用户是否为该消息的接收者
     *
     * @param messageIds the message id
     * @param accountId  the account id
     * @return the boolean
     * @see MessageService#isReceiver(List, Long)
     */
    @Override
    public boolean isReceiver(List<Long> messageIds, Long accountId) {
        // null用户不是任何消息的接收者
        if (accountId == null) {
            return false;
        }
        // 任何用户 是 null或空消息的接收者
        if (CollectionUtils.isEmpty(messageIds)) {
            return true;
        }

        MessageExample example = new MessageExample();
        example.createCriteria().andIdIn(messageIds).andReceiverIdEqualTo(accountId);

        int messageCount = messageMapper.countByExample(example);

        return messageCount == messageIds.size();
    }

    /**
     * Update message.
     *
     * @param messageDomain the message domain
     * @return the int
     * @see tiger.core.service.MessageService#updateMessage(tiger.core.domain.MessageDomain)
     */
    @Override
    public boolean updateMessage(MessageDomain messageDomain) {
        MessageDO messageDO = MessageConvert.convertDomainToDO(messageDomain);
        int updateResult = messageMapper.updateByPrimaryKeySelective(messageDO);
        return checkReturnCode(updateResult);
    }

    @Override
    @Transactional
    public boolean updateMessages(MessageDomain messageDomain, List<Long> messageIds) {
        if (messageDomain == null || CollectionUtils.isEmpty(messageIds)) {
            return true;
        }

        MessageExample example = new MessageExample();
        example.createCriteria().andIdIn(messageIds);

        int updateCount = messageMapper.updateByExampleSelective(MessageConvert.convertDomainToDO(messageDomain), example);

        if (updateCount != messageIds.size()) {
            throw new TigerException(ErrorCodeEnum.SYSTEM_EXCEPTION, "更新消息列表失败,请重新尝试");
        }

        return true;
    }

    /**
     * Delete message by id.
     *
     * @param id the id
     * @return the int
     * @see tiger.core.service.MessageService#deleteMessageByID(java.lang.Long)
     */
    @Override
    public boolean deleteMessageByID(Long id) {
        int deleteResult = messageMapper.deleteByPrimaryKey(id);
        return checkReturnCode(deleteResult);
    }

    /**
     * 分页列出站内消息.
     *
     * @param query the query domain
     * @return the page result
     * @see tiger.core.service.MessageService#listMessages(MessageQuery)
     */
    @Override
    public PageResult<List<MessageDomain>> listMessages(MessageQuery query) {
        PageResult<List<MessageDomain>> messages = new PageResult<>();
        int totalItems = messageMapper.countMessages(query);
        // 分页器构建
        Paginator paginator = new Paginator();
        paginator.setItems(totalItems); // 设置符合筛选条件的总数
        paginator.setItemsPerPage(query.getPageSize()); // 设置页面大小
        paginator.setPage(query.getPageNum()); // 目前选择的页码

        List<MessageDomain> messageDomains = MessageConvert.convertDOsToDomains(
                messageMapper.queryMessages(query, paginator.getOffset(), paginator.getLength()));

        messages.setData(messageDomains);
        messages.setPaginator(paginator);
        return messages;
    }

    /**
     * Send message.
     *
     * @param receiverID the receiver id
     * @param message    the message
     * @return true, if successful
     * @see tiger.core.service.MessageService#sendMessage(Long, tiger.core.domain.MessageDomain)
     */
    @Override
    public boolean sendMessage(Long receiverID, MessageDomain message) {
        if (null == receiverID) {
            return false;
        }
        MessageDO messageDO = MessageConvert.convertDomainToDO(message);
        messageDO.setReceiverId(receiverID);

        int rc = -1;

        try {
            rc = messageMapper.insertSelective(messageDO);
        } catch (Exception e) {
            logger.error("插入发送给 [" + receiverID + "] 消息错误, 参数为 [" + message + "]");
        }

        return checkReturnCode(rc);
    }

    /**
     * Send message.
     *
     * @param receiverIDs the receiver i ds
     * @param message     the message
     * @return true, if successful
     * @see tiger.core.service.MessageService#sendMessages(java.util.List, tiger.core.domain.MessageDomain)
     */
    @Override
    public boolean sendMessages(List<Long> receiverIDs, MessageDomain message) {
        if (CollectionUtils.isEmpty(receiverIDs)) {
            return false;
        }
        Set<Long> distinctReceiverIDs = new HashSet<>(receiverIDs);
        List<MessageDO> messageDOs = new ArrayList<>();
        for (Long id : distinctReceiverIDs) {
            MessageDO messageDO = MessageConvert.convertDomainToDO(message);
            messageDO.setReceiverId(id);
            messageDOs.add(messageDO);
        }

        return send(messageDOs);
    }

    /**
     * 发送多条消息
     *
     * @param messageDomains
     * @return
     */
    public boolean sendMessages(List<MessageDomain> messageDomains) {
        if (CollectionUtils.isEmpty(messageDomains)) {
            return false;
        }

        if (logger.isInfoEnabled()) {
            logger.info("创建" + messageDomains.size() + "条系统消息, 参数为 [" + messageDomains + "]");
        }
        List<MessageDO> messageDOs = MessageConvert.convertDomainsToDOs(messageDomains);

        return send(messageDOs);
    }

    /**
     * @see MessageService#countMessages(MessageQuery)
     */
    @Override
    public int countMessages(MessageQuery query) {
        return messageMapper.countMessages(query);
    }

    /**
     * @see MessageService#getUnreadMessageIds(Long)
     */
    @Override
    public List<Long> getUnreadMessageIds(Long accountId) {
        MessageExample example = new MessageExample();
        example.createCriteria().andReceiverIdEqualTo(accountId).andIsReadEqualTo((byte) 0);

        List<MessageDO> messageDOs = messageMapper.selectByExample(example);
        List<Long> ids = new ArrayList<>();
        for (MessageDO messageDO : messageDOs)
            ids.add(messageDO.getId());
        return ids;
    }

    // ~ private methods

    /**
     * Check return code.
     *
     * @param rc the rc
     * @return true, if successful
     */
    private boolean checkReturnCode(int rc) {
        return rc > 0;
    }

    /**
     * 真实的发送多条消息
     *
     * @param messageDOs
     * @return
     */
    private boolean send(List<MessageDO> messageDOs) {
        int rc = -1;

        try {
            rc = messageMapper.batchInsertSelective(messageDOs);
        } catch (Exception e) {
            logger.error("插入消息错误, 参数为 [" + messageDOs + "]");
        }

        return checkReturnCode(rc);
    }
}
