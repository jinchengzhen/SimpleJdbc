package org.simple.jdbc.statement.bean;

import org.simple.jdbc.statement.enumeration.Option;
import org.simple.jdbc.statement.enumeration.Relation;

public interface Expression {
    void setOption(Option option);

    Option getOption();

    void setRelation(Relation relation);

    Relation getRelation();

    void setColumn(ColumnEntry column);

    ColumnEntry getColumn();

    boolean isNonNull();

    void setNonNull(boolean nonNull);

    String toSQL();
}
