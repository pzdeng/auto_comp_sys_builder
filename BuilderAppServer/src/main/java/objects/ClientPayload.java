package main.java.objects;

import java.util.ArrayList;

/**
 * Alternative representation of ComputerBuild object
 * To send to client/frontend
 * @author Peter
 *
 */
public class ClientPayload {
	public String budget;
	public String computerType;
	public String totalPrice;
	public ArrayList<ComputerPartMin> components;
	public float responseTime;	//Response time to the client
	
	public ClientPayload(){
		components = new ArrayList<ComputerPartMin>();
	}
}
