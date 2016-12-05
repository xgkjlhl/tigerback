package tiger.kafka.consumer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.biz.message.support.MessageManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.util.component.jpush.JPushUtil;
import tiger.core.domain.MessageDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.convert.MessageConvert;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.service.SystemParamService;
import tiger.core.util.JPushUtilBuilder;
import tiger.kafka.consumer.config.BaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by domicc on 5/4/16.
 */
public class MessageConsumers extends TigerBaseConsumer {

    private static final Logger LOGGER = Logger.getLogger(MessageConsumers.class);

    @Autowired
    private MessageManager messageManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private SystemParamService systemParamService;

    public MessageConsumers(BaseConfig config) {
        super(config);
    }

    @Override
    protected void consume(Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap) {
        List<MessageDomain> messages = new ArrayList<>();
        // 逐个创建message
        notificationMap.forEach((mapBaseDomain, mapKeyEnum) -> {
            if (config.isSupport(mapBaseDomain.getTypeEnum())) {
                Long subjectId = mapBaseDomain.getSubjectId();
                Long senderId = mapBaseDomain.getAccountId();
                Long workspaceId = mapBaseDomain.getWorkspaceId();
                WorkspaceDomain workspaceDomain = workspaceMap.get(workspaceId);

                workspaceManager.getAllWorkspaceMemberIds(workspaceId).forEach(receiverId -> {
                    // 过滤发送给自己的消息
                    if (!receiverId.equals(senderId)) {
                        MessageDomain messageDomain = new MessageDomain();
                        messageDomain.setSenderId(senderId);
                        messageDomain.setSenderName(accountMap.get(senderId).getUserName());
                        messageDomain.setWorkspaceId(workspaceId);
                        if (workspaceDomain != null) {
                            messageDomain.setWorkspaceName(workspaceDomain.getName());
                        }
                        messageDomain.setBizId(subjectId);
                        messageDomain.setType(mapBaseDomain.getTypeEnum());
                        messageDomain.setReceiverId(receiverId);
                        messageDomain.setTypeParams(mapBaseDomain.getOperationParams());
                        switch (mapKeyEnum) {
                            case LOAN:
                                messageDomain.setBizType(BizObjectTypeEnum.LOAN);
                                messageDomain.setBizName(loanLightMap.get(subjectId).getKeyId());
                                messages.add(messageDomain);
                                break;
                            case CUSTOMER:
                                messageDomain.setBizType(BizObjectTypeEnum.CUSTOMER);
                                messageDomain.setBizName(customerMap.get(subjectId).getName());
                                messages.add(messageDomain);
                                break;
                            case WORKSPACE:
                                messageDomain.setBizId(mapBaseDomain.getWorkspaceId());
                                messageDomain.setBizType(BizObjectTypeEnum.WORKSPACE);
                                messageDomain.setBizName(messageDomain.getWorkspaceName());
                                messages.add(messageDomain);
                                break;
                            default:
                                LOGGER.error("kafka consumer a record without key; key: [" + mapKeyEnum + "], value: [" + mapBaseDomain + "]");
                                break;
                        }
                    }
                });
            }
        });

        // 一次性创建所有的activities
        if (messageManager.sendMessages(messages)) {
            sendJPushMessage(messages);
        }
    }

    // 线程池
    private final ExecutorService e = Executors.newCachedThreadPool();

    /**
     * 发送一条JPush推送
     *
     * @param messages
     */
    private void sendJPushMessage(List<MessageDomain> messages) {
        // 获取 jpushUtil
        final JPushUtil jPushUtil = JPushUtilBuilder.getJPushUtilInstance(systemParamService);
        messages.forEach(messageDomain ->
                e.execute(() ->
                        jPushUtil.send(messageDomain.getReceiverId(), MessageConvert.convert2String(messageDomain))
                )
        );
    }

}
