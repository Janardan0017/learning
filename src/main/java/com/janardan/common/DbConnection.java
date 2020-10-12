package com.janardan.common;

import com.janardan.application.opencsv.User;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created for janardan on 2/10/20
 */
public class DbConnection {

    public static Connection createConnection() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setServerName("com.mysql.jdbc.driver");
        dataSource.setURL("jdbc:mysql://localhost:3306/zeal");

        try {
            return dataSource.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static List<User> getUsers() {
        try {
            Connection connection = createConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id,firstname,email from globaluser");
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                users.add(user);
            }
            connection.close();
            statement.close();
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void addUsers() {
        try {
            Statement statement = createConnection().createStatement();
            while (true) {
                String[] queries = new String[1000000];
                for (int i = 0; i < 1000000; i++) {
                    queries[i] = "insert into demo (name) values('asdf')";
                }
                statement.execute("", queries);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
