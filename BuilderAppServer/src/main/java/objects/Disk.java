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
	
	//For quick add
	public Disk(String type){
		super();
		type = AppConstants.disk;
		diskType = type;
	}
	
	/**
	 * Price per gigabyte
	 */
	@Override
	public void computePricePerPoint(){
		if(capacity > 0){
			pricePerPoint = (float) (vendorPrice / capacity);
		}
		else{
			pricePerPoint = 0;
		}
	}
	
	@Override
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

	@Override
	public String specBuilder() {
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
	
	@Override
	public int getPowerUsage(){
		if(diskType.equals(AppConstants.ssd)){
			//SSD rated around 3W
			return 3;
		}
		else{
			if(formFactor.contains("2.5")){
				//HDD 2.5 inch are rated around 3W
				return 3;
			}
			if(formFactor.contains("3.5")){
				//HDD 3.5 inch are rated around 10W
				return 10;
			}
		}
		//If no disk type specified return max wattage for disk
		return 10;
	}
}
