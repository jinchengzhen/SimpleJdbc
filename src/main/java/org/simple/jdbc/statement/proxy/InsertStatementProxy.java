package org.simple.jdbc.statement.proxy;

import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.table.bean.TableBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertStatementProxy extends BasicStatementProxyImpl<Boolean> {
    private InsertBean insertBean;

    public InsertStatementProxy(InsertBean insertBean, DataSource dataSource) {
        super(dataSource);
        this.insertBean = insertBean;
    }

    public Boolean execute() {
        String sql = contactSQL();
        if (Boolean.getBoolean("debug")) {
            System.out.println(sql);
        }
        Connection connection = getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (List<ColumnEntry> columnEntries : insertBean.getValues()) {
                int i = 1;
                for (ColumnEntry columnEntry : columnEntries) {
                    if (columnEntry.getColumnValue() == null && !insertBean.isMust()) {
                        continue;
                    }
                    setValue(i++, columnEntry.getColumnBean().getBeanPropertyType().getSimpleName(), columnEntry.getColumnValue());
                }
                preparedStatement.addBatch();
            }
            //执行
            int[] nums = preparedStatement.executeBatch();
            if (nums.length > 0 && nums.length == 1) {
                if (nums[0] <= 0) {
                    return false;
                }
            } else {
                List<Integer> resultFailed = new ArrayList<>();
                int i = 1;
                for (int n : nums) {
                    if (n <= 0) {
                        resultFailed.add(i);
                    }
                    i++;
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
        StringBuilder sql = new StringBuilder(INSERT);
        sql.append(" ");
        TableBean tableBean = insertBean.getTableBean();
        sql.append(tableBean.getTableName()).append(" (");
        for (List<ColumnEntry> columnEntries : insertBean.getValues()) {
            for (ColumnEntry columnEntry : columnEntries) {
                if (columnEntry.getColumnValue() == null && !insertBean.isMust()) {
                    continue;
                }
                sql.append(columnEntry.getColumnBean().getColumnName()).append(",");
            }
            sql.deleteCharAt(sql.lastIndexOf(","));
            sql.append(") VALUES (");
            for (int i = 0; i < columnEntries.size(); i++) {
                sql.append("?,");
            }
            break;
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");
        return sql.toString();
    }
}
