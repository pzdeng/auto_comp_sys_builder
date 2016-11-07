package main.java.databuilder;

import java.util.ArrayList;

import main.java.objects.CPU;
import main.java.objects.GPU;
import main.java.objects.Motherboard;

/**
 * Data builder features/responsibilities
 * 1.) Read data from database
 * 2.) Create (if not present) an entry in database for each product
 * 3.) Add (if not present) hardware specifications for each product
 * 4.) Add and Update pricing information for each product
 * @author Peter
 *
 */
public class DataBuilder {
	private ArrayList<CPU> cpuList;
	private ArrayList<GPU> gpuList;
	private ArrayList<Motherboard> mbList;
	
	private static DataBuilder dBuild;
	
	public static DataBuilder getInstance(){
		if(dBuild != null){
			return dBuild;
		}
		dBuild = new DataBuilder();
		return dBuild;
	}
	
	private DataBuilder(){
		cpuList = new ArrayList<CPU>();
		gpuList = new ArrayList<GPU>();
		mbList = new ArrayList<Motherboard>();
		initData();
	}
	
	/**
	 * Pull existing data from database
	 */
	public void initData(){
		//TODO populate cpu, gpu, motherboard list
	}
	
	/**
	 * For any entry marked as dirty, update database
	 */
	public void updateData(){
		//TODO scan each entry and issue update if dirty
	}
	
	/**
	 * Method reads in data from some source and populate internal datastructure
	 * @param file (CSV file)
	 * @param type (type of data source)
	 */
	public void addProductListings(String file, String type){
		String[][] parsedFile = CSVReader.parseFile(file);
		switch(type){
		case "USERBENCHMARK":
			userbenchPopulate(parsedFile);
			break;
		default:
			//TODO
		}
	}
	
	/**
	 * Method to handle userbenchmark data format
	 * @param parsedFile
	 */
	private void userbenchPopulate(String[][] products) {
		CPU cpuTemp;
		GPU gpuTemp;
		if(products == null){
			return;
		}
		//skip first row header
		for(int i = 1; i < products.length; i++){
			//CPU, GPU, SSD/HDD, RAM
			//TODO recognize laptop components
			switch(products[i][0]){
			case "CPU":
				cpuTemp = new CPU();
				cpuTemp.make = products[i][2];
				cpuTemp.name = products[i][3];
				cpuTemp.relativeRating = Float.parseFloat(products[i][4]);
				cpuTemp.consolidateRating = Float.parseFloat(products[i][5]);
				cpuList.add(cpuTemp);
				break;
			case "GPU":
				gpuTemp = new GPU();
				gpuTemp.make = products[i][2];
				gpuTemp.name = products[i][3];
				gpuTemp.relativeRating = Float.parseFloat(products[i][4]);
				gpuTemp.consolidateRating = Float.parseFloat(products[i][5]);
				gpuList.add(gpuTemp);
				break;
			case "SSD":
			case "HDD":
				break;
			case "RAM":
				break;
			default:
				//Unrecognized Type, do nothing
			}
		}
		
	}

	public void addHardwareSpecs(String file){
		
	}
	
	public ArrayList<CPU> getCPUList(){
		return cpuList;
	}
	
	public ArrayList<GPU> getGPUList(){
		return gpuList;
	}
	
	public ArrayList<Motherboard> getMBList(){
		return mbList;
	}
	
	public void clear(){
		cpuList = new ArrayList<CPU>();
		gpuList = new ArrayList<GPU>();
		mbList = new ArrayList<Motherboard>();
	}
}
