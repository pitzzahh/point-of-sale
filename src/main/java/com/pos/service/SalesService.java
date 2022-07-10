package com.pos.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.pos.database.DatabaseConnection;
import com.pos.dao.SalesDAOImplementation;

public class SalesService extends SalesDAOImplementation implements DatabaseConnection {

    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }
}
