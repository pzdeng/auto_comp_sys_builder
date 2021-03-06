package main.java.objects;

import main.java.global.AppConstants;

public class CPU extends ComputerPart{
	public float coreSpeed;
	public float coreTurboSpeed;
	public int coreCount;
	public String socketType;
	public int l1Size;
	public int l2Size;
	public int l3Size;
	
	public CPU(){
		super();
		this.type = AppConstants.cpu;
	}
	
	//Populate this object with dummy data
	public void dummyPopulate(){
		productName = "AMD Phenom 2 1100T";
		productURL = "https://www.amazon.com/AMD-Phenom-1100T-Processor-HDE00ZFBGRBOX/dp/B004DALW5K";
		picURL = "https://images-na.ssl-images-amazon.com/images/I/51Az8R3douL.jpg";
		vendorPrice = (float) 340.00;
		coreSpeed = (float) 3.3;
		coreCount = 6;
		year = 2010;
	}
	
	public ComputerPartMin shortenSpecs(){
		ComputerPartMin part = new ComputerPartMin();
		part.type = type;
		part.name = productName;
		part.url = productURL;
		part.picUrl = picURL;
		part.price = String.format("%.2f", vendorPrice);
		part.specs = specBuilder();
		return part;
	}
	
	public String specBuilder(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(AppConstants.megahertz).append(AppConstants.newLine);
		specs.append("Core Count: ").append(coreCount).append(AppConstants.newLine);
		specs.append("Socket Type: ").append(socketType).append(AppConstants.newLine);
		specs.append("Year: ").append(year).append(AppConstants.newLine);
		specs.append("BenchScore (UserBenchmark): ").append(benchScore);
		return specs.toString();
	}

	public String dataContent(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(AppConstants.megahertz).append(AppConstants.separator);
		specs.append("Core Count: ").append(coreCount).append(AppConstants.separator);
		specs.append("ThermalRating: ").append(powerRating).append(AppConstants.separator);
		specs.append("BenchScore: ").append(benchScore).append(AppConstants.separator);
		specs.append("Year: ").append(year);
		
		return specs.toString();
	}

	@Override
	public boolean titleCheck(String productTitle) {
		productTitle = productTitle.toLowerCase();
		//Check make
		if(!productTitle.contains(make.toLowerCase())){
			return false;
		}
		//Check model name (in parts)
		String[] modelNameParition = modelName.toLowerCase().split(" |\\-");
		if(modelNameParition.length > 0){
			//Require 100% match with modelName
			for(String partition : modelNameParition){
				if(!productTitle.contains(partition)){
					return false;
				}
			}
		}
		return true;
	}
}
