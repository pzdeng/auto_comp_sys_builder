package main.java.objects;

import java.util.List;

import main.java.global.Constants;

public class Motherboard extends ComputerPart{
	public String formFactor;
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

	//Populate this object with dummy data
	public void dummyPopulate(){
		name = "Gigabyte AM3+ AMD 970 SATA 6Gbps USB 3.0 ATX AM3+ Socket DDR3 1600 Motherboards (GA-970A-DS3P)";
		productURL = "https://www.amazon.com/Gigabyte-6Gbps-Socket-Motherboards-GA-970A-DS3P/dp/B00CX4MUCC";
		picURL = "https://images-na.ssl-images-amazon.com/images/I/91AIuBuQjuL._AC_UL115_.jpg";
		make = "GIGABYTE";
		vendorPrice = (float) 60.93;
		year = 2013;
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
