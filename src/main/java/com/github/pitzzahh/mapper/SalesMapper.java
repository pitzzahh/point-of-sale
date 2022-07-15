package com.github.pitzzahh.mapper;

import org.springframework.jdbc.core.RowMapper;
import com.github.pitzzahh.entity.Sales;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Class used to map data from the table to a {@code Sales} object
 */
public class SalesMapper implements RowMapper<Sales> {

    /**
     * maps the data from the table to a {@code Sales} object.
     * @param resultSet the ResultSet to map (pre-initialized for the current row)
     * @param i the number of the current row
     * @return a {@code Sales} object.
     * @throws SQLException if somethings went wrong.
     */
    @Override
    public Sales mapRow(ResultSet resultSet, int i) throws SQLException {
        return Sales.builder()
                .salesNumber(resultSet.getInt("sales_number"))
                .dateProcessed(resultSet.getDate("date_processed").toLocalDate())
                .profit(resultSet.getDouble("profit"))
                .build();
    }
}
