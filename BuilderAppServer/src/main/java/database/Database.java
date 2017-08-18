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

public static Connection getConnection() throws Exception {
								//Using Local Database
								String url = "jdbc:mysql://localhost:3306/";
								String dbName = "AutoCompBuilderDB";
								String properties = "?useSSL=false";
								String driver = "com.mysql.jdbc.Driver";
								//App Database Credentials go here
								String userName = "appDataBuilder";
								String password = "autocompdatabuilder";

								Class.forName(driver).newInstance();
								Connection conn = DriverManager.getConnection(url + dbName + properties, userName,password);

								return conn;
}
}
