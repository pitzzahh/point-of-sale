/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pos;

import com.pos.service.SalesService;
import com.pos.service.SalesServiceImplementation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.pos.service.ProductService;
import com.pos.service.ProductServiceImplementation;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.pos.repository" })
@ComponentScan("com.pos")
@PropertySource("classpath:application.properties")
public class JPAConfiguration {

	@Bean
	public ProductService productService() {
		return new ProductServiceImplementation();
	}

	@Bean
	SalesService salesService() {
		return new SalesServiceImplementation();
	}

}
