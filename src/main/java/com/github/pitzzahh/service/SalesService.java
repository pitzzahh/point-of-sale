package com.github.pitzzahh.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.github.pitzzahh.database.DatabaseConnection;
import com.github.pitzzahh.dao.SalesDAOImplementation;

/**
 * The service for managing sales.
 */
public class SalesService extends SalesDAOImplementation implements DatabaseConnection {

    /**
     * Gets the datasource from the {@link DatabaseConnection} interface.
     * @return the datasource to be used.
     */
    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }
}
