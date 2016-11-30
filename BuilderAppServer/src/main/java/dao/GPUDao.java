package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.GPU;

public interface GPUDao {
   public ArrayList<GPU> getAllGPU() throws SQLException;
   public ArrayList<GPU> getAllValidGPU() throws SQLException;
   public void insertGPU(GPU gpu) throws SQLException;
   public void insertGPU(List<GPU> gpuList) throws SQLException;
   public void updateFullGPU(GPU gpu) throws SQLException;
   public void updateVendorInfoGPU(GPU gpu) throws SQLException;
   public void updateVendorInfoGPU(List<GPU> gpuList) throws SQLException;
   public GPU getGPUByName(String productName) throws SQLException;
   public void deleteGPU(GPU gpu) throws SQLException;
   public int getValidGPUCount() throws SQLException;
}
