package webservice;

import java.util.ArrayList;

import org.junit.Test;

import main.java.global.AppConstants;
import main.java.objects.ItemSearchExtract;
import main.java.webservice.AmazonWebService;

public class AmazonWebServiceTester {
	
	@Test
	public void testGoodWebServiceCall(){
		String maxPrice, minPrice, keywords, computerPartType;
		int pageLimit = 10;
		ArrayList<ItemSearchExtract> items;
		
		maxPrice = "50000";
		minPrice = "5000";
		keywords = "xeon";
		computerPartType = AppConstants.cpu;
		items = AmazonWebService.getGeneralItems(maxPrice,minPrice,pageLimit,keywords,computerPartType);
		for(ItemSearchExtract item : items){
			System.out.println(item.toString());
		}
	}
	
	@Test
	public void testGoodWebServiceCallDEBUG(){
		String maxPrice, minPrice, keywords, computerPartType;
		ArrayList<ItemSearchExtract> items;
		
		maxPrice = "50000";
		minPrice = "5000";
		keywords = "Pentium E5300";
		computerPartType = AppConstants.cpu;
		items = AmazonWebService.getSpecificItems(maxPrice,minPrice,keywords,computerPartType);
		for(ItemSearchExtract item : items){
			System.out.println(item.toString());
		}
	}
}
