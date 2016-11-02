package main.java.objects;

public enum ComputerType {
	GAMING, SERVER, WORKSTATION, GENERAL;
	
	public static ComputerType toType(String type){
		if(type != null){
			type = type.trim();
			if(type.equalsIgnoreCase(ComputerType.GAMING.name())){
				return ComputerType.GAMING;
			}
			if(type.equalsIgnoreCase(ComputerType.SERVER.name())){
				return ComputerType.SERVER;
			}
			if(type.equalsIgnoreCase(ComputerType.WORKSTATION.name())){
				return ComputerType.WORKSTATION;
			}
		}
		return ComputerType.GENERAL;
	}
}
