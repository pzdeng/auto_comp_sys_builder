package main.java.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.objects.CPU;

public class CPUDaoDummyImpl implements CPUDao{		
	//Dummy database
    Map<String, CPU> cpuList;

	public CPUDaoDummyImpl(){
		cpuList = new HashMap<String, CPU>();
	    CPU cpu1 = new CPU();
	    CPU cpu2 = new CPU();
	}


	@Override
	public CPU getCPU(int id) {
		return cpuList.get(id);
	}

	//Helper method for the above method
	private void updateTemp(CPU temp, CPU cpu) {
		//TODO fill in fields
	}

	@Override
	public Map<String, CPU> getAllCPU() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, CPU> getALLCPU(Map<String, String> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CPU getCPUByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void insertCPU(CPU cpu) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateCPU(CPU cpu) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteCPU(CPU cpu) {
		// TODO Auto-generated method stub
		
	}
	   
	}
