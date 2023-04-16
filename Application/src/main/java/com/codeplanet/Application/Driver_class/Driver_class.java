package com.codeplanet.Application.Driver_class;

import java.sql.Connection;
import java.sql.DriverManager;


final public class Driver_class {
	public final static Connection driver_class() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
        
	     // Setup the connection with the DB
	        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root" , "aalam");
	       return connect;
	}
}
