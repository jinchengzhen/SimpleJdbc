package org.simple.jdbc.statement.bean;

import org.simple.jdbc.statement.enumeration.SortWay;

public class SortBean {
    private String[] columnNames;
    private SortWay[] sortWays;

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public SortWay[] getSortWays() {
        return sortWays;
    }

    public void setSortWays(SortWay[] sortWays) {
        this.sortWays = sortWays;
    }
}
