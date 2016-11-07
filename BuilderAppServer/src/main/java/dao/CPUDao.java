package main.java.dao;
import java.util.Map;

import main.java.objects.CPU;

public interface CPUDao {
   public Map<String, CPU> getAllCPU();
   public Map<String, CPU> getALLCPU(Map<String, String> params);
   public CPU getCPU(int id);
   public CPU getCPUByName(String name);
   public void insertCPU(CPU cpu);
   public void updateCPU(CPU cpu);
   public void deleteCPU(CPU cpu);
}
