package tiger.kafka;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import tiger.biz.TigerApplication;

/**
 * Created by domicc on 5/3/16.
 */
public class KafkaMain extends SpringBootServletInitializer {

    /**
     * Kafka 程序入口
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        new TigerApplication(AppConfig.class)
                .run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppConfig.class, KafkaMain.class);
    }

}
