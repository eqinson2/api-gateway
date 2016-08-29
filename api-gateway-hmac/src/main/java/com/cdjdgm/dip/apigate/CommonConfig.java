package com.cdjdgm.dip.apigate;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class CommonConfig implements ServletContextAware {
	static Logger logger = LoggerFactory.getLogger(CommonConfig.class);

	@Autowired
	protected Environment env;
	@Autowired
	protected ApplicationContext appContext;

	protected ServletContext servletContext;

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected NamedParameterJdbcTemplate npJdbcTemplate;

	@PostConstruct
	public void init() {
		
	}


	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		// WebApplicationContext webApplicationContext =
		// (WebApplicationContext)servletContext
		// .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}


	
//	@Bean(name="shiroRealm")
//	@Autowired
//	public AuthorizingRealm shiroRealm(AuthorizingRealm realm){
//		return realm;
//	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	}

//	@Bean
//	public SpringUtil springUitl(){
//		return new SpringUtil();
//	}
	
//	@Bean
//	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//		// 防止IE下载JSON
//		jsonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
//		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		mapper.registerModule(buildSimpleModule());
//		jsonConverter.setObjectMapper(mapper);
//		return jsonConverter;
//	}
//	
//	private SimpleModule buildSimpleModule() {
//		SimpleModule module = new SimpleModule() {
//			private static final long serialVersionUID = 1L;
//
//			public void setupModule(SetupContext context) {
//				super.setupModule(context);
//
//				context.addBeanSerializerModifier(new BeanSerializerModifier() {
//
//					public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
//							JsonSerializer<?> serializer) {
//						if (serializer instanceof BeanSerializerBase) {
//							return new PropertyExtendableBeanSerializer((BeanSerializerBase) serializer);
//						}
//						return serializer;
//					}
//				});
//			}
//		};
//		//module.addSerializer(Date.class, new JsonTimeSerializer());
//		return module;
//	}

}
