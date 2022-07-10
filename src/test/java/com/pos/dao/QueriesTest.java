package com.pos.dao;

import static org.junit.jupiter.api.Assertions.*;
import com.pos.service.ProductService;
import org.junit.jupiter.api.Test;
import com.pos.enums.Category;
import com.pos.entity.Product;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.time.Month;
import java.util.List;

class QueriesTest {

    @Test
    void getProductByNameQuery() {
        ProductService productService = new ProductService();
        productService.setDataSource().accept(ProductService.getDataSource());

        Optional<Product> productOptional = productService.getProductByName().apply("SNICKERS");
        Product product = Product.builder()
                .id(17)
                .name("SNICKERS")
                .price(22.75)
                .category(Category.CHOCOLATE)
                .expirationDate(LocalDate.of(2022, Month.AUGUST, 4))
                .stocks(100)
                .discount(0.0)
                .build();
        assertEquals(productOptional.get().hashCode(), product.hashCode());
    }

    @Test
    void getProductByIdQuery() {
        ProductService productService = new ProductService();
        productService.setDataSource().accept(ProductService.getDataSource());

        Optional<Product> product = productService.getProductByName().apply("HERSHEYS");
        System.out.println(product.isEmpty());
        product.ifPresentOrElse(System.out::println, () -> new IllegalStateException("NO AVAILABLE"));
    }

    @Test
    void shouldAddALlProductsToList() {
        ProductService productService = new ProductService();
        productService.setDataSource().accept(ProductService.getDataSource());
        List<Optional<Product>> ALL_PRODUCTS = new ArrayList<>();
        productService.getAllProducts()
                .get()
                .stream()
                .forEach(ALL_PRODUCTS::add);
        System.out.println("SIZE: " + ALL_PRODUCTS.size());
        ALL_PRODUCTS.stream().forEach(System.out::println);
    }
}