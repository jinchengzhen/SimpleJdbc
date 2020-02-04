package org.simple.jdbc.statement.proxy;

import org.simple.jdbc.Storage;
import org.simple.jdbc.statement.bean.*;
import org.simple.jdbc.statement.enumeration.SortWay;
import org.simple.jdbc.table.bean.TableBean;
import org.simple.jdbc.validator.Validator;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SelectStatementProxy extends BasicStatementProxyImpl {
    private SelectBean selectBean;

    public SelectStatementProxy(SelectBean selectBean, DataSource dataSource) {
        super(dataSource);
        this.selectBean = selectBean;
    }

    @Override
    public Object execute() {
        String sql = contactSQL();
        if (Boolean.getBoolean("debug")) {
            System.out.println(sql);
        }
        Connection connection = getConnection();
        try {
            preparedStatement = connection.prepareStatement(sql);
            int i = 1;
            for (Expression expression : selectBean.getExpressionList()) {
                ColumnEntry columnEntry = expression.getColumn();
                if (columnEntry.getColumnValue() == null && !expression.isNonNull()) {
                    continue;
                }
                setValue(i++, columnEntry.getColumnBean().getBeanPropertyType().getSimpleName(), columnEntry.getColumnValue());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            Type returnType = selectBean.getReturnType();
            connection.commit();
            return getResult(resultSet, returnType);
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
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
        return null;
    }

    @Override
    public String contactSQL() {
        StringBuilder sql = new StringBuilder(SELECT);
        sql.append(' ');
        for (String columnName : selectBean.getColumnNames()) {
            sql.append(columnName).append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" FROM ");
        TableBean tableBean = selectBean.getTableBean();
        sql.append(tableBean.getTableName()).append(',');
        //关联表
        Class[] contactTables = selectBean.getContactTable();
        if (contactTables != null && contactTables.length > 0 && !contactTables[0].equals(Object.class)) {
            for (Class<?> tableClazz : contactTables) {
                TableBean tableBean1 = Storage.get(tableClazz);
                Objects.requireNonNull(tableBean1);
                sql.append(tableBean1.getTableName()).append(',');
            }
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        List<Expression> expressionList = selectBean.getExpressionList();
        if (expressionList != null && !expressionList.isEmpty()) {
            boolean isFirst = true;
            sql.append(" WHERE ");
            for (Expression expression : expressionList) {
                ColumnEntry columnEntry = expression.getColumn();
                if (columnEntry.getColumnValue() == null && !expression.isNonNull()) {
                    continue;
                }
                if (isFirst) {
                    isFirst = false;
                    expression.setRelation(null);
                }
                sql.append(expression.toSQL()).append(' ');
            }
        }
        SortBean sortBean = selectBean.getSortBean();
        if (sortBean != null) {
            String[] columnNames = sortBean.getColumnNames();
            SortWay[] sortWays = sortBean.getSortWays();
            if (columnNames.length == sortWays.length) {
                sql.append("ORDER BY ");
                for (int i = 0; i < columnNames.length; i++) {
                    sql.append(columnNames[i]).append(' ');
                    sql.append(sortWays[i]).append(',');
                }
                sql.deleteCharAt(sql.lastIndexOf(","));
            }
        }
        LimitBean limitBean = selectBean.getLimitBean();
        if (limitBean != null) {
            sql.append(" LIMIT ");
            sql.append(limitBean.getPageIndex()).append(',');
            sql.append(limitBean.getPageSize());
        }
        return sql.toString();
    }

    protected Object getResult(ResultSet resultSet, Type returnType) throws IllegalAccessException, InstantiationException, SQLException {
        Object result = null;
        if (returnType instanceof ParameterizedType) {
            if (List.class.equals(((ParameterizedType) returnType).getRawType())) {
                //获取List中泛型
                Class<?> tableType = (Class<?>) ((ParameterizedType) returnType).getActualTypeArguments()[0];
                result = resultToList(resultSet, tableType);
            } else if(Map.class.equals(((ParameterizedType) returnType).getRawType())){
                result = resultToMap(resultSet, resultSet.next());
            }
        } else if (returnType.equals(Integer.class)) {
            result = resultToInt(resultSet);
        } else if (returnType.equals(ResultSet.class)) {
            result = resultSet;
        } else { //bean对象
            result = resultToBean(resultSet, (Class<?>) returnType, resultSet.next());
        }
        return result;
    }

    protected List resultToList(ResultSet resultSet, Class<?> beanClazz) throws SQLException {
        List list = new ArrayList();
        while (resultSet.next()) {
            if(beanClazz.isAssignableFrom(Map.class)){
                Map map = resultToMap(resultSet, true);
                list.add(map);
            }else {
                Object obj = resultToBean(resultSet, beanClazz, true);
                list.add(obj);
            }
        }
        return list;
    }

    protected Object resultToBean(ResultSet resultSet, Class<?> beanClazz, boolean flag) throws SQLException {
        Object object = null;
        if (flag) {
            TableBean tableBean = selectBean.getTableBean();
            List<String> columnNames = selectBean.getColumnNames();
            try {
                object = beanClazz.newInstance();
                int index = 1;
                for (String columnName : columnNames) {
                    String filedName = tableBean.getColumnBean(columnName).getPropertyName();
                    Field field = beanClazz.getDeclaredField(filedName);
                    field.setAccessible(true);
                    if (Validator.checkField(field)) {
                        Object val = resultSet.getObject(index++);
                        field.set(object, val);
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    protected Map resultToMap(ResultSet resultSet, boolean flag) throws SQLException {
        Map<String,Object> map = new HashMap<>();
        if (flag) {
            TableBean tableBean = selectBean.getTableBean();
            List<String> columnNames = selectBean.getColumnNames();
            int index = 1;
            for (String columnName : columnNames) {
                String filedName = tableBean.getColumnBean(columnName).getPropertyName();
                Object val = resultSet.getObject(index++);
                map.put(filedName,val);
            }
        }
        return map;
    }

    protected Integer resultToInt(ResultSet resultSet) throws SQLException {
        int res = -1;
        if (resultSet.next()) {
            res = resultSet.getInt(0);
        }
        return res;
    }
}
