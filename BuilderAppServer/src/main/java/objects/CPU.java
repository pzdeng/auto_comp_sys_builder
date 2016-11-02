package main.java.objects;

import main.java.global.Constants;

public class CPU extends ComputerPart{
	public String make;
	public int year;
	public float coreSpeed;
	public int coreCount;
	public String socketType;
	public int thermalRating;
	public int l1Size;
	public int l2Size;
	public int l3Size;
	
	public CPU(){
		this.type = Constants.cpu;
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
		specs.append("Year: ").append(year);
		return specs.toString();
	}
}
