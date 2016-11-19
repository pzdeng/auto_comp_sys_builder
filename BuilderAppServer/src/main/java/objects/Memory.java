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
	public ComputerPartMin shortenSpecs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String specBuilder() {
		// TODO Auto-generated method stub
		return null;
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
