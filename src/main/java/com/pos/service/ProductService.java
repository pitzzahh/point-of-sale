package com.pos.service;

import com.pos.entity.Product;
import java.util.function.*;
import java.util.Optional;
import java.util.List;

public interface ProductService {

    Supplier<List<Product>> getAllProducts();

    Supplier<Integer> getExpiredProductsCount();

    Consumer<Product> saveProduct();

    Consumer<List<Product>> saveAllProducts();

    Function<Integer, Optional<Product>> getProductById();
    
    Function<String, Product> getProductByName();

    Function<Integer, Integer> getProductStocksById();
    
    BiConsumer<Double, Integer> updateProductPriceById();

    BiConsumer<Integer, String> updateProductStocksByName();

    BiConsumer<Double, Integer> updateProductDiscountById();

    Consumer<Integer> deleteProductById();

    void deleteAllExpiredProducts();

    void insertAllProductsToDatabase();
    
    void updateAllProductsExpirationDate();

}
