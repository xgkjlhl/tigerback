package tiger.web.mng;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置
 *
 * @author HuPeng
 * @version v 0.1 2015年10月19日 下午4:29:34 HuPeng Exp $
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        Map<String, String> filterChainDefinitionMapping = new LinkedHashMap<>();
        filterChainDefinitionMapping.put("/static/**", "anon");
        filterChainDefinitionMapping.put("/logout", "logout");
        filterChainDefinitionMapping.put("/", "authc");
        filterChainDefinitionMapping.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
        shiroFilter.setSecurityManager(securityManager());
        Map<String, Filter> filters = new HashMap<>();
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setLoginUrl("/login");
        formAuthenticationFilter.setSuccessUrl("/index");
        formAuthenticationFilter.setPasswordParam("password");
        formAuthenticationFilter.setUsernameParam("username");
        filters.put("authc", formAuthenticationFilter);
        filters.put("anon", new AnonymousFilter());
        filters.put("logout", new LogoutFilter());
        shiroFilter.setFilters(filters);
        return shiroFilter;
    }

    @Bean(name = "securityManager")
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        // securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean(name = "myRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public AuthorizingRealm myRealm() {
        ShiroRealm realm = new ShiroRealm();
        // realm.setCacheManager(cacheManager());
        realm.init();
        return realm;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheManager();
    }
}
