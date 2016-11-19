package main.java.webservice;

import java.util.ArrayList;

import main.java.global.AppConstants;
import main.java.objects.ComputerPart;
import main.java.objects.ItemSearchExtract;
import main.java.objects.Memory;

/**
 * Utility class to get product information from vendors
 * @author Peter
 *
 */
public class VendorProductSearch {
	private static String maxPrice = "100000";
	private static String minPrice = "5000";
	private static final String space = " ";
	
	public static ComputerPart getProductInfo(ComputerPart part){
		//Memory products are a bit tricky, needs to be handled differently
		if(part.type.equals(AppConstants.memory)){
			return getMemoryProductInfo((Memory) part);
		}
		
		ArrayList<ItemSearchExtract> productInfo = AmazonWebService.getSpecificItems(
				maxPrice, minPrice, part.productName, part.type);
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
	
	private static ComputerPart getMemoryProductInfo(Memory memProduct){
		String searchProductName;
		//We get a better match if we compose components for memory
		StringBuilder memProductName = new StringBuilder();
		memProductName.append(memProduct.make).append(space);
		memProductName.append(memProduct.modelName).append(space);
		memProductName.append(memProduct.totalCapacity).append(AppConstants.gigabyte).append(space);
		//Adding in string like "2x4GB"
		memProductName.append(memProduct.numModules).append("x").append(
				memProduct.totalCapacity / memProduct.numModules).append(AppConstants.gigabyte).append(space);
		memProductName.append(memProduct.memType).append(space);
		memProductName.append((int) memProduct.memSpeed);
		searchProductName = memProductName.toString();
		//System.out.println(searchProductName);
		
		ArrayList<ItemSearchExtract> productInfo = AmazonWebService.getSpecificItems(
				maxPrice, minPrice, searchProductName, memProduct.type);
		//Check if amazon produced no results
		if(productInfo == null || productInfo.isEmpty()){
			if(memProduct.productID == null || memProduct.productID.isEmpty()){
				memProduct.productID = "-";
			}
			memProduct.vendorPrice = 0;
			memProduct.picURL = "-";
			memProduct.productURL = "-";	
		}
		else{
			//Get the first item match from productInfo list
			boolean foundMatch = false;
			for(int i = 0; i < productInfo.size(); i++){
				if(memProduct.titleCheck(productInfo.get(i).itemName)){
					//System.out.println(memProduct.productName + " == " + productInfo.get(i).itemName);
					memProduct.productID = productInfo.get(i).itemPartID == null ? "-" : productInfo.get(i).itemPartID;
					memProduct.vendorPrice = productInfo.get(i).itemPrice;
					memProduct.picURL = productInfo.get(i).itemPicURL;
					memProduct.productURL = productInfo.get(i).itemURL;
					foundMatch = true;
					break;
				}
				/*
				else{
					System.out.println(memProduct.productName + " != " + productInfo.get(i).itemName);
				}
				*/
			}
			if(!foundMatch){
				//Just get the first item from list
				memProduct.productID = productInfo.get(0).itemPartID == null ? "-" : productInfo.get(0).itemPartID;
				memProduct.vendorPrice = productInfo.get(0).itemPrice;
				memProduct.picURL = productInfo.get(0).itemPicURL;
				memProduct.productURL = productInfo.get(0).itemURL;
			}
		}
		System.out.println(memProduct.id + " :: " + memProduct.productName + " :: " + memProduct.productID + " :: " + memProduct.productURL );
		memProduct.dirty = true;
		return memProduct;
	}
}
