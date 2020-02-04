package org.simple.jdbc.statement.bean;

import org.simple.jdbc.statement.annotation.Insert;

public class LimitBean {
    private Integer pageIndex;
    private Integer pageSize;

    public LimitBean() {

    }

    public LimitBean(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
