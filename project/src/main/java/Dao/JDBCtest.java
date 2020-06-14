package Dao;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCtest {
    private static String url = "jdbc:sqlserver://localhost:1433;DatabaseName=db";
    public static void update() throws SQLException {
        Connection connection = DriverManager.getConnection(url,"sa","123456");
        Statement statement= connection.createStatement();
        String sql="update test set val=val+1";
        statement.executeUpdate(sql);
    }
}
