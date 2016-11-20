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
}
