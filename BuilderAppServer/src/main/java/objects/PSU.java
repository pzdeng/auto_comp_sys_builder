package main.java.objects;

import main.java.global.AppConstants;

public class PSU extends ComputerPart{
	public int powerWattage;
	public String efficiency;
	
	public PSU(){
		super();
		type = AppConstants.psu;
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
		specs.append("PowerWattage: ").append(powerWattage).append(AppConstants.separator);
		specs.append("EfficiencyRating: ");	
		return specs.toString();
	}
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
				if(productTitle.contains(partition)){
					partialMatch = true;
				}
			}
		}
		if(!partialMatch){
			return false;
		}
		//Check power
		if(!(productTitle.contains((powerWattage + AppConstants.wattage).toLowerCase()) ||
				productTitle.contains((powerWattage + " " + AppConstants.wattage).toLowerCase()))){
			return false;
		}
		
		//Check proper interface
		/*
		if(interfaceType.equals(AppConstants.sata2)){
			if(productTitle){
				return false;
			}
		}
		else if(interfaceType.equals(AppConstants.sata3)){
			
		}
		*/
		return true;
	}
}
