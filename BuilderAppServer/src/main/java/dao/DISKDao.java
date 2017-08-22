package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Disk;

public interface DISKDao {
	public ArrayList<Disk> getAll() throws SQLException;
	public ArrayList<Disk> getAllValid() throws SQLException;
	public void insert(Disk disk) throws SQLException;
	public void insertList(List<Disk> diskList) throws SQLException;
	public void updateFull(Disk disk) throws SQLException;
	public void updateVendorInfo(Disk disk) throws SQLException;
	public void updateVendorInfoList(List<Disk> diskList) throws SQLException;
	public Disk getByName(String productName) throws SQLException;
	public void delete(Disk disk) throws SQLException;
	public int getValidCount() throws SQLException;
}
