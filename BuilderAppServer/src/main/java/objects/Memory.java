package main.java.objects;

import main.java.global.AppConstants;

public class Memory extends ComputerPart{
	public int totalCapacity;
	public int numModules;
	public String memType;
	public float memSpeed;
	
	public Memory(){
		super();
		type = AppConstants.memory;
	}
	
	/**
	 * Check to see if this title matches the specs we have
	 * @param productTitle
	 * @return
	 */
	public boolean titleCheck(String productTitle){
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
				if(productTitle.contains(partition)){
					partialMatch = true;
				}
			}
		}
		if(!partialMatch){
			return false;
		}
		//Check memtype
		if(!productTitle.contains(memType.toLowerCase())){
			return false;
		}
		//Check memspeed
		String memorySpeed = (int) memSpeed + "";
		if(!productTitle.contains(memorySpeed)){
			return false;
		}
		//Check capacity
		if(!productTitle.contains((totalCapacity+AppConstants.gigabyte).toLowerCase())){
			return false;
		}
		return true;
	}
	
	@Override
	public ComputerPartMin shortenSpecs(){
		ComputerPartMin part = new ComputerPartMin();
		part.type = type;
		part.name = productName;
		part.url = productURL;
		part.picUrl = picURL;
		part.price = String.format("%.2f", vendorPrice);
		part.specs = specBuilder();
		return part;
	}
	
	/**
	 * Price per gigabyte
	 */
	@Override
	public void computePricePerPoint(){
		if(totalCapacity > 0){
			pricePerPoint = (float) (vendorPrice / totalCapacity);
		}
		else{
			pricePerPoint = 0;
		}
	}

	@Override
	public String specBuilder() {
		StringBuilder specs = new StringBuilder();
		specs.append("Make: ").append(make).append(AppConstants.newLine);
		specs.append("Memory Type: ").append(memType).append(AppConstants.newLine);
		specs.append("Memory Speed: ").append(memSpeed).append(AppConstants.megahertz).append(AppConstants.newLine);
		specs.append("Number of modules: ").append(numModules);
		return specs.toString();
	}
	
	@Override
	public int getPowerUsage() {
		//Expected number of modules to be at least one
		if(numModules < 1){
			numModules = 1;
		}
		switch(memType){
			case(AppConstants.ddr2):
				//DDR2 rated at around 5W
				return 5 * numModules;
			case(AppConstants.ddr3):
				//DDR3 rated at around 3W
				return 3 * numModules;
			case(AppConstants.ddr4):
				//DDR3 rated at around 3W
				return 3 * numModules;
			default:
				//Return max power consumption for memory
				//DDR rated at around 6W
				return 6 * numModules;
		}
	}	
	
	public String dataContent(){
		StringBuilder specs = new StringBuilder();
		specs.append("TotalCapacity: ").append(totalCapacity).append(AppConstants.gigabyte).append(AppConstants.separator);
		specs.append("# of Modules: ").append(numModules).append(AppConstants.separator);
		specs.append("MemoryType: ").append(memType).append(AppConstants.separator);	
		specs.append("MemorySpeed: ").append(memSpeed).append(AppConstants.megahertz);
		return specs.toString();
	}
}
