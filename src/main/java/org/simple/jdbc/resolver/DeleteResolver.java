package org.simple.jdbc.resolver;

import org.simple.jdbc.statement.annotation.Delete;
import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.table.bean.TableBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DeleteResolver extends StatementResolver<DeleteBean> {
    private DeleteBean deleteBean;

    public DeleteResolver(Class<?> tableClazz, Method method, Object[] args) {
        super(tableClazz, method, args);
    }

    @Override
    public DeleteBean resolver() {
        if (method.isAnnotationPresent(Delete.class)) {
            Annotation[][] annotationArr = method.getParameterAnnotations();
            if(deleteBean == null){
                deleteBean = new DeleteBean();
                tableBean = getTableBean();
                deleteBean.setTableBean(tableBean);
            }
            List<Expression> expressionList = new ArrayList<>();
            int i = 0;
            for (Annotation[] annotations : annotationArr) {
                org.simple.jdbc.statement.annotation.Expression expressionAnnotation = getAnnotation(annotations, org.simple.jdbc.statement.annotation.Expression.class);
                if (expressionAnnotation != null) {
                    Object paramVal = args[i];
                    Expression expression = getExpression(expressionAnnotation, paramVal, tableBean);
                    expressionList.add(expression);
                }
                i++;
            }
            deleteBean.setExpressionList(expressionList);
            return deleteBean;
        }
        return null;
    }
}
