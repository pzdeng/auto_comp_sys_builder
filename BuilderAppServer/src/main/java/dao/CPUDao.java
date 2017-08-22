package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.CPU;

public interface CPUDao {
   public ArrayList<CPU> getAll() throws SQLException;
   public ArrayList<CPU> getAllValid() throws SQLException;
   public void insert(CPU cpu) throws SQLException;
   public void insertList(List<CPU> cpuList) throws SQLException;
   public void updateFull(CPU cpu) throws SQLException;
   public void updateVendorInfo(CPU cpu) throws SQLException;
   public void updateVendorInfoList(List<CPU> cpuList) throws SQLException;
   public CPU getByName(String productName) throws SQLException;
   public void delete(CPU cpu) throws SQLException;
   public int getValidCount() throws SQLException;
}
