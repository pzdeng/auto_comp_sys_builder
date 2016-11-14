package database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import main.java.dao.CPUDao;
import main.java.dao.CPUDaoMySQLImpl;
import main.java.database.Database;
import main.java.objects.CPU;

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
	
	@Test
	public void testCPUDAO(){
		CPU aCPU = dummyCPU();
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		try {
			CPU temp;
			cpuDao.insertCPU(aCPU);
			temp = cpuDao.getCPUByName(aCPU.productName);
			assertEquals(aCPU.productName, temp.productName);
			System.out.println(temp.id);
			cpuDao.deleteCPU(temp);
			temp = cpuDao.getCPUByName(aCPU.productName);
			assertEquals(temp, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private CPU dummyCPU(){
		CPU temp = new CPU();
		temp.productName = "DUMMYCPU";
		return temp;
	}
}
