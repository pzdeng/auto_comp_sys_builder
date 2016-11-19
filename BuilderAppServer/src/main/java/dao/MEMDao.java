package main.java.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Memory;

public interface MEMDao {
	public ArrayList<Memory> getAllMemory() throws SQLException;
	public void insertMemory(Memory mem) throws SQLException;
	public void insertMemory(List<Memory> memList) throws SQLException;
	public void updateFullMemory(Memory mem) throws SQLException;
	public void updateVendorInfoMemory(Memory mem) throws SQLException;
	public void updateVendorInfoMemory(List<Memory> memList) throws SQLException;
	public Memory getMemoryByName(String productName) throws SQLException;
	public void deleteMemory(Memory mem) throws SQLException;
}
