package main.java.objects.comparator;

import java.util.Comparator;

import main.java.objects.ComputerPart;

/**
 * Sort some list based on computer part's bench score
 * @author Peter
 *
 */
public class BenchScoreComparator implements Comparator<ComputerPart>{

	@Override
	public int compare(ComputerPart o1, ComputerPart o2) {
		return (int) (o2.benchScore - o1.benchScore);
	}

}
