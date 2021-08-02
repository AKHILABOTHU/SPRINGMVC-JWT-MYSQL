/**
 * This class contains the details for the configurations
 * needed for the app to start
 * @author akhil
 *
*/

package com.thinkbig.configuration;

import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

//import com.thinkbig.service.AmazonS3Service;
//import com.thinkbig.service.AmazonS3ServiceImpl;
//import com.thinkbig.service.FireBaseService;
//import com.thinkbig.service.FireBaseServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.thinkbig.filter.RequestInterceptor;


@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.thinkbig")
@PropertySource({"classpath:mysql.properties","classpath:token.properties"})
public class ApplicationConfiguration implements WebMvcConfigurer { //extends WebSecurityConfigurerAdapter  {
	
	
	@Autowired
	private Environment env;
	
	private Logger logger = Logger.getLogger(getClass().getName()); 
	
	// define a bean for ViewResolver
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp ");
		return viewResolver;
	}
	
	//creating Datasource
	
	@Bean
	public DataSource myDataSource() {
		
		// create connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		// jdbc driver class
		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
			securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
			securityDataSource.setUser(env.getProperty("jdbc.user"));
			securityDataSource.setPassword(env.getProperty("jdbc.password"));
			securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
			securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
			securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
			securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		return securityDataSource;
	}
	
	
	// get Hibernate Properties
	private Properties getHibernateProperties() {
		Properties props = new Properties();
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		props.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
		return props;				
	}
	
	// get session factory
	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		// create session factorys
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set the properties
		sessionFactory.setDataSource(myDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
	
		return sessionFactory;
	}
	
	// Configure Hibernate Transaction Manager
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}	
	
	// Helper method
	private int getIntProperty(String s) {
		return Integer.parseInt(env.getProperty(s));
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestInterceptor(env.getProperty("secret_key"))).addPathPatterns("/**").excludePathPatterns("/authentication/**");
	}
	

	//Multipart Resolver
	@Bean
	public  CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(25971520); //25MB
		resolver.setMaxInMemorySize(1048576); //1MB
		return resolver;
		
	}
	
//	 @Bean
//	 public FireBaseService myService() {
//	        return new FireBaseServiceImpl(); 
//	 }
	 
//	 @Bean
//	 public AmazonS3Service s3Operations() {
//	        return new AmazonS3ServiceImpl(); 
//	 }
}
