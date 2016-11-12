package main.java.objects.comparator;

import java.util.Comparator;

import main.java.objects.ComputerPart;
/**
 * Sort some list based on computer part's model name
 * @author Peter
 *
 */
public class ModelNameComparator implements Comparator<ComputerPart>{

	@Override
	public int compare(ComputerPart o1, ComputerPart o2) {
		return o1.modelName.compareTo(o2.modelName);
	}
}
