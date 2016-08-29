package com.cdjdgm.dip.apigate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.config.WebIniSecurityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cdjdgm.dip.apigate.shiro.filter.HmacAuthenticationFilter;
import com.cdjdgm.dip.apigate.shiro.filter.UrlPermFilter;


@SpringBootApplication
@PropertySource("classpath:application.properties")
//@ImportResource({
//        //"classpath*:appContext-dubbo.xml",
//    	"classpath*:uaa-shiro-plugin.xml"
//    })
@Import({CommonConfig.class})
@ComponentScan(basePackages={"com.cdjdgm.dip.apigate"})
@EnableCaching
@EnableTransactionManagement(proxyTargetClass = true)
public class ApiGateApplication extends SpringBootServletInitializer {
	static Logger logger = LoggerFactory.getLogger(ApiGateApplication.class);

	@Bean
	public SecurityManager securityManager() {
		// "classpath:shiro.ini"
		WebIniSecurityManagerFactory factory = new WebIniSecurityManagerFactory();
		SecurityManager securityManager = factory.getInstance();
		return securityManager;
	}
	
//	@Autowired
//	HmacAuthenticationFilter hmacAuthenFilter;
	
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager());
		
		Map<String, Filter> filters = new HashMap<String, Filter>();
//	    filters.put("anon", new AnonymousFilter());
//	    filters.put("authc", new FormAuthenticationFilter());
//	    filters.put("logout", new LogoutFilter());
//	    filters.put("roles", new RolesAuthorizationFilter());
//	    filters.put("user", new UserFilter());
	    filters.put("authcHmac", authcHmac());
	    filters.put("urlPerm", urlPermFilter());
	    factoryBean.setFilters(filters);
	    
		return factoryBean;
	}
	
	@Bean
	public UrlPermFilter urlPermFilter(){
		UrlPermFilter urlPermFilter = new UrlPermFilter();
		return urlPermFilter;
	}
	
	@Bean
	public HmacAuthenticationFilter authcHmac(){
		HmacAuthenticationFilter authcHmac = new HmacAuthenticationFilter();
		authcHmac.setExpirationThreshold(600000);
		authcHmac.setAuthzScheme("AWS");
		return authcHmac;
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ApiGateApplication.class);
	}

	public static void main(String[] args) {
        //ApplicationContext ctx = SpringApplication.run(PluginConfig.class, args);
        //String[] beanNames = ctx.getBeanDefinitionNames();
		SpringApplication.run(ApiGateApplication.class, args);
	}

}
