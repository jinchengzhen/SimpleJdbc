package org.simple.jdbc.table.bean;

public class ColumnBean {
    private String columnName;
    private String propertyName;
    private Class<?> beanPropertyType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class<?> getBeanPropertyType() {
        return beanPropertyType;
    }

    public void setBeanPropertyType(Class<?> beanPropertyType) {
        this.beanPropertyType = beanPropertyType;
    }
}
