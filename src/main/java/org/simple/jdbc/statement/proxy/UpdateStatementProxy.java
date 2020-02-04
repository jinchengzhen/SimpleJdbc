package org.simple.jdbc.statement.proxy;

import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.statement.bean.UpdateBean;
import org.simple.jdbc.table.bean.TableBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpdateStatementProxy extends BasicStatementProxyImpl {
    private UpdateBean updateBean;

    public UpdateStatementProxy(UpdateBean updateBean, DataSource dataSource) {
        super(dataSource);
        this.updateBean = updateBean;
    }

    @Override
    public Boolean execute() {
        String sql = contactSQL();
        if (Boolean.getBoolean("debug")) {
            System.out.println(sql);
        }
        Connection connection = getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            int i = 1;
            for (ColumnEntry columnEntry : updateBean.getValues()) {
                if (columnEntry.getColumnValue() == null && !updateBean.isMust()) {
                    continue;
                }
                setValue(i++, columnEntry.getColumnBean().getBeanPropertyType().getSimpleName(), columnEntry.getColumnValue());
            }
            for (Expression expression : updateBean.getExpressionList()) {
                ColumnEntry columnEntry = expression.getColumn();
                if(columnEntry.getColumnValue() == null && !expression.isNonNull()){
                    continue;
                }
                setValue(i++, columnEntry.getColumnBean().getBeanPropertyType().getSimpleName(), columnEntry.getColumnValue());
            }
            preparedStatement.addBatch();
            //执行
            int[] nums = preparedStatement.executeBatch();
            if (nums.length > 0 && nums.length == 1) {
                if (nums[0] <= 0) {
                    return false;
                }
            } else {
                List<Integer> resultFailed = new ArrayList<>();
                int ii = 1;
                for (int n : nums) {
                    if (n <= 0) {
                        resultFailed.add(ii);
                    }
                    ii++;
                }
                if (!resultFailed.isEmpty()) {
                    if (Boolean.getBoolean("debug")) {
                        for (Integer n : resultFailed) {
                            System.out.println("Execute insert sql error at line : " + n);
                        }
                    }
                    throw new SQLException();
                }
            }
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String contactSQL() {
        StringBuilder sql = new StringBuilder(UPDATE);
        sql.append(" ");
        TableBean tableBean = updateBean.getTableBean();
        sql.append(tableBean.getTableName());
        sql.append(" SET ");
        for (ColumnEntry columnEntry : updateBean.getValues()) {
            if (columnEntry.getColumnValue() == null && !updateBean.isMust()) {
                continue;
            }
            sql.append(columnEntry.getColumnBean().getColumnName()).append("=?,");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        List<Expression> expressionList = updateBean.getExpressionList();
        if (expressionList != null && !expressionList.isEmpty()) {
            boolean isFirst = true;
            sql.append(" WHERE ");
            for (Expression expression : expressionList) {
                ColumnEntry columnEntry = expression.getColumn();
                if(columnEntry.getColumnValue() == null && !expression.isNonNull()){
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                    expression.setRelation(null);
                }
                sql.append(expression.toSQL()).append(' ');
            }
        }
        return sql.toString();
    }
}
