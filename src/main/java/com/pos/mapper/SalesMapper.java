package com.pos.mapper;

import org.springframework.jdbc.core.RowMapper;
import java.sql.SQLException;
import com.pos.entity.Sales;
import java.sql.ResultSet;

public class SalesMapper implements RowMapper<Sales> {

    @Override
    public Sales mapRow(ResultSet resultSet, int i) throws SQLException {
        return Sales.builder()
                .salesNumber(resultSet.getInt("sales_number"))
                .dateProcessed(resultSet.getDate("date_processed").toLocalDate())
                .profit(resultSet.getDouble("profit"))
                .build();
    }
}
