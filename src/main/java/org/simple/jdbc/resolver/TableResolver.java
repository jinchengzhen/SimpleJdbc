package org.simple.jdbc.resolver;

import org.simple.jdbc.table.annotation.Table;
import org.simple.jdbc.table.bean.ColumnBean;
import org.simple.jdbc.table.bean.TableBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableResolver implements Resolver<TableBean> {
    private Class<?> tableClazz;

    public TableResolver(Class<?> tableClazz) {
        if(tableClazz.isInterface()){
            this.tableClazz = tableClazz;
        }
    }

    public TableBean resolver() {
        Objects.requireNonNull(tableClazz, "table class is null because it is not interface!");
        Table table = tableClazz.getAnnotation(Table.class);
        if (table != null) {
            TableBean tableBean = new TableBean();
            List<ColumnBean> columnBeanList = new ArrayList<>();
            Field[] fields = tableClazz.getFields();
            for (Field field : fields) {
                field.setAccessible(true);
                ColumnBean columnBean = getColumnBean(field, tableClazz);
                columnBeanList.add(columnBean);
            }
            tableBean.setBeanClass(table.beanClass());
            tableBean.setTableName(table.name());
            tableBean.setColumnBeanList(columnBeanList);
            return tableBean;
        }
        return null;
    }

    public ColumnBean getColumnBean(Field field, Class<?> tableClazz) {
        ColumnBean columnBean = new ColumnBean();
        columnBean.setBeanPropertyType(field.getType());
        columnBean.setPropertyName(field.getName());
        try {
            columnBean.setColumnName((String) field.get(tableClazz));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return columnBean;
    }
}
