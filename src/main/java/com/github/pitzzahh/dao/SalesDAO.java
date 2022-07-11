package com.github.pitzzahh.dao;

import com.github.pitzzahh.entity.Sales;
import com.github.pitzzahh.enums.Status;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.sql.DataSource;
import java.util.List;

public interface SalesDAO {

    Consumer<DataSource> setDataSource();

    Supplier<List<Sales>> getAllSales();

    Function<Sales, Status> saveSales();

}
