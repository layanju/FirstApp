package pack305;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author soso_
 */
public class CreateDB {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/project2";
    private static final String USER = "root";
    private static final String PASSWORD = "layanasdf";

    public static void main(String[] args) {
        System.out.println("Database creation example!");
        Connection con = null;
        try {

            // (1) set the path for the database
            String ConnectionURL = "jdbc:mysql://localhost:3306";

            // (2) create connection
            con = DriverManager.getConnection(ConnectionURL, "root", "layanasdf");

            // (3) create statment object
            Statement st = con.createStatement();

            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter Database name:");
            String database = bf.readLine();

            // (4) excute sql statment
            st.executeUpdate("CREATE DATABASE " + database);

            System.out.println("1 row(s) affacted");

            // (5) close connection
            con.close();
        } catch (SQLException s) {
            System.out.println("SQL statement is not executed!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
