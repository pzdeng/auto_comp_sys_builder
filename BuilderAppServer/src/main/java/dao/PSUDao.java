package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.PSU;

public interface PSUDao {
	public ArrayList<PSU> getAll() throws SQLException;
	public ArrayList<PSU> getAllValid() throws SQLException;
	public void insert(PSU psu) throws SQLException;
	public void insertList(List<PSU> psuList) throws SQLException;
	public void updateFull(PSU psu) throws SQLException;
	public void updateVendorInfo(PSU psu) throws SQLException;
	public void updateVendorInfoList(List<PSU> psuList) throws SQLException;
	public PSU getByName(String productName) throws SQLException;
	public void delete(PSU psu) throws SQLException;
	public int getValidCount() throws SQLException;
}
