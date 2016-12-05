/*
 *
 */
package tiger.web.mng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * @author Hupeng
 * @version v 0.1 2015年10月9日 上午12:40:29 Hupeng Exp $
 */
public class TigerMngMain extends SpringBootServletInitializer{
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        //程序启动入口
        new SpringApplication(MngConfig.class).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MngConfig.class, TigerMngMain.class);
    }
}
