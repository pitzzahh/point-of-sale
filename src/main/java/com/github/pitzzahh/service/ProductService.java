package com.github.pitzzahh.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.github.pitzzahh.dao.ProductDAOImplementation;
import com.github.pitzzahh.database.DatabaseConnection;

public class ProductService extends ProductDAOImplementation implements DatabaseConnection {

    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }

}
