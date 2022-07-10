package com.pos.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import com.pos.service.ProductService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import static com.pos.dao.Queries.*;
import com.pos.entity.Sales;
import com.pos.enums.Status;
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
        return getAllSalesQuery(jdbcTemplate);
    }

    @Override
    public Function<Sales, Status> saveSales() {
        return sales -> saveSalesQuery(sales, jdbcTemplate) ;
    }

}
