package main.java.objects;

import java.nio.charset.Charset;
import java.sql.Timestamp;

import main.java.global.AppConstants;

/**
 * Superclass for computer parts
 * @author Peter
 *
 */
public abstract class ComputerPart {
	//Database information
	public int id;
	public Timestamp createTime;
	public Timestamp modifyTime;
	//If dirty, needs to update the database
	public boolean dirty;
	//Computer component type
	public String type;
	//Computer component product name/title
	public String productName;
	//Computer component product identification (unique)
	public String productID;
	//Computer component model name
	public String modelName;
	//Computer component make
	public String make;
	//Computer component year
	public int year;
	//Computer power requirement
	public int powerRating;
	//Standard price
	public float msrpPrice;
	//Product resources (picture, link, and price)
	public String picURL;
	public String productURL;
	public float vendorPrice;
	//Comparison metrics
	public int relativeRating;
	public float benchScore;
	public float pricePerPoint;
	
	public abstract ComputerPartMin shortenSpecs();
	public abstract String specBuilder();
	
	public int getPowerUsage(){
		return powerRating;
	}
	
	public void computePricePerPoint(){
		if(benchScore > 0){
			pricePerPoint = (float) vendorPrice / benchScore;
		}
		else{
			pricePerPoint = 0;
		}
	}
	
	public ComputerPart(){
		dirty = false;
	}
	
	//Print out basic details for this part
	public String toString(){
		int makeLimit, nameLimit;
		makeLimit = 10;
		nameLimit = 40;
		String printMake = make;
		if(printMake != null){
			if(printMake.length() > makeLimit){
				printMake = printMake.substring(0, makeLimit);
			}
		}
		String printName = productName;
		if(printName != null){
			if(printName.length() > nameLimit){
				printName = printName.substring(0, nameLimit);
			}
			else if(printName.length() < nameLimit){
				String formatTarget = "%-" + (nameLimit - printName.length()) + "s";
	    		printName = String.format(formatTarget, printName, Charset.forName("UTF8"));
			}
		}
		StringBuilder out = new StringBuilder();
		out.append(type).append(" :: ").append(printMake).append(",").append(AppConstants.tab).append(printName);
		return out.toString();
	}
	
	public abstract boolean titleCheck(String itemName);
}
