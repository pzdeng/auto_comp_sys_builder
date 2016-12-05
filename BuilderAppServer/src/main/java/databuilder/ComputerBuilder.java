package main.java.databuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ComputerType;
import main.java.objects.Disk;
import main.java.objects.GPU;
import main.java.objects.Memory;
import main.java.objects.Motherboard;
import main.java.objects.PSU;
import main.java.objects.comparator.PricePerPointComparator;

public class ComputerBuilder {
	private ArrayList<CPU> cpuCandidates;
	private ArrayList<ArrayList<GPU>> gpuCandidates;
	private ArrayList<Motherboard> mbCandidates;
	private ArrayList<ArrayList<Memory>> memCandidates;
	private ArrayList<ArrayList<Disk>> diskCandidates;
	private ArrayList<PSU> psuCandidates;
	
	private int partSetSize = 50;
	private int coolingCost = 25;
	private int caseCost = 50;
	private int budget;
	private int timeout;
	private ComputerType type;
	private ComputerBuild currBuild;
	
	private DataInitializer parts;
	
	public ComputerBuilder(int budget, String compType, int buildTimeOut){
		this.budget = budget;
		type = ComputerType.toType(compType);
		timeout = buildTimeOut;
		initParts();
		buildComp();
	}
	
	private void initParts(){		
		cpuCandidates = new ArrayList<CPU>();
		mbCandidates = new ArrayList<Motherboard>();
		memCandidates = new ArrayList<ArrayList<Memory>>();
		gpuCandidates = new ArrayList<ArrayList<GPU>>();
		diskCandidates = new ArrayList<ArrayList<Disk>>();
		psuCandidates = new ArrayList<PSU>();
		
		parts = DataInitializer.getInstance();
		parts.initValidComputerParts();
		currBuild = new ComputerBuild();
		currBuild.budget = budget;
		currBuild.timeout = timeout;
		currBuild.type = type;
	}
	
	/**
	 * Method should choose some optimal build based on target computer type
	 */
	private void buildComp(){		
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
		// GPU : 40%+
		// PSU : 20%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .3;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .2;
		float gpuAlloc = (float) .4;
		float psuAlloc = (float) .2;
		float diskAlloc = (float) .1;
		float compBudget = budget - (coolingCost + caseCost);
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
		// PSU : 20%
		// Disk : 10%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .3;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .15;
		float gpuAlloc = (float) .1;
		float psuAlloc = (float) .2;
		float diskAlloc = (float) .2;
		float compBudget = budget - (coolingCost + caseCost);
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
		//Any additional funds left goes to CPU
		//TODO: incremental improvement
		constructValidBuild();
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
		// CPU : 15%
		// Motherboard : 20%
		// Memory : 20%
		// GPU : 10%
		// PSU : 20%
		// Disk : 30%+
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .15;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .2;
		float gpuAlloc = (float) .1;
		float psuAlloc = (float) .2;
		float diskAlloc = (float) .30;
		float compBudget = budget - (coolingCost + caseCost);
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpuCandidates.isEmpty()){
			//give up, no valid build
			return;
		}
		findMEM(compBudget * memAlloc, mbCandidates.get(0), type);
		findGPU(compBudget * gpuAlloc, mbCandidates.get(0), type);
		findDisk(compBudget * diskAlloc, mbCandidates.get(0), type);
		findPSU(compBudget * psuAlloc, estimatePower());
		//TODO: incremental improvement
		constructValidBuild();	
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
		// CPU : 25%
		// Motherboard : 20%
		// Memory : 15%
		// GPU : 20%
		// PSU : 20%
		// Disk : 20%
		// Case and Cooling : fixed -$75
		float cpuAlloc = (float) .3;
		float mbAlloc = (float) .2;
		float memAlloc = (float) .2;
		float gpuAlloc = (float) .2;
		float psuAlloc = (float) .2;
		float diskAlloc = (float) .2;
		float compBudget = budget - (coolingCost + caseCost);
		setCPUandMBComponents(cpuAlloc, mbAlloc);
		if(cpuCandidates.isEmpty()){
			//give up, no valid build
			return;
		}
		findMEM(compBudget * memAlloc, mbCandidates.get(0), type);
		findGPU(compBudget * gpuAlloc, mbCandidates.get(0), type);
		findDisk(compBudget * diskAlloc, mbCandidates.get(0), type);
		findPSU(compBudget * psuAlloc, estimatePower());
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
		
		else if(type.equals(ComputerType.GAMING)){	
			//Scan for good CPUs
			budgetAlloc = budget * cpuAlloc;
			for(int i = 0; i < parts.getCPUList().size(); i++){
				CPU aCPU = parts.getCPUList().get(i);
				//Check if within price range
				if(aCPU.vendorPrice <= budgetAlloc){
					//TODO: (future scope) consider hyperthreading for intel
					//Check number of cores (4 physical cores)
					//NOTE: < 4 cores too low, > 8 cores too high
					if(aCPU.coreCount >= 4 && aCPU.coreCount <= 8){
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
							if(aMB.memType.equals(AppConstants.ddr4) || aMB.memType.equals(AppConstants.ddr3)){
								goodMB.add(aMB);
							}
						}
					}
				}
			}
		}
		
		else if(type.equals(ComputerType.WORKSTATION)){
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
					goodMB.add(aMB);
				}
			}
		}
		
		else {
			//ComputerType.GENERAL
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
		boolean fits = false;
		HashSet<CPU> cpuSet = new HashSet<CPU>();
		HashSet<Motherboard> mbSet = new HashSet<Motherboard>();
		for(int j = 0; j < goodMB.size(); j++){
			for(int i = 0; i < goodCPU.size(); i++){
				if(goodMB.get(j).fitCPU(goodCPU.get(i))){
					cpuSet.add(goodCPU.get(i));
					fits = true;
				}
			}
			if(fits){
				mbSet.add(goodMB.get(j));
				fits = false;
			}
			//quit looping once we hit partSetSize
			if(mbSet.size() >= partSetSize){
				break;
			}
		}
		cpuCandidates.addAll(cpuSet);
		mbCandidates.addAll(mbSet);
		//if we could not get a good CPU and MB component, try against with a slightly bigger budget
		if(cpuCandidates.isEmpty()){ 
			if(cpuAlloc + mbAlloc < .8){
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
	}
	
	private void findGPU(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<GPU> goodGPU = new ArrayList<GPU>();
		ArrayList<GPU> temp = new ArrayList<GPU>();
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
		else if(compType.equals(ComputerType.GENERAL) || compType.equals(ComputerType.SERVER) || compType.equals(ComputerType.WORKSTATION)){
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
		}
		Collections.sort(goodGPU, new PricePerPointComparator());
		//Choose (10) gpus over range (best value, mid value, low value)
		if(goodGPU.isEmpty()){
			return;
		}
		if(goodGPU.size() <= partSetSize){
			for(GPU gpu : goodGPU){
				temp.add(gpu);
				gpuCandidates.add(temp);
				temp = new ArrayList<GPU>();
			}
			return;
		}
		for(int i = 0; i < partSetSize; i++){
			index = i == partSetSize - 1 ? goodGPU.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodGPU.size());
			temp.add(goodGPU.get(index));
			gpuCandidates.add(temp);
			temp = new ArrayList<GPU>();
		}
	}
	
	private void findMEM(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<ArrayList<Memory>> memSet = new ArrayList<ArrayList<Memory>>();
		ArrayList<Memory> goodMem = new ArrayList<Memory>();
		ArrayList<Memory> temp = new ArrayList<Memory>();
		int index = 0;
		if(compType.equals(ComputerType.GAMING) || compType.equals(ComputerType.WORKSTATION) || compType.equals(ComputerType.SERVER)){
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
		else if(compType.equals(ComputerType.GENERAL)){
			for(int i = 0; i < parts.getMEMList().size(); i++){
				Memory mem = parts.getMEMList().get(i);
				//Check if within price range
				if(mem.vendorPrice <= budgetAlloc){
					//Check if it fits motherboard
					if(mb.fitMem(mem)){
						goodMem.add(mem);
					}
				}
			}
		}
		Collections.sort(goodMem, new PricePerPointComparator());
		//Choose (10) memory units (best value, mid value, low value)
		if(goodMem.isEmpty()){
			return;
		}
		if(goodMem.size() <= partSetSize){
			for(Memory mem : goodMem){
				temp.add(mem);
				memCandidates.add(temp);
				temp = new ArrayList<Memory>();
			}
			return;
		}
		for(int i = 0; i < partSetSize; i++){
			index = i == partSetSize - 1 ? goodMem.size() - 1 : (int)(((float)i/(partSetSize - 1)*goodMem.size()));
			temp.add(goodMem.get(index));
			memCandidates.add(temp);
			temp = new ArrayList<Memory>();
		}
	}
	
	private void findDisk(float budgetAlloc, Motherboard mb, ComputerType compType) {
		ArrayList<Disk> goodSSD = new ArrayList<Disk>();
		ArrayList<Disk> goodHDD = new ArrayList<Disk>();
		ArrayList<Disk> temp = new ArrayList<Disk>();
		int index = 0;
		ArrayList<ArrayList<Disk>> diskSet = new ArrayList<ArrayList<Disk>>();
		if(compType.equals(ComputerType.GENERAL)){
			for(int i = 0; i < parts.getDISKList().size(); i++){
				Disk disk = parts.getDISKList().get(i);
				//Check if within price range
				if(disk.vendorPrice <= budgetAlloc){
					//GAMING
					//SSD >= 500GB
					//HDD >= 500GB
					if(disk.diskType.equals(AppConstants.ssd)){
						if(disk.capacity >= 500){
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
			for(int i = 0; i < partSetSize; i++){
				//Consider one drive setups
				if(!goodSSD.isEmpty()){
					index = i == partSetSize - 1 ? goodSSD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodSSD.size());
					temp.add(goodSSD.get(index));
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
				if(!goodHDD.isEmpty()){
					index = i == partSetSize - 1 ? goodHDD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodHDD.size());
					temp.add(goodHDD.get(index));	
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
			}
		}
		if(compType.equals(ComputerType.GAMING) || compType.equals(ComputerType.WORKSTATION) || compType.equals(ComputerType.SERVER)){
			for(int i = 0; i < parts.getDISKList().size(); i++){
				Disk disk = parts.getDISKList().get(i);
				//Check if within price range
				if(disk.vendorPrice <= budgetAlloc){
					if(compType.equals(ComputerType.GAMING)){
						//GAMING
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
					else if(compType.equals(ComputerType.WORKSTATION)){
						//WORKSTATION
						//SSD >= 256GB
						//HDD >= 10000GB
						if(disk.diskType.equals(AppConstants.ssd)){
							if(disk.capacity >= 256){
								goodSSD.add(disk);
							}
						}
						if(disk.diskType.equals(AppConstants.hdd)){
							if(disk.capacity >= 1000){
								goodHDD.add(disk);
							}
						}
					}
					//TODO: server with multi disk configurations 3+
					else if(compType.equals(ComputerType.SERVER)){
						//SERVER
						//SSD >= 1000GB
						//HDD >= 2000GB
						if(disk.diskType.equals(AppConstants.ssd)){
							if(disk.capacity >= 1000){
								goodSSD.add(disk);
							}
						}
						if(disk.diskType.equals(AppConstants.hdd)){
							if(disk.capacity >= 2000){
								goodHDD.add(disk);
							}
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
						index = i == partSetSize - 1 ? goodSSD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodSSD.size());
						temp.add(goodSSD.get(index));
					}
					if(!goodHDD.isEmpty()){
						index = i == partSetSize - 1 ? goodHDD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodHDD.size());
						temp.add(goodHDD.get(index));
					}
				}
				diskSet.add(temp);
				temp = new ArrayList<Disk>();
			}
			
			for(int i = 0; i < partSetSize; i++){
				//Consider one drive setups
				if(!goodSSD.isEmpty()){
					index = i == partSetSize - 1 ? goodSSD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodSSD.size());
					temp.add(goodSSD.get(index));
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
				if(!goodHDD.isEmpty()){
					index = i == partSetSize - 1 ? goodHDD.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodHDD.size());
					temp.add(goodHDD.get(index));	
					diskSet.add(temp);
					temp = new ArrayList<Disk>();
				}
			}
		}
		diskCandidates = diskSet;
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
			index = i == partSetSize - 1 ? goodPSU.size() - 1 : (int)(((float)i/(partSetSize - 1))*goodPSU.size());
			psuCandidates.add(goodPSU.get(index));
		}
	}
	
	/**
	 * Construct and Validate computer build
	 * @return the first build that is good
	 */
	private void constructValidBuild() {
		//Give method 'buildTime' seconds to run
		long expireTime = System.currentTimeMillis() + timeout * 1000;
		ComputerBuild temp = new ComputerBuild();
		temp.budget = budget;
		temp.type = type;
		temp.timeout = timeout;
		for(int i = 0; i < mbCandidates.size(); i++){
			temp.mb = mbCandidates.get(i);
			for(int j = 0; j < cpuCandidates.size(); j++){
				temp.cpu = cpuCandidates.get(j);
				//Check: CPU fits into Motherboard CPU socket
				if(temp.mb.fitCPU(temp.cpu)){
					for(int k = 0; k < memCandidates.size(); k++){
						temp.memList = memCandidates.get(k);
						//Check: Memory/RAM Type is compatible with Motherboard and CPU
						//Check: Memory/RAM units fits into Motherboard's available memory slots
						if(temp.mb.fitMem(temp.memList)){
							for(int l = 0; l < diskCandidates.size(); l++){
								temp.diskList = diskCandidates.get(l);
								//Check: Storage Units fits into Motherboard's available SATA ports
								if(temp.mb.fitDisk(temp.diskList)){
									for(int m = 0; m <= gpuCandidates.size(); m++){
										if(m == gpuCandidates.size()){
											if(type == ComputerType.GAMING){
												//GPU is mandatory for GAMING Computer
												break;
											}
											//Consider empty gpu list
											temp.gpuList = new ArrayList<GPU>();
										}
										else{
											temp.gpuList = gpuCandidates.get(m);
										}
										//Check: GPU(s) fits into Motherboard's available interface(s)
										if(temp.mb.fitGPU(temp.gpuList)){
											temp.calcCurrentPower();
											for(int n = 0; n < psuCandidates.size(); n++){
												temp.psu = psuCandidates.get(n);
												//Check: Fits into power requirements
												if(temp.psu.checkPower(temp.totalPower)){
													//Check: Build is within budget
													temp.computeCost();
													if(temp.totalCost <= budget){
														//Replace goodBuild if temp is better
														if(compareBuilds(currBuild, temp) > 0){
															currBuild.copyComputerBuild(temp);
															break;
														}
													}
												}
											}
											if(expireTime < System.currentTimeMillis()){
												//End early
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
	
	//TODO: Finish
	/*
	 * Get all possible combinations
	 * @return

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
	 */
	
	/**
	 * Method to compare two builds
	 * @param goodBuild
	 * @param temp
	 * @return 0 if they are equal, -1 if goodBuild is better, 1 if temp is better
	 */
	private int compareBuilds(ComputerBuild goodBuild, ComputerBuild temp) {
		int compareVal = 0;
		switch(type){
		case GAMING:
			//Favor higher ranked GPU
			if(compareVal == 0 && goodBuild.getGPUScore() != temp.getGPUScore()){
				compareVal = goodBuild.getGPUScore() > temp.getGPUScore() ? -1 : 1; 
			}
			//Favor higher ranked CPU
			if(compareVal == 0 && goodBuild.getCPUScore() != temp.getCPUScore()){
				compareVal = goodBuild.getCPUScore() > temp.getCPUScore() ? -1 : 1;
			}
			//Favor better memory
			if(compareVal == 0 && goodBuild.getMemScore() != temp.getMemScore()){
				compareVal = goodBuild.getMemScore() > temp.getMemScore() ? -1 : 1;
			}
			//Favor higher storage
			if(compareVal == 0 && goodBuild.getDiskScore() != temp.getDiskScore()){
				compareVal = goodBuild.getDiskScore() > temp.getDiskScore() ? -1 : 1;
			}
			break;
		case SERVER:
			//Favor higher storage
			if(compareVal == 0 && goodBuild.getDiskScore() != temp.getDiskScore()){
				compareVal = goodBuild.getDiskScore() > temp.getDiskScore() ? -1 : 1;
			}
			//Favor better memory
			if(compareVal == 0 && goodBuild.getMemScore() != temp.getMemScore()){
				compareVal = goodBuild.getMemScore() > temp.getMemScore() ? -1 : 1;
			}
			//Favor higher ranked CPU
			if(compareVal == 0 && goodBuild.getCPUScore() != temp.getCPUScore()){
				compareVal = goodBuild.getCPUScore() > temp.getCPUScore() ? -1 : 1;
			}
			break;
		case WORKSTATION:
			//Favor higher CPU
			if(compareVal == 0 && goodBuild.getCPUScore() != temp.getCPUScore()){
				compareVal = goodBuild.getCPUScore() > temp.getCPUScore() ? -1 : 1;
			}
			//Favor better memory
			if(compareVal == 0 && goodBuild.getMemScore() != temp.getMemScore()){
				compareVal = goodBuild.getMemScore() > temp.getMemScore() ? -1 : 1;
			}			
			//Favor higher storage
			if(compareVal == 0 && goodBuild.getDiskScore() != temp.getDiskScore()){
				compareVal = goodBuild.getDiskScore() > temp.getDiskScore() ? -1 : 1;
			}
			//Favor higher GPU
			if(compareVal == 0 && goodBuild.getGPUScore() != temp.getGPUScore()){
				compareVal = goodBuild.getGPUScore() > temp.getGPUScore() ? -1 : 1; 
			}
		//GENERAL
		default:
			//Favor higher CPU
			if(compareVal == 0 && goodBuild.getCPUScore() != temp.getCPUScore()){
				compareVal = goodBuild.getCPUScore() > temp.getCPUScore() ? -1 : 1;
			}
			//Favor better memory
			if(compareVal == 0 && goodBuild.getMemScore() != temp.getMemScore()){
				compareVal = goodBuild.getMemScore() > temp.getMemScore() ? -1 : 1;
			}
			//Favor higher GPU
			if(compareVal == 0 && goodBuild.getGPUScore() != temp.getGPUScore()){
				compareVal = goodBuild.getGPUScore() > temp.getGPUScore() ? -1 : 1; 
			}
			//Favor higher storage
			if(compareVal == 0 && goodBuild.getDiskScore() != temp.getDiskScore()){
				compareVal = goodBuild.getDiskScore() > temp.getDiskScore() ? -1 : 1;
			}
		}
		return compareVal;
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
		return ComputerBuild.calcPower(aCPU, aMB, aMEMList, aGPUList, aDiskList);
	}
	
	public ComputerBuild getBuild(){
		return currBuild;
	}
}
