package main.java.webservice;

import java.util.ArrayList;

import main.java.global.AppConstants;
import main.java.objects.ComputerPart;
import main.java.objects.Disk;
import main.java.objects.ItemSearchExtract;
import main.java.objects.Memory;
import main.java.objects.PSU;

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
		ArrayList<ItemSearchExtract> productInfo = null;
		
		//Memory products are a bit tricky, needs to be handled differently
		if(part.type.equals(AppConstants.memory)){
			productInfo = getMemoryProductInfo((Memory) part);
		}
		//Disk products may need name composure
		else if(part.type.equals(AppConstants.disk)){
			productInfo = getDiskProductInfo((Disk) part);
		}
		//PSU needs special characters handling
		else if(part.type.equals(AppConstants.psu)){
			productInfo = getPSUProductInfo((PSU) part);
		}
		else{
			productInfo = AmazonWebService.getSpecificItems(maxPrice, minPrice, part.productName, part.type);
		}
		
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
				if(part.titleCheck(productInfo.get(i).itemName)){
					System.out.println(part.productName + " == " + productInfo.get(i).itemName);
					part.productID = productInfo.get(i).itemPartID == null ? "-" : productInfo.get(i).itemPartID;
					part.vendorPrice = productInfo.get(i).itemPrice;
					part.picURL = productInfo.get(i).itemPicURL;
					part.productURL = productInfo.get(i).itemURL;
					foundMatch = true;
					break;
				}
				else{
					System.out.println(part.productName + " != " + productInfo.get(i).itemName);
				}
			}
			if(!foundMatch){
				//Empty contents
				part.productID = "-";
				part.vendorPrice = 0;
				part.picURL = "-";
				part.productURL = "-";	
			}
		}
		System.out.println(part.id + " :: " + part.productName + " :: " + part.productID + " :: " + part.productURL );
		part.dirty = true;
		return part;
	}
	
	private static ArrayList<ItemSearchExtract> getPSUProductInfo(PSU psuProduct) {
		String searchProductName;
		//We get a better match if we compose certain components for psu
		StringBuilder psuProductName = new StringBuilder();
		psuProductName.append(removeSpecialCharacters(psuProduct.productName));
		searchProductName = psuProductName.toString();
		System.out.println(searchProductName);
		
		return AmazonWebService.getSpecificItems(maxPrice, minPrice, searchProductName, psuProduct.type);
	}

	private static ArrayList<ItemSearchExtract> getDiskProductInfo(Disk diskProduct) {
		String searchProductName;
		//We get a better match if we compose certain components for disk
		StringBuilder diskProductName = new StringBuilder();
		diskProductName.append(diskProduct.make).append(space);
		diskProductName.append(diskProduct.modelName).append(space);
		diskProductName.append(diskProduct.capacity).append(AppConstants.gigabyte).append(space);
		if(diskProduct.type.equals(AppConstants.hdd)){
			diskProductName.append(diskProduct.rotationSpeed).append(AppConstants.rpm).append(space);
		}
		diskProductName.append(diskProduct.formFactor);
		searchProductName = diskProductName.toString();
		//System.out.println(searchProductName);
		
		return AmazonWebService.getSpecificItems(maxPrice, minPrice, searchProductName, diskProduct.type);
	}

	private static ArrayList<ItemSearchExtract> getMemoryProductInfo(Memory memProduct){
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
		
		return AmazonWebService.getSpecificItems(maxPrice, minPrice, searchProductName, memProduct.type);
	}
	
	private static String removeSpecialCharacters(String input){
		//Amazon doesn't like:
		// {'!'}
		return input.replace('!', ' ');
	}
	
	public static ComputerPart updateProductInfo(ComputerPart part){
		ArrayList<ItemSearchExtract> productInfo = AmazonWebService.getSpecificItems(maxPrice, minPrice, part.productID, part.type);
		
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
				if(part.titleCheck(productInfo.get(i).itemName)){
				//if((productInfo.get(i).itemPartID).equals(part.productID)){
					System.out.println(part.productName + " == " + productInfo.get(i).itemName);
					part.productID = productInfo.get(i).itemPartID == null ? "-" : productInfo.get(i).itemPartID;
					part.vendorPrice = productInfo.get(i).itemPrice;
					part.picURL = productInfo.get(i).itemPicURL;
					part.productURL = productInfo.get(i).itemURL;
					foundMatch = true;
					break;
				}
				else{
					System.out.println(part.productName + " != " + productInfo.get(i).itemName);
				}
			}
			if(!foundMatch){
				//Empty contents
				//part.productID = "-";
				part.vendorPrice = 0;
				part.picURL = "-";
				part.productURL = "-";	
			}
		}
		System.out.println(part.id + " :: " + part.productName + " :: " + part.productID + " :: " + part.productURL );
		part.dirty = true;
		return part;
	}
}
