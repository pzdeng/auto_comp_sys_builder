package main.java.databuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import main.java.dao.CPUDao;
import main.java.dao.CPUDaoMySQLImpl;
import main.java.dao.GPUDao;
import main.java.dao.GPUDaoMySQLImpl;
import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ComputerPart;
import main.java.objects.GPU;
import main.java.objects.Motherboard;
import main.java.objects.comparator.ModelNameComparator;

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
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		try {
			cpuList = cpuDao.getAllCPU();
			gpuList = gpuDao.getAllGPU();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cpuList.isEmpty()){
			firstTimeLoadCPUData();
		}
		if(gpuList.isEmpty()){
			firstTimeLoadGPUData();
		}
		/*
		if(mbList.isEmpty()){
			firstTimeLoadMBData();
		}
		*/
		//CPUDao cpuDao = new CPUDaoMySQLImpl();
		//cpuList = cpuDao.getAllCPU();
		//CPUDao cpuDao = new CPUDaoMySQLImpl();
		//cpuList = cpuDao.getAllCPU();
		//TODO populate cpu, gpu, motherboard list
	}
	
	private void firstTimeLoadMBData() {
		String productFile = new String("datasourceExtract" + File.separator + "MoboSimpleData.csv");
		String productFileCat = "HARDWAREINFO_MB";
		addProductListings(productFile, productFileCat);
		//TODO DAO object
	}

	private void firstTimeLoadGPUData() {
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String techPoweredUpCPUFile = new String("datasourceExtract" + File.separator + "techPoweredUpCPU.csv");
		String productFileCat = "USERBENCHMARK";
		String techPoweredUpCPUFileCat = "TECHPOWEREDUP_CPU";
		addProductListings(productFile, productFileCat);
		autoMapHardwareSpecs(techPoweredUpCPUFile, techPoweredUpCPUFileCat);
		try{
			gpuDao.insertGPU(gpuList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into gpu table; Error: " + e);
		}

		//Refresh GPUlist, to get id and time stamps
		try{
			gpuList = gpuDao.getAllGPU();
		} catch(Exception e){
			System.err.println("Error getting GPU's, Error: " + e);
		}		
	}

	private void firstTimeLoadCPUData() {
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		String productFile = new String("datasourceExtract" + File.separator + "ALL_UserBenchmarks.csv");
		String techPoweredUpCPUFile = new String("datasourceExtract" + File.separator + "techPoweredUpCPU.csv");
		String productFileCat = "USERBENCHMARK";
		String techPoweredUpCPUFileCat = "TECHPOWEREDUP_CPU";
		addProductListings(productFile, productFileCat);
		autoMapHardwareSpecs(techPoweredUpCPUFile, techPoweredUpCPUFileCat);
		try{
			cpuDao.insertCPU(cpuList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into cpu table; Error: " + e);
		}

		//Refresh CPUlist, to get id and time stamps
		try{
			cpuList = cpuDao.getAllCPU();
		} catch(Exception e){
			System.err.println("Error getting CPU's, Error: " + e);
		}	
	}

	/**
	 * For any entry marked as dirty, update database
	 */
	public void updateData(){
		//TODO scan each entry and issue update if dirty
	}
	
	public void initProductInfo(){
		
	}
	
	public void updateProductPricing(){
		
	}
	
	public void updateProductPricing(ComputerPart part){
		
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
		case "HARDWAREINFO_MB":
			hardwareInfoMBPopulate(parsedFile);
			break;
		}
	}
	
	/**
	 * Method to populate motherboard list from HardwareInfo datasource extract
	 * @param parsedFile
	 */
	private void hardwareInfoMBPopulate(String[][] parsedFile) {
		Motherboard temp;
		//Skip header line
		for(int i = 1; i < parsedFile.length; i++){
			temp = new Motherboard();
			temp.productName = parsedFile[i][0];
			//Extract Make/Brand
			//Assume it is the first word from product name
			temp.make = temp.productName.split(" ")[0];
			temp.formFactor = parsedFile[i][1];
			//Format: Socket socketType
			temp.socketType = parsedFile[i][2].split(" ")[1];
			temp.memType = parsedFile[i][4];
			//Captures number of Sata Ports capable of 6Gbps
			temp.sataNum = extractSataNum(parsedFile[i][5]);
			temp.memSlotNum = extractMemSlots(temp.memType, parsedFile[i][7], parsedFile[i][8]);
			temp.pciExpressX16Num = extractPCIESlots(parsedFile[i][6]);
			mbList.add(temp);
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
				cpuTemp.modelName = cpuTemp.productName;
				cpuTemp.relativeRating = Integer.parseInt(products[i][4]);
				cpuTemp.benchScore = Float.parseFloat(products[i][5]);
				cpuList.add(cpuTemp);
				break;
			case "GPU":
				gpuTemp = new GPU();
				gpuTemp.productID = products[i][1].trim();
				gpuTemp.make = products[i][2].trim();
				gpuTemp.productName = products[i][3].trim();
				gpuTemp.relativeRating = Integer.parseInt(products[i][4]);
				gpuTemp.benchScore = Float.parseFloat(products[i][5]);
				//Check if partnumber column is empty
				if(gpuTemp.productID.length() == 0){
					//ASSUMPTION: Product name is also model type for this GPU
					//ASSUMPTION: Similar GPU models will be grouped together sequentially 
					similarGPUModel = products[i][3];
					gpuTemp.productID = "*"; //Star indicates no single correspondence to a product
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
		case "TECHPOWEREDUP_CPU":
			techpoweredup_cpu(parsedFile);
			break;
		case "TECHPOWEREDUP_GPU":
			techpoweredup_gpu(parsedFile);
			break;	
		case "GPU_POWER":
			gpuPower(parsedFile);
			break;
		default:
			//TODO
		}
	}

	private void techpoweredup_cpu(String[][] parsedFile) {
		//Sort based on model name
		Collections.sort(cpuList, new ModelNameComparator());
		//Additional CPUs not captured with benchmark data
		ArrayList<CPU> extras = new ArrayList<CPU>();
		
		CPU temp;
		//Skip headers
		for(int i = 1; i < parsedFile.length; i++){
			temp = new CPU();
			temp.modelName = parsedFile[i][0].trim();
			int index = Collections.binarySearch(cpuList, temp, new ModelNameComparator());
			try{
				if(index >= 0){
					//replace temp with actual cpu from list
					temp = cpuList.get(index);
				}
				//Set in data for CPU
				temp.coreSpeed = extractCoreSpeed(parsedFile[i][6]);
				temp.coreCount = extractCoreCount(parsedFile[i][2], AppConstants.cpu);
				temp.socketType = parsedFile[i][4];
				temp.powerRating = extractTDP(parsedFile[i][10]);
				temp.year = extractYear(parsedFile[i][11]);
				
				if(index >= 0){
					//update entry in cpulist
					cpuList.set(index, temp);
				}
				else{
					//Add in the extra cpu
					temp.productName = temp.modelName;
					temp.make = parsedFile[i][3];
					if(extras.isEmpty()){
						extras.add(temp);
					}
					else if(!extras.get(extras.size() - 1).productName.equals(temp.productName)){
						extras.add(temp);
					}
				}
			} catch (Exception e){
				System.err.println("Error parsing data entry for {" + parsedFile[i][0]+ "}: " + e.getMessage());
			}
			
		}
		//Add in the extras to cpuList
		cpuList.addAll(extras);
	}

	private void techpoweredup_gpu(String[][] parsedFile) {		
		GPU temp;
		//Go through each item
		for(int i = 0; i < gpuList.size(); i++){
			for(int j = 0; j < parsedFile.length; j++){
				temp = gpuList.get(i);
				
				if((parsedFile[j][0]).contains(temp.modelName)){
					try{
						temp.coreSpeed = extractCoreSpeed(parsedFile[j][5]);
						temp.coreCount = extractCoreCount(parsedFile[j][7], AppConstants.gpu);
						temp.memClockSpeed = extractCoreSpeed(parsedFile[j][6]);
						temp.interfaceType = parsedFile[j][3];
						temp.year = extractYear(parsedFile[i][2]);
						gpuList.set(i, temp);
					} catch (Exception e){
						System.err.println("Error parsing data entry for {" + parsedFile[j][0]+ "}: " + e.getMessage());
					}
					break;
				}
			}
		}
	}
	
	private void gpuPower(String[][] parsedFile) {
		GPU temp;
		//Create map of gpu power: name | Wattage
		HashMap<String, String> gpuPower = new HashMap<String, String>();
		for(int i = 1; i < parsedFile.length; i++){
			gpuPower.put(parsedFile[i][0], parsedFile[i][1]);
		}
		//Go through each item
		for(int i = 0; i < gpuList.size(); i++){
			for(int j = 0; j < parsedFile.length; j++){
				temp = gpuList.get(i);
				
				if((parsedFile[j][0]).contains(temp.modelName)){
					try{
						temp.powerRating = Integer.parseInt(parsedFile[j][1].trim());
						gpuList.set(i, temp);
					} catch (Exception e){
						System.err.println("Error parsing data entry for {" + parsedFile[j][0]+ "}: " + e.getMessage());
					}
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
	private int extractCoreCount(String string, String type) {
		int coreCount = 0;
		//Expected format (CPU): Number OptionalText
		//Expected input examples (CPU): 1 or 1024 someText
		
		//Custom to TechPoweredUp (just take the first value)
		//Expected format (GPU): Shaders / TMUs / ROPs
		//Expected input examples (GPU): 1 / 1 / 2 / 3
		String[] partition;
		
		try{
			if(type.equals(AppConstants.cpu)){
				//TechPoweredUp delimiter "-"
				partition = string.split("-");
				coreCount = Integer.parseInt(partition[0]);
			}
			else if(type.equals(AppConstants.gpu)){
				partition = string.split("/");
				coreCount = Integer.parseInt(partition[0].trim());
			}
		}
		catch(Exception e){
			System.err.println("Unable to parse {" + string + "}");
		}
		
		return coreCount;
	}
	
	/**
	 * Method to extract TDP value
	 * @param string
	 * @return
	 */
	private int extractTDP(String string) {
		//Expected format: number sometext
		//Expected input examples: 65W or 65 w or 65Watts
		//TechPoweredUp follows "65W" example
		try{
			return Integer.parseInt(string.substring(0, string.length() - 1));
		}
		catch(Exception e){
			System.err.println("Unable to parse {" + string + "}");
		}
		
		return 0;
	}
	
	/**
	 * Method to extract Year
	 * @param string
	 * @return
	 */
	private int extractYear(String string) {
		//Expected format: some text year
		//Expected input examples: Mar-2013 or May 23 2011 or Jun2011 or Jan 2010
		//TechPoweredUp follows "Jan-10" example
		try{
			String year = string.substring(string.length() - 2);
			if(Integer.parseInt(year) > 70){
				year = "19" + year;
			}
			else{
				year = "20" + year;
			}
			return Integer.parseInt(year);
		} catch(Exception e){
			//Any exception will be treated as 0
		}
		return 0;
	}
	
	private int extractPCIESlots(String string) {
		try{
			string = string.trim();
			if(string.length() > 0){
				return Integer.parseInt(string); 
			}
		} catch(Exception e){
			//Any exception will be treated as 0
		}
		return 0;
	}

	/**
	 * Report the number of slots for the highest memory type supported
	 * @param memType
	 * @param ddr3Slots
	 * @param ddr4Slots
	 * @return
	 */
	private int extractMemSlots(String memType, String ddr3Slots, String ddr4Slots) {
		try{
			if(memType.contains(AppConstants.ddr4)){
				return Integer.parseInt(ddr4Slots);
			}
			if(memType.contains(AppConstants.ddr3)){
				return Integer.parseInt(ddr3Slots);
			}
		} catch(Exception e){
			//Any exception will be treated as 0
		}
		
		return 0;
	}

	private int extractSataNum(String string) {
		string = string.trim();
		if(string.length() > 0){
			return Integer.parseInt(string); 
		}
		return 0;
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
