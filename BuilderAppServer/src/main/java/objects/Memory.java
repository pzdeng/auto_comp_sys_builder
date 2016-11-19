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
