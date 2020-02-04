package org.simple.jdbc.datasource;

import javax.sql.DataSource;

public interface DataSourceFactory {
    DataSource getDataSource();
}
