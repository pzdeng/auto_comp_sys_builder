package main.java.databuilder;

import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.objects.CPU;
import main.java.objects.ClientPayload;
import main.java.objects.ComputerType;
import main.java.objects.GPU;
import main.java.objects.Memory;
import main.java.objects.Motherboard;
import main.java.objects.PSU;

/**
 * In memory data class to hold computer build information
 * @author Peter
 *
 */
public class ComputerBuild {

	private CPU cpu;
	private ArrayList<GPU> gpuList;
	private Motherboard mb;
	private ArrayList<Memory> memList;
	private PSU psu;
	private float totalCost;
	private int budget;
	private ComputerType type;
	private DataBuilder parts;
	
	public ComputerBuild(){
		cpu = null;
		gpuList = new ArrayList<GPU>();
		mb = null;
		type = ComputerType.GENERAL;
		parts = DataBuilder.getInstance();
		parts.initValidComputerParts();
		buildComp();
	}
	
	public ComputerBuild(int budget, String compType){
		this.budget = budget;
		type = ComputerType.toType(compType);
		gpuList = new ArrayList<GPU>();
		parts = DataBuilder.getInstance();
		parts.initValidComputerParts();
		buildComp();
		//buildRandomComp();
	}
	
	/*
	 * Not used yet...
	public ComputerBuild(CPU cpu, GPU gpu, Motherboard mb, String compType){
		this.cpu = cpu;
		gpuList = new ArrayList<GPU>();
		gpuList.add(gpu);
		this.mb = mb;
		type = ComputerType.toType(compType);
		buildComp();
	}
	*/
	
	/**
	 * Method should choose some optimal build based on target computer type
	 */
	private void buildComp(){		
		//Keeping Dummy Section until algorithm works
		cpu = new CPU();
		cpu.dummyPopulate();
		
		GPU gpu = new GPU();
		gpu.dummyPopulate();
		
		mb = new Motherboard();
		mb.dummyPopulate();
		
		gpuList.add(gpu);
		
		//Algorithm to build computer
		//Check Computer Type
		switch(type){
			case SERVER:
				buildServerComp();
				break;
			case GAMING:
				buildGamingComp();
				break;
			case WORKSTATION:
				buildWorkstationComp();
				break;
			default:
				buildGeneralComp();
		}
	}
	
	private void buildGeneralComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 4 GB
		// Mid - Low CPU
		// Motherboard
		// Mid - Low Graphics
		// Storage should be > 500 GB
		validateBuild();
	}

	private void buildWorkstationComp() {
		//Could also be BATCH PROCESSING SERVER
		// TODO:
		//Outlining Computer Specs:
		// > 8GB RAM
		// High CPU, favor high threading (40% of budget)
		// Motherboard
		// Mid - Low Graphics
		// Storage should be > 500GB
		validateBuild();
	}

	private void buildGamingComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 8GB RAM
		// > Mid CPU
		// Motherboard, needs to have > 1 PCIExpress slot
		// High Graphics (40% of budget)
			// Dual Graphics Cards (if time permits) 
		// Storage should be > 500GB
		validateBuild();
	}

	private void buildServerComp() {
		// TODO:
		//Outlining Computer Specs:
		// > 16GB RAM
		// > Low CPU
		// Motherboard, favor high # of SataPorts, favor high number of memory slots
		// Graphics (None)
		// Storage should be > 10TB
		validateBuild();
	}

	/**
	 * Build some random computer...
	 */
	private void buildRandomComp(){
		DataBuilder components = DataBuilder.getInstance();
		Random rand = new Random();
		
		do{
			//random cpu
			cpu = components.getCPUList().get(rand.nextInt(components.getCPUList().size()));
			//random gpu
			GPU gpu = components.getGPUList().get(rand.nextInt(components.getGPUList().size()));
			//random motherboard
			mb = components.getMBList().get(rand.nextInt(components.getMBList().size()));
			gpuList.add(gpu);
		}
		while(!validateBuild());
	}
	
	/**
	 * Validate computer build
	 * @return
	 */
	private boolean validateBuild() {
		//Check 1: CPU fits into Motherboard CPU socket
		if(!mb.fitCPU(cpu)){
			return false;
		}
		//Check 2: GPU(s) fits into Motherboard's available interface(s)
		if(!mb.fitGPU(gpuList)){
			return false;
		}
		//Check 3: Memory/RAM Type is compatible with Motherboard and CPU
		//TODO: Fail check if DDR2/DDR3/DDR4 are not the same
		//Check 4: Memory/RAM unit fits into Motherboard's available memory slots
		//TODO: Fail check # RAM modules > Motherboard's # mem slots
		//Check 5: Storage Units fits into Motherboard's available SATA ports
		//TODO: Fail check if # of HDD/SDD > Motherboard's # SATA ports
		//Check 6: Fits into power requirements (require power calculation)
		//TODO: Compute power requirements of current build
		//TODO: Check against PSU capacity
		//Check 7: Build is within budget
		//TODO: Check prices of items are within budget
		return true;
	}

	/**
	 * Create custom payload to frontend client
	 * @return client payload
	 */
	public ClientPayload createClientPayload(){
		ClientPayload clientObj = new ClientPayload();
		//transform information to client object
		clientObj.budget = budget;
		clientObj.computerType = type.name();
		clientObj.totalPrice = totalCost;
		//add computer parts as components
		if(cpu != null){
			clientObj.components.add(cpu.shortenSpecs());
		}
		for(GPU gpu : gpuList){
			clientObj.components.add(gpu.shortenSpecs());
		}
		if(mb != null){
			clientObj.components.add(mb.shortenSpecs());
		}
		/*
		//transform to JSON
		try {
			jsonStr = new ObjectMapper().writeValueAsString(clientObj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return clientObj;
	}
}
