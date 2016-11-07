package main.java.objects;

import java.sql.Timestamp;

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
	//Standard price
	public float msrpPrice;
	//Product resources (picture, link, and price)
	public String picURL;
	public String productURL;
	public float vendorPrice;
	//Comparison metrics
	public float relativeRating;
	
	public abstract ComputerPartMin shortenSpecs();
	public abstract String specBuilder();
}
