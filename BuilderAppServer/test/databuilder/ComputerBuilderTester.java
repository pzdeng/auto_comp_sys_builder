package databuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.ComputerBuilder;
import main.java.objects.ComputerType;

public class ComputerBuilderTester {
    /*
	@Test
	public void testAllPossbileBuilds(){
		ComputerBuild compBuild = new ComputerBuild();
		int numBuilds = compBuild.getAllPossibleCombinations();
		System.out.println(numBuilds);
		assertTrue(numBuilds > 0);
	}
	*/
	@Test
	public void testGamingBuild(){
		int budget = 1000;
		String compType = ComputerType.GAMING.name();
		
		ComputerBuilder compBuilder = new ComputerBuilder(budget, compType, 20);
		System.out.println(compBuilder.getBuild().toString());
		
	}
}
