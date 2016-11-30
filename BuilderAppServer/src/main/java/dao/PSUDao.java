package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.PSU;

public interface PSUDao {
	public ArrayList<PSU> getAllPSU() throws SQLException;
	public ArrayList<PSU> getAllValidPSU() throws SQLException;
	public void insertPSU(PSU psu) throws SQLException;
	public void insertPSU(List<PSU> psuList) throws SQLException;
	public void updateFullPSU(PSU psu) throws SQLException;
	public void updateVendorInfoPSU(PSU psu) throws SQLException;
	public void updateVendorInfoPSU(List<PSU> psuList) throws SQLException;
	public PSU getPSUByName(String productName) throws SQLException;
	public void deletePSU(PSU psu) throws SQLException;
	public int getValidPSUCount() throws SQLException;
}
