package main.java.objects;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * In memory data class to hold computer build information
 * @author Peter
 *
 */
public class ComputerBuild {

	private CPU cpu;
	private ArrayList<GPU> gpuList;
	private Motherboard mb;
	private float totalCost;
	private int budget;
	private ComputerType type;
	
	public ComputerBuild(){
		cpu = null;
		gpuList = new ArrayList<GPU>();
		mb = null;
		type = ComputerType.GENERAL;
		buildComp();
	}
	
	public ComputerBuild(int budget, String compType){
		this.budget = budget;
		type = ComputerType.toType(compType);
		gpuList = new ArrayList<GPU>();
		buildComp();
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
	
	private void buildComp(){
		//TODO redo section
		//Init all computer parts to test response
		cpu = new CPU();
		cpu.dummyPopulate();
		
		GPU gpu = new GPU();
		gpu.dummyPopulate();
		
		mb = new Motherboard();
		mb.dummyPopulate();
		
		gpuList.add(gpu);
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
