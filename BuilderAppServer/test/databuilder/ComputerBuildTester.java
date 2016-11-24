package databuilder;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.databuilder.ComputerBuild;

public class ComputerBuildTester {
	private ComputerBuild compBuild;
	
    @Before
    public void setUp() throws Exception {
    	compBuild = new ComputerBuild();
    }
    
    @After
    public void tearDown() throws Exception {
    	compBuild = null;
    }
    
	@Test
	public void testAllPossbileBuilds(){
		int numBuilds = compBuild.getAllPossibleCombinations();
		System.out.println(numBuilds);
		assertTrue(numBuilds > 0);
	}
}
