package databuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.ComputerBuild;
import main.java.objects.ComputerType;

public class ComputerBuildTester {
    
	@Test
	public void testAllPossbileBuilds(){
		ComputerBuild compBuild = new ComputerBuild();
		int numBuilds = compBuild.getAllPossibleCombinations();
		System.out.println(numBuilds);
		assertTrue(numBuilds > 0);
	}
	
	@Test
	public void testGamingBuild(){
		int budget = 2000;
		String compType = ComputerType.GAMING.name();
		
		ComputerBuild compBuild = new ComputerBuild(budget, compType);
		System.out.println(compBuild.toString());
		
	}
}
