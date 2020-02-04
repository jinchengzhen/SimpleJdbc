package testJdbc.Statement;

import org.simple.jdbc.statement.annotation.*;
import testJdbc.bean.User;
import testJdbc.table.UserTable;

import java.util.List;

@Statement(table = UserTable.class)
public interface UserStatement {
    @Insert
    boolean insert(User user);

    @Delete
    boolean delete(
            @Expression(column = UserTable.id) String id
    );

    @Update
    boolean update(
            User user,
            @Expression(column = UserTable.id) String id
    );

    @Select
    User selectById(
            @Expression(column = UserTable.id) String id
    );

    @Select
    @Sort(column = UserTable.id)
    List<User> selectByName(
            @Expression(column = UserTable.name) String name,
            @Limit int pageIndex,
            @LimitSize int pageSize
    );
}
