package main.java.objects;

import java.util.List;

import main.java.global.Constants;

public class Motherboard extends ComputerPart{
	public String make;
	public String formFactor;
	public int year;
	public String socketType;
	public String memType;
	public int memMaxSize;
	public int memSlot;
	public int sataNum;
	public List<String> interfaceList;
	public int thermalRating;
	
	public Motherboard(){
		type = Constants.mobo;
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
		specs.append("Make: ").append(make).append(Constants.newLine);
		specs.append("Year: ").append(year);
		return specs.toString();
	}
}
