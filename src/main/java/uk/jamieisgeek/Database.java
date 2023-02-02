package uk.jamieisgeek;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String address;
    private final String databaseName;
    private final String username;
    private final String password;
    private final String port;

    private static Database database = null;
    private final HikariDataSource dataSource;

    public Database(String address, String database, String username, String password, String port) {
        this.address = address;
        this.databaseName = database;
        this.username = username;
        this.password = password;
        this.port = port;

        this.dataSource = createDataSource();

        Database.database = this;
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s?autoReconnect=true&useSSL=false", address, port, databaseName));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Database getDatabase() {
        return database;
    }

    public void closeConnection() throws SQLException {
        dataSource.close();
    }

    public List<String> grabLines() {
        List<String> messages = new ArrayList<>();
        try (Connection connection = Database.getDatabase().getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SQL QUERY HERE");
            statement.execute();

            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                messages.add(rs.getString("collumn_name"));
                System.out.println("Added line");
            }

            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
