package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.objects.GPU;

public interface GPUDao {
   public ArrayList<GPU> getAllGPU() throws SQLException;
   public void insertGPU(GPU gpu) throws SQLException;
   public void updateFullGPU(GPU gpu) throws SQLException;
   public void updatePriceGPU(GPU gpu) throws SQLException;
   public GPU getGPUByName(String productName) throws SQLException;
   public void deleteGPU(GPU gpu) throws SQLException;
}
