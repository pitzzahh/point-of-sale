package com.github.pitzzahh.database;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public interface DatabaseConnection {

    static DriverManagerDataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost/pos");
        dataSource.setUsername("postgres");
        dataSource.setPassword("!Password123");
        return dataSource;
    }

}
