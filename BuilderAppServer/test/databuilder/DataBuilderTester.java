package databuilder;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.DataBuilder;
import main.java.objects.CPU;
import main.java.objects.GPU;

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
    public void testLoad_Map_GPU_Data() {
		int popGPU = 0;
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String techPoweredUpGPUFile = new String("datasourceExtract" + File.separator + "techPoweredUpGPU.csv");
		String productFileCat = "USERBENCHMARK";
		String techPoweredUpCPUCat = "TECHPOWEREDUP_GPU";
		dataBuild.addProductListings(productFile, productFileCat);
		dataBuild.autoMapHardwareSpecs(techPoweredUpGPUFile, techPoweredUpCPUCat);
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
}
