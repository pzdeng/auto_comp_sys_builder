package main.java.objects;

import main.java.global.Constants;

public class GPU extends ComputerPart{
	public String branding;
	public float coreSpeed;
	public float memClockSpeed;
	public int coreCount;
	public String interfaceType;
	public int memSize;
	public int multiCardSupport;
	public int thermalRating;
	
	public GPU(){
		this.type = Constants.gpu;
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
		part.price = vendorPrice;
		part.specs = specBuilder();
		return part;
	}
	
	public String specBuilder(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(Constants.megahertz).append(Constants.newLine);
		specs.append("Stream Processors: ").append(coreCount).append(Constants.newLine);
		specs.append("Video Memory: ").append(memSize).append(Constants.megabyte).append(Constants.newLine);
		specs.append("Year: ").append(year);
		return specs.toString();
	}
	
	public String dataContent(){
		StringBuilder specs = new StringBuilder();
		specs.append("Clock Speed: ").append(coreSpeed).append(Constants.megahertz).append(Constants.separator);
		specs.append("Stream Processors: ").append(coreCount).append(Constants.separator);
		specs.append("Mem Speed: ").append(memClockSpeed).append(Constants.megahertz).append(Constants.separator);
		specs.append("Model Name: ").append(modelName);
		return specs.toString();
	}
}
