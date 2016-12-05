/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.kafka;

import org.apache.commons.collections.map.HashedMap;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.util.JsonUtil;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.kafka.consumer.TigerBaseConsumer;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * @author alfred_yuan
 * @version v 0.1 16:57 alfred_yuan Exp $
 */
@Component
public class TigerConsumerStarter {

    @Autowired
    @Qualifier("activityConsumers")
    TigerBaseConsumer activityConsumers;

    @Autowired
    @Qualifier("messageConsumers")
    TigerBaseConsumer messageConsumers;

    // 是否启动
    private boolean enabled = true;

    // log单位
    public static final int LOG_UNIT = 10 * 6;

    // 消费者
    private KafkaConsumer consumer;
    // 消息
    private ConsumerRecords<String, String> records;

    private final Logger logger = Logger.getLogger(TigerConsumerStarter.class);

    // 计数器
    private int count = 0;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            //启动kafka
            if (enabled) {
                // CREATE
                consumer = new KafkaConsumer(getConsumerProperties());
                // 订阅主题
                consumer.subscribe(Arrays.asList(SystemConstants.KAFKA_TIGER_TOPIC));

                while (true) {
                    try {
                        // 休息一下
                        Thread.sleep(SystemConstants.KAFKA_SLEEP_UNIT);

                        if (count == LOG_UNIT) {
                            logger.info("tiger consumer is working!");
                            count = 0;
                        } else {
                            count++;
                        }

                        // 获取消息
                        records = consumer.poll(1000);

                        if (records.isEmpty()) {
                            continue;
                        }

                        // 转换成 notification -> notificationKeyEnum
                        Map<NotificationBasicDomain, NotificationKeyEnum> notificationMap = new HashedMap(records.count());
                        records.forEach(record -> notificationMap.put(JsonUtil.fromJson(record.value(), NotificationBasicDomain.class), NotificationKeyEnum.getEnumByCode(record.key())));

                        // 处理信息
                        activityConsumers.run(notificationMap);
                        messageConsumers.run(notificationMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // commit the offset
                        consumer.commitAsync();
                    }
                }
            }
        }).start();
    }

    /**
     * 默认的消费者参数
     *
     * @return
     */
    private Properties getConsumerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "tiger");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
