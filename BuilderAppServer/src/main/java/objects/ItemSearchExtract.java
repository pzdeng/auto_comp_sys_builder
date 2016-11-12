package main.java.objects;

import main.java.global.AppConstants;

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
		out.append(itemName).append(AppConstants.separator);
		out.append(itemURL).append(AppConstants.separator);
		out.append(itemPicURL).append(AppConstants.separator);
		out.append(itemPrice).append(AppConstants.separator);
		out.append(itemPartID);
		
		return out.toString();
	}
}
