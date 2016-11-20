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
import main.java.objects.Disk;

public class DISKDaoMySQLImpl implements DISKDao{
	
	private final String selectDisk = "SELECT * FROM disk WHERE productName = ?";
	private final String insertStmt = "INSERT INTO disk(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore, "
			+ "diskType, capacity, rotationSpeed, readSpeed, writeSpeed, interfaceType, formFactor)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE disk SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, "
			+ "diskType = ?, capacity = ?, rotationSpeed = ?, readSpeed = ?, writeSpeed = ?, "
			+ "interfaceType = ?, formFactor = ? WHERE diskid = ?";
	private final String updatePriceStmt = "UPDATE disk SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE diskid = ?";
	private final String deleStmt = "DELETE FROM disk WHERE diskid = ?";
	
	@Override
	public ArrayList<Disk> getAllDisk() throws SQLException{
		ArrayList<Disk> diskList = new ArrayList<Disk>();
		Connection dbConn = null;
		Statement stmt = null;
		Disk temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery("select * from disk");  
			while(rs.next()) {
				temp = new Disk();
				temp.id = rs.getInt("diskID");
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
				temp.diskType = rs.getString("diskType");
				temp.capacity = rs.getInt("capacity");
				temp.rotationSpeed = rs.getInt("rotationSpeed");
				temp.readSpeed = rs.getInt("readSpeed");
				temp.writeSpeed = rs.getInt("writeSpeed");
				temp.interfaceType = rs.getString("interfaceType");
				temp.formFactor = rs.getString("formFactor");
				diskList.add(temp);
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
		return diskList;
	}	
	

	@Override
	public void insertDisk(Disk disk) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, disk.type);
			stmt.setString(4, disk.productName);
			stmt.setString(5, disk.productID);
			stmt.setString(6, disk.modelName);
			stmt.setString(7, disk.make);
			stmt.setInt(8, disk.year);
			stmt.setInt(9, disk.powerRating);
			stmt.setString(10, disk.picURL);
			stmt.setString(11, disk.productURL);
			stmt.setFloat(12, disk.vendorPrice);
			stmt.setInt(13, disk.relativeRating);
			
			stmt.setFloat(14, disk.benchScore);
			stmt.setString(15, disk.diskType);
			stmt.setInt(16, disk.capacity);
			stmt.setInt(17, disk.rotationSpeed);
			stmt.setInt(18, disk.readSpeed);
			stmt.setInt(19, disk.writeSpeed);
			stmt.setString(20, disk.interfaceType);
			stmt.setString(21, disk.formFactor);
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
	public void updateFullDisk(Disk disk) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, disk.type);
			stmt.setString(3, disk.productName);
			stmt.setString(4, disk.productID);
			stmt.setString(5, disk.modelName);
			stmt.setString(6, disk.make);
			stmt.setInt(7, disk.year);
			stmt.setInt(8, disk.powerRating);
			stmt.setString(9, disk.picURL);
			stmt.setString(10, disk.productURL);
			stmt.setFloat(11, disk.vendorPrice);
			stmt.setInt(12, disk.relativeRating);
			
			stmt.setFloat(13, disk.benchScore);
			stmt.setString(14, disk.diskType);
			stmt.setInt(15, disk.capacity);
			stmt.setInt(16, disk.rotationSpeed);
			stmt.setInt(17, disk.readSpeed);
			stmt.setInt(18, disk.writeSpeed);
			stmt.setString(19, disk.interfaceType);
			stmt.setString(20, disk.formFactor);
			stmt.setInt(21, disk.id);
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
	public void updateVendorInfoDisk(Disk disk) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, disk.picURL);
			stmt.setString(3, disk.productURL);
			stmt.setFloat(4, disk.vendorPrice);
			stmt.setString(5, disk.productID);
			
			stmt.setInt(6, disk.id);
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
	public void deleteDisk(Disk disk) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, disk.id);
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
	public Disk getDiskByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		Disk temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectDisk);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new Disk();
				temp.id = rs.getInt("diskID");
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
				temp.diskType = rs.getString("diskType");
				temp.capacity = rs.getInt("capacity");
				temp.rotationSpeed = rs.getInt("rotationSpeed");
				temp.readSpeed = rs.getInt("readSpeed");
				temp.writeSpeed = rs.getInt("writeSpeed");
				temp.interfaceType = rs.getString("interfaceType");
				temp.formFactor = rs.getString("formFactor");
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
	public void insertDisk(List<Disk> diskList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			for(int i = 0; i < diskList.size(); i++){
				if(count < AppConstants.batchCommit){
					Disk disk = diskList.get(i);	
					
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setTimestamp(2, getCurrentTimeStamp());
					stmt.setString(3, disk.type);
					stmt.setString(4, disk.productName);
					stmt.setString(5, disk.productID);
					stmt.setString(6, disk.modelName);
					stmt.setString(7, disk.make);
					stmt.setInt(8, disk.year);
					stmt.setInt(9, disk.powerRating);
					stmt.setString(10, disk.picURL);
					stmt.setString(11, disk.productURL);
					stmt.setFloat(12, disk.vendorPrice);
					stmt.setInt(13, disk.relativeRating);
					
					stmt.setFloat(14, disk.benchScore);
					stmt.setString(15, disk.diskType);
					stmt.setInt(16, disk.capacity);
					stmt.setInt(17, disk.rotationSpeed);
					stmt.setInt(18, disk.readSpeed);
					stmt.setInt(19, disk.writeSpeed);
					stmt.setString(20, disk.interfaceType);
					stmt.setString(21, disk.formFactor);
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
	public void updateVendorInfoDisk(List<Disk> diskList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < diskList.size(); i++){
				if(count < AppConstants.batchCommit){
					Disk disk = diskList.get(i);	
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, disk.picURL);
					stmt.setString(3, disk.productURL);
					stmt.setFloat(4, disk.vendorPrice);
					stmt.setString(5, disk.productID);
					
					stmt.setInt(6, disk.id);
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
