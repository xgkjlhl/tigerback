package tiger.core.service.component.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.util.JsonUtil;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.service.component.KafkaService;

import java.util.List;

/**
 * Created by domicc on 5/5/16.
 */
@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = Logger.getLogger(KafkaServiceImpl.class);

    @Autowired
    KafkaProducer<String, String> kafkaProducer;

    /**
     * @see KafkaService#sendOneToKafka(NotificationKeyEnum, NotificationBasicDomain)
     */
    @Override
    public void sendOneToKafka(NotificationKeyEnum key, NotificationBasicDomain notification) {
        if (key == null || notification == null) {
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("send to kafka, topic: [" + SystemConstants.KAFKA_TIGER_TOPIC + "], key: [" + key + "], obj: [" + notification + "]");
        }

        try {
            kafkaProducer.send(new ProducerRecord<>(SystemConstants.KAFKA_TIGER_TOPIC, key.getCode(), JsonUtil.toJson(notification)));
            kafkaProducer.flush();
        } catch (Exception e) {
            logger.error("kafka send error, topic: [" + SystemConstants.KAFKA_TIGER_TOPIC + "], key: [" + key + "], obj: [" + notification + "]");
        }
    }

    /**
     * @see KafkaService#sendAllToKafka(NotificationKeyEnum, List)
     */
    @Override
    public void sendAllToKafka(NotificationKeyEnum key, List<NotificationBasicDomain> notifications) {
        if (key == null || CollectionUtils.isEmpty(notifications)) {
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("send to kafka, topic: [" + SystemConstants.KAFKA_TIGER_TOPIC + "], key: [" + key + "], obj: [" + notifications.size() + " notifications ]");
        }

        try {
            notifications.forEach(obj -> kafkaProducer.send(new ProducerRecord<>(SystemConstants.KAFKA_TIGER_TOPIC, key.getCode(), JsonUtil.toJson(obj))));
            kafkaProducer.flush();
        } catch (Exception e) {
            logger.error("kafka send error, topic: [" + SystemConstants.KAFKA_TIGER_TOPIC + "], key: [" + key + "], obj size: [" + notifications.size() + "]");
        }
    }
}
