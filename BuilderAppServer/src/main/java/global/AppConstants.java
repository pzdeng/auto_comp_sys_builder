package main.java.global;

/**
 * Declare any global constants 
 * @author Peter
 *
 */
public class AppConstants {
	//Text formating
	public final static String newLine	 = "\n";
	public final static String tab		 = "\t";
	public final static String separator = " :: ";
	//Suffixes
	public final static String gigahertz = "GHz";
	public final static String megahertz = "MHz";
	public final static String gigabyte	 = "GB";
	public final static String terabit	 = "Tb";
	public final static String terabyte	 = "TB";
	public final static String gigabit	 = "Gb";
	public final static String megabyte	 = "MB";
	public final static String megabit	 = "Mb";
	public final static String kilobyte	 = "KB";
	public final static String kilobit	 = "Kb";
	public final static String wattage	 = "W";
	public final static String perSecond = "/s";
	//Component names
	public final static String cpu		 = "CPU";
	public final static String gpu		 = "GPU";
	public final static String mobo		 = "Motherboard";
	public final static String psu		 = "PSU";
	public final static String memory	 = "Memory";
	public final static String disk		 = "Disk";
	//Motherboard types
	public final static String atx		 = "ATX";
	public final static String uatx		 = "Micro ATX";
	//Disk terminology
	public final static String hdd		 = "HDD";
	public final static String ssd		 = "SSD";
	public final static String sshd		 = "SSHD";
	public final static String rpm		 = "RPM";
	public final static String sata2	 = "Serial ATA 300";
	public final static String sata3	 = "Serial ATA 600";
	//MemoryTypes
	public final static String ddr		 = "DDR";
	public final static String ddr2		 = "DDR2";
	public final static String ddr3		 = "DDR3";
	public final static String ddr4		 = "DDR4";
	//Database
	public final static int batchCommit	 = 200;
	//PSU brand (multi-word)
	public final static String[] multiWordBrand = {
			"Be quiet!", "Cooler Master", "Fractal Design", "Super Flower",
			"Western Digital", "Silicon Power", "Super Talent", "Mach Xtreme Technology", "Micro Storage"
			
	};
	//Timeout
	public final static int buildTime	= 10;
}
