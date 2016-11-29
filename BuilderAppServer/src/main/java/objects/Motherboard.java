package main.java.objects;

import java.util.ArrayList;
import java.util.List;

import main.java.global.AppConstants;

public class Motherboard extends ComputerPart{
	public String formFactor;
	public String socketType;
	//There are motherboards that can accept multiple MemoryTypes but will only utilize one type
	public String memType;
	public int memMaxSize;
	//Implicitly assume every motherboard will have at least 2 slots minimum of stated memtype(s)
	public int memSlotNum;
	//Sata num regards 6Gbps connection
	//if 0, then assume there is at least 1 sata port for 3Gbps
	public int sataNum;
	//Simplify interface to number of PCIe_X16 slots 
	public int pciExpressX16Num;
	//public List<String> interfaceList;
	
	public Motherboard(){
		super();
		type = AppConstants.mobo;
	}	

	//Populate this object with dummy data
	public void dummyPopulate(){
		productName = "Gigabyte AM3+ AMD 970 SATA 6Gbps USB 3.0 ATX AM3+ Socket DDR3 1600 Motherboards (GA-970A-DS3P)";
		productURL = "https://www.amazon.com/Gigabyte-6Gbps-Socket-Motherboards-GA-970A-DS3P/dp/B00CX4MUCC";
		picURL = "https://images-na.ssl-images-amazon.com/images/I/91AIuBuQjuL._AC_UL115_.jpg";
		make = "GIGABYTE";
		vendorPrice = (float) 60.93;
		year = 2013;
	}
	
	public ComputerPartMin shortenSpecs(){
		ComputerPartMin part = new ComputerPartMin();
		part.type = type;
		part.name = productName;
		part.url = productURL;
		part.picUrl = picURL;
		part.price = vendorPrice;
		part.specs = specBuilder();
		return part;
	}
	
	public String specBuilder(){
		StringBuilder specs = new StringBuilder();
		specs.append("Make: ").append(make).append(AppConstants.newLine);
		specs.append("Supported CPU socket: ").append(socketType).append(AppConstants.newLine);
		specs.append("Supported Memory Types: ").append(memType).append(AppConstants.newLine);
		specs.append("Avaliable Memory Slots: ").append(memSlotNum);
		specs.append("FormFactor: ").append(formFactor).append(AppConstants.separator);
		return specs.toString();
	}
	
	public String dataContent(){
		StringBuilder specs = new StringBuilder();
		specs.append("Make: ").append(make).append(AppConstants.separator);
		specs.append("CPU Socket: ").append(socketType).append(AppConstants.separator);
		specs.append("FormFactor: ").append(formFactor).append(AppConstants.separator);
		specs.append("Memory Type: ").append(memType).append(AppConstants.separator);
		specs.append("Memory Slots: ").append(memSlotNum).append(AppConstants.separator);
		specs.append("Sata Ports (6Gbps): ").append(sataNum).append(AppConstants.separator);
		specs.append("PCI Express X16 Slot(s): ").append(pciExpressX16Num);
		return specs.toString();
	}
	
	public boolean fitCPU(CPU cpu){
		if(cpu == null || cpu.socketType == null){
			return false;
		}
		//If socket types are perfect match
		if(socketType.equals(cpu.socketType)){
			return true;
		}
		/*
		if(socketType.contains(cpu.socketType) || cpu.socketType.contains(socketType)){
			return true;
		}
		*/
		//TODO: check partial match?
		return false;
	}
	
	public boolean fitGPU(List<GPU> gpuList){
		//No gpu check
		if(gpuList.isEmpty()){
			return true;
		}
		int pciex16 = pciExpressX16Num;
		if(pciex16 == 0){
			pciex16 = 1;
		}
		if(gpuList.size() < pciex16){
			return true;
		}
		return false;
	}
	
	public boolean fitMem(Memory mem) {
		int numSlots = memSlotNum > 0 ? memSlotNum : 2;
		if(!memType.contains(mem.memType)){
			return false;
		}
		if(numSlots < mem.numModules){
			return false;
		}
		return true;
	}

	public boolean fitMem(List<Memory> memList) {
		int numSlots = memSlotNum > 0 ? memSlotNum : 2;
		int totalModules = 0;
		for(int i = 0; i < memList.size(); i++){
			if(!memType.contains(memList.get(i).memType)){
				return false;
			}
			totalModules += memList.get(i).numModules;
		}
		if(numSlots < totalModules){
			return false;
		}
		return true;
	}
	
	@Override
	public int getPowerUsage(){
		//TODO: get power difference between standard and high performance Motherboards
		if(powerRating < 1){
			//Motherboards rated around 50W
			powerRating = 50;
		}
		return powerRating;
	}

	public boolean fitDisk(List<Disk> diskList) {
		//Assume all motherboards should have at least one sata port
		int numSata = sataNum > 0 ? sataNum : 1;
		if(diskList.size() > numSata){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean titleCheck(String productTitle) {
		productTitle = productTitle.toLowerCase();
		//Check make
		if(!productTitle.contains(make.toLowerCase())){
			return false;
		}
		//Check model name (in parts)
		String[] modelNameParition = modelName.toLowerCase().split(" ");
		boolean partialMatch = false;
		if(modelNameParition.length > 0){
			for(String partition : modelNameParition){
				if(!productTitle.contains(partition)){
					//partialMatch = true;
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString(){
		String base = super.toString();
		base += " :: " + memType;
		return base;
	}
}
