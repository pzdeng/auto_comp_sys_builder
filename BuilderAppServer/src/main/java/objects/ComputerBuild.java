package main.java.objects;

/**
 * In memory class to hold computer build information to send to client
 * @author Peter
 *
 */
public class ComputerBuild {

	private CPU cpu;
	private GPU gpu;
	private Motherboard mb;
	private float totalCost;
	private float budget;
	private ComputerType type;
	
	public ComputerBuild(){
		cpu = null;
		gpu = null;
		mb = null;
		type = ComputerType.GENERAL;
	}
	
	public ComputerBuild(CPU cpu, GPU gpu, Motherboard mb, String compType){
		this.cpu = cpu;
		this.gpu = gpu;
		this.mb = mb;
		type = ComputerType.toType(compType);
	}
	
}
