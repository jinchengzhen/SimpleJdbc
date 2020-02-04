package org.simple.jdbc.statement.bean;

import org.simple.jdbc.table.bean.ColumnBean;

public class ColumnEntry {
    private ColumnBean ColumnBean;
    private Object columnValue;

    public org.simple.jdbc.table.bean.ColumnBean getColumnBean() {
        return ColumnBean;
    }

    public void setColumnBean(org.simple.jdbc.table.bean.ColumnBean columnBean) {
        ColumnBean = columnBean;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }
}
