package com.github.pitzzahh.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import static com.github.pitzzahh.dao.Queries.*;
import com.github.pitzzahh.entity.Product;
import com.github.pitzzahh.enums.Status;
import javax.sql.DataSource;
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
    public Function<String, Optional<Product>> getProductByName() {
        return name -> getProductByNameQuery(name, jdbcTemplate);
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
