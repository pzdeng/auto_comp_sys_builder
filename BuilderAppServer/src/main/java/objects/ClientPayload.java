package main.java.objects;

import java.util.ArrayList;

/**
 * Alternative representation of ComputerBuild object
 * To send to client/frontend
 * @author Peter
 *
 */
public class ClientPayload {
	public int budget;
	public String computerType;
	public float totalPrice;
	public ArrayList<ComputerPartMin> components;
	
	public ClientPayload(){
		components = new ArrayList<ComputerPartMin>();
	}
}
