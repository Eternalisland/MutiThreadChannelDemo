package com.ergs.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnection {
    private static final String pas ="";
	private static final String url = "jdbc:mysql://localhost:3306/ergs?useUnicode=true&characterEncoding=utf8";
	private static final String user = "root";

	public static Connection getConnection(){  
	try {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection(url, user,pas);	
		}
	          catch (ClassNotFoundException e) {
		e.printStackTrace();}
	          catch (SQLException e) {
		e.printStackTrace();
	}
	return null;
	}
}
