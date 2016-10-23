package main.java.objects;


public class ComputerBuild {

	private CPU cpu;
	private GPU gpu;
	private Motherboard mb;
	
	public ComputerBuild(){
		cpu = null;
		gpu = null;
		mb = null;
	}
	
	public ComputerBuild(CPU cpu, GPU gpu, Motherboard mb){
		this.cpu = cpu;
		this.gpu = gpu;
		this.mb = mb;
	}
	
}
