package org.simple.jdbc.table.bean;

import java.util.*;

public class TableBean {
    private String tableName;
    private Class<?> beanClass;
    private List<ColumnBean> columnBeanList;
    private Map<String, ColumnBean> columnBeanMap;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public List<ColumnBean> getColumnBeanList() {
        return columnBeanList;
    }

    public void setColumnBeanList(List<ColumnBean> columnBeanList) {
        this.columnBeanList = columnBeanList;
        setColumnBeanMap();
    }

    public void setColumnBeanMap() {
        Objects.requireNonNull(columnBeanList);
        columnBeanMap = new HashMap<>();
        for (ColumnBean columnBean : columnBeanList) {
            columnBeanMap.put(columnBean.getColumnName(), columnBean);
        }
    }

    public ColumnBean getColumnBean(String columnName) {
        if (columnBeanMap != null) {
            return columnBeanMap.get(columnName);
        }
        return null;
    }

    public List<String> getAllColumnNames(){
        if(columnBeanMap != null){
            return new ArrayList<>(columnBeanMap.keySet());
        }
        return null;
    }
}
