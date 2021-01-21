package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBService {
    private static DBService service;
    private final SQLiteDataSource dataSource;

    private DBService(String dbName) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + dbName);
    }

    public static DBService getInstance(String dbName) {
        if (service == null) {
            service = new DBService(dbName);
        }
        return service;
    }

    public void modifyData(String query) {
        try (Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet extractData(String query) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
