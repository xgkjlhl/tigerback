/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.MessageDomain;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 测试MessageService
 *
 * @author alfred.yx
 * @version v 0.1 Sep 24, 2015 8:16:30 PM alfred Exp $
 */

public class MessageServiceImplTest extends AbstractCoreTests {

    @Autowired
    private MessageService messageService;


    @Test
    public void testSendMessageToOne() {
        logger.info("开始测试sendMessage(Long receiverID, MessageDomain message)");
        Long receiverID = Long.valueOf(1l);
        String content = "test " + System.currentTimeMillis();
        MessageDomain message = getMessage(MessageTypeEnum.SYSTEM, content);
        boolean ret = messageService.sendMessage(receiverID, message);
        assertEquals(true, ret);
    }

    @Test
    public void testSendMessageToMany() {
        logger.info("开始测试sendMessage(List<Long> receiverIDs, MessageDomain message)");
        List<Long> receiverIDs = new ArrayList<>();
        for (long tempReceiverID = 1l; tempReceiverID <= 10l; ++tempReceiverID) {
            receiverIDs.add(tempReceiverID);
        }
        String content = "test " + System.currentTimeMillis();
        MessageDomain message = getMessage(MessageTypeEnum.SYSTEM, content);
        boolean ret = messageService.sendMessages(receiverIDs, message);
        assertEquals(true, ret);
    }

    //～ private methods
    private MessageDomain getMessage(MessageTypeEnum bt, String content) {
        MessageDomain message = new MessageDomain();
        message.setType(bt);
        message.setTitle(SystemConstants.SYSTEM_MESSAGE_TITLE);
        message.setContent(content);
        return message;
    }

}
