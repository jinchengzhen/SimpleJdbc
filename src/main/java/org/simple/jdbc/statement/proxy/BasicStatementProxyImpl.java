package org.simple.jdbc.statement.proxy;

import javax.sql.DataSource;
import java.sql.*;

public abstract class BasicStatementProxyImpl<T> implements StatementProxy {
    protected DataSource dataSource;
    protected PreparedStatement preparedStatement;

    public BasicStatementProxyImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void setValue(int i, String typeName, Object o) {
        try {
            switch (typeName) {
                case "String":
                    preparedStatement.setString(i, (String) o);
                    break;
                case "Integer":
                    preparedStatement.setInt(i, (Integer) o);
                    break;
                case "Boolean":
                    preparedStatement.setBoolean(i, (Boolean) o);
                    break;
                case "Double":
                    preparedStatement.setDouble(i, (Double) o);
                    break;
                case "Float":
                    preparedStatement.setFloat(i, (Float) o);
                    break;
                case "Array":
                    preparedStatement.setArray(i, (Array) o);
                    break;
                case "List":
                    preparedStatement.setArray(i, (Array) o);
                    break;
                case "Date":
                    preparedStatement.setDate(i, (Date) o);
                    break;
                default:
                    preparedStatement.setObject(i, o);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
