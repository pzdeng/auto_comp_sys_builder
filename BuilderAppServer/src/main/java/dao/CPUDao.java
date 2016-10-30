package main.java.dao;

import java.util.List;

import main.java.objects.CPU;

public interface CPUDao {
   public List<CPU> getAllCPU();
   public CPU getCPU(int id);
   public void insertCPU(CPU cpu);
   public void updateCPU(CPU cpu);
   public void deleteCPU(CPU cpu);
}
