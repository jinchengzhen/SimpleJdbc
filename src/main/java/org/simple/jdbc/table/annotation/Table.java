package org.simple.jdbc.table.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    //表名
    String name();

    //java bean映射
    Class<?> beanClass();

    //_转驼峰命名，默认false
//    boolean mapUnderscoreToCamelCase() default false;
}
