package database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import main.java.dao.CPUDao;
import main.java.dao.CPUDaoMySQLImpl;
import main.java.dao.GPUDao;
import main.java.dao.GPUDaoMySQLImpl;
import main.java.database.Database;
import main.java.objects.CPU;
import main.java.objects.GPU;

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
			CPU temp, temp2;
			//Check insert
			cpuDao.insert(aCPU);
			temp = cpuDao.getByName(aCPU.productName);
			assertEquals(aCPU.productName, temp.productName);			
			//Check update
			temp.picURL = temp.picURL + "2";
			cpuDao.updateFull(temp);
			temp2 = cpuDao.getByName(temp.productName);
			assertEquals(temp.picURL, temp2.picURL);
			temp2 = null;
			//Check delete
			cpuDao.delete(temp);
			temp = cpuDao.getByName(aCPU.productName);
			assertEquals(temp, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private CPU dummyCPU(){
		CPU temp = new CPU();
		temp.productName = "DUMMYCPU";
		temp.picURL = "URL";
		return temp;
	}
	
	@Test
	public void testGPUDAO(){
		GPU aGPU = dummyGPU();
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		try {
			GPU temp, temp2;
			//Check insert
			gpuDao.insert(aGPU);
			temp = gpuDao.getByName(aGPU.productName);
			assertEquals(aGPU.productName, temp.productName);			
			//Check update
			temp.picURL = temp.picURL + "2";
			gpuDao.updateFull(temp);
			temp2 = gpuDao.getByName(temp.productName);
			assertEquals(temp.picURL, temp2.picURL);
			temp2 = null;
			//Check delete
			gpuDao.delete(temp);
			temp = gpuDao.getByName(aGPU.productName);
			assertEquals(temp, null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GPU dummyGPU(){
		GPU temp = new GPU();
		temp.productName = "DUMMYGPU";
		temp.picURL = "URL";
		return temp;
	}
}
