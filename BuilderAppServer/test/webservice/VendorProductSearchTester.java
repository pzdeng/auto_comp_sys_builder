package webservice;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import main.java.dao.MEMDao;
import main.java.dao.MEMDaoMySQLImpl;
import main.java.databuilder.DataBuilder;
import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.Disk;
import main.java.objects.ItemSearchExtract;
import main.java.objects.Memory;
import main.java.objects.PSU;
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
	
	@Test
	public void testDISKProductItemService(){
		DataBuilder dBuild = DataBuilder.getInstance();
		Disk temp;
		ArrayList<Disk> diskList = dBuild.getDISKList();
		int startOverride = 50;
		int rangeLimit = 70;
		for(int i = startOverride; i < diskList.size() && i < startOverride + rangeLimit; i++){
			if(diskList.get(i).productID == null || diskList.get(i).productID.isEmpty()){
				System.out.println(diskList.get(i).toString());
				temp = (Disk) VendorProductSearch.getProductInfo(diskList.get(i));
				
				//System.out.println(temp.dataContent());
			}
		}
	}
	
	@Test
	public void testPSUProductItemService(){
		DataBuilder dBuild = DataBuilder.getInstance();
		ArrayList<PSU> psuList = dBuild.getPSUList();
		PSU temp;
		int startOverride = 50;
		int rangeLimit = 70;
		for(int i = startOverride; i < psuList.size() && i < startOverride + rangeLimit; i++){
			if(psuList.get(i).productID == null || psuList.get(i).productID.isEmpty()){
				System.out.println(psuList.get(i).toString());
				temp = (PSU) VendorProductSearch.getProductInfo(psuList.get(i));
				
				//System.out.println(temp.dataContent());
			}
		}
	}
}
