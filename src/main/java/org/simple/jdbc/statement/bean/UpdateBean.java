package org.simple.jdbc.statement.bean;

import org.simple.jdbc.table.bean.TableBean;

import java.util.List;

public class UpdateBean {
    private TableBean tableBean;
    private List<ColumnEntry> values;
    private List<Expression> expressionList;
    private boolean isMust;

    public TableBean getTableBean() {
        return tableBean;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }

    public List<ColumnEntry> getValues() {
        return values;
    }

    public void setValues(List<ColumnEntry> values) {
        this.values = values;
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }
}
