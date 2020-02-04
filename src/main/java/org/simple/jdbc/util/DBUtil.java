package org.simple.jdbc.util;

import org.simple.jdbc.Storage;
import org.simple.jdbc.datasource.BasicDataSourceFactory;
import org.simple.jdbc.datasource.DataSourceFactory;
import org.simple.jdbc.datasource.DefaultDataSourceFactory;

public class DBUtil {
    public static void init(){
        String driverClass = PropertyUtil.getValue("driverClass");
        String jdbcUrl = PropertyUtil.getValue("jdbcUrl");
        String username = PropertyUtil.getValue("username");
        String password = PropertyUtil.getValue("password");
        try {
            Class.forName(driverClass);
            String dataSourceType = PropertyUtil.getValue("dataSourceType");
            BasicDataSourceFactory dataSourceFactory = null;
            if(dataSourceType != null && !"".equals(dataSourceType)){
                Class<?> dataSourceFactoryClazz = Class.forName(dataSourceType);
                dataSourceFactory = (BasicDataSourceFactory) dataSourceFactoryClazz.newInstance();
            }else {
                dataSourceFactory = new DefaultDataSourceFactory();
            }
            dataSourceFactory.setJdbcUrl(jdbcUrl);
            dataSourceFactory.setUsername(username);
            dataSourceFactory.setPassword(password);
            //注入容器
            Storage.put(DataSourceFactory.class,dataSourceFactory);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
