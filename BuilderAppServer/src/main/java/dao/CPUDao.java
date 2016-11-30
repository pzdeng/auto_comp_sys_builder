package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.CPU;

public interface CPUDao {
   public ArrayList<CPU> getAllCPU() throws SQLException;
   public ArrayList<CPU> getAllValidCPU() throws SQLException;
   public void insertCPU(CPU cpu) throws SQLException;
   public void insertCPU(List<CPU> cpuList) throws SQLException;
   public void updateFullCPU(CPU cpu) throws SQLException;
   public void updateVendorInfoCPU(CPU cpu) throws SQLException;
   public void updateVendorInfoCPU(List<CPU> cpuList) throws SQLException;
   public CPU getCPUByName(String productName) throws SQLException;
   public void deleteCPU(CPU cpu) throws SQLException;
   public int getValidCPUCount() throws SQLException;
}
