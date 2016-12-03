package main.java.databuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
 * Read data from database and hold it in memory
 * @author Peter
 *
 */
public class DataInitializer {
	private ArrayList<CPU> cpuList;
	private ArrayList<GPU> gpuList;
	private ArrayList<Motherboard> mbList;
	private ArrayList<Memory> memList;
	private ArrayList<PSU> psuList;
	private ArrayList<Disk> diskList;
	private static DataInitializer dInit;
	
	public static DataInitializer getInstance(){
		if(dInit != null){
			return dInit;
		}
		dInit = new DataInitializer();
		return dInit;
	}
	
	private DataInitializer(){
		cpuList = new ArrayList<CPU>();
		gpuList = new ArrayList<GPU>();
		mbList = new ArrayList<Motherboard>();
		psuList = new ArrayList<PSU>();
		memList = new ArrayList<Memory>();
		diskList = new ArrayList<Disk>();
	}
	
	/**
	 * Pull valid existing data from database
	 * For purposes of building computer
	 */
	public synchronized void initValidComputerParts(){
		if(!cpuList.isEmpty()){
			//Expected data is loaded
			return;
		}
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
	
	public ArrayList<CPU> getCPUList(){
		return cpuList;
	}
	
	public ArrayList<GPU> getGPUList(){
		return gpuList;
	}
	
	public ArrayList<Motherboard> getMBList(){
		return mbList;
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
	
	public void refresh(){
		cpuList = new ArrayList<CPU>();
		gpuList = new ArrayList<GPU>();
		mbList = new ArrayList<Motherboard>();
		psuList = new ArrayList<PSU>();
		memList = new ArrayList<Memory>();
		initValidComputerParts();
	}

	public Map<String, String> getInventory() {
		HashMap<String, String> stats = new HashMap<String, String>();
		CPUDao cpuDao = new CPUDaoMySQLImpl();
		GPUDao gpuDao = new GPUDaoMySQLImpl();
		MBDao mbDao = new MBDaoMySQLImpl();
		MEMDao memDao = new MEMDaoMySQLImpl();
		PSUDao psuDao = new PSUDaoMySQLImpl();
		DISKDao diskDao = new DISKDaoMySQLImpl();
		try {
			stats.put(AppConstants.cpu, cpuDao.getValidCPUCount() + "");
			stats.put(AppConstants.gpu, gpuDao.getValidGPUCount() + "");
			stats.put(AppConstants.mobo, mbDao.getValidMotherboardCount() + "");
			stats.put(AppConstants.memory, memDao.getValidMemoryCount() + "");
			stats.put(AppConstants.psu, psuDao.getValidPSUCount() + "");
			stats.put(AppConstants.disk, diskDao.getValidDiskCount() + "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stats;
	}
}
