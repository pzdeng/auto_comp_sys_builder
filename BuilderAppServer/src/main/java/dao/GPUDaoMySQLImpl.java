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
import main.java.objects.GPU;

public class GPUDaoMySQLImpl implements GPUDao{

	private final String generalSelect = "select * from gpu";
	private final String selectGPU = "SELECT * FROM gpu WHERE productName = ?";
	private final String insertStmt = "INSERT INTO gpu(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore, branding, "
			+ "coreSpeed, memClockSpeed, coreCount, interfaceType, memSize) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE gpu SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, branding = ?, "
			+ "coreSpeed = ?, memClockSpeed = ?, coreCount = ?, interfaceType = ?, memSize = ? WHERE gpuid = ?";
	private final String updatePriceStmt = "UPDATE gpu SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE gpuid = ?";
	private final String deleStmt = "DELETE FROM gpu WHERE gpuid = ?";
	private final String validSelect = "select * from gpu where productURL != '-' and vendorPrice > 10 order by vendorPrice desc";
	
	@Override
	public ArrayList<GPU> getAllGPU() throws SQLException{
		return selectCore(generalSelect);
	}	
	
	@Override
	public ArrayList<GPU> getAllValidGPU() throws SQLException {
		return selectCore(validSelect);
	}
	
	/**
	 * Central helper method to select cpu's
	 * @return
	 */
	private ArrayList<GPU> selectCore(String selectStmt) throws SQLException{
		ArrayList<GPU> gpuList = new ArrayList<GPU>();
		Connection dbConn = null;
		Statement stmt = null;
		GPU temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(selectStmt);  
			while(rs.next()) {
				temp = new GPU();
				temp.id = rs.getInt("gpuID");
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
				temp.branding = rs.getString("branding");
				temp.coreSpeed = rs.getFloat("coreSpeed");
				temp.memClockSpeed = rs.getFloat("memClockSpeed");
				temp.coreCount = rs.getInt("coreCount");
				temp.interfaceType = rs.getString("interfaceType");
				temp.memSize = rs.getInt("memSize");
				temp.computePricePerPoint();
				gpuList.add(temp);
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
		return gpuList;
	}	

	@Override
	public void insertGPU(GPU gpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, gpu.type);
			stmt.setString(4, gpu.productName);
			stmt.setString(5, gpu.productID);
			stmt.setString(6, gpu.modelName);
			stmt.setString(7, gpu.make);
			stmt.setInt(8, gpu.year);
			stmt.setInt(9, gpu.powerRating);
			stmt.setString(10, gpu.picURL);
			stmt.setString(11, gpu.productURL);
			stmt.setFloat(12, gpu.vendorPrice);
			stmt.setInt(13, gpu.relativeRating);
			
			stmt.setFloat(14, gpu.benchScore);
			stmt.setString(15, gpu.branding);
			stmt.setFloat(16, gpu.coreSpeed);
			stmt.setFloat(17, gpu.memClockSpeed);
			stmt.setInt(18, gpu.coreCount);
			stmt.setString(19, gpu.interfaceType);
			stmt.setInt(20, gpu.memSize);
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
	public void updateFullGPU(GPU gpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, gpu.type);
			stmt.setString(3, gpu.productName);
			stmt.setString(4, gpu.productID);
			stmt.setString(5, gpu.modelName);
			stmt.setString(6, gpu.make);
			stmt.setInt(7, gpu.year);
			stmt.setInt(8, gpu.powerRating);
			stmt.setString(9, gpu.picURL);
			stmt.setString(10, gpu.productURL);
			stmt.setFloat(11, gpu.vendorPrice);
			stmt.setInt(12, gpu.relativeRating);
			
			stmt.setFloat(13, gpu.benchScore);
			stmt.setString(14, gpu.branding);
			stmt.setFloat(15, gpu.coreSpeed);
			stmt.setFloat(16, gpu.memClockSpeed);
			stmt.setInt(17, gpu.coreCount);
			stmt.setString(18, gpu.interfaceType);
			stmt.setInt(19, gpu.memSize);
			stmt.setInt(20, gpu.id);
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
	public void updateVendorInfoGPU(GPU gpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, gpu.picURL);
			stmt.setString(3, gpu.productURL);
			stmt.setFloat(4, gpu.vendorPrice);
			stmt.setString(5, gpu.productID);
			
			stmt.setInt(6, gpu.id);
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
	public void deleteGPU(GPU gpu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, gpu.id);
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
	public GPU getGPUByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		GPU temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectGPU);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new GPU();
				temp.id = rs.getInt("gpuID");
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
				temp.branding = rs.getString("branding");
				temp.coreSpeed = rs.getFloat("coreSpeed");
				temp.memClockSpeed = rs.getFloat("memClockSpeed");
				temp.coreCount = rs.getInt("coreCount");
				temp.interfaceType = rs.getString("interfaceType");
				temp.memSize = rs.getInt("memSize");
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
	public void insertGPU(List<GPU> gpuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			for(int i = 0; i < gpuList.size(); i++){
				GPU gpu = gpuList.get(i);
			
				stmt.setTimestamp(1, getCurrentTimeStamp());
				stmt.setTimestamp(2, getCurrentTimeStamp());
				stmt.setString(3, gpu.type);
				stmt.setString(4, gpu.productName);
				stmt.setString(5, gpu.productID);
				stmt.setString(6, gpu.modelName);
				stmt.setString(7, gpu.make);
				stmt.setInt(8, gpu.year);
				stmt.setInt(9, gpu.powerRating);
				stmt.setString(10, gpu.picURL);
				stmt.setString(11, gpu.productURL);
				stmt.setFloat(12, gpu.vendorPrice);
				stmt.setInt(13, gpu.relativeRating);
				
				stmt.setFloat(14, gpu.benchScore);
				stmt.setString(15, gpu.branding);
				stmt.setFloat(16, gpu.coreSpeed);
				stmt.setFloat(17, gpu.memClockSpeed);
				stmt.setInt(18, gpu.coreCount);
				stmt.setString(19, gpu.interfaceType);
				stmt.setInt(20, gpu.memSize);
				stmt.addBatch();
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
	public void updateVendorInfoGPU(List<GPU> gpuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < gpuList.size(); i++){
				if(count < AppConstants.batchCommit){
					GPU gpu = gpuList.get(i);
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, gpu.picURL);
					stmt.setString(3, gpu.productURL);
					stmt.setFloat(4, gpu.vendorPrice);
					stmt.setString(5, gpu.productID);
					
					stmt.setInt(6, gpu.id);
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
