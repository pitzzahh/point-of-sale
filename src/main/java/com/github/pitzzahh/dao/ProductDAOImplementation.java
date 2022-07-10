package com.github.pitzzahh.dao;

import com.github.pitzzahh.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import static com.github.pitzzahh.dao.Queries.*;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import javax.sql.DataSource;
import com.github.pitzzahh.enums.Status;
import java.util.function.*;
import java.util.Optional;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Consumer<DataSource> setDataSource() {
        return source -> {
            this.dataSource = source;
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        };
    }

    @Override
    public Supplier<List<Optional<Product>>> getAllProducts() {
        return getAllProductsQuery(jdbcTemplate);
    }

    @Override
    public Optional<Long> getExpiredProductsCount() {
        return getExpiredProductsCountQuery(jdbcTemplate);
    }

    @Override
    public Function<Product, Status> saveProduct() {
        return product -> saveProductQuery(product, jdbcTemplate);
    }

    @Override
    public Consumer<List<Product>> saveAllProducts() {
        return products -> saveALlProductsQuery(products, jdbcTemplate);
    }

    @Override
    public Function<Integer, Optional<Product>> getProductById() {
        return id -> getProductByIdQuery(id, jdbcTemplate);
    }

    @Override
    public Function<Integer, OptionalDouble> getProductPriceById() {
        return id -> getProductPriceByIdQuery(id, jdbcTemplate);
    }

    @Override
    public Function<String, Optional<Product>> getProductByName() {
        return name -> getProductByNameQuery(name, jdbcTemplate);
    }

    @Override
    public Function<Integer, OptionalInt> getProductStocksById() {
        return id -> getProductStocksByIdQuery(id, jdbcTemplate);
    }

    @Override
    public BiFunction<Double, Integer, Status> updateProductPriceById() {
        return (newPrice, id) -> updateProductPriceByIdQuery(newPrice, id, jdbcTemplate);
    }

    @Override
    public BiFunction<Integer, String, Status> updateProductStocksByName() {
        return (newStocks, name) -> updateProductStocksByNameQuery(newStocks, name, jdbcTemplate);
    }

    @Override
    public BiFunction<Double, Integer, Status> updateProductDiscountById() {
        return (newDiscount, id) -> updateProductDiscountByIdQuery(newDiscount, id, jdbcTemplate);
    }

    @Override
    public Function<Integer, Status> deleteProductById() {
        return id -> deleteProductByIdQuery(id, jdbcTemplate);
    }

    @Override
    public Supplier<Status> deleteAllExpiredProducts() {
        return deleteAllExpiredProductsQuery(jdbcTemplate);
    }

}
