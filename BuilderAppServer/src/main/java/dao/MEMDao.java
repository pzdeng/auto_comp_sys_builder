package main.java.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.Memory;

public interface MEMDao {
	public ArrayList<Memory> getAll() throws SQLException;
	public ArrayList<Memory> getAllValid() throws SQLException;
	public void insert(Memory mem) throws SQLException;
	public void insertList(List<Memory> memList) throws SQLException;
	public void updateFull(Memory mem) throws SQLException;
	public void updateVendorInfo(Memory mem) throws SQLException;
	public void updateVendorInfoList(List<Memory> memList) throws SQLException;
	public Memory getByName(String productName) throws SQLException;
	public void delete(Memory mem) throws SQLException;
	public int getValidCount() throws SQLException;
}
