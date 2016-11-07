package main.java.objects;

import java.nio.charset.Charset;
import java.sql.Timestamp;

import main.java.global.Constants;

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
	//Computer component name
	public String name;
	//Computer component make
	public String make;
	//Computer component year
	public int year;
	//Standard price
	public float msrpPrice;
	//Product resources (picture, link, and price)
	public String picURL;
	public String productURL;
	public float vendorPrice;
	//Comparison metrics
	public float relativeRating;
	public float consolidateRating;
	
	public abstract ComputerPartMin shortenSpecs();
	public abstract String specBuilder();
	
	//Print out basic details for this part
	public String toString(){
		int makeLimit, nameLimit;
		makeLimit = 7;
		nameLimit = 40;
		String printMake = make;
		if(printMake.length() > makeLimit){
			printMake = printMake.substring(0, makeLimit);
		}
		String printName = name;
		if(printName.length() > nameLimit){
			printName = printName.substring(0, nameLimit);
		}
		else if(printName.length() < nameLimit){
			String formatTarget = "%-" + (nameLimit - printName.length()) + "s";
    		printName = String.format(formatTarget, printName, Charset.forName("UTF8"));
		}
		StringBuilder out = new StringBuilder();
		out.append(type).append(" :: ").append(printMake).append(",").append(Constants.tab).append(printName);
		return out.toString();
	}
}
