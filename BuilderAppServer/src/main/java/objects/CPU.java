package main.java.objects;

public class CPU {
	private int id;
	private float msrpPrice;
	private String name;
	private String make;
	private int year;
	private float coreSpeed;
	private int coreCount;
	private String socketType;
	private int thermalRating;
	private int l1Size;
	private int l2Size;
	private int l3Size;
	private float relativeRating;
	
	public CPU(){
	}
	
	public CPU(int cpuID){
		setId(cpuID);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getMsrpPrice() {
		return msrpPrice;
	}

	public void setMsrpPrice(float msrpPrice) {
		this.msrpPrice = msrpPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getCoreSpeed() {
		return coreSpeed;
	}

	public void setCoreSpeed(float coreSpeed) {
		this.coreSpeed = coreSpeed;
	}

	public int getCoreCount() {
		return coreCount;
	}

	public void setCoreCount(int coreCount) {
		this.coreCount = coreCount;
	}

	public String getSocketType() {
		return socketType;
	}

	public void setSocketType(String socketType) {
		this.socketType = socketType;
	}

	public int getThermalRating() {
		return thermalRating;
	}

	public void setThermalRating(int thermalRating) {
		this.thermalRating = thermalRating;
	}

	public int getL1Size() {
		return l1Size;
	}

	public void setL1Size(int l1) {
		l1Size = l1;
	}

	public int getL2Size() {
		return l2Size;
	}

	public void setL2Size(int l2) {
		l2Size = l2;
	}

	public int getL3Size() {
		return l3Size;
	}

	public void setL3Size(int l3) {
		l3Size = l3;
	}

	public float getRelativeRating() {
		return relativeRating;
	}

	public void setRelativeRating(float relativeRating) {
		this.relativeRating = relativeRating;
	}
}
