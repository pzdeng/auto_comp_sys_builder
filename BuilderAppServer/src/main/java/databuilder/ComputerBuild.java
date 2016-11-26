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

	private CPU cpu;
	private ArrayList<CPU> cpuCandidates;
	
	private ArrayList<GPU> gpuList;
	private ArrayList<ArrayList<GPU>> gpuCandidates;
	
	private Motherboard mb;
	private ArrayList<Motherboard> mbCandidates;
	
	private ArrayList<Memory> memList;
	private ArrayList<ArrayList<Memory>> memCandidates;
	
	private ArrayList<Disk> diskList;
	private ArrayList<ArrayList<Disk>> diskCandidates;
	
	private PSU psu;
	private ArrayList<PSU> psuCandidates;
	
	private int partSetSize = 25;
	private float totalCost;
	private float totalPower;
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
		
		cpuCandidates = new ArrayList<CPU>();
		mbCandidates = new ArrayList<Motherboard>();
		memCandidates = new ArrayList<ArrayList<Memory>>();
		gpuCandidates = new ArrayList<ArrayList<GPU>>();
		diskCandidates = new ArrayList<ArrayList<Disk>>();
		psuCandidates = new ArrayList<PSU>();
		
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
						ArrayList<Memory> temp = new ArrayList<Memory>();
						temp.add(mem);
						if(mb.fitMem(temp)){
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
	 * Calculate (estimate) power of builds involving candidates
	 * @return
	 */
	private int estimatePower(){
		//TODO: probably needs to be better
		//Estimate the power based on the first components of each part
		CPU aCPU = cpuCandidates.isEmpty() ? new CPU() : cpuCandidates.get(0);
		Motherboard aMB = mbCandidates.isEmpty() ? new Motherboard() : mbCandidates.get(0);
		ArrayList<Memory> aMEMList = memCandidates.isEmpty() ? new ArrayList<Memory>() : memCandidates.get(0);
		ArrayList<GPU> aGPUList = gpuCandidates.isEmpty() ? new ArrayList<GPU>() : gpuCandidates.get(0);
		ArrayList<Disk> aDiskList = diskCandidates.isEmpty() ? new ArrayList<Disk>() : diskCandidates.get(0);
		return calcPower(aCPU, aMB, aMEMList, aGPUList, aDiskList);
	}
	
	/**
	 * Calculate power of current build
	 * @return
	 */
	private int calcCurrentPower(){
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
		/*
		cpu = new CPU();
		cpu.dummyPopulate();
		
		GPU gpu = new GPU();
		gpu.dummyPopulate();
		
		mb = new Motherboard();
		mb.dummyPopulate();
		
		gpuList.add(gpu);
		*/
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
		if(cpuCandidates.isEmpty()){
			//give up, no valid build
			return;
		}
		findMEM(compBudget * memAlloc, mb, type);
		findGPU(compBudget * gpuAlloc, mb, type);
		findDisk(compBudget * diskAlloc, mb, type);
		findPSU(compBudget * psuAlloc, estimatePower());
		//Any additional funds left goes to CPU
		//TODO: incremental improvement
		constructValidBuild();	
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
		// GPU : 30%+
		// PSU : 15%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .2;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .2;
		float gpuAlloc = (float) .3;
		float psuAlloc = (float) .15;
		float diskAlloc = (float) .1;
		float compBudget = budget - 75;
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpuCandidates.isEmpty()){
			//give up, no valid build
			return;
		}
		//Assume that the first motherboard best represents the mb candidates
		findMEM(compBudget * memAlloc, mbCandidates.get(0), type);
		findGPU(compBudget * gpuAlloc, mbCandidates.get(0), type);
		findDisk(compBudget * diskAlloc, mbCandidates.get(0), type);
		findPSU(compBudget * psuAlloc, estimatePower());
		//Any additional funds left goes to GPU
		//TODO: incremental improvement
		constructValidBuild();
	}

	private void setCPUandMBComponents(float cpuAlloc, float mbAlloc) {
		//Create a subset of good cpus and good motherboards
		ArrayList<CPU> goodCPU = new ArrayList<CPU>();
		ArrayList<Motherboard> goodMB = new ArrayList<Motherboard>();
		int index = 0;
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
					//Favor ATX motherboards
					if(aMB.formFactor.equals(AppConstants.atx)){
						//Choose motherboards with at least two sata ports 
						if(aMB.sataNum > 1){
							//Choose/Favor motherboards with DDR4
							if(aMB.memType.equals(AppConstants.ddr4)){
								goodMB.add(aMB);
							}
						}
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
		Collections.sort(goodCPU, new PricePerPointComparator());
		Collections.sort(goodMB, new PricePerPointComparator());
		//TODO: better logic here if time permits
		//Put in the first 5 matching cpu and motherboard combos		
		for(int i = 0; i < goodCPU.size(); i++){
			for(int j = 0; j < goodMB.size(); j++){
				if(goodMB.get(j).fitCPU(goodCPU.get(i))){
					cpuCandidates.add(goodCPU.get(i));
					mbCandidates.add(goodMB.get(j));
					//quit looping once we hit partSetSize
					if(cpuCandidates.size() == partSetSize){
						return;
					}
				}
			}
		}
		//if we could not get a good CPU and MB component, try against with a slightly bigger budget
		if(cpuCandidates.isEmpty() && cpuAlloc + mbAlloc < .8){
			System.out.println("Increasing budget for CPU/MB to " + (cpuAlloc + .1)*budget + "/" + (mbAlloc + .1)*budget + ". Trying again...");
			cpuCandidates = new ArrayList<CPU>();
			mbCandidates = new ArrayList<Motherboard>();
			setCPUandMBComponents((float) (cpuAlloc + .1), (float) (mbAlloc + .1));
		}
		else{
			//budget is too small to get a CPU and MB combo, or there are no suitable parts
			cpuCandidates = new ArrayList<CPU>();
			mbCandidates = new ArrayList<Motherboard>();
		}
	}

	private void findPSU(float budgetAlloc, int powerUsage) {
		ArrayList<PSU> goodPSU = new ArrayList<PSU>();
		int index = 0;
		//ArrayList<PSU> goodPSU = new ArrayList<PSU>();
		for(int i = 0; i < parts.getPSUList().size(); i++){
			PSU psu = parts.getPSUList().get(i);
			//Check if within price range
			if(psu.vendorPrice <= budgetAlloc){
				//TODO: favor higher efficiency
				if(psu.powerWattage >= powerUsage){
					goodPSU.add(psu);
				}
			}
		}
		Collections.sort(goodPSU, new PricePerPointComparator());
		//Choose (5) PSUs (best value, mid value, low value)
		if(goodPSU.isEmpty()){
			return;
		}
		if(goodPSU.size() <= partSetSize){
			psuCandidates.addAll(goodPSU);
			return;
		}
		for(int i = 0; i < partSetSize; i++){
			index = i == partSetSize - 1 ? goodPSU.size() - 1 : (i/(partSetSize - 1))*goodPSU.size();
			psuCandidates.add(goodPSU.get(index));
		}
	}

	private void findDisk(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<Disk> goodSSD = new ArrayList<Disk>();
		ArrayList<Disk> goodHDD = new ArrayList<Disk>();
		ArrayList<Disk> temp = new ArrayList<Disk>();
		int index = 0;
		ArrayList<ArrayList<Disk>> diskSet = new ArrayList<ArrayList<Disk>>();
		if(compType.equals(ComputerType.GAMING)){
			for(int i = 0; i < parts.getDISKList().size(); i++){
				Disk disk = parts.getDISKList().get(i);
				//Check if within price range
				if(disk.vendorPrice <= budgetAlloc){
					//SSD >= 128GB
					//HDD >= 500GB
					if(disk.diskType.equals(AppConstants.ssd)){
						if(disk.capacity >= 128){
							goodSSD.add(disk);
						}
					}
					if(disk.diskType.equals(AppConstants.hdd)){
						if(disk.capacity >= 500){
							goodHDD.add(disk);
						}
					}
				}
			}
			Collections.sort(goodSSD, new PricePerPointComparator());
			Collections.sort(goodHDD, new PricePerPointComparator());
			//Choose (5) configurations with one SSD and one HDD (best value, mid value, low value)
			if(goodSSD.isEmpty() && goodHDD.isEmpty()){
				return;
			}
			//Considering two drive setups
			for(int i = 0; i < partSetSize; i++){
				while(temp.size() < 2){
					if(!goodSSD.isEmpty()){
						index = i == partSetSize - 1 ? goodSSD.size() - 1 : (i/(partSetSize - 1))*goodSSD.size();
						temp.add(goodSSD.get(index));
					}
					if(!goodHDD.isEmpty()){
						index = i == partSetSize - 1 ? goodHDD.size() - 1 : (i/(partSetSize - 1))*goodHDD.size();
						temp.add(goodHDD.get(index));
					}
				}
				diskSet.add(temp);
				temp = new ArrayList<Disk>();
			}
			
			for(int i = 0; i < partSetSize; i++){
				//Consider one drive setups
				if(!goodSSD.isEmpty()){
					index = i == partSetSize - 1 ? goodSSD.size() - 1 : (i/(partSetSize - 1))*goodSSD.size();
					temp.add(goodSSD.get(index));
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
				if(!goodHDD.isEmpty()){
					index = i == partSetSize - 1 ? goodHDD.size() - 1 : (i/(partSetSize - 1))*goodHDD.size();
					temp.add(goodHDD.get(index));	
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
			}
		}
		diskCandidates = diskSet;
	}

	private void findGPU(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<GPU> goodGPU = new ArrayList<GPU>();
		ArrayList<GPU> temp = new ArrayList<GPU>();
		ArrayList<ArrayList<GPU>> gpuSet = new ArrayList<ArrayList<GPU>>();
		int index = 0;
		if(compType.equals(ComputerType.GAMING)){
			do{
				for(int i = 0; i < parts.getGPUList().size(); i++){
					GPU aGPU = parts.getGPUList().get(i);
					//Check if within price range
					if(aGPU.vendorPrice <= budgetAlloc){
						goodGPU.add(aGPU);
						//Check if it fits motherboard
						if(mb.fitGPU(goodGPU)){
							goodGPU.add(aGPU);
						}
					}
				}
				
				budgetAlloc += budget * .1;
			} while(budgetAlloc < budget && goodGPU.isEmpty());
		}
		Collections.sort(goodGPU, new PricePerPointComparator());
		//Choose (10) gpus over range (best value, mid value, low value)
		if(goodGPU.isEmpty()){
			return;
		}
		for(int i = 0; i < partSetSize; i++){
			index = i == partSetSize - 1 ? goodGPU.size() - 1 : (i/(partSetSize - 1))*goodGPU.size();
			temp.add(goodGPU.get(index));
			gpuSet.add(temp);
			temp = new ArrayList<GPU>();
		}
		gpuCandidates = gpuSet;
	}

	private void findMEM(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<ArrayList<Memory>> memSet = new ArrayList<ArrayList<Memory>>();
		ArrayList<Memory> goodMem = new ArrayList<Memory>();
		ArrayList<Memory> temp = new ArrayList<Memory>();
		int index = 0;
		if(compType.equals(ComputerType.GAMING)){
			for(int i = 0; i < parts.getMEMList().size(); i++){
				Memory mem = parts.getMEMList().get(i);
				//Check if within price range
				if(mem.vendorPrice <= budgetAlloc){
					//Favor +8GB total capacity
					if(mem.totalCapacity >= 8){
						//Check if it fits motherboard
						//TODO: consider multiple memory configurations
						if(mb.fitMem(mem)){
							goodMem.add(mem);
						}
					}
				}
			}
		}
		Collections.sort(goodMem, new PricePerPointComparator());
		//Choose (10) memory units (best value, mid value, low value)
		if(goodMem.isEmpty()){
			return;
		}
		for(int i = 0; i < partSetSize; i++){
			index = i == partSetSize - 1 ? goodMem.size() - 1 : (i/(partSetSize - 1))*goodMem.size();
			temp.add(goodMem.get(index));
			memSet.add(temp);
			temp = new ArrayList<Memory>();
		}
		memCandidates = memSet;
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
		
	}
	
	/**
	 * Construct and Validate computer build
	 * @return the first build that is good
	 */
	private void constructValidBuild() {
		for(int i = 0; i < cpuCandidates.size(); i++){
			cpu = cpuCandidates.get(i);
			for(int j = 0; j < mbCandidates.size(); j++){
				mb = mbCandidates.get(j);
				//Check: CPU fits into Motherboard CPU socket
				if(mb.fitCPU(cpu)){
					for(int k = 0; k < memCandidates.size(); k++){
						memList = memCandidates.get(k);
						//Check: Memory/RAM Type is compatible with Motherboard and CPU
						//Check: Memory/RAM units fits into Motherboard's available memory slots
						if(mb.fitMem(memList)){
							for(int l = 0; l < diskCandidates.size(); l++){
								diskList = diskCandidates.get(l);
								//Check: Storage Units fits into Motherboard's available SATA ports
								if(mb.fitDisk(diskList)){
									for(int m = 0; m <= gpuCandidates.size(); m++){
										//Consider empty gpu list
										gpuList = m == gpuCandidates.size() ? new ArrayList<GPU>() : gpuCandidates.get(m);
										//Check: GPU(s) fits into Motherboard's available interface(s)
										if(mb.fitGPU(gpuList)){
											totalPower = calcCurrentPower();
											for(int n = 0; n < psuCandidates.size(); n++){
												psu = psuCandidates.get(n);
												//Check: Fits into power requirements
												if(psu.checkPower(totalPower)){
													//Check: Build is within budget
													computeCost();
													if(totalCost <= budget){
														return;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		//If we hit here, that means no valid build
		cpu = null;
		mb = null;
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
}
