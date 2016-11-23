package main.java.objects;

import main.java.global.AppConstants;

public class Disk extends ComputerPart{
	public String diskType;
	public int capacity;
	public int rotationSpeed;
	public int readSpeed;
	public int writeSpeed;
	public String interfaceType;
	public String formFactor;
	
	public Disk(){
		super();
		type = AppConstants.disk;
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
		specs.append("DiskType: ").append(diskType).append(AppConstants.separator);
		specs.append("Capacity: ").append(capacity).append(AppConstants.gigabyte).append(AppConstants.separator);
		if(diskType.equals(AppConstants.hdd)){
			specs.append("RotationSpeed: ").append(rotationSpeed).append(AppConstants.rpm).append(AppConstants.separator);
		}
		if(diskType.equals(AppConstants.ssd)){
			specs.append("ReadSpeed: ").append(readSpeed).append(AppConstants.megabyte).append(AppConstants.perSecond).append(AppConstants.separator);
			specs.append("WriteSpeed: ").append(writeSpeed).append(AppConstants.megabyte).append(AppConstants.perSecond).append(AppConstants.separator);
		}
		specs.append("Interface: ").append(interfaceType).append(AppConstants.separator);
		specs.append("FormFactor: ").append(formFactor).append(AppConstants.separator);
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
		//Check capacity
		boolean capacityCheck = true;
		if(!(productTitle.contains((capacity + AppConstants.gigabyte).toLowerCase()) ||
				productTitle.contains((capacity + " " + AppConstants.gigabyte).toLowerCase()))){
			capacityCheck = false;
		}
		//Check TB
		if(capacity > 1000 && !capacityCheck){
			if(!(productTitle.contains((capacity/1000 + AppConstants.terabyte).toLowerCase()) ||
					productTitle.contains((capacity/1000 + " " + AppConstants.terabyte).toLowerCase()))){
				return false;
			}
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
