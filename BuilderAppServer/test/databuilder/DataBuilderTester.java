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
		String fileDir = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String type = "USERBENCHMARK";
		dataBuild.addProductListings(fileDir, type);
		for(CPU aCPU : dataBuild.getCPUList()){
			System.out.println(aCPU.toString());
		}
		for(GPU aGPU : dataBuild.getGPUList()){
			System.out.println(aGPU.toString());
		}
	}
}
