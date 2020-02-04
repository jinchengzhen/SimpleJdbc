package org.simple.jdbc.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Validator {
    public static boolean checkField(Field field) {
        if ((Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
            return false;
        }
        return true;
    }
}
