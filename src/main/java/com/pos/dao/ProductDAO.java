package com.pos.dao;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import com.pos.entity.Product;
import com.pos.enums.Status;
import java.util.function.*;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.List;

public interface ProductDAO {

    Consumer<DataSource> setDataSource();

    Supplier<List<Optional<Product>>> getAllProducts();

    Optional<Long> getExpiredProductsCount();

    Function<Product, Status> saveProduct();

    Consumer<List<Product>> saveAllProducts();

    Function<Integer, Optional<Product>> getProductById();

    Function<Integer, OptionalDouble> getProductPriceById();

    Function<String, Optional<Product>> getProductByName();

    Function<Integer, OptionalInt> getProductStocksById();

    BiFunction<Double, Integer, Status> updateProductPriceById();

    BiFunction<Integer, String, Status> updateProductStocksByName();

    BiFunction<Double, Integer, Status> updateProductDiscountById();

    Function<Integer, Status> deleteProductById();

    Supplier<Status> deleteAllExpiredProducts();
}