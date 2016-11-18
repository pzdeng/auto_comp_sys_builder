package webservice;

import java.util.ArrayList;

import org.junit.Test;

import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ItemSearchExtract;
import main.java.webservice.AmazonWebService;
import main.java.webservice.VendorProductSearch;

public class VendorProductSearchTester {
	@Test
	public void testCPUProductItemService(){
		CPU temp = new CPU();
		temp.modelName = "Pentium E5300";
		temp.productName = temp.modelName;
		CPU result = (CPU) VendorProductSearch.getProductInfo(temp);
		System.out.println(result.productID + " :: " + result.productName);
	}
}
