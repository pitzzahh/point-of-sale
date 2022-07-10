package com.pos.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.pos.entity.Product;
import com.pos.enums.Category;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Optional;

public class ProductMapper implements RowMapper<Optional<Product>> {

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
