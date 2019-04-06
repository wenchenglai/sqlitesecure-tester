package com.example.sqlitesecure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.time.Duration;
import java.time.Instant;
import java.util.function.BiConsumer;

@SpringBootApplication
public class SqlitesecureApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SqlitesecureApplication.class, args);
    }

    private void CreateEncryptedFile(String password) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:encrypted.db", "", password);
            statement = connection.createStatement();

            statement.execute(String.format("PRAGMA key='%s'", password));
            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'John')");
            statement.executeUpdate("insert into person values(2, 'Mary')");

            ResultSet rs = statement.executeQuery("select * from person");

            System.out.println("Created database file successfully, print all the test data.");
            while(rs.next()) {
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }

        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null) {
                    connection.close();
                }

                if(statement != null) {
                    statement.close();
                }
            } catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void GetDataFromEncryptedFile(String password) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:encrypted.db", "", password);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM person");
            resultSet.next();
            String name = resultSet.getString("name");
            System.out.println("Data fetched from encrypted SQLite file: " + name);
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void run(String... args) {
        CreateEncryptedFile("good-password");

        // by providing good password, we could see the data
        GetDataFromEncryptedFile("good-password");

        // We should fail to get data due to bad password
        GetDataFromEncryptedFile("bad-password");
    }


}
