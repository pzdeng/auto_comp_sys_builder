package main.java.objects;

public enum ComputerType {
	GAMING, WEBSERVER, GENERAL;
	
	public static ComputerType toType(String type){
		type = type.trim();
		if(type.equalsIgnoreCase(ComputerType.GAMING.name())){
			return ComputerType.GAMING;
		}
		if(type.equalsIgnoreCase(ComputerType.WEBSERVER.name())){
			return ComputerType.WEBSERVER;
		}
		return ComputerType.GENERAL;
	}
}
