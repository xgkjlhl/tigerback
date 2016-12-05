package tiger.core.service.component;

import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.core.domain.Notification.NotificationBasicDomain;

import java.util.List;

/**
 * Created by domicc on 5/5/16.
 */
public interface KafkaService {

    /**
     * 推送一条到kafka
     *
     * @param key
     * @param notification
     */
    void sendOneToKafka(NotificationKeyEnum key, NotificationBasicDomain notification);

    /**
     * 推送多条到kafka
     *
     * @param key
     * @param notifications
     */
    void sendAllToKafka(NotificationKeyEnum key, List<NotificationBasicDomain> notifications);
}
