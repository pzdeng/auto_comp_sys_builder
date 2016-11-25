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

/**
 * In memory data class to hold computer build information
 * @author Peter
 *
 */
public class ComputerBuild {

	private CPU cpu;
	private ArrayList<GPU> gpuList;
	private Motherboard mb;
	private ArrayList<Memory> memList;
	private ArrayList<Disk> diskList;
	private PSU psu;
	private float totalCost;
	private int budget;
	private ComputerType type;
	private DataBuilder parts;
	
	public ComputerBuild(){
		budget = 500;
		type = ComputerType.GENERAL;
		initParts();
	}
	
	public ComputerBuild(int budget, String compType){
		this.budget = budget;
		type = ComputerType.toType(compType);
		initParts();
		buildComp();
	}
	
	private void initParts(){
		cpu = null;
		mb = null;
		memList = new ArrayList<Memory>();
		gpuList = new ArrayList<GPU>();
		diskList = new ArrayList<Disk>();
		psu = null;
		parts = DataBuilder.getInstance();
		parts.initValidComputerParts();
	}
	
	/**
	 * Get all possible combinations
	 * @return
	 */
	public int getAllPossibleCombinations(){
		ArrayList<CPU> cpuList = parts.getCPUList();
		ArrayList<GPU> gpuList = parts.getGPUList();
		ArrayList<Motherboard> mbList = parts.getMBList();
		ArrayList<Memory> memList = parts.getMEMList();
		ArrayList<Disk> diskList = parts.getDISKList();
		ArrayList<PSU> psuList = parts.getPSUList();
		int numBuilds = 0;
		int powerUsage = 0;
		ArrayList<Memory> memTemp = new ArrayList<Memory>();
		ArrayList<GPU> gpuTemp = new ArrayList<GPU>();
		ArrayList<Disk> diskTemp = new ArrayList<Disk>();
		diskTemp.add(new Disk(AppConstants.hdd));
		for(Motherboard mb : mbList){
			for(CPU cpu : cpuList){
				if(mb.fitCPU(cpu)){
					for(Memory mem : memList){
						if(mb.fitMem(mem)){
							//TODO: consider additional memory units
							//Assume all motherboards should accommodate at least one disk
							//TODO: consider multiple disk setups
							//Consider builds without discrete GPU 
							memTemp.add(mem);
							powerUsage = calcPower(cpu, mb, memTemp, gpuTemp, diskTemp);
							for(PSU psu : psuList){
								if(powerUsage < psu.powerWattage){
									numBuilds += diskList.size();
								}
							}
							//Consider builds with (one) discrete GPU
							//TODO: consider multiGPU setups?
							for(GPU gpu : gpuList){
								gpuTemp.add(gpu);
								powerUsage = calcPower(cpu, mb, memTemp, gpuTemp, diskTemp);
								System.out.println(powerUsage);
								for(PSU psu : psuList){
									if(powerUsage < psu.powerWattage){
										numBuilds += diskList.size();
									}
								}
							}
							if(numBuilds > 5000){
								System.out.println(cpu.toString() + " | " + mb.toString() + " | " + mem.toString());
								break;
							}
						}
						memTemp.clear();
						gpuTemp.clear();
					}	
				}
			}
		}
		return numBuilds;
	}
	
	/**
	 * Calculate power of current build
	 * @return
	 */
	private int calcPower(){
		return calcPower(cpu, mb, memList, gpuList, diskList);
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
	private int calcPower(CPU aCPU, Motherboard aMB, List<Memory> memList, List<GPU> gpuList, List<Disk> diskList) {
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
		cost += 75;
		
		totalCost = cost;
	}
	
	/**
	 * Method should choose some optimal build based on target computer type
	 */
	private void buildComp(){		
		//Keeping Dummy Section until algorithm works
		cpu = new CPU();
		cpu.dummyPopulate();
		
		GPU gpu = new GPU();
		gpu.dummyPopulate();
		
		mb = new Motherboard();
		mb.dummyPopulate();
		
		gpuList.add(gpu);
		
		//Algorithm to build computer
		//Check Computer Type
		switch(type){
			case SERVER:
				buildServerComp();
				break;
			case GAMING:
				buildGamingComp();
				break;
			case WORKSTATION:
				buildWorkstationComp();
				break;
			default:
				buildGeneralComp();
		}
	}
	
	private void buildGeneralComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 4 GB
		// Mid - Low CPU
		// Motherboard
		// Mid - Low Graphics
		// Storage should be > 500 GB

		//TRY Budget Allocation Approach
		// CPU : 20%
		// Motherboard : 20%
		// Memory : 15%
		// GPU : 10%
		// PSU : 15%
		// Disk : 20%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .2;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .15;
		float gpuAlloc = (float) .1;
		float psuAlloc = (float) .15;
		float diskAlloc = (float) .2;
		float compBudget = budget - 75;
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpu == null || mb == null){
			//give up, no valid build
			return;
		}
		memList = findMEM(compBudget * memAlloc, mb, type);
		gpuList = findGPU(compBudget * gpuAlloc, mb, type);
		diskList = findDisk(compBudget * diskAlloc, mb, type);
		psu = findPSU(compBudget * psuAlloc, calcPower());
		//Any additional funds left goes to CPU
		//TODO: incremental improvement
		validateBuild();	
	}

	private void buildWorkstationComp() {
		//Could also be BATCH PROCESSING SERVER
		// TODO:
		//Outlining Computer Specs:
		// > 8GB RAM
		// High CPU, favor high threading (40% of budget)
		// Motherboard
		// Mid - Low Graphics
		// Storage should be > 500GB
		//validateBuild();
		
		//TRY Budget Allocation Approach
		// CPU : 30%+
		// Motherboard : 20%
		// Memory : 15%
		// GPU : 10%
		// PSU : 15%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .3;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .15;
		float gpuAlloc = (float) .1;
		float psuAlloc = (float) .15;
		float diskAlloc = (float) .1;
		float compBudget = budget - 75;
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpu == null || mb == null){
			//give up, no valid build
			return;
		}
		memList = findMEM(compBudget * memAlloc, mb, type);
		gpuList = findGPU(compBudget * gpuAlloc, mb, type);
		diskList = findDisk(compBudget * diskAlloc, mb, type);
		psu = findPSU(compBudget * psuAlloc, calcPower());
		//Any additional funds left goes to CPU
		//TODO: incremental improvement
		validateBuild();		
	}

	private void buildGamingComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 8GB RAM
		// > Mid CPU
		// Motherboard, needs to have > 1 PCIExpress slot
		// High Graphics (40% of budget)
			// Dual Graphics Cards (if time permits) 
		// Storage should be > 500GB
		
		//TRY Budget Allocation Approach
		// CPU : 20%
		// Motherboard : 20%
		// Memory : 15%
		// GPU : 20%+
		// PSU : 15%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .2;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .15;
		float gpuAlloc = (float) .2;
		float psuAlloc = (float) .1;
		float diskAlloc = (float) .1;
		float compBudget = budget - 75;
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpu == null || mb == null){
			//give up, no valid build
			return;
		}
		memList = findMEM(compBudget * memAlloc, mb, type);
		gpuList = findGPU(compBudget * gpuAlloc, mb, type);
		diskList = findDisk(compBudget * diskAlloc, mb, type);
		psu = findPSU(compBudget * psuAlloc, calcPower());
		//Any additional funds left goes to GPU
		//TODO: incremental improvement
		validateBuild();
	}

	private void setCPUandMBComponents(float cpuAlloc, float mbAlloc) {
		//Create a subset of good cpus and good motherboards
		ArrayList<CPU> goodCPU = new ArrayList<CPU>();
		ArrayList<Motherboard> goodMB = new ArrayList<Motherboard>();
		float budgetAlloc;
		
		if(type.equals(ComputerType.SERVER)){
			//Scan for good CPUs
			budgetAlloc = budget * cpuAlloc;
			for(int i = 0; i < parts.getCPUList().size(); i++){
				CPU aCPU = parts.getCPUList().get(i);
				//Check if within price range
				if(aCPU.vendorPrice <= budgetAlloc){
					//Any CPU would work
					goodCPU.add(aCPU);
				}
			}
			
			//Scan for good MBs
			budgetAlloc = budget * mbAlloc;
			for(int i = 0; i < parts.getMBList().size(); i++){
				Motherboard aMB = parts.getMBList().get(i);
				//Check if within price range
				if(aMB.vendorPrice <= budgetAlloc){
					//Favor motherboard with a lot of sata ports (>= 4)
					if(aMB.sataNum >= 4){
						goodMB.add(aMB);
					}
				}
			}
		}
		
		if(type.equals(ComputerType.GAMING)){	
			//Scan for good CPUs
			budgetAlloc = budget * cpuAlloc;
			for(int i = 0; i < parts.getCPUList().size(); i++){
				CPU aCPU = parts.getCPUList().get(i);
				//Check if within price range
				if(aCPU.vendorPrice <= budgetAlloc){
					//TODO: (future scope) consider hyperthreading for intel
					//Check number of cores (4 physical cores)
					//NOTE: < 4 cores too low, > 4 cores too high
					if(aCPU.coreCount == 4){
						goodCPU.add(aCPU);
					}
				}
			}
			//Scan for good MBs
			budgetAlloc = budget * mbAlloc;
			for(int i = 0; i < parts.getMBList().size(); i++){
				Motherboard aMB = parts.getMBList().get(i);
				//Check if within price range
				if(aMB.vendorPrice <= budgetAlloc){
					//Favor motherboards with DDR4
					//TODO: consider DDR3 if there are no motherboards with DDR4
					if(aMB.memType.equals(AppConstants.ddr4)){
						goodMB.add(aMB);
					}
				}
			}
		}
		
		if(type.equals(ComputerType.WORKSTATION)){
			//Scan for good CPUs
			budgetAlloc = budget * cpuAlloc;
			for(int i = 0; i < parts.getCPUList().size(); i++){
				CPU aCPU = parts.getCPUList().get(i);
				//Check if within price range
				if(aCPU.vendorPrice <= budgetAlloc){
					//Favor highly threaded CPU
					if(aCPU.coreCount > 4){
						goodCPU.add(aCPU);
					}
				}
			}
			
			//Scan for good MBs
			budgetAlloc = budget * mbAlloc;
			for(int i = 0; i < parts.getMBList().size(); i++){
				Motherboard aMB = parts.getMBList().get(i);
				//Check if within price range
				if(aMB.vendorPrice <= budgetAlloc){
					//Favor motherboard with DDR4 memory
					if(aMB.memType.equals(AppConstants.ddr4)){
						goodMB.add(aMB);
					}
				}
			}
		}
		
		if(type.equals(ComputerType.GENERAL)){
			//Scan for good CPUs
			budgetAlloc = budget * cpuAlloc;
			for(int i = 0; i < parts.getCPUList().size(); i++){
				CPU aCPU = parts.getCPUList().get(i);
				//Check if within price range
				if(aCPU.vendorPrice <= budgetAlloc){
					goodCPU.add(aCPU);
				}
			}
			//Scan for good MBs
			budgetAlloc = budget * mbAlloc;
			for(int i = 0; i < parts.getMBList().size(); i++){
				Motherboard aMB = parts.getMBList().get(i);
				//Check if within price range
				if(aMB.vendorPrice <= budgetAlloc){
					goodMB.add(aMB);
				}
			}
		}
		//Match cpu with motherboard
		System.out.println(goodCPU.size());
		System.out.println(goodMB.size());
		for(int i = 0; i < goodCPU.size(); i++){
			for(int j = 0; j < goodMB.size(); j++){
				//TODO: better logic here, probably do some rank compare
				if(goodMB.get(j).fitCPU(goodCPU.get(i))){
					cpu = goodCPU.get(i);
					mb = goodMB.get(j);
					return;
				}
			}
		}
		//if we could not get a good CPU and MB component, try against with a slightly bigger budget
		if(cpu == null && cpuAlloc + mbAlloc < .8){
			System.out.println("Trying again...");
			setCPUandMBComponents((float) (cpuAlloc + .1), (float) (mbAlloc + .1));
		}
		else{
			//budget is too small to get a CPU and MB combo, or there are no suitable parts
			cpu = null;
			mb = null;
		}
	}

	private PSU findPSU(float budgetAlloc, int powerUsage) {
		//TODO: pick better (create a subset of good PSUs and rank them)
		//ArrayList<PSU> goodPSU = new ArrayList<PSU>();
		for(int i = 0; i < parts.getPSUList().size(); i++){
			PSU psu = parts.getPSUList().get(i);
			//Check if within price range
			if(psu.vendorPrice <= budgetAlloc){
				//TODO: favor higher efficiency
				if(psu.powerWattage > powerUsage){
					return psu;
				}
			}
		}
		return null;
	}

	private ArrayList<Disk> findDisk(float budgetAlloc, Motherboard mb, ComputerType compType) {
		if(compType.equals(ComputerType.GAMING)){
			//TODO: pick better (create a subset of good disks and rank them)
			ArrayList<Disk> goodDisk = new ArrayList<Disk>();
			for(int i = 0; i < parts.getDISKList().size(); i++){
				Disk disk = parts.getDISKList().get(i);
				//Check if within price range
				if(disk.vendorPrice <= budgetAlloc){
					//TODO: handle choosing multiple disks
					//TODO: favor configuration with one SSD and one HDD
					//SSD >= 128GB
					//HDD >= 500GB
					if(disk.diskType.equals(AppConstants.ssd)){
						if(disk.capacity >= 128){
							goodDisk.add(disk);
						}
						//Check if it fits motherboard
						if(mb.fitDisk(goodDisk)){
								return goodDisk;
						}
					}
					if(disk.diskType.equals(AppConstants.hdd)){
						if(disk.capacity >= 500){
							goodDisk.add(disk);
						}
						//Check if it fits motherboard
						if(mb.fitDisk(goodDisk)){
								return goodDisk;
						}
					}
					goodDisk.clear();
				}
			}
		}
		return null;
	}

	private ArrayList<GPU> findGPU(float budgetAlloc, Motherboard mb, ComputerType compType) {
		//TODO: pick better (create a subset of good GPUs and rank them)
		ArrayList<GPU> goodGPU = new ArrayList<GPU>();
		if(compType.equals(ComputerType.GAMING)){
			for(int i = 0; i < parts.getGPUList().size(); i++){
				GPU aGPU = parts.getGPUList().get(i);
				//Check if within price range
				if(aGPU.vendorPrice <= budgetAlloc){
					goodGPU.add(aGPU);
					//Check if it fits motherboard
					if(mb.fitGPU(goodGPU)){
							return goodGPU;
					}
					goodGPU.clear();
				}
			}
		}
		return goodGPU;
	}

	private ArrayList<Memory> findMEM(float budgetAlloc, Motherboard mb, ComputerType compType) {
		if(compType.equals(ComputerType.GAMING)){
			//TODO: pick better (create a subset of valid memory units and rank them)
			ArrayList<Memory> goodMEM = new ArrayList<Memory>();
			for(int i = 0; i < parts.getMEMList().size(); i++){
				Memory mem = parts.getMEMList().get(i);
				//Check if within price range
				if(mem.vendorPrice <= budgetAlloc){
					//Favor +8GB total capacity
					if(mem.totalCapacity >= 8){
						//Check if it fits motherboard
						if(mb.fitMem(mem)){
							goodMEM.add(mem);
							return goodMEM;
						}
					}
				}
			}
		}
		System.out.println("SHIT");
		return null;
	}

	private void buildServerComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 16GB RAM
		// > Low CPU
		// Motherboard, favor high # of SataPorts, favor high number of memory slots
		// Graphics (None)
		// Storage should be > 10TB
		//TRY Budget Allocation Approach
		// CPU : 30%+
		// Motherboard : 20%
		// Memory : 10%
		// GPU : 20%
		// PSU : 15%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .3;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .15;
		float gpuAlloc = (float) .2;
		float psuAlloc = (float) .15;
		float diskAlloc = (float) .1;
		float compBudget = budget - 75;
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpu == null || mb == null){
			//give up, no valid build
			return;
		}
		memList = findMEM(compBudget * memAlloc, mb, type);
		gpuList = findGPU(compBudget * gpuAlloc, mb, type);
		diskList = findDisk(compBudget * diskAlloc, mb, type);
		psu = findPSU(compBudget * psuAlloc, calcPower());
		//Any additional funds left goes to GPU
		//TODO: incremental improvement
		validateBuild();
	}
	
	/**
	 * Validate computer build
	 * @return
	 */
	private boolean validateBuild() {
		//Check 1: CPU fits into Motherboard CPU socket
		if(!mb.fitCPU(cpu)){
			return false;
		}
		//Check 2: GPU(s) fits into Motherboard's available interface(s)
		if(!mb.fitGPU(gpuList)){
			return false;
		}
		//Check 3: Memory/RAM Type is compatible with Motherboard and CPU
		//TODO: Fail check if DDR2/DDR3/DDR4 are not the same
		//Check 4: Memory/RAM unit fits into Motherboard's available memory slots
		//TODO: Fail check # RAM modules > Motherboard's # mem slots
		//Check 5: Storage Units fits into Motherboard's available SATA ports
		//TODO: Fail check if # of HDD/SDD > Motherboard's # SATA ports
		//Check 6: Fits into power requirements (require power calculation)
		//TODO: Compute power requirements of current build
		//TODO: Check against PSU capacity
		//Check 7: Build is within budget
		//TODO: Check prices of items are within budget
		computeCost();
		return true;
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
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		if(cpu == null || mb == null){
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
		str.append("Total Price: " + totalCost);
		return str.toString();
	}
}
