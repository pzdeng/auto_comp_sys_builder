package main.java.databuilder;

import java.util.ArrayList;

import main.java.global.AppConstants;
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
	 * Method reads in data from some offline source and populate internal datastructure
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
		String similarGPUModel = null;
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
				cpuTemp.productID = products[i][1].trim();
				cpuTemp.make = products[i][2].trim();
				cpuTemp.productName = products[i][3].trim();
				cpuTemp.relativeRating = Float.parseFloat(products[i][4]);
				cpuTemp.benchScore = Float.parseFloat(products[i][5]);
				cpuList.add(cpuTemp);
				break;
			case "GPU":
				gpuTemp = new GPU();
				gpuTemp.productID = products[i][1].trim();
				gpuTemp.make = products[i][2].trim();
				gpuTemp.productName = products[i][3].trim();
				gpuTemp.relativeRating = Float.parseFloat(products[i][4]);
				gpuTemp.benchScore = Float.parseFloat(products[i][5]);
				//Check if partnumber column is empty
				if(gpuTemp.productID.length() == 0){
					//ASSUMPTION: Product name is also model type for this GPU
					//ASSUMPTION: Similar GPU models will be grouped together sequentially 
					similarGPUModel = products[i][3];
				}
				gpuTemp.modelName = similarGPUModel;
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

	/**
	 * This method populates the products hardware specifications
	 * @param file (CSV file)
	 * @param type (type of data source)
	 */
	public void autoMapHardwareSpecs(String file, String type){
		String[][] parsedFile = CSVReader.parseFile(file);
		switch(type){
		case "NVIDIA_GPU_SPECS_1":
			nvidiaGPUSpecs1Populate(parsedFile);
			break;
		case "AMD_GPU_SPECS_1":
			amdGPUSpecs1Populate(parsedFile);
			break;	
		default:
			//TODO
		}
	}
	
	private void amdGPUSpecs1Populate(String[][] parsedFile) {
		GPU temp;
		//Go through each item
		for(int i = 0; i < gpuList.size(); i++){
			for(int j = 0; j < parsedFile.length; j++){
				temp = gpuList.get(i);
				
				if((parsedFile[j][0]).contains(temp.modelName)){
					temp.coreSpeed = extractCoreSpeed(parsedFile[j][1]);
					temp.coreCount = extractCoreCount(parsedFile[j][6]);
					temp.memClockSpeed = extractCoreSpeed(parsedFile[j][3]);
					temp.interfaceType = parsedFile[j][8];
					gpuList.set(i, temp);
					break;
				}
			}
		}
	}

	private void nvidiaGPUSpecs1Populate(String[][] parsedFile) {
		GPU temp;
		//Go through each item
		for(int i = 0; i < gpuList.size(); i++){
			for(int j = 0; j < parsedFile.length; j++){
				temp = gpuList.get(i);
				
				if((parsedFile[j][0]).contains(temp.modelName)){
					temp.coreSpeed = extractCoreSpeed(parsedFile[j][1]);
					temp.coreCount = extractCoreCount(parsedFile[j][6]);
					temp.memClockSpeed = extractCoreSpeed(parsedFile[j][3]);
					temp.interfaceType = parsedFile[j][9];
					gpuList.set(i, temp);
					break;
				}
			}
		}
	}

	/**
	 * Method to extract core speed from string
	 * Format to MHz
	 * @param string
	 * @return
	 */
	private float extractCoreSpeed(String string) {
		float speed = 0;
		int index;
		//Expected format: Number Units OptionalText
		//Expected input examples: 250 Mhz or 1000 GHZ someText or 2.0 ghz
		String[] partition = string.split(" ");
		//First part is a number
		try{
			index = 0;
			speed = Float.parseFloat(partition[index]);
			index++;
			//if there are two reported speeds
			//expect some divider '\'
			if(partition[index].equals("\\")){
				index++;
				//take the higher speeds of the two
				float speed2 = Float.parseFloat(partition[index]);
				speed = speed > speed2 ? speed : speed2;
				index++;
			}
			//Convert to MHz
			if(partition[index].equalsIgnoreCase(AppConstants.gigahertz)){
				speed *= 1000;
			}
		}
		catch(Exception e){
			System.err.println("Unable to parse {" + string + "}");
		}
		
		return speed;
	}
	
	/**
	 * Method to extract core speed from string
	 * Format to MHz
	 * @param string
	 * @return
	 */
	private int extractCoreCount(String string) {
		int coreCount = 0;
		//Expected format: Number OptionalText
		//Expected input examples: 1 or 1024 someText
		String[] partition = string.split(" ");
		//First part is a number
		try{
			coreCount = Integer.parseInt(partition[0]);
			//TODO: if GPU has some weird configuration
		}
		catch(Exception e){
			System.err.println("Unable to parse {" + string + "}");
		}
		
		return coreCount;
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
