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
}
