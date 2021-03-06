package main.java.objects;

import main.java.global.AppConstants;

public class GPU extends ComputerPart{
	public String branding;
	public float coreSpeed;
	public float memClockSpeed;
	public int coreCount;
	public String interfaceType;
	public int memSize;
	public int multiCardSupport;
	
	public GPU(){
		super();
		this.type = AppConstants.gpu;
	}
	
	//Populate this object with dummy data
	public void dummyPopulate(){
		productName = "Sapphire AMD Radeon HD 6850 1GB PCI-E Video Card (100315L)";
		productURL = "https://www.amazon.com/Sapphire-Radeon-6850-Video-100315L/dp/B0047ZGIUK";
		picURL = "https://images-na.ssl-images-amazon.com/images/I/41qu7WKjnBL.jpg";
		vendorPrice = (float) 60.00;
		coreSpeed = (float) 775;
		coreCount = 960;
		memSize = 1;
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
		specs.append("BenchScore (UserBenchmark): ").append(benchScore);
		return specs.toString();
	}
	
	public String dataContent(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(AppConstants.megahertz).append(AppConstants.separator);
		specs.append("Stream Processors: ").append(coreCount).append(AppConstants.separator);
		specs.append("Mem Speed: ").append(memClockSpeed).append(AppConstants.megahertz).append(AppConstants.separator);
		specs.append("Model Name: ").append(modelName).append(AppConstants.separator);
		specs.append("Power Wattage: ").append(powerRating).append(AppConstants.separator);
		specs.append("Year: ").append(year);
		return specs.toString();
	}
	
	@Override
	public boolean titleCheck(String productTitle) {
		productTitle = productTitle.toLowerCase();
		//Check make (GPU data bugged... slight mix up between make and brand; is part of product name)
		/*
		if(!productTitle.contains(make.toLowerCase())){
			return false;
		}
		*/
		//Check model name (in parts)
		String[] productNameParition = productName.toLowerCase().split(" ");
		boolean partialMatch = false;
		if(productNameParition.length > 0){
			for(String partition : productNameParition){
				if(!productTitle.contains(partition)){
					//partialMatch = true;
					System.out.println(partition);
					return false;
				}
			}
		}
		return true;
	}
}
