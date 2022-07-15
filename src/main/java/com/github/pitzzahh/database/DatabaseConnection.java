package com.github.pitzzahh.database;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.github.pitzzahh.service.*;
/**
 * Interface used by services to gain access to the database.
 */
public interface DatabaseConnection {

    /**
     * Setup the datasource.
     * @see ProductService
     * @see SalesService
     * @return the datasource.
     */
    static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/pos");
        dataSource.setUsername("postgres");
        dataSource.setPassword("!Password123");
        return dataSource;
    }

}
