package org.simple.jdbc.statement.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
    String[] column() default "";
    //空值是否处理，false不检查，true空值抛异常
    boolean isMust() default false;
}
