package main.java.dao;
import java.util.ArrayList;
import java.util.List;

import main.java.objects.CPU;

public class CPUDaoDummyImpl implements CPUDao{		
	//Dummy database
    List<CPU> cpuList;

	public CPUDaoDummyImpl(){
		cpuList = new ArrayList<CPU>();
	    CPU cpu1 = new CPU();
	    CPU cpu2 = new CPU();
	}

	@Override
	public List<CPU> getAllCPU() {
		return cpuList;
	}

	@Override
	public CPU getCPU(int id) {
		return cpuList.get(id);
	}

	@Override
	public void insertCPU(CPU cpu) {
		cpuList.add(cpu);
	}

	@Override
	public void updateCPU(CPU cpu) {
		CPU temp = cpuList.get(cpu.getId());
		updateTemp(temp, cpu);
	}

	//Helper method for the above method
	private void updateTemp(CPU temp, CPU cpu) {
		//TODO fill in fields
	}

	@Override
	public void deleteCPU(CPU cpu) {
		cpuList.remove(cpu.getId());
	}
	   
	}
