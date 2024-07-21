package com.myapp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCon {
	private static String db_user = "root";
	private static String db_password = "Nguyenleviz@311";
	
	public static Connection getConnection() {
		Connection con = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce_cart", db_user, db_password);
			
			System.out.println("connected");
		} catch (ClassNotFoundException e) {
            // Handle the exception if the driver class is not found
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle exceptions related to the connection
            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        }
		
		return con;
	}
}
