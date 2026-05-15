package org.example.studycarddemo.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//JDBC Connection to the created Database
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/studycard";
    private static final String USER = "postgres";
    private static final String PASSWORD = "chiphH1312@5";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
