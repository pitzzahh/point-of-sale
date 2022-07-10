package com.pos.dao;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import com.pos.entity.Sales;
import com.pos.enums.Status;
import javax.sql.DataSource;
import java.util.List;

public interface SalesDAO {

    Consumer<DataSource> setDataSource();

    Supplier<List<Sales>> getAllSales();

    Function<Sales, Status> saveSales();

}
