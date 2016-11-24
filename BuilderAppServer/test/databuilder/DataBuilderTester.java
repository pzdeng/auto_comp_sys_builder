package databuilder;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.DataBuilder;
import main.java.objects.CPU;
import main.java.objects.Disk;
import main.java.objects.GPU;
import main.java.objects.Memory;
import main.java.objects.Motherboard;
import main.java.objects.PSU;

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
    
    /**
     * Check all computer parts data
     */
    @Test
    public void testDataBuilder_Init(){
    	dataBuild.initAllData();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPUs: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPUs: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards: " + dataBuild.getMBList().size());
    	assertTrue(dataBuild.getMEMList().size() > 0);
    	System.out.println("Total # of Memory Units: " + dataBuild.getMEMList().size());
    	assertTrue(dataBuild.getPSUList().size() > 0);
    	System.out.println("Total # of PSUs: " + dataBuild.getPSUList().size());
    	assertTrue(dataBuild.getDISKList().size() > 0);
    	System.out.println("Total # of Disks: " + dataBuild.getDISKList().size());
    }
    
    /**
     * Check only valid computer parts data
     */
    @Test
    public void testDataBuilder_ValidInit(){
    	dataBuild.initValidComputerParts();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPUs: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPUs: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards: " + dataBuild.getMBList().size());
    	assertTrue(dataBuild.getMEMList().size() > 0);
    	System.out.println("Total # of Memory Units: " + dataBuild.getMEMList().size());
    	assertTrue(dataBuild.getPSUList().size() > 0);
    	System.out.println("Total # of PSUs: " + dataBuild.getPSUList().size());
    	assertTrue(dataBuild.getDISKList().size() > 0);
    	System.out.println("Total # of Disks: " + dataBuild.getDISKList().size());
    }
    
    //Update if productID is empty or null
    @Test
    public void testDataBuilder_Update(){
    	dataBuild.initAllData();
    	assertTrue(dataBuild.getCPUList().size() > 0);
    	System.out.println("Total # of CPU's: " + dataBuild.getCPUList().size());
    	assertTrue(dataBuild.getGPUList().size() > 0);
    	System.out.println("Total # of GPU's: " + dataBuild.getGPUList().size());
    	assertTrue(dataBuild.getMBList().size() > 0);
    	System.out.println("Total # of Motherboards's: " + dataBuild.getMBList().size());
    	assertTrue(dataBuild.getMEMList().size() > 0);
    	System.out.println("Total # of Memory Units: " + dataBuild.getMEMList().size());
    	assertTrue(dataBuild.getPSUList().size() > 0);
    	System.out.println("Total # of PSU: " + dataBuild.getPSUList().size());
    	dataBuild.updateCheckProductID();
    	System.out.println("Update Complete");
    }
    
    //Update if productID is some valid value
    @Test
    public void testDataBuilder_Update2(){
    	dataBuild.initAllData();
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
	
	@Test
	public void testLoad_Map_PSU_Data(){
		String productFile = new String("datasourceExtract" + File.separator + "PSUSimpleData.csv");
		String productFileCat = "HARDWAREINFO_PSU";
		dataBuild.addProductListings(productFile, productFileCat);
		for(PSU aPSU : dataBuild.getPSUList()){
			System.out.println(aPSU.toString());
			System.out.println(aPSU.dataContent());
		}
		
		System.out.println("Total Number of PSUs: " + dataBuild.getPSUList().size());
	}
	
	@Test
	public void testLoad_Map_Memory_Data(){
		String productFile = new String("datasourceExtract" + File.separator + "MemorySimpleData.csv");
		String productFileCat = "HARDWAREINFO_MEM";
		dataBuild.addProductListings(productFile, productFileCat);
		for(Memory aMem : dataBuild.getMEMList()){
			System.out.println(aMem.toString() + " :: " + aMem.modelName);
			System.out.println(aMem.dataContent());
		}
		
		System.out.println("Total Number of Memory products: " + dataBuild.getMEMList().size());
	}
	
	@Test
	public void testLoad_Map_Disk_Data(){
		String productFileHDD = new String("datasourceExtract" + File.separator + "HDDSimpleData.csv");
		String productFileCatHDD = "HARDWAREINFO_HDD";
		String productFileSSD = new String("datasourceExtract" + File.separator + "SSDSimpleData.csv");
		String productFileCatSSD = "HARDWAREINFO_SSD";
		dataBuild.addProductListings(productFileHDD, productFileCatHDD);
		dataBuild.addProductListings(productFileSSD, productFileCatSSD);
		for(Disk aDisk : dataBuild.getDISKList()){
			System.out.println(aDisk.toString() + " :: " + aDisk.modelName);
			System.out.println(aDisk.dataContent());
		}
		
		System.out.println("Total Number of disk products: " + dataBuild.getDISKList().size());
	}
}
