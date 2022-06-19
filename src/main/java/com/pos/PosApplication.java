package com.pos;

import com.pos.entity.Category;
import com.pos.entity.Product;
import com.pos.service.ProductService;
import java.time.LocalDate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class PosApplication {

	public static void main(String[] args) {
            AbstractApplicationContext context = new AnnotationConfigApplicationContext(JPAConfiguration.class);
            ProductService productService = context.getBean(ProductService.class);  
            
            Product product = new Product();
            
            product.setName("Ferrero Rocher");
            product.setPrice(100.00);
            product.setCategory(Category.CHOCOLATE);
            product.setExpirationDate(LocalDate.of(2022, 7, 7));
            product.setStocks(100);
            product.setExpired(LocalDate.of(2022, 7, 7).isBefore(LocalDate.now()));
            product.setDiscount(null);
            productService.saveProduct().accept(product);
            
            
	}

}
