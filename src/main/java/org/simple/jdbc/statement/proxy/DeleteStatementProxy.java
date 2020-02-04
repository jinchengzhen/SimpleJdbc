package org.simple.jdbc.statement.proxy;

import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.table.bean.TableBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteStatementProxy extends BasicStatementProxyImpl<Boolean> {
    private DeleteBean deleteBean;

    public DeleteStatementProxy(DeleteBean deleteBean, DataSource dataSource) {
        super(dataSource);
        this.deleteBean = deleteBean;
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
            for (Expression expression : deleteBean.getExpressionList()) {
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

    //拼sql
    public String contactSQL() {
        StringBuilder sql = new StringBuilder(DELETE);
        sql.append(" FROM ");
        TableBean tableBean = deleteBean.getTableBean();
        sql.append(tableBean.getTableName());
        List<Expression> expressionList = deleteBean.getExpressionList();
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
