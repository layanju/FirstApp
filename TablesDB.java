/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pack305;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DELL
 */
public class TablesDB {

    public static void main(String[] args) {
        Connection con = null;
        try {
            // Set the path for the database
            String ConnectionURL = "jdbc:mysql://localhost:3306/project2";
            // Create connection
            con = DriverManager.getConnection(ConnectionURL, "root", "layanasdf");
            // Create statement object
            Statement st = con.createStatement();
//
            String createTableSQL = "CREATE TABLE USERS ("
                    + "FirstName VARCHAR(50), "
                    + "LastName VARCHAR(50), "
                    + "Username CHAR(50),"
                    + "Email VARCHAR(50), "
                    + "Role VARCHAR(50), "
                    + "Password VARCHAR(50)"
                    + ")";
            st.executeUpdate(createTableSQL);

            String createTableSQLcons = "CREATE TABLE Consultative  ("
                    + "FirstName VARCHAR(50), "
                    + "LastName VARCHAR(50), "
                    + "Username CHAR(50), "
                    + "Email VARCHAR(50), "
                    + "Role VARCHAR(50), "
                    + "Password VARCHAR(50),"
                    + "Profession VARCHAR(50)"
                    + ")";
            st.executeUpdate(createTableSQLcons);
            String createAppointmentTableSQL = "CREATE TABLE APPOINTMENTS ("
                    + "AppointmentID INT AUTO_INCREMENT PRIMARY KEY, "
                    + "Fname VARCHAR(50),"
                    + "Lname VARCHAR(50),"
                    + "Username CHAR(50), " 
                    + "Beneficiary_Name VARCHAR(50),"
                    + "AppointmentDate DATE, "
                    + "AppointmentTime TIME, "
                    + "Booked BOOLEAN)";
            st.executeUpdate(createAppointmentTableSQL);
            // Confirm database and table creation
            System.out.println("Database and table created successfully.");

            // Query employees with salary > 5000
        } catch (SQLException s) {
            System.out.println("SQL statement is not executed!");
            s.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
