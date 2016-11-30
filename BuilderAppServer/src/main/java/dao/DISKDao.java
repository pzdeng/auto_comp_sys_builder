package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Disk;

public interface DISKDao {
	public ArrayList<Disk> getAllDisk() throws SQLException;
	public ArrayList<Disk> getAllValidDisk() throws SQLException;
	public void insertDisk(Disk disk) throws SQLException;
	public void insertDisk(List<Disk> diskList) throws SQLException;
	public void updateFullDisk(Disk disk) throws SQLException;
	public void updateVendorInfoDisk(Disk disk) throws SQLException;
	public void updateVendorInfoDisk(List<Disk> diskList) throws SQLException;
	public Disk getDiskByName(String productName) throws SQLException;
	public void deleteDisk(Disk disk) throws SQLException;
	public int getValidDiskCount() throws SQLException;
}
