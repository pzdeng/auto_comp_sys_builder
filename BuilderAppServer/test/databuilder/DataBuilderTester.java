package databuilder;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.DataBuilder;
import main.java.objects.CPU;
import main.java.objects.GPU;
import main.java.objects.Motherboard;

public class DataBuilderTester {
	private DataBuilder dataBuild;
	
    @Before
    public void setUp() throws Exception {
    	dataBuild = DataBuilder.getInstance();
    }
    
    @After
    public void tearDown() throws Exception {
    	dataBuild.clear();
    }
    
    
    @Test
    public void testDataBuilder_Init(){
    	dataBuild.initData();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPU's: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPU's: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards's: " + dataBuild.getMBList().size());
    }
    
    //Update if productID is empty or null
    @Test
    public void testDataBuilder_Update(){
    	dataBuild.initData();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPU's: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPU's: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards's: " + dataBuild.getMBList().size());
    	dataBuild.updateCheckProductID();
    	System.out.println("Update Complete");
    }
    
    //Update if productID is some valid value
    @Test
    public void testDataBuilder_Update2(){
    	dataBuild.initData();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPU's: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPU's: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards's: " + dataBuild.getMBList().size());
    	dataBuild.updateProductPricing();
    	System.out.println("Update Complete");
    }
    
	@Test
    public void testLoad_Map_GPU_Data() {
		int popGPU = 0;
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String techPoweredUpGPUFile = new String("datasourceExtract" + File.separator + "techPoweredUpGPU.csv");
		String gpuPowerFile = new String("datasourceExtract" + File.separator + "GPUTDP.csv");
		String productFileCat = "USERBENCHMARK";
		String techPoweredUpCPUCat = "TECHPOWEREDUP_GPU";
		String gpuPowerCat = "GPU_POWER";
		dataBuild.addProductListings(productFile, productFileCat);
		dataBuild.autoMapHardwareSpecs(techPoweredUpGPUFile, techPoweredUpCPUCat);
		dataBuild.autoMapHardwareSpecs(gpuPowerFile, gpuPowerCat);
		for(GPU aGPU : dataBuild.getGPUList()){
			System.out.println(aGPU.toString());
			System.out.println(aGPU.dataContent());
			if(aGPU.year > 0){
				popGPU++;
			}
		}
		
		System.out.println("Total Number of GPUs: " + dataBuild.getGPUList().size());
		System.out.println("Populated GPUs: " + popGPU);
	}
	
	@Test
    public void testLoad_Map_CPU_Data() {
		int popCPU = 0;
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String techPoweredUpCPUFile = new String("datasourceExtract" + File.separator + "techPoweredUpCPU.csv");
		String productFileCat = "USERBENCHMARK";
		String techPoweredUpCPUFileCat = "TECHPOWEREDUP_CPU";
		dataBuild.addProductListings(productFile, productFileCat);
		dataBuild.autoMapHardwareSpecs(techPoweredUpCPUFile, techPoweredUpCPUFileCat);
		for(CPU aCPU : dataBuild.getCPUList()){
			System.out.println(aCPU.toString());
			System.out.println(aCPU.dataContent());
			if(aCPU.year > 0){
				popCPU++;
			}
		}
		
		System.out.println("Total Number of CPUs: " + dataBuild.getCPUList().size());
		System.out.println("Populated CPUs: " + popCPU);
	}
	
	@Test
	public void testLoad_Map_MB_Data(){
		String productFile = new String("datasourceExtract" + File.separator + "MoboSimpleData.csv");
		String productFileCat = "HARDWAREINFO_MB";
		dataBuild.addProductListings(productFile, productFileCat);
		for(Motherboard aMB : dataBuild.getMBList()){
			System.out.println(aMB.toString());
			System.out.println(aMB.dataContent());
		}
		
		System.out.println("Total Number of Motherboards: " + dataBuild.getMBList().size());
	}
}
