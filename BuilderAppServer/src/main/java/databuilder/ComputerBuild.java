package main.java.databuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ClientPayload;
import main.java.objects.ComputerType;
import main.java.objects.Disk;
import main.java.objects.GPU;
import main.java.objects.Memory;
import main.java.objects.Motherboard;
import main.java.objects.PSU;
import main.java.objects.comparator.PricePerPointComparator;

/**
 * In memory data class to hold computer build information
 * @author Peter
 *
 */
public class ComputerBuild {
	public CPU cpu;	
	public ArrayList<GPU> gpuList;	
	public Motherboard mb;	
	public ArrayList<Memory> memList;	
	public ArrayList<Disk> diskList;	
	public PSU psu;
	
	private int coolingCost = 25;
	private int caseCost = 50;
	public float totalCost;
	public float totalPower;
	public float totalStorageCapacity;
	public int budget;
	public ComputerType type;
	
	
	public ComputerBuild(){
		budget = 500;
		type = ComputerType.GENERAL;
		init();
	}
	
	public ComputerBuild(int budget, String compType){
		this.budget = budget;
		type = ComputerType.toType(compType);
		init();
	}
	
	private void init(){
		cpu = null;
		mb = null;
		memList = new ArrayList<Memory>();
		gpuList = new ArrayList<GPU>();
		diskList = new ArrayList<Disk>();
		psu = null;
		
		totalCost = -1;
		totalPower = -1;
		totalStorageCapacity = -1;
	}
	
	/**
	 * Calculate power of current build
	 * @return
	 */
	public void calcCurrentPower(){
		totalPower = calcPower(cpu, mb, memList, gpuList, diskList);
	}
	
	/**
	 * Calculate power of build based on parts
	 * @param aCPU
	 * @param aMB
	 * @param aMEM
	 * @param aGPU
	 * @param diskNum
	 * @return
	 */
	public static int calcPower(CPU aCPU, Motherboard aMB, List<Memory> memList, List<GPU> gpuList, List<Disk> diskList) {
		int powerUsage = 0;
		powerUsage += aCPU.getPowerUsage();
		powerUsage += aMB.getPowerUsage();
		for(Memory mem : memList){
			powerUsage += mem.getPowerUsage();
		}
		for(Disk disk : diskList){
			powerUsage += disk.getPowerUsage();
		}
		for(GPU gpu : gpuList){
			powerUsage += gpu.getPowerUsage();
		}
		//Cooling consider 5 fans where each fan is rated around 2W 
		powerUsage += 10;
		//Recommended PSU should be able to handle max computer output + 50%
		powerUsage *= 1.5;
		return powerUsage;
	}
	
	public void computeCost(){
		float cost = 0;
		cost += cpu.vendorPrice;
		cost += mb.vendorPrice;
		for(Memory mem : memList){
			cost += mem.vendorPrice;
		}
		for(Disk disk : diskList){
			cost += disk.vendorPrice;
		}
		for(GPU gpu : gpuList){
			cost += gpu.vendorPrice;
		}
		cost += psu.vendorPrice;
		//Cooling consider cooling and case
		//Case: ~$50
		//Cooling: ~$25
		cost += coolingCost + caseCost;
		
		totalCost = cost;
	}
	
	/**
	 * Create custom payload to frontend client
	 * @return client payload
	 */
	public ClientPayload createClientPayload(){
		ClientPayload clientObj = new ClientPayload();
		//transform information to client object
		clientObj.budget = budget;
		clientObj.computerType = type.name();
		clientObj.totalPrice = totalCost;
		//add computer parts as components
		if(cpu != null){
			clientObj.components.add(cpu.shortenSpecs());
		}
		if(mb != null){
			clientObj.components.add(mb.shortenSpecs());
		}
		for(Memory mem : memList){
			clientObj.components.add(mem.shortenSpecs());
		}
		for(GPU gpu : gpuList){
			clientObj.components.add(gpu.shortenSpecs());
		}
		for(Disk disk : diskList){
			clientObj.components.add(disk.shortenSpecs());
		}
		if(psu != null){
			clientObj.components.add(psu.shortenSpecs());
		}
		return clientObj;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		if(cpu == null || mb == null || psu == null){
			return "No valid build...";
		}
		str.append(cpu.toString()).append(AppConstants.newLine);
		str.append(mb.toString()).append(AppConstants.newLine);
		for(Memory mem : memList){
			str.append(mem.toString()).append(AppConstants.newLine);
		}
		for(GPU gpu : gpuList){
			str.append(gpu.toString()).append(AppConstants.newLine);
		}
		for(Disk disk : diskList){
			str.append(disk.toString()).append(AppConstants.newLine);
		}
		str.append(psu.toString()).append(AppConstants.newLine);
		str.append("Total Price: " + totalCost).append(AppConstants.newLine);
		str.append("Total Power: " + totalPower);
		return str.toString();
	}

	public float getGPUScore() {
		float runningSum = 0;
		for(GPU gpu : gpuList){
			runningSum += gpu.benchScore;
		}
		return runningSum;
	}

	public float getMemScore() {
		float runningSum = 0;
		for(Memory mem : memList){
			runningSum += (float) (mem.memSpeed / 100);
			runningSum += mem.totalCapacity;
		}
		return runningSum;
	}
	
	public float getDiskScore() {
		float runningSum = 0;
		for(Disk disk : diskList){
			if(disk.diskType.equals(AppConstants.ssd)){
				runningSum += 2*disk.capacity;
			}
			runningSum += disk.capacity;
		}
		return runningSum;
	}

	public float getCPUScore() {
		if(cpu == null){
			return 0;
		}
		return cpu.benchScore;
	}
	
	public void copyComputerBuild(ComputerBuild copy){
		cpu = copy.cpu;	
		gpuList = copy.gpuList;	
		mb = copy.mb;	
		memList = copy.memList;	
		diskList = copy.diskList;	
		psu = copy.psu;
		
		totalCost = copy.totalCost;
		totalPower = copy.totalPower;
		totalStorageCapacity = copy.totalStorageCapacity;
		budget = copy.budget;
		type = copy.type;
	}
	
}
