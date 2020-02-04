package org.simple.jdbc.statement.handler.impl;

import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.statement.bean.SelectBean;
import org.simple.jdbc.statement.bean.UpdateBean;
import org.simple.jdbc.statement.proxy.DeleteStatementProxy;
import org.simple.jdbc.statement.proxy.InsertStatementProxy;
import org.simple.jdbc.statement.proxy.SelectStatementProxy;
import org.simple.jdbc.statement.proxy.UpdateStatementProxy;

public class DBHandler extends BaseHandler {

    @Override
    public boolean insert(InsertBean insertBean) {
        InsertStatementProxy insertStatementProxy = new InsertStatementProxy(insertBean, dataSourceFactory.getDataSource());
        return insertStatementProxy.execute();
    }

    @Override
    public boolean update(UpdateBean updateBean) {
        UpdateStatementProxy updateStatementProxy = new UpdateStatementProxy(updateBean, dataSourceFactory.getDataSource());
        return updateStatementProxy.execute();
    }

    @Override
    public boolean delete(DeleteBean deleteBean) {
        DeleteStatementProxy deleteStatementProxy = new DeleteStatementProxy(deleteBean, dataSourceFactory.getDataSource());
        return deleteStatementProxy.execute();
    }

    @Override
    public <T> T select(SelectBean selectBean) {
        SelectStatementProxy selectStatementProxy = new SelectStatementProxy(selectBean, dataSourceFactory.getDataSource());
        return (T) selectStatementProxy.execute();
    }

}
