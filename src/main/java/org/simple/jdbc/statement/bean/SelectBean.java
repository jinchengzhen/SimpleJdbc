package org.simple.jdbc.statement.bean;

import org.simple.jdbc.table.bean.TableBean;

import java.lang.reflect.Type;
import java.util.List;

public class SelectBean {
    private TableBean tableBean;
    private Class[] contactTable;
    private List<String> columnNames;
    private List<Expression> expressionList;
    private LimitBean limitBean;
    private SortBean sortBean;
    private boolean isMust;
    private Type returnType;

    public TableBean getTableBean() {
        return tableBean;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

    public LimitBean getLimitBean() {
        return limitBean;
    }

    public void setLimitBean(LimitBean limitBean) {
        this.limitBean = limitBean;
    }

    public SortBean getSortBean() {
        return sortBean;
    }

    public void setSortBean(SortBean sortBean) {
        this.sortBean = sortBean;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Class[] getContactTable() {
        return contactTable;
    }

    public void setContactTable(Class[] contactTable) {
        this.contactTable = contactTable;
    }
}
