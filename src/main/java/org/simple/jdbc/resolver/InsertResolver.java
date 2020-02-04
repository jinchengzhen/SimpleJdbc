package org.simple.jdbc.resolver;

import org.simple.jdbc.statement.annotation.Insert;
import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.table.bean.TableBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InsertResolver extends StatementResolver<InsertBean> {
    private InsertBean insertBean;

    public InsertResolver(Class<?> tableClazz, Method method, Object[] args) {
        super(tableClazz, method, args);
    }

    public InsertBean resolver() {
        if (method.isAnnotationPresent(Insert.class)) {
            Insert insert = method.getAnnotation(Insert.class);
            Objects.requireNonNull(args);
            isMust = insert.isMust();
            if(insertBean == null){
                insertBean = new InsertBean();
                tableBean = getTableBean();
                insertBean.setTableBean(tableBean);
                insertBean.setMust(isMust);
            }
            List<List<ColumnEntry>> columnEntryList = new ArrayList<>();
            Object obj = args[0];
            //是否为数组
            if (obj.getClass().isArray()) {
                Object[] beans = (Object[]) obj;
                for (Object bean : beans) {
                    if (bean.getClass() != tableBean.getBeanClass()) {
                        throw new TypeNotPresentException(bean.getClass().getName(), null);
                    }
                    List<ColumnEntry> columnEntries = getColumnEntries(bean, tableBean, insert.column());
                    columnEntryList.add(columnEntries);
                }
            }else {
                try {
                    //是否为list集合
                    if(List.class.isAssignableFrom(obj.getClass()) || obj.getClass().newInstance() instanceof List){
                        List<Object> beans = (List) obj;
                        for (Object bean : beans){
                            if(bean.getClass() != tableBean.getBeanClass()){
                                throw new TypeNotPresentException(bean.getClass().getName(),null);
                            }
                            List<ColumnEntry> columnEntries = getColumnEntries(bean, tableBean, insert.column());
                            columnEntryList.add(columnEntries);
                        }
                    }
                    //是否为bean
                    else {
                        Object bean = obj;
                        if(bean.getClass() != tableBean.getBeanClass()){
                            throw new TypeNotPresentException(bean.getClass().getName(),null);
                        }
                        List<ColumnEntry> columnEntries = getColumnEntries(bean, tableBean, insert.column());
                        columnEntryList.add(columnEntries);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            insertBean.setValues(columnEntryList);
            return insertBean;
        }
        return null;
    }

}
