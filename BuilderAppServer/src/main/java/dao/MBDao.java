package main.java.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Motherboard;

public interface MBDao {
	public ArrayList<Motherboard> getAll() throws SQLException;
	public ArrayList<Motherboard> getAllValid() throws SQLException;
	public void insert(Motherboard mb) throws SQLException;
	public void insertList(List<Motherboard> mbList) throws SQLException;
	public void updateFull(Motherboard mb) throws SQLException;
	public void updateVendorInfo(Motherboard mb) throws SQLException;
	public void updateVendorInfoList(List<Motherboard> mbList) throws SQLException;
	public Motherboard getByName(String productName) throws SQLException;
	public void delete(Motherboard mb) throws SQLException;
	public int getValidCount() throws SQLException;
}
