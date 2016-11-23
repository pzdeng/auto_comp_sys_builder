package main.java.databuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import main.java.dao.CPUDao;
import main.java.dao.CPUDaoMySQLImpl;
import main.java.dao.DISKDao;
import main.java.dao.DISKDaoMySQLImpl;
import main.java.dao.GPUDao;
import main.java.dao.GPUDaoMySQLImpl;
import main.java.dao.MBDao;
import main.java.dao.MBDaoMySQLImpl;
import main.java.dao.MEMDao;
import main.java.dao.MEMDaoMySQLImpl;
import main.java.dao.PSUDao;
import main.java.dao.PSUDaoMySQLImpl;
import main.java.global.AppConstants;
import main.java.objects.CPU;
import main.java.objects.ComputerPart;
import main.java.objects.Disk;
import main.java.objects.GPU;
import main.java.objects.Memory;
import main.java.objects.Motherboard;
import main.java.objects.PSU;
import main.java.objects.comparator.ModelNameComparator;
import main.java.webservice.VendorProductSearch;

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
	private ArrayList<Memory> memList;
	private ArrayList<PSU> psuList;
	private ArrayList<Disk> diskList;
	//Flag to determine if the data stored in this singleton is for building or data loading
	private boolean buildMode;
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
		psuList = new ArrayList<PSU>();
		memList = new ArrayList<Memory>();
		diskList = new ArrayList<Disk>();
	}
	
	/**
	 * Pull (all) existing data from database
	 * For purposes of bulk data loading/updating to database
	 * SHOULD ONLY BE AVAILABLE/CALLED IN TESTER
	 */
	public void initAllData(){
		if(buildMode){
			buildMode = false;
		}
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		MBDao mbDao = new MBDaoMySQLImpl();
		MEMDao memDao = new MEMDaoMySQLImpl();
		PSUDao psuDao = new PSUDaoMySQLImpl();
		DISKDao diskDao = new DISKDaoMySQLImpl();
		
		try {
			cpuList = cpuDao.getAllCPU();
			gpuList = gpuDao.getAllGPU();
			mbList = mbDao.getAllMotherboard();
			memList = memDao.getAllMemory();
			psuList = psuDao.getAllPSU();
			diskList = diskDao.getAllDisk();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Populate respective lists if database table is empty
		if(cpuList.isEmpty()){
			firstTimeLoadCPUData();
		}
		if(gpuList.isEmpty()){
			firstTimeLoadGPUData();
		}
		if(mbList.isEmpty()){
			firstTimeLoadMBData();
		}
		if(memList.isEmpty()){
			firstTimeLoadMEMData();
		}
		if(psuList.isEmpty()){
			firstTimeLoadPSUData();
		}
		if(diskList.isEmpty()){
			firstTimeLoadDISKData();
		}
	}
	
	/**
	 * Pull valid existing data from database
	 * For purposes of building computer
	 */
	public synchronized void initValidComputerParts(){
		if(buildMode && !cpuList.isEmpty()){
			//Expected data is loaded
			return;
		}
		buildMode = true;
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		MBDao mbDao = new MBDaoMySQLImpl();
		MEMDao memDao = new MEMDaoMySQLImpl();
		PSUDao psuDao = new PSUDaoMySQLImpl();
		DISKDao diskDao = new DISKDaoMySQLImpl();
		
		try {
			cpuList = cpuDao.getAllValidCPU();
			gpuList = gpuDao.getAllValidGPU();
			mbList = mbDao.getAllValidMotherboard();
			memList = memDao.getAllValidMemory();
			psuList = psuDao.getAllValidPSU();
			diskList = diskDao.getAllValidDisk();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void firstTimeLoadDISKData() {
		DISKDao diskDao = new DISKDaoMySQLImpl();
		String productFileHDD = new String("datasourceExtract" + File.separator + "HDDSimpleData.csv");
		String productFileCatHDD = "HARDWAREINFO_HDD";
		String productFileSSD = new String("datasourceExtract" + File.separator + "SSDSimpleData.csv");
		String productFileCatSSD = "HARDWAREINFO_SSD";
		addProductListings(productFileHDD, productFileCatHDD);
		addProductListings(productFileSSD, productFileCatSSD);
		try{
			diskDao.insertDisk(diskList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into disk table; Error: " + e);
		}
		//Refresh disk list, to get id and time stamps
		try{
			diskList = diskDao.getAllDisk();
		} catch(Exception e){
			System.err.println("Error getting disks, Error: " + e);
		}
	}

	private void firstTimeLoadPSUData() {
		PSUDao psuDao = new PSUDaoMySQLImpl();
		String productFile = new String("datasourceExtract" + File.separator + "PSUSimpleData.csv");
		String productFileCat = "HARDWAREINFO_PSU";
		addProductListings(productFile, productFileCat);
		try{
			psuDao.insertPSU(psuList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into psu table; Error: " + e);
		}
		//Refresh psu list, to get id and time stamps
		try{
			psuList = psuDao.getAllPSU();
		} catch(Exception e){
			System.err.println("Error getting psus, Error: " + e);
		}
	}

	private void firstTimeLoadMEMData() {
		MEMDao memDao = new MEMDaoMySQLImpl();
		String productFile = new String("datasourceExtract" + File.separator + "MemorySimpleData.csv");
		String productFileCat = "HARDWAREINFO_MEM";
		addProductListings(productFile, productFileCat);
		try{
			memDao.insertMemory(memList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into memory table; Error: " + e);
		}
		//Refresh memory list, to get id and time stamps
		try{
			memList = memDao.getAllMemory();
		} catch(Exception e){
			System.err.println("Error getting memory units, Error: " + e);
		}
	}

	private void firstTimeLoadMBData() {
		MBDao mbDao = new MBDaoMySQLImpl();
		String productFile = new String("datasourceExtract" + File.separator + "MoboSimpleData.csv");
		String productFileCat = "HARDWAREINFO_MB";
		addProductListings(productFile, productFileCat);
		try{
			mbDao.insertMotherboard(mbList);
		} catch(Exception e){
			System.err.println("Records cannot be inserted into motherboard table; Error: " + e);
		}
		//Refresh motherboard list, to get id and time stamps
		try{
			mbList = mbDao.getAllMotherboard();
		} catch(Exception e){
			System.err.println("Error getting motherboards', Error: " + e);
		}
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
	
	/**
	 * Additional graphic cards entries
	 * Assume that all of these records would be new
	 */
	public void additionalLoadGPUData(){
		//TODO: 
		//GPUDao gpuDao = new GPUDaoMySQLImpl();
		
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
	public void updateCPUData(){
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		ArrayList<CPU> dirtyCPU = new ArrayList<CPU>();
		for(int i = 0; i < cpuList.size(); i++){
			if(cpuList.get(i).dirty){
				dirtyCPU.add(cpuList.get(i));
				cpuList.get(i).dirty = false;
			}
		}
		try{
			cpuDao.updateVendorInfoCPU(dirtyCPU);			
		}
		catch(Exception e){
			System.err.println("Something bad happened during record updates, handle me... " + e);
		}
	}
	
	public void updateGPUData(){
		GPUDao gpuDao = new GPUDaoMySQLImpl();		
		ArrayList<GPU> dirtyGPU = new ArrayList<GPU>();
		for(int i = 0; i < gpuList.size(); i++){
			if(gpuList.get(i).dirty){
				dirtyGPU.add(gpuList.get(i));
				gpuList.get(i).dirty = false;
			}
		}
		try{
			gpuDao.updateVendorInfoGPU(dirtyGPU);
		}
		catch(Exception e){
			System.err.println("Something bad happened during gpu record updates, handle me... " + e);
		}
	}
	
	public void updateMBData(){
		MBDao mbDao = new MBDaoMySQLImpl();	
		ArrayList<Motherboard> dirtyMB = new ArrayList<Motherboard>();
		for(int i = 0; i < mbList.size(); i++){
			if(mbList.get(i).dirty){
				dirtyMB.add(mbList.get(i));
				mbList.get(i).dirty = false;
			}
		}
		try{
			mbDao.updateVendorInfoMotherboard(dirtyMB);
		}
		catch(Exception e){
			System.err.println("Something bad happened during motherboard record updates, handle me... " + e);
		}
	}
	
	public void updateMEMData(){
		MEMDao memDao = new MEMDaoMySQLImpl();	
		ArrayList<Memory> dirtyMEM = new ArrayList<Memory>();
		for(int i = 0; i < memList.size(); i++){
			if(memList.get(i).dirty){
				dirtyMEM.add(memList.get(i));
				memList.get(i).dirty = false;
			}
		}
		try{
			memDao.updateVendorInfoMemory(dirtyMEM);
		}
		catch(Exception e){
			System.err.println("Something bad happened during memory record updates, handle me... " + e);
		}
	}
	
	public void updateDISKData(){
		DISKDao memDao = new DISKDaoMySQLImpl();	
		ArrayList<Disk> dirtyDISK = new ArrayList<Disk>();
		for(int i = 0; i < diskList.size(); i++){
			if(diskList.get(i).dirty){
				dirtyDISK.add(diskList.get(i));
				diskList.get(i).dirty = false;
			}
		}
		try{
			memDao.updateVendorInfoDisk(dirtyDISK);
		}
		catch(Exception e){
			System.err.println("Something bad happened during disk record updates, handle me... " + e);
		}
	}
	
	public void updatePSUData(){
		PSUDao memDao = new PSUDaoMySQLImpl();	
		ArrayList<PSU> dirtyPSU = new ArrayList<PSU>();
		for(int i = 0; i < psuList.size(); i++){
			if(psuList.get(i).dirty){
				dirtyPSU.add(psuList.get(i));
				psuList.get(i).dirty = false;
			}
		}
		try{
			memDao.updateVendorInfoPSU(dirtyPSU);
		}
		catch(Exception e){
			System.err.println("Something bad happened during psu record updates, handle me... " + e);
		}
	}
	
	public void updateCheckProductID(){
		//For each computer part that has an empty productID, fetch information from Vendor (Amazon)
		for(int i = 0; i < cpuList.size(); i++){
			if(cpuList.get(i).productID == null || cpuList.get(i).productID.isEmpty()){
				cpuList.set(i, (CPU) VendorProductSearch.getProductInfo(cpuList.get(i)));
			}
		}
		updateCPUData();
		for(int i = 0; i < gpuList.size(); i++){
			if(gpuList.get(i).productID == null || gpuList.get(i).productID.isEmpty()){
				gpuList.set(i, (GPU) VendorProductSearch.getProductInfo(gpuList.get(i)));
			}
		}
		updateGPUData();
		for(int i = 0; i < mbList.size(); i++){
			if(mbList.get(i).productID == null || mbList.get(i).productID.isEmpty()){
				mbList.set(i, (Motherboard) VendorProductSearch.getProductInfo(mbList.get(i)));
			}
		}
		updateMBData();
		for(int i = 0; i < memList.size(); i++){
			if(memList.get(i).productID == null || memList.get(i).productID.isEmpty()){
				memList.set(i, (Memory) VendorProductSearch.getProductInfo(memList.get(i)));
			}
		}
		updateMEMData();
		
		for(int i = 0; i < diskList.size(); i++){
			if(diskList.get(i).productID == null || diskList.get(i).productID.isEmpty()){
				diskList.set(i, (Disk) VendorProductSearch.getProductInfo(diskList.get(i)));
			}
		}
		updateDISKData();
		
		for(int i = 0; i < psuList.size(); i++){
			if(psuList.get(i).productID == null || psuList.get(i).productID.isEmpty()){
				psuList.set(i, (PSU) VendorProductSearch.getProductInfo(psuList.get(i)));
			}
		}
		updatePSUData();
	}
	
	public void updateProductPricing(){
		//For each computer part that has some valid productID, fetch information from Vendor (Amazon)
		//TODO: due to slow nature of hitting amazon, do time checking
		ComputerPart temp;
		for(int i = 0; i < cpuList.size(); i++){
			if(cpuList.get(i).productID != null && !cpuList.get(i).productID.equals("-")){
				temp = VendorProductSearch.getProductInfo(cpuList.get(i));
				if(!temp.productID.equals("-")){
					cpuList.set(i, (CPU) temp);
				}
			}
		}
		updateCPUData();
		for(int i = 0; i < gpuList.size(); i++){
			if(gpuList.get(i).productID != null && !(gpuList.get(i).productID.equals("-") || gpuList.get(i).productID.equals("*"))){
				temp = VendorProductSearch.getProductInfo(gpuList.get(i));
				if(!temp.productID.equals("-")){
					gpuList.set(i, (GPU) temp);
				}
			}
		}
		updateGPUData();
		for(int i = 0; i < mbList.size(); i++){
			if(mbList.get(i).productID != null && !mbList.get(i).productID.equals("-")){
				temp = VendorProductSearch.getProductInfo(mbList.get(i));
				if(!temp.productID.equals("-")){
					mbList.set(i, (Motherboard) temp);
				}
			}
		}
		updateMBData();
		
		for(int i = 0; i < diskList.size(); i++){
			if(diskList.get(i).productID != null || !diskList.get(i).productID.equals("-")){
				temp = VendorProductSearch.getProductInfo(diskList.get(i));
				if(!temp.productID.equals("-")){
					diskList.set(i, (Disk) temp);
				}
			}
		}
		updateDISKData();
		
		for(int i = 0; i < psuList.size(); i++){
			if(psuList.get(i).productID != null || !psuList.get(i).productID.equals("-")){
				temp = VendorProductSearch.getProductInfo(psuList.get(i));
				if(!temp.productID.equals("-")){
					psuList.set(i, (PSU) temp);
				}
			}
		}
		updatePSUData();
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
		case "HARDWAREINFO_MEM":
			hardwareInfoMEMPopulate(parsedFile);
			break;
		case "HARDWAREINFO_PSU":
			hardwareInfoPSUPopulate(parsedFile);
			break;
		case "HARDWAREINFO_HDD":
			hardwareInfoHDDPopulate(parsedFile);
			break;
		case "HARDWAREINFO_SSD":
			hardwareInfoSSDPopulate(parsedFile);
			break;
		}
	}
	
	private void hardwareInfoSSDPopulate(String[][] parsedFile) {
		Disk temp;
		String middlePortion;
		String[] fullProductName;
		//Skip header line
		for(int i = 1; i < parsedFile.length; i++){
			temp = new Disk();
			temp.productName = parsedFile[i][0];
			//Extract Make/Brand
			temp.make = extractMultiWordBrand(temp.productName);
			//extract the middle portion (stop at 12GB or DDR type)
			middlePortion = "";
			fullProductName = temp.productName.substring(temp.make.length()).split(" ");
			for(int j = 0; j < fullProductName.length; j++){
				if(!(fullProductName[j].contains(AppConstants.terabyte) || 
						fullProductName[j].contains(AppConstants.gigabyte))){
					middlePortion += fullProductName[j] + " ";
				}
				else{
					break;
				}
			}
			temp.modelName = middlePortion.trim();
			temp.diskType = AppConstants.ssd;
			temp.capacity = extractStructuredValue(parsedFile[i][2]);
			temp.readSpeed = extractStructuredValue(parsedFile[i][4]);
			temp.writeSpeed = extractStructuredValue(parsedFile[i][5]);
			temp.interfaceType = parsedFile[i][3];
			temp.formFactor = parsedFile[i][6];
			diskList.add(temp);
		}
	}

	private void hardwareInfoHDDPopulate(String[][] parsedFile) {
		Disk temp;
		String middlePortion;
		String[] fullProductName;
		//Skip header line
		for(int i = 1; i < parsedFile.length; i++){
			temp = new Disk();
			temp.productName = parsedFile[i][0];
			//Extract Make/Brand
			temp.make = extractMultiWordBrand(temp.productName);
			//extract the middle portion (stop at 12GB or DDR type)
			middlePortion = "";
			fullProductName = temp.productName.substring(temp.make.length()).split(" ");
			for(int j = 0; j < fullProductName.length; j++){
				if(!(fullProductName[j].contains(AppConstants.terabyte) || 
						fullProductName[j].contains(AppConstants.gigabyte))){
					middlePortion += fullProductName[j] + " ";
				}
				else{
					break;
				}
			}
			temp.modelName = middlePortion.trim();
			temp.diskType = AppConstants.hdd;
			temp.capacity = extractStructuredValue(parsedFile[i][2]);
			temp.rotationSpeed = extractStructuredValue(parsedFile[i][3]);
			temp.interfaceType = parsedFile[i][4];
			temp.formFactor = parsedFile[i][5];
			diskList.add(temp);
		}
	}

	private void hardwareInfoPSUPopulate(String[][] parsedFile) {
		PSU temp;
		String middlePortion;
		String[] fullProductName;
		//Skip header line
		for(int i = 1; i < parsedFile.length; i++){
			temp = new PSU();
			temp.productName = parsedFile[i][0];
			//Extract Make/Brand
			temp.make = extractMultiWordBrand(temp.productName);
			//extract the middle portion (stop at 200W value type)
			middlePortion = "";
			fullProductName = temp.productName.substring(temp.make.length()).split(" ");
			for(int j = 0; j < fullProductName.length; j++){
				if(!(isNumeric(fullProductName[j]))){
					middlePortion += fullProductName[j] + " ";
				}
				else{
					break;
				}
			}
			temp.modelName = middlePortion.trim();
			//Piggy back on TDP value extraction
			temp.powerWattage = extractTDP(parsedFile[i][1]);
			temp.efficiency = parsedFile[i][2];
			psuList.add(temp);
		}
	}
	
	/**
	 * Checker to see if the str is some measured value
	 * Like: 200W
	 * @param str
	 * @return
	 */
	private boolean isNumeric(String str){
		if(str.contains(AppConstants.wattage)){
			try{
				Integer.parseInt(str.substring(0, str.length() - 1));
				return true;
			}
			catch(Exception e){
				return false;
			}
		}
		return false;
	}

	private String extractMultiWordBrand(String productName) {
		//Check if brand consist of multiple words
		for(String brand : AppConstants.multiWordBrand){
			if(productName.contains(brand)){
				return brand;
			}
		}
		//otherwise return first word in productName
		return productName.split(" ")[0];
	}

	private void hardwareInfoMEMPopulate(String[][] parsedFile) {
		Memory temp;
		String[] fullProductName;
		String middlePortion;
		//Skip header line
		for(int i = 1; i < parsedFile.length; i++){
			temp = new Memory();
			temp.productName = parsedFile[i][0];
			fullProductName = temp.productName.split(" ");
			//Extract Make/Brand
			//Assume it is the first word from product name
			temp.make = fullProductName[0];
			//model name is the memory series name
			//extract the middle portion (stop at 12GB or DDR type)
			middlePortion = "";
			for(int j = 1; j < fullProductName.length; j++){
				if(!(fullProductName[j].contains("GB") || fullProductName[j].contains("DDR"))){
					middlePortion += fullProductName[j] + " ";
				}
				else{
					break;
				}
			}
			temp.modelName = middlePortion.trim();
			temp.totalCapacity = extractStructuredValue(parsedFile[i][1]);
			temp.numModules = extractNumModules(parsedFile[i][2]);
			temp.memType = parsedFile[i][3];
			//Piggy back method to extract memory speed in MHz
			temp.memSpeed = extractCoreSpeed(parsedFile[i][4]);
			memList.add(temp);
		}
	}

	/**
	 * Helper method to extract numerical values from below similar formats (separated by spaces)
	 * Expected format: number GB or number rpm
	 * Expected input examples: 2 GB or 50 GB or 125 GB or 5200 rpm
	 * @param string
	 * @return
	 */
	private int extractStructuredValue(String string) {
		if(string.length() == 0 || string.split(" ").length == 0){
			return 0;
		}
		return Integer.parseInt(string.split(" ")[0]);
	}

	private int extractNumModules(String string) {
		//Expected format: number x
		//Expected input examples: 2 x or 4 x or 8 x
		//ASSUME single digit value
		return Integer.parseInt(string.substring(0, 1));
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
			temp.modelName = temp.productName.substring(temp.make.length()).trim();
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
		//HardwareInfo follows "65 W" example
		String[] temp = string.split(" ");
		if(temp.length == 1){
			try{
				return Integer.parseInt(string.substring(0, string.length() - 1));
			}
			catch(Exception e){
				System.err.println("Unable to parse {" + string + "}");
			}
		}
		else{
			return Integer.parseInt(temp[0]);
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
		psuList = new ArrayList<PSU>();
		memList = new ArrayList<Memory>();
	}

	public ArrayList<PSU> getPSUList() {
		return psuList;
	}
	
	public ArrayList<Memory> getMEMList() {
		return memList;
	}

	public ArrayList<Disk> getDISKList() {
		if(diskList == null || diskList.isEmpty()){
			try {
				diskList = new DISKDaoMySQLImpl().getAllDisk();
			} catch (SQLException e) {
				//do nothing for now
			}
		}
		return diskList;
	}
}
