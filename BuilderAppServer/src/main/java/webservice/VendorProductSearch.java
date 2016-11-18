package main.java.webservice;

import java.util.ArrayList;

import main.java.objects.ComputerPart;
import main.java.objects.ItemSearchExtract;

/**
 * Utility class to get product information from vendors
 * @author Peter
 *
 */
public class VendorProductSearch {
	private static String maxPrice = "100000";
	private static String minPrice = "5000";
	
	public static ComputerPart getProductInfo(ComputerPart part){
		ArrayList<ItemSearchExtract> productInfo = AmazonWebService.getSpecificItems(maxPrice, minPrice, part.productName, part.type);
		//Check if error occurred during call
		if(productInfo == null){
			System.out.println("Unable to get info on: " + part.productName);
			return part;
		}
		//Check if amazon produced no results
		if(productInfo.isEmpty()){
			if(part.productID == null || part.productID.isEmpty()){
				part.productID = "-";
			}
			part.vendorPrice = 0;
			part.picURL = "-";
			part.productURL = "-";	
		}
		else{
			//Get the first item match from productInfo list
			boolean foundMatch = false;
			for(int i = 0; i < productInfo.size(); i++){
				if(productInfo.get(i).itemName.contains(part.modelName)){
					part.productID = productInfo.get(i).itemPartID == null ? "-" : productInfo.get(i).itemPartID;
					part.vendorPrice = productInfo.get(i).itemPrice;
					part.picURL = productInfo.get(i).itemPicURL;
					part.productURL = productInfo.get(i).itemURL;
					foundMatch = true;
					break;
				}
			}
			if(!foundMatch){
				//Just get the first item from list
				part.productID = productInfo.get(0).itemPartID == null ? "-" : productInfo.get(0).itemPartID;
				part.vendorPrice = productInfo.get(0).itemPrice;
				part.picURL = productInfo.get(0).itemPicURL;
				part.productURL = productInfo.get(0).itemURL;
			}
		}
		System.out.println(part.id + " :: " + part.productName + " :: " + part.productID + " :: " + part.productURL );
		part.dirty = true;
		return part;
	}
}
