/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 5:05 PM yiliang.gyl Exp $
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"tiger.core.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource(value = {"classpath:kafka.properties", "kafka.properties"}, ignoreResourceNotFound = true)
public class CoreConfig {

    /**
     * The environment.
     */
    @Autowired
    Environment environment;

    @Bean
    public KafkaProducer kafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", environment.getProperty("bootstrap.servers"));
        props.put("acks", environment.getProperty("acks"));
        props.put("retries", environment.getProperty("retries"));
        props.put("batch.size", environment.getProperty("batch.size"));
        props.put("linger.ms", environment.getProperty("linger.ms"));
        props.put("buffer.memory", environment.getProperty("buffer.memory"));
        props.put("key.serializer", environment.getProperty("key.serializer"));
        props.put("value.serializer", environment.getProperty("value.serializer"));
        return new KafkaProducer<String, String>(props);
    }


}
