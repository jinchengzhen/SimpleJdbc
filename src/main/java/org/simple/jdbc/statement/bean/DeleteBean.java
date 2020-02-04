package org.simple.jdbc.statement.bean;

import org.simple.jdbc.table.bean.TableBean;

import java.util.List;

public class DeleteBean {
    private TableBean tableBean;
    private List<Expression> expressionList;

    public TableBean getTableBean() {
        return tableBean;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }

    public List<Expression> getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(List<Expression> expressionList) {
        this.expressionList = expressionList;
    }
}
