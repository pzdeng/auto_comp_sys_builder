package main.java.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for getting database connections
 * @author Peter
 *
 */
public class Database {

	public Database(){
		//nothing here
	}
	
	public static Connection getConnection() throws Exception{
		String url = "jdbc:mysql://autocompbuilder.ccmk3v3o5ojq.us-east-1.rds.amazonaws.com:3306/";
        String dbName = "AutoCompBuilderDB";
        String driver = "com.mysql.jdbc.Driver";
        //Database Credentials go here
        String userName = "app";
        String password = "autocomp";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName, userName,password);
        
		return conn;
	}
}
