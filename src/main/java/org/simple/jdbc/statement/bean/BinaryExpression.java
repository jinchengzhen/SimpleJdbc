package org.simple.jdbc.statement.bean;


import org.simple.jdbc.statement.enumeration.Option;
import org.simple.jdbc.statement.enumeration.Relation;

public class BinaryExpression implements Expression {
    private ColumnEntry column;
    private Option option;
    private Relation relation;
    private boolean nonNull;

    public ColumnEntry getColumn() {
        return column;
    }

    public void setColumn(ColumnEntry column) {
        this.column = column;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public boolean isNonNull() {
        return nonNull;
    }

    public void setNonNull(boolean nonNull) {
        this.nonNull = nonNull;
    }

    public String toSQL(){
        StringBuilder stringBuilder = new StringBuilder();
        String relation = getRelation() == null ? "" : getRelation().toString()+" ";
        stringBuilder.append(relation);
        String columnName = getColumn().getColumnBean().getColumnName();
        String tmp = getOption().toSQL(columnName);
        stringBuilder.append(tmp);
        return stringBuilder.toString();
    }

}
