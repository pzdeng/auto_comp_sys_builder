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
import main.java.objects.Motherboard;

public class MBDaoMySQLImpl implements MBDao{

	private final String generalSelect = "select * from motherboard";
	private final String selectMotherboard = "SELECT * FROM motherboard WHERE productName = ?";
	private final String insertStmt = "INSERT INTO motherboard(createTime, modifyTime, type, productName, productID, "
			+ "modelName, make, year, powerRating, picURL, productURL, "
			+ "vendorPrice, relativeRating, benchScore, formFactor, "
			+ "socketType, memType, memSlotNum, sataNum, pciExpressX16Num) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final String updateStmt = "UPDATE motherboard SET modifyTime = ?, type = ?, productName = ?, productID = ?, "
			+ "modelName = ?, make = ?, year = ?, powerRating = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, relativeRating = ?, benchScore = ?, formFactor = ?, "
			+ "socketType = ?, memType = ?, memSlotNum = ?, sataNum = ?, pciExpressX16Num = ? WHERE mbid = ?";
	private final String updatePriceStmt = "UPDATE motherboard SET modifyTime = ?, picURL = ?, productURL = ?, "
			+ "vendorPrice = ?, productID = ? WHERE mbid = ?";
	private final String deleStmt = "DELETE FROM motherboard WHERE mbid = ?";
	private final String validSelect = "select * from motherboard where productURL != '-' and vendorPrice > 0 order by vendorPrice desc";
	
	@Override
	public ArrayList<Motherboard> getAllMotherboard() throws SQLException{
		return selectCore(generalSelect);
	}	
	
	@Override
	public ArrayList<Motherboard> getAllValidMotherboard() throws SQLException {
		return selectCore(validSelect);
	}
	
	/**
	 * Central helper method to select cpu's
	 * @return
	 */
	private ArrayList<Motherboard> selectCore(String selectStmt) throws SQLException{
		ArrayList<Motherboard> mbList = new ArrayList<Motherboard>();
		Connection dbConn = null;
		Statement stmt = null;
		Motherboard temp;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.createStatement();  
			ResultSet rs = stmt.executeQuery(selectStmt);  
			while(rs.next()) {
				temp = new Motherboard();
				temp.id = rs.getInt("mbID");
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
				temp.formFactor = rs.getString("formFactor");
				temp.socketType = rs.getString("socketType");
				temp.memType = rs.getString("memType");
				temp.memSlotNum = rs.getInt("memSlotNum");
				temp.sataNum = rs.getInt("sataNum");
				temp.pciExpressX16Num = rs.getInt("pciExpressX16Num");
				mbList.add(temp);
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
		return mbList;
	}	

	@Override
	public void insertMotherboard(Motherboard mb) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setTimestamp(2, getCurrentTimeStamp());
			stmt.setString(3, mb.type);
			stmt.setString(4, mb.productName);
			stmt.setString(5, mb.productID);
			stmt.setString(6, mb.modelName);
			stmt.setString(7, mb.make);
			stmt.setInt(8, mb.year);
			stmt.setInt(9, mb.powerRating);
			stmt.setString(10, mb.picURL);
			stmt.setString(11, mb.productURL);
			stmt.setFloat(12, mb.vendorPrice);
			stmt.setInt(13, mb.relativeRating);
			
			stmt.setFloat(14, mb.benchScore);
			stmt.setString(15, mb.formFactor);
			stmt.setString(16, mb.socketType);
			stmt.setString(17, mb.memType);
			stmt.setInt(18, mb.memSlotNum);
			stmt.setInt(19, mb.sataNum);
			stmt.setInt(20, mb.pciExpressX16Num);
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
	public void updateFullMotherboard(Motherboard mb) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updateStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, mb.type);
			stmt.setString(3, mb.productName);
			stmt.setString(4, mb.productID);
			stmt.setString(5, mb.modelName);
			stmt.setString(6, mb.make);
			stmt.setInt(7, mb.year);
			stmt.setInt(8, mb.powerRating);
			stmt.setString(9, mb.picURL);
			stmt.setString(10, mb.productURL);
			stmt.setFloat(11, mb.vendorPrice);
			stmt.setInt(12, mb.relativeRating);
			
			stmt.setFloat(14, mb.benchScore);
			stmt.setString(15, mb.formFactor);
			stmt.setString(16, mb.socketType);
			stmt.setString(17, mb.memType);
			stmt.setInt(18, mb.memSlotNum);
			stmt.setInt(19, mb.sataNum);
			stmt.setInt(20, mb.pciExpressX16Num);
			stmt.setInt(21, mb.id);
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
	public void updateVendorInfoMotherboard(Motherboard mb) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			
			stmt.setTimestamp(1, getCurrentTimeStamp());
			stmt.setString(2, mb.picURL);
			stmt.setString(3, mb.productURL);
			stmt.setFloat(4, mb.vendorPrice);
			
			stmt.setInt(5, mb.id);
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
	public void deleteMotherboard(Motherboard mb) throws SQLException{
		Connection dbConn = null;
		PreparedStatement stmt = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(deleStmt);
			
			stmt.setInt(1, mb.id);
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
	public Motherboard getMotherboardByName(String productName) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		Motherboard temp = null;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(selectMotherboard);  
			stmt.setString(1, productName);
			ResultSet rs = stmt.executeQuery();  
			while(rs.next()) {
				temp = new Motherboard();
				temp.id = rs.getInt("mbID");
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
				temp.formFactor = rs.getString("formFactor");
				temp.socketType = rs.getString("socketType");
				temp.memType = rs.getString("memType");
				temp.memSlotNum = rs.getInt("memSlotNum");
				temp.sataNum = rs.getInt("sataNum");
				temp.pciExpressX16Num = rs.getInt("pciExpressX16Num");
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
	public void insertMotherboard(List<Motherboard> mbList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(insertStmt);
			
			for(int i = 0; i < mbList.size(); i++){
				//Commit 200 or n records at a time
				if(count < AppConstants.batchCommit){
					Motherboard mb = mbList.get(i);	
					
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setTimestamp(2, getCurrentTimeStamp());
					stmt.setString(3, mb.type);
					stmt.setString(4, mb.productName);
					stmt.setString(5, mb.productID);
					stmt.setString(6, mb.modelName);
					stmt.setString(7, mb.make);
					stmt.setInt(8, mb.year);
					stmt.setInt(9, mb.powerRating);
					stmt.setString(10, mb.picURL);
					stmt.setString(11, mb.productURL);
					stmt.setFloat(12, mb.vendorPrice);
					stmt.setInt(13, mb.relativeRating);
					
					stmt.setFloat(14, mb.benchScore);
					stmt.setString(15, mb.formFactor);
					stmt.setString(16, mb.socketType);
					stmt.setString(17, mb.memType);
					stmt.setInt(18, mb.memSlotNum);
					stmt.setInt(19, mb.sataNum);
					stmt.setInt(20, mb.pciExpressX16Num);
					stmt.addBatch();
					count++;
				}
				else{
					stmt.executeBatch();
					count = 0;
				}
			}
			//Process any leftovers
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
	public void updateVendorInfoMotherboard(List<Motherboard> mbList) throws SQLException {
		Connection dbConn = null;
		PreparedStatement stmt = null;
		int count = 0;
		
		try{
			dbConn = Database.getConnection();
			stmt = dbConn.prepareStatement(updatePriceStmt);
			for(int i = 0; i < mbList.size(); i++){
				if(count < AppConstants.batchCommit){
					Motherboard mb = mbList.get(i);	
					stmt.setTimestamp(1, getCurrentTimeStamp());
					stmt.setString(2, mb.picURL);
					stmt.setString(3, mb.productURL);
					stmt.setFloat(4, mb.vendorPrice);
					stmt.setString(5, mb.productID);
					
					stmt.setInt(6, mb.id);
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
