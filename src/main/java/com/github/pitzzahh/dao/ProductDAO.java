package com.github.pitzzahh.dao;

import java.util.OptionalDouble;
import java.util.OptionalInt;

import com.github.pitzzahh.entity.Product;
import com.github.pitzzahh.enums.Status;
import java.util.function.*;
import javax.sql.DataSource;
import java.util.Optional;
import java.util.List;

public interface ProductDAO {

    Consumer<DataSource> setDataSource();

    Supplier<List<Optional<Product>>> getAllProducts();

    Optional<Long> getExpiredProductsCount();

    Function<String, Optional<Product>> getProductByName();

    BiFunction<Double, Integer, Status> updateProductPriceById();

    BiFunction<Integer, String, Status> updateProductStocksByName();

    BiFunction<Double, Integer, Status> updateProductDiscountById();

    Function<Integer, Status> deleteProductById();

    Supplier<Status> deleteAllExpiredProducts();
}