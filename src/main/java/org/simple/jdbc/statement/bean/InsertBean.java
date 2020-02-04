package org.simple.jdbc.statement.bean;

import org.simple.jdbc.table.bean.TableBean;

import java.util.List;

public class InsertBean {
    private TableBean tableBean;
    private List<List<ColumnEntry>> values;
    private boolean isMust;

    public TableBean getTableBean() {
        return tableBean;
    }

    public void setTableBean(TableBean tableBean) {
        this.tableBean = tableBean;
    }

    public List<List<ColumnEntry>> getValues() {
        return values;
    }

    public void setValues(List<List<ColumnEntry>> values) {
        this.values = values;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }
}
