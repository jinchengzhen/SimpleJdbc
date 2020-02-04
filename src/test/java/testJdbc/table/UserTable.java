package testJdbc.table;

import org.simple.jdbc.table.annotation.Table;
import testJdbc.bean.User;

@Table(name = "t_user", beanClass = User.class)
public interface UserTable {
    String name = "t_user.t_name";
    String id = "t_user.t_id";
}
