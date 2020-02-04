package org.simple.jdbc.resolver;

import org.simple.jdbc.statement.annotation.Limit;
import org.simple.jdbc.statement.annotation.LimitSize;
import org.simple.jdbc.statement.annotation.Select;
import org.simple.jdbc.statement.annotation.Sort;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.statement.bean.LimitBean;
import org.simple.jdbc.statement.bean.SelectBean;
import org.simple.jdbc.statement.bean.SortBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SelectResolver extends StatementResolver<SelectBean> {
    private SelectBean selectBean;

    public SelectResolver(Class<?> tableClazz, Method method, Object[] args) {
        super(tableClazz, method, args);
    }

    @Override
    public Object resolver() {
        if (method.isAnnotationPresent(Select.class)) {
            Select select = method.getAnnotation(Select.class);
            Objects.requireNonNull(args);
            isMust = select.isMust();
            if (selectBean == null) {
                selectBean = new SelectBean();
                tableBean = getTableBean();
                selectBean.setTableBean(tableBean);
                selectBean.setMust(isMust);
                selectBean.setContactTable(select.contactTable());
                List<String> columnNames = null;
                if (select.column().length == 1 && "".equals(select.column()[0])) {
                    columnNames = tableBean.getAllColumnNames();
                } else {
                    columnNames = Arrays.asList(select.column());
                }
                selectBean.setColumnNames(columnNames);
                Type returnType = method.getGenericReturnType();
                selectBean.setReturnType(returnType);
                if (method.isAnnotationPresent(Sort.class)) {
                    Sort sort = method.getAnnotation(Sort.class);
                    SortBean sortBean = new SortBean();
                    sortBean.setColumnNames(sort.column());
                    sortBean.setSortWays(sort.type());
                    selectBean.setSortBean(sortBean);
                }
            }
            Annotation[][] annotationArr = method.getParameterAnnotations();
            List<Expression> expressionList = new ArrayList<>();
            int i = 0;
            for (Annotation[] annotations : annotationArr) {
                org.simple.jdbc.statement.annotation.Expression expressionAnnotation = getAnnotation(annotations, org.simple.jdbc.statement.annotation.Expression.class);
                if (expressionAnnotation != null) {
                    Object paramVal = args[i];
                    Expression expression = getExpression(expressionAnnotation, paramVal, tableBean);
                    expressionList.add(expression);
                }
                Limit limit = getAnnotation(annotations, Limit.class);
                if (limit != null) {
                    int pageIndex = (int) args[i];
                    LimitBean limitBean = new LimitBean();
                    limitBean.setPageIndex(pageIndex);
                    if (limit.pageSize() > 0) {
                        limitBean.setPageSize(limit.pageSize());
                    }
                    selectBean.setLimitBean(limitBean);
                }
                LimitSize limitSize = getAnnotation(annotations, LimitSize.class);
                if (limitSize != null) {
                    LimitBean limitBean = selectBean.getLimitBean();
                    limitBean.setPageSize((int) args[i]);
                    selectBean.setLimitBean(limitBean);
                }
                i++;
            }
            selectBean.setExpressionList(expressionList);
            return selectBean;
        }
        return null;
    }
}
