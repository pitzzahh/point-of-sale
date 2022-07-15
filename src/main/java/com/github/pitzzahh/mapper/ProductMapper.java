package com.github.pitzzahh.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.github.pitzzahh.entity.Product;
import com.github.pitzzahh.enums.Category;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * Class used to map data from the table to a {@code Optional<Product>} object
 */
public class ProductMapper implements RowMapper<Optional<Product>> {

    /**
     * maps the data from the table to a {@code Optional<Product>} object.
     * @param resultSet the ResultSet to map (pre-initialized for the current row)
     * @param numberOfRow the number of the current row
     * @return {@code Optional<Product>} object.
     * @throws SQLException if somethings went wrong.
     */
    @Override
    public Optional<Product> mapRow(ResultSet resultSet, int numberOfRow) throws SQLException {
        return Optional.ofNullable(Product.builder()
                .id(resultSet.getInt("product_id"))
                .name(resultSet.getString("name"))
                .price(resultSet.getDouble("price"))
                .category(Category.valueOf(resultSet.getString("category")))
                .expirationDate(resultSet.getDate("expiration_date").toLocalDate())
                .stocks(resultSet.getInt("stocks_left"))
                .discount(resultSet.getDouble("discount"))
                .build());

    }
}
