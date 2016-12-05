package tiger.web.mng;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import tiger.biz.mng.MngBizConfig;
import tiger.common.dal.DataConfig;
import tiger.core.CoreConfig;

/**
 * APP 主配置
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 13, 2015 7:14:03 AM yiliang.gyl Exp $
 */
@SpringBootApplication
@ComponentScan(basePackages = { "tiger.web", "tiger.biz", "tiger.common.dal",
                                "tiger.core.service", "mng.core.service"})
@Import({ DataConfig.class, CoreConfig.class, MngBizConfig.class, ShiroConfig.class })
@Configuration
@EnableWebMvc
public class MngConfig extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
                                                                   "classpath:/resources/",
                                                                   "classpath:/static/",
                                                                   "classpath:/public/" };

    //    private String freemarkerTemplate = "classpath:/resources/templates/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/index");
    }

    //    @Bean
    //    public freemarker.template.Configuration getFreeMarkerConfig() throws IOException {
    //        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
    //        cfg.setDirectoryForTemplateLoading(new File(freemarkerTemplate));
    //        cfg.setDefaultEncoding("UTF-8");
    //        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    //        cfg.addAutoImport("tiger", "basic.ftl");
    //        return cfg;
    //    }

}
