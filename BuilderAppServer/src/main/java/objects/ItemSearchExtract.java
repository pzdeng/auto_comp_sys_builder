package main.java.objects;

import main.java.global.Constants;

/**
 * Data class that holds information from Amazon ItemSearch service call
 * @author Peter
 *
 */
public class ItemSearchExtract {
	public String itemName;
	public String itemURL;
	public String itemPicURL;
	public float itemPrice;
	public String itemPartID;
	
	public ItemSearchExtract(){
		
	}
	
	public String toString(){
		StringBuilder out = new StringBuilder();
		out.append(itemName).append(Constants.separator);
		out.append(itemURL).append(Constants.separator);
		out.append(itemPicURL).append(Constants.separator);
		out.append(itemPrice).append(Constants.separator);
		out.append(itemPartID);
		
		return out.toString();
	}
}
