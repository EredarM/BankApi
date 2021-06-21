package supportClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String url = "jdbc:h2:/Users/a19215121/IdeaProjects/BankApi/src/main/resources/bank;AUTO_SERVER=TRUE";
    private static String user = "root";
    private static String password = "testtest";
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    public static Connection getConnection(String url, String user, String password) {
        if (connection == null) {
            ConnectionFactory.url = url;
            ConnectionFactory.user = user;
            ConnectionFactory.password = password;
            createConnection();
        }

        return connection;
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
