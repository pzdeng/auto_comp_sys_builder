package webservice;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import main.java.dao.MEMDao;
import main.java.dao.MEMDaoMySQLImpl;
import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ItemSearchExtract;
import main.java.objects.Memory;
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
	
	@Test
	public void testMemoryProductItemService(){
		MEMDao memDao = new MEMDaoMySQLImpl();
		String name = "Corsair Vengeance Black/Blue LED 32GB DDR4-3000 CL15 kit";
		Memory temp = null;
		try {
			temp = memDao.getMemoryByName(name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Memory result = (Memory) VendorProductSearch.getProductInfo(temp);
		System.out.println(result.toString());
		System.out.println(result.dataContent());
	}
}
