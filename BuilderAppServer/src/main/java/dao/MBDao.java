package main.java.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Motherboard;

public interface MBDao {
	public ArrayList<Motherboard> getAllMotherboard() throws SQLException;
	public ArrayList<Motherboard> getAllValidMotherboard() throws SQLException;
	public void insertMotherboard(Motherboard mb) throws SQLException;
	public void insertMotherboard(List<Motherboard> mbList) throws SQLException;
	public void updateFullMotherboard(Motherboard mb) throws SQLException;
	public void updateVendorInfoMotherboard(Motherboard mb) throws SQLException;
	public void updateVendorInfoMotherboard(List<Motherboard> mbList) throws SQLException;
	public Motherboard getMotherboardByName(String productName) throws SQLException;
	public void deleteMotherboard(Motherboard mb) throws SQLException;
	public int getValidMotherboardCount() throws SQLException;
}
