package database;

import config.LoggerConfig;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String SELECT_ALL_FROM_ACCOUNT_QUERY = "select * from account";
    private static final String ID_LABEL = "ID";
    private static final String NAME_LABEL = "Name";
    private static final String BALANCE_LABEL = "Balance";
    protected static Connection connection;
    private static final Logger LOGGER = LoggerConfig.getLoggerInstance();
    private static final String CLASS_NAME = "DatabaseConnector";

    protected static void openConnection() {
        String methodName = "openConnection()";
        LOGGER.entering(CLASS_NAME, methodName);
        if (connection == null) {
            try {
                LOGGER.fine("Creating connection");
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
                LOGGER.fine("Created connection");
            } catch (ClassNotFoundException | SQLException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
            }
        }
        LOGGER.exiting(CLASS_NAME, methodName);
    }

    protected static void closeConnection() {
        String methodName = "closeConnection()";
        LOGGER.entering(CLASS_NAME, methodName);
        try {
            LOGGER.fine("Closing connection");
            connection.close();
            connection = null;
            LOGGER.fine("Closed connection");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        LOGGER.exiting(CLASS_NAME, methodName);
    }

    protected static void printSqlQuery() {
        String methodName = "printSqlQuery()";
        LOGGER.entering(CLASS_NAME, methodName);
        openConnection();
        try ( Statement stmt = connection.createStatement();  ResultSet rs = stmt.executeQuery(SELECT_ALL_FROM_ACCOUNT_QUERY)) {
            LOGGER.fine("Reading from database");
            while (rs.next()) {
                LOGGER.fine("New line was read");
                System.out.println(rs.getInt(ID_LABEL) + " " + rs.getString(NAME_LABEL) + " " + rs.getDouble(BALANCE_LABEL));
            }
            LOGGER.fine("Read from database");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        closeConnection();
        LOGGER.exiting(CLASS_NAME, methodName);
    }
}
