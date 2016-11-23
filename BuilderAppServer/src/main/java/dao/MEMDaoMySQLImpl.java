package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import main.java.database.Database;
import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.Memory;

public class MEMDaoMySQLImpl implements MEMDao{

	private final String generalSelect = "select * from memory";
	private final String selectmem = "SELECT * FROM memory WHERE productName = ?";
	private final String insertStmt = "INSERT INTO memory(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore,"
			+ "totalCapacity, numModules, memType, memSpeed) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE memory SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, "
			+ "totalCapacity = ?, numModules = ?, memType = ?, memSpeed = ?, WHERE memid = ?";
	private final String updatePriceStmt = "UPDATE memory SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE memid = ?";
	private final String deleStmt = "DELETE FROM memory WHERE memid = ?";
	private final String validSelect = "select * from memory where productURL != '-' and vendorPrice > 0 order by vendorPrice asc";
	
	@Override
	public ArrayList<Memory> getAllMemory() throws SQLException{
		return selectCore(generalSelect);
	}
	
	@Override
	public ArrayList<Memory> getAllValidMemory() throws SQLException {
		return selectCore(validSelect);
	}
	
	/**
	 * Central helper method to select cpu's
	 * @return
	 */
	private ArrayList<Memory> selectCore(String selectStmt) throws SQLException{
		ArrayList<Memory> memList = new ArrayList<Memory>();
		Connection dbConn = null;
		Statement stmt = null;
		Memory temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(selectStmt);  
			while(rs.next()) {
				temp = new Memory();
				temp.id = rs.getInt("memID");
				temp.createTime = rs.getTimestamp("createTime");
				temp.modifyTime = rs.getTimestamp("modifyTime");
				temp.type = rs.getString("type");
				temp.productName = rs.getString("productName");
				temp.productID = rs.getString("productID");
				temp.modelName = rs.getString("modelName");
				temp.make = rs.getString("make");
				temp.year = rs.getInt("year");
				temp.powerRating = rs.getInt("powerRating");
				temp.picURL = rs.getString("picURL");
				temp.productURL = rs.getString("productURL");
				temp.vendorPrice = rs.getFloat("vendorPrice");
				temp.relativeRating = rs.getInt("relativeRating");
				
				temp.benchScore = rs.getFloat("benchScore");
				temp.totalCapacity = rs.getInt("totalCapacity");
				temp.numModules = rs.getInt("numModules");
				temp.memType = rs.getString("memType");
				temp.memSpeed = rs.getFloat("memSpeed");
				memList.add(temp);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
		return memList;
	}	
	

	@Override
	public void insertMemory(Memory mem) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, mem.type);
			stmt.setString(4, mem.productName);
			stmt.setString(5, mem.productID);
			stmt.setString(6, mem.modelName);
			stmt.setString(7, mem.make);
			stmt.setInt(8, mem.year);
			stmt.setInt(9, mem.powerRating);
			stmt.setString(10, mem.picURL);
			stmt.setString(11, mem.productURL);
			stmt.setFloat(12, mem.vendorPrice);
			stmt.setInt(13, mem.relativeRating);
			
			stmt.setFloat(14, mem.benchScore);
			stmt.setInt(15, mem.totalCapacity);
			stmt.setInt(16, mem.numModules);
			stmt.setString(17, mem.memType);
			stmt.setFloat(18, mem.memSpeed);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}


	@Override
	public void updateFullMemory(Memory mem) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, mem.type);
			stmt.setString(3, mem.productName);
			stmt.setString(4, mem.productID);
			stmt.setString(5, mem.modelName);
			stmt.setString(6, mem.make);
			stmt.setInt(7, mem.year);
			stmt.setInt(8, mem.powerRating);
			stmt.setString(9, mem.picURL);
			stmt.setString(10, mem.productURL);
			stmt.setFloat(11, mem.vendorPrice);
			stmt.setInt(12, mem.relativeRating);
			
			stmt.setFloat(13, mem.benchScore);
			stmt.setInt(14, mem.totalCapacity);
			stmt.setInt(15, mem.numModules);
			stmt.setString(16, mem.memType);
			stmt.setFloat(17, mem.memSpeed);
			stmt.setInt(18, mem.id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}
	
	@Override
	public void updateVendorInfoMemory(Memory mem) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, mem.picURL);
			stmt.setString(3, mem.productURL);
			stmt.setFloat(4, mem.vendorPrice);
			stmt.setString(5, mem.productID);
			
			stmt.setInt(6, mem.id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}

	@Override
	public void deleteMemory(Memory mem) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, mem.id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}
	
	@Override
	public Memory getMemoryByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		Memory temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectmem);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new Memory();
				temp.id = rs.getInt("memID");
				temp.createTime = rs.getTimestamp("createTime");
				temp.modifyTime = rs.getTimestamp("modifyTime");
				temp.type = rs.getString("type");
				temp.productName = rs.getString("productName");
				temp.productID = rs.getString("productID");
				temp.modelName = rs.getString("modelName");
				temp.make = rs.getString("make");
				temp.year = rs.getInt("year");
				temp.powerRating = rs.getInt("powerRating");
				temp.picURL = rs.getString("picURL");
				temp.productURL = rs.getString("productURL");
				temp.vendorPrice = rs.getFloat("vendorPrice");
				temp.relativeRating = rs.getInt("relativeRating");
				
				temp.benchScore = rs.getFloat("benchScore");
				temp.totalCapacity = rs.getInt("totalCapacity");
				temp.numModules = rs.getInt("numModules");
				temp.memType = rs.getString("memType");
				temp.memSpeed = rs.getFloat("memSpeed");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
		return temp;
	}
	
	private static Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis());

	}


	@Override
	public void insertMemory(List<Memory> memList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			for(int i = 0; i < memList.size(); i++){
				if(count < AppConstants.batchCommit){
					Memory mem = memList.get(i);	
					
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setTimestamp(2, getCurrentTimeStamp());
					stmt.setString(3, mem.type);
					stmt.setString(4, mem.productName);
					stmt.setString(5, mem.productID);
					stmt.setString(6, mem.modelName);
					stmt.setString(7, mem.make);
					stmt.setInt(8, mem.year);
					stmt.setInt(9, mem.powerRating);
					stmt.setString(10, mem.picURL);
					stmt.setString(11, mem.productURL);
					stmt.setFloat(12, mem.vendorPrice);
					stmt.setInt(13, mem.relativeRating);
					
					stmt.setFloat(14, mem.benchScore);
					stmt.setInt(15, mem.totalCapacity);
					stmt.setInt(16, mem.numModules);
					stmt.setString(17, mem.memType);
					stmt.setFloat(18, mem.memSpeed);
					stmt.addBatch();
					count++;
				}
				else{
					stmt.executeBatch();
					count = 0;
				}
			}
			stmt.executeBatch();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}


	@Override
	public void updateVendorInfoMemory(List<Memory> memList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < memList.size(); i++){
				if(count < AppConstants.batchCommit){
					Memory mem = memList.get(i);	
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, mem.picURL);
					stmt.setString(3, mem.productURL);
					stmt.setFloat(4, mem.vendorPrice);
					stmt.setString(5, mem.productID);
					
					stmt.setInt(6, mem.id);
					stmt.addBatch();
					count++;
				}
				else{
					stmt.executeBatch();
					count = 0;
				}
			}
			stmt.executeBatch();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally{
			if(dbConn != null){
				dbConn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}
}
