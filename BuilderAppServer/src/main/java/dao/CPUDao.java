package main.java.dao;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.objects.CPU;

public interface CPUDao {
   public ArrayList<CPU> getAllCPU() throws SQLException;
   public void insertCPU(CPU cpu) throws SQLException;
   public void updateFullCPU(CPU cpu) throws SQLException;
   public void updatePriceCPU(CPU cpu) throws SQLException;
   public CPU getCPUByName(String productName) throws SQLException;
   public void deleteCPU(CPU cpu) throws SQLException;
}
