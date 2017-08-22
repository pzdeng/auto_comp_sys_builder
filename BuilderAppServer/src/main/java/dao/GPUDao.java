package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.GPU;

public interface GPUDao {
   public ArrayList<GPU> getAll() throws SQLException;
   public ArrayList<GPU> getAllValid() throws SQLException;
   public void insert(GPU gpu) throws SQLException;
   public void insertList(List<GPU> gpuList) throws SQLException;
   public void updateFull(GPU gpu) throws SQLException;
   public void updateVendorInfo(GPU gpu) throws SQLException;
   public void updateVendorInfoList(List<GPU> gpuList) throws SQLException;
   public GPU getByName(String productName) throws SQLException;
   public void delete(GPU gpu) throws SQLException;
   public int getValidCount() throws SQLException;
}
