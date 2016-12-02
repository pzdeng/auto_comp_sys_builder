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

public class CPUDaoMySQLImpl implements CPUDao{

	private final String generalSelect = "select * from cpu";
	private final String selectCPU = "SELECT * FROM cpu WHERE productName = ?";
	private final String insertStmt = "INSERT INTO cpu(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore, coreSpeed, "
			+ "coreTurboSpeed, coreCount, socketType, l1Size, l2Size, l3Size) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE cpu SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, coreSpeed = ?, "
			+ "coreTurboSpeed = ?, coreCount = ?, socketType = ?, l1Size = ?, l2Size = ?, l3Size = ? WHERE cpuid = ?";
	private final String updatePriceStmt = "UPDATE cpu SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE cpuid = ?";
	private final String deleStmt = "DELETE FROM cpu WHERE cpuid = ?";
	private final String validSelect = "select * from cpu where productURL != '-' and benchscore > 0 and socketType is not null and vendorPrice > 10 order by vendorPrice desc";
	private final String validSelectCount = "select count(*) from cpu where productURL != '-' and benchscore > 0 and socketType is not null and vendorPrice > 10 order by vendorPrice desc";
	
	@Override
	public ArrayList<CPU> getAllCPU() throws SQLException{
		return selectCore(generalSelect);
	}	
	
	@Override
	public ArrayList<CPU> getAllValidCPU() throws SQLException {
		return selectCore(validSelect);
	}
	
	/**
	 * Central helper method to select cpu's
	 * @return
	 */
	private ArrayList<CPU> selectCore(String selectStmt) throws SQLException{
		ArrayList<CPU> cpuList = new ArrayList<CPU>();
		Connection dbConn = null;
		Statement stmt = null;
		CPU temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(selectStmt);  
			while(rs.next()) {
				temp = new CPU();
				temp.id = rs.getInt("cpuID");
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
				temp.coreSpeed = rs.getFloat("coreSpeed");
				temp.coreTurboSpeed = rs.getFloat("coreTurboSpeed");
				temp.coreCount = rs.getInt("coreCount");
				temp.socketType = rs.getString("socketType");
				temp.l1Size = rs.getInt("l1Size");
				temp.l2Size = rs.getInt("l2Size");
				temp.l3Size = rs.getInt("l3Size");
				temp.computePricePerPoint();
				cpuList.add(temp);
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
		return cpuList;
	}
	

	@Override
	public void insertCPU(CPU cpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, cpu.type);
			stmt.setString(4, cpu.productName);
			stmt.setString(5, cpu.productID);
			stmt.setString(6, cpu.modelName);
			stmt.setString(7, cpu.make);
			stmt.setInt(8, cpu.year);
			stmt.setInt(9, cpu.powerRating);
			stmt.setString(10, cpu.picURL);
			stmt.setString(11, cpu.productURL);
			stmt.setFloat(12, cpu.vendorPrice);
			stmt.setInt(13, cpu.relativeRating);
			
			stmt.setFloat(14, cpu.benchScore);
			stmt.setFloat(15, cpu.coreSpeed);
			stmt.setFloat(16, cpu.coreTurboSpeed);
			stmt.setInt(17, cpu.coreCount);
			stmt.setString(18, cpu.socketType);
			stmt.setInt(19, cpu.l1Size);
			stmt.setInt(20, cpu.l2Size);
			stmt.setInt(21, cpu.l3Size);
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
	public void updateFullCPU(CPU cpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, cpu.type);
			stmt.setString(3, cpu.productName);
			stmt.setString(4, cpu.productID);
			stmt.setString(5, cpu.modelName);
			stmt.setString(6, cpu.make);
			stmt.setInt(7, cpu.year);
			stmt.setInt(8, cpu.powerRating);
			stmt.setString(9, cpu.picURL);
			stmt.setString(10, cpu.productURL);
			stmt.setFloat(11, cpu.vendorPrice);
			stmt.setInt(12, cpu.relativeRating);
			
			stmt.setFloat(13, cpu.benchScore);
			stmt.setFloat(14, cpu.coreSpeed);
			stmt.setFloat(15, cpu.coreTurboSpeed);
			stmt.setInt(16, cpu.coreCount);
			stmt.setString(17, cpu.socketType);
			stmt.setInt(18, cpu.l1Size);
			stmt.setInt(19, cpu.l2Size);
			stmt.setInt(20, cpu.l3Size);
			stmt.setInt(21, cpu.id);
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
	public void updateVendorInfoCPU(CPU cpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, cpu.picURL);
			stmt.setString(3, cpu.productURL);
			stmt.setFloat(4, cpu.vendorPrice);
			stmt.setString(5, cpu.productID);
			
			stmt.setInt(6, cpu.id);
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
	public void deleteCPU(CPU cpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, cpu.id);
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
	public CPU getCPUByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		CPU temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectCPU);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new CPU();
				temp.id = rs.getInt("cpuID");
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
				temp.coreSpeed = rs.getFloat("coreSpeed");
				temp.coreTurboSpeed = rs.getFloat("coreTurboSpeed");
				temp.coreCount = rs.getInt("coreCount");
				temp.socketType = rs.getString("socketType");
				temp.l1Size = rs.getInt("l1Size");
				temp.l2Size = rs.getInt("l2Size");
				temp.l3Size = rs.getInt("l3Size");
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
	public void insertCPU(List<CPU> cpuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			for(int i = 0; i < cpuList.size(); i++){
				if(count < AppConstants.batchCommit){
					CPU cpu = cpuList.get(i);	
					
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setTimestamp(2, getCurrentTimeStamp());
					stmt.setString(3, cpu.type);
					stmt.setString(4, cpu.productName);
					stmt.setString(5, cpu.productID);
					stmt.setString(6, cpu.modelName);
					stmt.setString(7, cpu.make);
					stmt.setInt(8, cpu.year);
					stmt.setInt(9, cpu.powerRating);
					stmt.setString(10, cpu.picURL);
					stmt.setString(11, cpu.productURL);
					stmt.setFloat(12, cpu.vendorPrice);
					stmt.setInt(13, cpu.relativeRating);
					
					stmt.setFloat(14, cpu.benchScore);
					stmt.setFloat(15, cpu.coreSpeed);
					stmt.setFloat(16, cpu.coreTurboSpeed);
					stmt.setInt(17, cpu.coreCount);
					stmt.setString(18, cpu.socketType);
					stmt.setInt(19, cpu.l1Size);
					stmt.setInt(20, cpu.l2Size);
					stmt.setInt(21, cpu.l3Size);
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
	public void updateVendorInfoCPU(List<CPU> cpuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < cpuList.size(); i++){
				if(count < AppConstants.batchCommit){
					CPU cpu = cpuList.get(i);	
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, cpu.picURL);
					stmt.setString(3, cpu.productURL);
					stmt.setFloat(4, cpu.vendorPrice);
					stmt.setString(5, cpu.productID);
					
					stmt.setInt(6, cpu.id);
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
	public int getValidCPUCount() throws SQLException {
		int count = 0;
		Connection dbConn = null;
		Statement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(validSelectCount);  
			if(rs.next()) {
				count = rs.getInt(1);
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
		return count;
	}
}
