package com.github.pitzzahh.service;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.github.pitzzahh.dao.ProductDAOImplementation;
import com.github.pitzzahh.database.DatabaseConnection;

/**
 * The service for managing products.
 */
public class ProductService extends ProductDAOImplementation implements DatabaseConnection {

    /**
     * Gets the datasource from the {@link DatabaseConnection} interface.
     * @return the datasource to be used.
     */
    public static DriverManagerDataSource getDataSource() {
        return DatabaseConnection.getDataSource();
    }

}
