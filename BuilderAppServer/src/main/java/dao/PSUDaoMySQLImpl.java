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
import main.java.objects.PSU;

public class PSUDaoMySQLImpl implements PSUDao{

	private final String generalSelect = "select * from psu";
	private final String selectPSU = "SELECT * FROM psu WHERE productName = ?";
	private final String insertStmt = "INSERT INTO psu(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore, "
			+ "powerWattage, efficiency) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE psu SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, "
			+ "powerWattage = ?, efficiency = ? WHERE psuid = ?";
	private final String updatePriceStmt = "UPDATE psu SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE psuid = ?";
	private final String deleStmt = "DELETE FROM psu WHERE psuid = ?";
	private final String validSelect = "select * from psu where productURL != '-' and vendorPrice > 10 order by vendorPrice desc";
	private final String validSelectCount = "select count(*) from psu where productURL != '-' and vendorPrice > 10 order by vendorPrice desc";
	
	@Override
	public ArrayList<PSU> getAllPSU() throws SQLException{
		return selectCore(generalSelect);
	}
	
	@Override
	public ArrayList<PSU> getAllValidPSU() throws SQLException {
		return selectCore(validSelect);
	}
	
	/**
	 * Central helper method to select cpu's
	 * @return
	 */
	private ArrayList<PSU> selectCore(String selectStmt) throws SQLException{
		ArrayList<PSU> psuList = new ArrayList<PSU>();
		Connection dbConn = null;
		Statement stmt = null;
		PSU temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(selectStmt);  
			while(rs.next()) {
				temp = new PSU();
				temp.id = rs.getInt("psuID");
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
				temp.powerWattage = rs.getInt("powerWattage");
				temp.efficiency = rs.getString("efficiency");
				temp.computePricePerPoint();
				psuList.add(temp);
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
		return psuList;
	}	
	

	@Override
	public void insertPSU(PSU psu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, psu.type);
			stmt.setString(4, psu.productName);
			stmt.setString(5, psu.productID);
			stmt.setString(6, psu.modelName);
			stmt.setString(7, psu.make);
			stmt.setInt(8, psu.year);
			stmt.setInt(9, psu.powerRating);
			stmt.setString(10, psu.picURL);
			stmt.setString(11, psu.productURL);
			stmt.setFloat(12, psu.vendorPrice);
			stmt.setInt(13, psu.relativeRating);
			
			stmt.setFloat(14, psu.benchScore);
			stmt.setInt(15, psu.powerWattage);
			stmt.setString(16, psu.efficiency);
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
	public void updateFullPSU(PSU psu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, psu.type);
			stmt.setString(3, psu.productName);
			stmt.setString(4, psu.productID);
			stmt.setString(5, psu.modelName);
			stmt.setString(6, psu.make);
			stmt.setInt(7, psu.year);
			stmt.setInt(8, psu.powerRating);
			stmt.setString(9, psu.picURL);
			stmt.setString(10, psu.productURL);
			stmt.setFloat(11, psu.vendorPrice);
			stmt.setInt(12, psu.relativeRating);
			
			stmt.setFloat(13, psu.benchScore);
			stmt.setInt(14, psu.powerWattage);
			stmt.setString(15, psu.efficiency);
			stmt.setInt(16, psu.id);
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
	public void updateVendorInfoPSU(PSU psu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, psu.picURL);
			stmt.setString(3, psu.productURL);
			stmt.setFloat(4, psu.vendorPrice);
			stmt.setString(5, psu.productID);
			
			stmt.setInt(6, psu.id);
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
	public void deletePSU(PSU psu) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, psu.id);
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
	public PSU getPSUByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		PSU temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectPSU);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new PSU();
				temp.id = rs.getInt("psuID");
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
				temp.powerWattage = rs.getInt("powerWattage");
				temp.efficiency = rs.getString("efficiency");
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
	public void insertPSU(List<PSU> psuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			for(int i = 0; i < psuList.size(); i++){
				if(count < AppConstants.batchCommit){
					PSU psu = psuList.get(i);	
					
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setTimestamp(2, getCurrentTimeStamp());
					stmt.setString(3, psu.type);
					stmt.setString(4, psu.productName);
					stmt.setString(5, psu.productID);
					stmt.setString(6, psu.modelName);
					stmt.setString(7, psu.make);
					stmt.setInt(8, psu.year);
					stmt.setInt(9, psu.powerRating);
					stmt.setString(10, psu.picURL);
					stmt.setString(11, psu.productURL);
					stmt.setFloat(12, psu.vendorPrice);
					stmt.setInt(13, psu.relativeRating);
					
					stmt.setFloat(14, psu.benchScore);
					stmt.setInt(15, psu.powerWattage);
					stmt.setString(16, psu.efficiency);
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
	public void updateVendorInfoPSU(List<PSU> psuList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < psuList.size(); i++){
				if(count < AppConstants.batchCommit){
					PSU psu = psuList.get(i);	
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, psu.picURL);
					stmt.setString(3, psu.productURL);
					stmt.setFloat(4, psu.vendorPrice);
					stmt.setString(5, psu.productID);
					
					stmt.setInt(6, psu.id);
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
	public int getValidPSUCount() throws SQLException {
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
