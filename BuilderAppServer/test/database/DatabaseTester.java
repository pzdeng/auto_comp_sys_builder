package database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import main.java.database.Database;

/**
 * Sanity (Access) Check on database
 * @author Peter
 *
 */
public class DatabaseTester {
	
	@Test
    public void testDatabaseAccess() {
		Connection dbConn;
		boolean result = false;
		
		try {
			dbConn = Database.getConnection();
			result = runSQLStatement(dbConn);
			dbConn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertEquals(result, true);
	}
	
	/**
	 * Count number of rows in cpu table
	 * @param conn
	 * @return
	 */
	private boolean runSQLStatement(Connection conn){
		int counter = 0;
		try{
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery("select * from cpu");  
			while(rs.next()) {
				counter++;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Number of rows: " + counter);
		return true;
	}
}
