package org.simple.jdbc.resolver;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public interface Resolver<T> {
    <T>T resolver();
}
