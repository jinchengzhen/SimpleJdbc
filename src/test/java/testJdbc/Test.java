package testJdbc;

import org.simple.jdbc.Storage;
import testJdbc.Statement.UserStatement;
import testJdbc.bean.User;

public class Test {
    public static void init() {
        String basePath = System.getProperty("user.dir");
        String configFile = basePath + "\\config.properties";
        Storage.init(configFile);
    }

    public static void main(String[] args) {
        init();
        UserStatement userStatement = Storage.get(UserStatement.class);
//        long start = System.currentTimeMillis();
//        userStatement.insert(new User("aaa", "444"));
//        long end1 = System.currentTimeMillis();
//        userStatement.insert(new User("aaa", "555"));
//        long end2 = System.currentTimeMillis();
//        userStatement.update(new User("eee", null),"444");
//        long end3 = System.currentTimeMillis();
//        System.out.println("first : " + (end1 - start));
//        System.out.println("second : " + (end2 - end1));
//        System.out.println("third : " + (end3 - end2));
//        userStatement.delete("1126");
//        userStatement.update(new User("eee", null),"666");
//        userStatement.update(new User("qqq", null),"226");
        for(int i = 0; i < 10; i++) {
            userStatement.insert(new User("bbb",Integer.toString(i)));
        }
        userStatement.delete("3");
        userStatement.update(new User("ccccc",null), "5");
        User user = userStatement.selectById("6");
        System.out.println(user.toString());
    }

}
