package org.simple.jdbc.resolver;

import org.simple.jdbc.Storage;
import org.simple.jdbc.statement.bean.BinaryExpression;
import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.statement.enumeration.Option;
import org.simple.jdbc.statement.enumeration.Relation;
import org.simple.jdbc.table.annotation.Table;
import org.simple.jdbc.table.bean.ColumnBean;
import org.simple.jdbc.table.bean.TableBean;
import org.simple.jdbc.validator.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class StatementResolver<T> implements Resolver {
    protected Class<?> tableClazz;
    protected TableBean tableBean;
    protected Method method;
    protected Object[] args;
    protected boolean isMust;

    public StatementResolver(Class<?> tableClazz, Method method, Object[] args){
        this.tableClazz = tableClazz;
        this.method = method;
        this.args = args;
    }

    protected List<ColumnEntry> getColumnEntries(Object bean, TableBean tableBean, String[] columns) {
        List<ColumnEntry> columnEntries = new ArrayList<>();
        if (columns.length > 0 && !"".equals(columns[0])) {
            for (String columnName : columns) {
                ColumnEntry columnEntry = getFiledValue(bean, columnName, tableBean);
                columnEntries.add(columnEntry);
            }
        } else {
            for (ColumnBean columnBean : tableBean.getColumnBeanList()) {
                ColumnEntry columnEntry = getFiledValue(bean, columnBean.getColumnName(), tableBean);
                columnEntries.add(columnEntry);
            }
        }
        return columnEntries;
    }

    protected ColumnEntry getFiledValue(Object bean, String columnName, TableBean tableBean) {
        Class<?> clazz = bean.getClass();
        ColumnEntry columnEntry = new ColumnEntry();
        ColumnBean columnBean = tableBean.getColumnBean(columnName);
        String filedName = columnBean.getPropertyName();
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            if(Validator.checkField(field)){
                Object value = field.get(bean);
                //空值检查，为空抛异常
                if(isMust){
                    Objects.requireNonNull(value);
                }
                columnEntry.setColumnBean(columnBean);
                columnEntry.setColumnValue(value);
                return columnEntry;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Class<?> getBeanClass(){
       return tableClazz.getAnnotation(Table.class).beanClass();
    }

    protected TableBean getTableBean(){
        if(tableBean == null){
            tableBean = Storage.get(tableClazz);
        }
        return tableBean;
    }

    protected Expression getExpression(org.simple.jdbc.statement.annotation.Expression expression, Object paramVal, TableBean tableBean){
        Expression expressionBean = null;
        String columnName = expression.column();
        ColumnBean columnBean = tableBean.getColumnBean(columnName);
        String value = expression.value();
        ColumnEntry columnEntry = new ColumnEntry();
        columnEntry.setColumnBean(columnBean);
        if("".equals(value)){
            columnEntry.setColumnValue(paramVal);
        }else {
            columnEntry.setColumnValue(value);
        }
        Option option = expression.option();
        Relation relation = expression.relation();
        boolean nonNull = expression.nonNull();
        expressionBean = new BinaryExpression();
        expressionBean.setColumn(columnEntry);
        expressionBean.setOption(option);
        expressionBean.setRelation(relation);
        expressionBean.setNonNull(nonNull);
        return expressionBean;
    }

    protected <T>T getAnnotation(Annotation[] annotations, Class<?> annotationClazz){
        for (Annotation annotation : annotations){
            if (annotationClazz.isAssignableFrom(annotation.getClass())) {
                return (T) annotation;
            }
        }
        return null;
    }

    public void setArgs(Object[] args){
        this.args = args;
    }
}
