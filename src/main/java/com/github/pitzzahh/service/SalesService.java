package com.github.pitzzahh.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.github.pitzzahh.database.DatabaseConnection;
import com.github.pitzzahh.dao.SalesDAOImplementation;

public class SalesService extends SalesDAOImplementation implements DatabaseConnection {

    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }
}
