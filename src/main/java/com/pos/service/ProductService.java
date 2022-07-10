package com.pos.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.pos.dao.ProductDAOImplementation;
import com.pos.database.DatabaseConnection;

public class ProductService extends ProductDAOImplementation implements DatabaseConnection {

    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }

}
