package org.simple.jdbc.statement.handler.impl;


import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.statement.bean.SelectBean;
import org.simple.jdbc.statement.bean.UpdateBean;

public class DefaultHandler extends BaseHandler {

    @Override
    public boolean insert(InsertBean insertBean) {
        //校验数据

        //添加
//        DataFrame dataFrame = Storage.getDataFrame(insertBean.getTableBean().getTable());
//        dataFrame.addRowDataList(insertBean.getDataVectors());

        return true;
    }

    @Override
    public boolean update(UpdateBean updateBean) {
        return false;
    }

    @Override
    public boolean delete(DeleteBean deleteBean) {
        return false;
    }

    @Override
    public <T> T select(SelectBean selectBean) {
        return null;
    }

//    @Override
//    public <T> T select(SelectBean selectBean) {
//        return null;
//    }
//
//    @Override
//    public int count(SelectBean selectBean) {
//        return 0;
//    }


}
