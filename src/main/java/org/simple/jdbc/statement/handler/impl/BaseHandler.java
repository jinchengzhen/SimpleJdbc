package org.simple.jdbc.statement.handler.impl;


import org.simple.jdbc.Storage;
import org.simple.jdbc.datasource.DataSourceFactory;
import org.simple.jdbc.resolver.DeleteResolver;
import org.simple.jdbc.resolver.InsertResolver;
import org.simple.jdbc.statement.annotation.Delete;
import org.simple.jdbc.statement.annotation.Insert;
import org.simple.jdbc.statement.annotation.Select;
import org.simple.jdbc.statement.annotation.Update;
import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.statement.bean.SelectBean;
import org.simple.jdbc.statement.bean.UpdateBean;
import org.simple.jdbc.statement.handler.Handler;

import java.lang.reflect.Method;

public abstract class BaseHandler implements Handler {
    protected Class<?> tableClazz;
    protected DataSourceFactory dataSourceFactory;

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Select.class)) {
            SelectBean selectBean = Storage.getStatementBean(tableClazz, method, args, SelectBean.class);
            return select(selectBean);
        }else if (method.isAnnotationPresent(Insert.class)) {
            InsertBean insertBean = Storage.getStatementBean(tableClazz, method, args, InsertBean.class);
            return insert(insertBean);
        }else if (method.isAnnotationPresent(Update.class)) {
            UpdateBean updateBean = Storage.getStatementBean(tableClazz, method, args, UpdateBean.class);
            return update(updateBean);
        }else if (method.isAnnotationPresent(Delete.class)) {
            DeleteBean deleteBean = Storage.getStatementBean(tableClazz, method, args, DeleteBean.class);
            return delete(deleteBean);
        }

        return null;
    }

    public abstract boolean insert(InsertBean insertBean);

    public abstract boolean update(UpdateBean updateBean);

    public abstract boolean delete(DeleteBean deleteBean);

    public abstract <T> T select(SelectBean selectBean);


    public Class<?> getTableClazz() {
        return tableClazz;
    }

    public void setTableClazz(Class<?> tableClazz) {
        this.tableClazz = tableClazz;
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }
}
