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
    public void testReadUserBenchMarkDS() {
		int popGPU = 0;
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String nvidiaGPUFile = new String("datasourceExtract" + File.separator + "BaseGPUListNVIDIA.csv");
		String amdGPUFile = new String("datasourceExtract" + File.separator + "BaseGPUListAMD.csv");
		String productFileType = "USERBENCHMARK";
		String nvidiaGPUFileType = "NVIDIA_GPU_SPECS_1";
		String amdGPUFileType = "AMD_GPU_SPECS_1";
		dataBuild.addProductListings(productFile, productFileType);
		dataBuild.autoMapHardwareSpecs(nvidiaGPUFile, nvidiaGPUFileType);
		dataBuild.autoMapHardwareSpecs(amdGPUFile, amdGPUFileType);
		for(CPU aCPU : dataBuild.getCPUList()){
			System.out.println(aCPU.toString());
		}
		for(GPU aGPU : dataBuild.getGPUList()){
			System.out.println(aGPU.toString());
			System.out.println(aGPU.dataContent());
			if(aGPU.coreSpeed > 0){
				popGPU++;
			}
		}
		
		System.out.println("Total Number of GPUs: " + dataBuild.getGPUList().size());
		System.out.println("Populated GPUs: " + popGPU);
	}
}
