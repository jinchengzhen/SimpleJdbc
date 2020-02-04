package org.simple.jdbc.statement.annotation;


import org.simple.jdbc.statement.enumeration.Option;
import org.simple.jdbc.statement.enumeration.Relation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Expression {
    Relation relation() default Relation.AND;

    String column();

    Option option() default Option.EQUAL;

    //false表示null值不处理该表达式
    boolean nonNull() default false;

    String value() default "";
}
