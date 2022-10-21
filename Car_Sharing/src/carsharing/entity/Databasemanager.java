package carsharing.entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Databasemanager {
    static final String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:file:./src/carsharing/db/";

    public static Connection createConnection(String dbName){
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void createTableCompany(Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL)";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(statement!=null) statement.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        }
    }

    public static void createTableCar(Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CAR " +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "COMPANY_ID INTEGER NOT NULL, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(statement!=null) statement.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        }
    }

    public static void createTableCustomer(Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String sql =  "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INTEGER, " +
                    "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(statement!=null) statement.close();
            } catch(SQLException se2) {
            } // nothing we can do
            try {
                if(connection!=null) connection.close();
            } catch(SQLException se){
                se.printStackTrace();
            } //end finally try
        }
    }

}
