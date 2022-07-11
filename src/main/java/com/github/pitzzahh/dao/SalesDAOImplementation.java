package com.github.pitzzahh.dao;

import com.github.pitzzahh.service.ProductService;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.pitzzahh.entity.Sales;
import com.github.pitzzahh.enums.Status;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.sql.DataSource;
import java.util.List;

public class SalesDAOImplementation extends ProductService implements SalesDAO  {

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
    public Supplier<List<Sales>> getAllSales() {
        return Queries.getAllSalesQuery(jdbcTemplate);
    }

    @Override
    public Function<Sales, Status> saveSales() {
        return sales -> Queries.saveSalesQuery(sales, jdbcTemplate) ;
    }

}
