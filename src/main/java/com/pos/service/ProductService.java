package com.pos.service;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import com.pos.entity.Product;
import java.util.List;

public interface ProductService {

    Supplier<List<Product>> getAllProducts();

    Supplier<Integer> getExpiredProductsCount();

    Consumer<Product> saveProduct();

    Consumer<List<Product>> saveAllProducts();

    Function<Integer, Product> getProductById();
    Function<String, Product> getProductByName();

    Function<Integer, Integer> getProductStocksById();
    
    BiConsumer<Integer, Double> updateProductPriceById();

    Consumer<Integer> deleteProductById();

    void deleteAllExpiredProducts();

    void insertAllProductsToDatabase();
    
    void updateAllProductsExpirationDate();

}
