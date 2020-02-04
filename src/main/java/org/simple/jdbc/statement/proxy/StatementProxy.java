package org.simple.jdbc.statement.proxy;

public interface StatementProxy<T> {
    String INSERT = "INSERT";
    String DELETE = "DELETE";
    String UPDATE = "UPDATE";
    String SELECT = "SELECT";

    <T> T execute();

    String contactSQL();
}
