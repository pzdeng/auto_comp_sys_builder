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
		String url = "jdbc:mysql://autocompsysbuilderdb.ccmk3v3o5ojq.us-east-1.rds.amazonaws.com:3306/";
        String dbName = "autocompdb";
        String driver = "com.mysql.jdbc.Driver";
        //Database Credentials go here
        String userName = "";
        String password = "";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName, userName,password);
        
		return conn;
	}
	
	public static void closeConnection(Connection conn) throws Exception{
		if(conn != null){
			conn.close();
		}
	}
}