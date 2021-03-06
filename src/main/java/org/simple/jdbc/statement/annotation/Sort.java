package org.simple.jdbc.statement.annotation;

import org.simple.jdbc.statement.enumeration.SortWay;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sort {
    String[] column();
    SortWay[] type() default SortWay.DESC;
}
