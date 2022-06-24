package com.pos;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import com.pos.service.ProductServiceImplementation;
import org.springframework.context.annotation.Bean;
import com.pos.service.SalesServiceImplementation;
import com.pos.service.ProductService;
import com.pos.service.SalesService;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.pos.repository" })
@ComponentScan("com.pos")
@PropertySource("classpath:application.properties")
public class Config {

	@Bean
	public ProductService productService() {
		return new ProductServiceImplementation();
	}

	@Bean
	SalesService salesService() {
		return new SalesServiceImplementation();
	}

}
