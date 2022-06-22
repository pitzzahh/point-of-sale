package com.pos.service;

import java.util.function.*;

import com.pos.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Supplier<List<Product>> getAllProducts();

    Supplier<Integer> getExpiredProductsCount();

    Consumer<Product> saveProduct();

    Consumer<List<Product>> saveAllProducts();

    Function<Integer, Optional<Product>> getProductById();
    
    Function<String, Product> getProductByName();

    Function<Integer, Integer> getProductStocksById();
    
    BiConsumer<Double, Integer> updateProductPriceById();

    BiConsumer<Integer, Integer> updateProductStocksById();

    BiConsumer<Double, Integer> updateProductDiscountById();

    Consumer<Integer> deleteProductById();

    void deleteAllExpiredProducts();

    void insertAllProductsToDatabase();
    
    void updateAllProductsExpirationDate();

}
