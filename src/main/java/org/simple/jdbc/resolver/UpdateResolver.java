package org.simple.jdbc.resolver;

import org.simple.jdbc.statement.annotation.Update;
import org.simple.jdbc.statement.bean.ColumnEntry;
import org.simple.jdbc.statement.bean.Expression;
import org.simple.jdbc.statement.bean.UpdateBean;
import org.simple.jdbc.table.bean.TableBean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateResolver extends StatementResolver<UpdateBean> {
    private UpdateBean updateBean;

    public UpdateResolver(Class<?> tableClazz, Method method, Object[] args) {
        super(tableClazz, method, args);
    }

    @Override
    public UpdateBean resolver() {
        if (method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);
            Objects.requireNonNull(args);
            isMust = update.isMust();
            if(updateBean == null){
                updateBean = new UpdateBean();
                tableBean = getTableBean();
                updateBean.setTableBean(tableBean);
                updateBean.setMust(isMust);
            }

            List<ColumnEntry> columnEntries = new ArrayList<>();
            Annotation[][] annotationArr = method.getParameterAnnotations();
            List<Expression> expressionList = new ArrayList<>();
            int i = 0;
            for(Annotation[] annotations : annotationArr){
                org.simple.jdbc.statement.annotation.Expression expressionAnnotation = getAnnotation(annotations, org.simple.jdbc.statement.annotation.Expression.class);
                if(expressionAnnotation != null && args.length > 1){
                    Object paramVal = args[i];
                    Expression expression = getExpression(expressionAnnotation, paramVal, tableBean);
                    expressionList.add(expression);
                }else {
                    Object bean = args[0];
                    if(bean.getClass() != tableBean.getBeanClass()){
                        throw new TypeNotPresentException(bean.getClass().getName(),null);
                    }
                    columnEntries = getColumnEntries(bean, tableBean, update.column());
                }
                i++;
            }
            updateBean.setValues(columnEntries);
            updateBean.setExpressionList(expressionList);
            return updateBean;
        }
        return null;
    }
}
