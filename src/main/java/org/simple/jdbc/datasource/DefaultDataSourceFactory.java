package org.simple.jdbc.datasource;

import javax.sql.DataSource;

public class DefaultDataSourceFactory extends BasicDataSourceFactory {
    public DefaultDataSourceFactory(String jdbcUrl, String driverClass, String username, String password){
        this.jdbcUrl = jdbcUrl;
        this.driverClass = driverClass;
        this.username = username;
        this.password = password;
    }
    public DefaultDataSourceFactory(){}
    @Override
    public DataSource getDataSource() {
        DataSource dataSource = new BasicDataSource(jdbcUrl,username,password);
        return dataSource;
    }
}
