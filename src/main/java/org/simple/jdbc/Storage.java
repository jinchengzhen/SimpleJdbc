package org.simple.jdbc;

import org.simple.jdbc.datasource.DataSourceFactory;
import org.simple.jdbc.resolver.*;
import org.simple.jdbc.statement.annotation.Statement;
import org.simple.jdbc.statement.bean.DeleteBean;
import org.simple.jdbc.statement.bean.InsertBean;
import org.simple.jdbc.statement.bean.SelectBean;
import org.simple.jdbc.statement.bean.UpdateBean;
import org.simple.jdbc.statement.handler.impl.BaseHandler;
import org.simple.jdbc.table.bean.TableBean;
import org.simple.jdbc.util.DBUtil;
import org.simple.jdbc.util.FileUtil;
import org.simple.jdbc.util.PropertyUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class Storage {
    private static Map<Class<?>, Object> objectMap;
    private static Map<String, StatementResolver> statementResolverMap;

    public static void init(String configFile) {
        PropertyUtil.init(configFile);
        objectMap = new HashMap<>();
        statementResolverMap = new HashMap<>();
        //初始化数据源
        DBUtil.init();

        //扫描映射文件
        String packages = PropertyUtil.getValue("packages");
        String[] packageArr = packages.split("-");

        // 存储当前加载的所有的类
        HashSet<String> classNames = new HashSet<>();
        for (String packageSingle : packageArr) {
            classNames.addAll(FileUtil.doScanPackage(packageSingle));
        }
        //实例化，动态代理
        for (String className : classNames) {
            String cn = className.replaceAll(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                Statement statement = clazz.getAnnotation(Statement.class);
                if (statement != null) {
                    if (statement.handler().getSuperclass() == BaseHandler.class) {
                        Object handlerIns = statement.handler().newInstance();
                        //set table class
                        Method method1 = statement.handler().getMethod("setTableClazz", Class.class);
                        Class<?> tableClazz = statement.table();
                        method1.invoke(handlerIns, tableClazz);
                        //set data source factory
                        Method method2 = statement.handler().getMethod("setDataSourceFactory", DataSourceFactory.class);
                        DataSourceFactory dataSourceFactory = Storage.get(DataSourceFactory.class);
                        method2.invoke(handlerIns, dataSourceFactory);
                        //动态代理生成代理对象
                        Object statementIns = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (InvocationHandler) handlerIns);
                        objectMap.put(clazz, statementIns);
                        //保存tableBean
                        TableResolver tableResolver = new TableResolver(tableClazz);
                        TableBean tableBean = tableResolver.resolver();
                        objectMap.put(tableClazz, tableBean);
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }


    }

    public static void put(Class<?> key, Object value) {
        objectMap.put(key, value);
    }

    public static <T> T get(Class<?> key) {
        Object obj = objectMap.get(key);
        Objects.requireNonNull(obj);
        return (T) obj;
    }

    public static <T> T getStatementBean(Class<?> tableClazz, Method method, Object[] args, Class<?> beanType) {
        String argsKey = args == null ? "" : Integer.toString(args.length);
        String key = tableClazz.getName() + method.getName() + argsKey;
        StatementResolver resolver = statementResolverMap.get(key);
        if (resolver == null) {
            if (InsertBean.class.isAssignableFrom(beanType)) {
                resolver = new InsertResolver(tableClazz, method, args);
            } else if (DeleteBean.class.isAssignableFrom(beanType)) {
                resolver = new DeleteResolver(tableClazz, method, args);
            } else if (UpdateBean.class.isAssignableFrom(beanType)) {
                resolver = new UpdateResolver(tableClazz, method, args);
            } else if (SelectBean.class.isAssignableFrom(beanType)) {
                resolver = new SelectResolver(tableClazz, method, args);
            }
            statementResolverMap.put(key, resolver);
        } else {
            resolver.setArgs(args);
        }
        return (T) resolver.resolver();
    }
}
