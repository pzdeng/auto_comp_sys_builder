package main.java.databuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Simple CSV Reader
 * @author Peter
 *
 */
public class CSVReader {
	// using comma as separator
	private static String cvsSplitBy = ",";
	
	public CSVReader(){
		
	}
	
	/**
	 * Accounts for trailing empty entries
	 * @param csvFile
	 * @return
	 */
	public static String[][] parseFile(String csvFile){
		BufferedReader br = null;
		String line;
		String[] temp;
		ArrayList<String[]> contents = new ArrayList<String[]>();
		int length;
		
		try {
            br = new BufferedReader(new FileReader(csvFile));
            //Assume Header line
            line = br.readLine();
            length = line.split(cvsSplitBy).length;
            contents.add(line.split(cvsSplitBy, length));
            
            while ((line = br.readLine()) != null) {
            	temp = line.split(cvsSplitBy, length);
            	//remove double quotes for each entry
            	for(int i = 0; i < temp.length; i++){
            		if(temp[i].length() > 0 && temp[i].charAt(0) == '"'){
            			temp[i] = temp[i].substring(1, temp[i].length() - 1);
            		}
            	}
                contents.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return contents.toArray(new String[contents.size()][contents.get(0).length]);
	}
}
