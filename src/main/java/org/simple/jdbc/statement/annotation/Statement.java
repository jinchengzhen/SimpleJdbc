package org.simple.jdbc.statement.annotation;


import org.simple.jdbc.statement.handler.impl.DBHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Statement {
    Class<?> handler() default DBHandler.class;
    Class<?> table();
}
