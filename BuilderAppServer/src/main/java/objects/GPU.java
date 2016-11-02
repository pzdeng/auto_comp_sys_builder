package main.java.objects;

import main.java.global.Constants;

public class GPU extends ComputerPart{
	public String branding;
	public String make;
	public int year;
	public float coreSpeed;
	public int coreCount;
	public String interfaceType;
	public int memSize;
	public int multiCardSupport;
	public int thermalRating;
	
	public GPU(){
		this.type = Constants.gpu;
	}
	
	public ComputerPartMin shortenSpecs(){
		ComputerPartMin part = new ComputerPartMin();
		part.type = type;
		part.name = name;
		part.url = productURL;
		part.picUrl = picURL;
		part.price = vendorPrice;
		part.specs = specBuilder();
		return part;
	}
	
	public String specBuilder(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(Constants.gigahertz).append(Constants.newLine);
		specs.append("Core Count: ").append(coreCount).append(Constants.newLine);
		specs.append("Video Memory: ").append(memSize).append(Constants.megabyte).append(Constants.newLine);
		specs.append("Year: ").append(year);
		return specs.toString();
	}
}
