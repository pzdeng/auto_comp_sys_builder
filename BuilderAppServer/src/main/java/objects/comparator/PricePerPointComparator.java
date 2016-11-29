package main.java.objects.comparator;

import java.util.Comparator;

import main.java.objects.ComputerPart;

/**
 * Sort some list based on computer part's bench score
 * @author Peter
 *
 */
public class PricePerPointComparator implements Comparator<ComputerPart>{

	@Override
	public int compare(ComputerPart o1, ComputerPart o2) {
		if(o2.pricePerPoint > o1.pricePerPoint){
			return 1;
		}
		if(o2.pricePerPoint < o1.pricePerPoint){
			return -1;
		}
		return 0; 
	}

}
