package org.aid.externalsorting.common;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.aid.externalsorting.ExternalMargeSortTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExternalMargeSortTest {
	
	
	
	@Before
	public void testSetup() {
		
		TestUtils.cleanDirectory("input");
		TestUtils.cleanDirectory("temp");
	}
	@After
	public void testCleanup() {
		
		TestUtils.cleanDirectory("input");
		TestUtils.cleanDirectory("temp");
	}
	
	@Test
	public void test_basic_with_integer_values() throws Exception {	
        
        File inputFolder = TestUtils.generateRandomNumbersFilesInFolder(5, 10, "input");		
		ExternalMargeSortTask<Integer> sortingTask = ExternalMargeSortTask.creatIntergerSortingTask(inputFolder, new File("temp"), 2);		
		File outputFile = sortingTask.executeSort();       
		assertTrue( TestUtils.isIntegerSorted(outputFile.getPath()));
    
    }
    
	
	@Test
	public void test_basic_with_String_values() throws Exception {	
		
		File inputFolder = TestUtils.generateRandomStringsFilesInFolder(5, 10, "input");		
		ExternalMargeSortTask<String> sortingTask = ExternalMargeSortTask.creatStringSortingTask(inputFolder, new File("temp"), 2);		
		File outputFile = sortingTask.executeSort();       
        assertTrue( TestUtils.isStringSorted(outputFile.getPath()));      	
    
    }
	
	
	@Test
    public void test_sample() throws Exception {		
    	
    	File inputFolder = new File("test-cases/example");   	    	
    	
    	ExternalMargeSortTask<String> sortingTask = ExternalMargeSortTask.creatStringSortingTask(inputFolder, new File("temp"), 2);    			
		File outputFile = sortingTask.executeSort();       
        assertTrue( TestUtils.isStringSorted(outputFile.getPath()));      	
    	    	
    	
    	StringLineReader aSimpleLineReader = StringLineReader.createLineReader(outputFile);
	    System.out.println("\n");
        while (aSimpleLineReader.hasNextLine()) {
            System.out.print(" "+aSimpleLineReader.nextLine());
        }     
        
        
        System.out.println("\nis sorted: "+TestUtils.isStringSorted(outputFile.getPath()));
        assertTrue( TestUtils.isStringSorted(outputFile.getPath()));    	
    }
	
	@Test
    public void test_with_predifined_test_data() throws Exception {		
    	
    	File inputFolder = new File("test-cases/sample2");   	    	
    	
    	ExternalMargeSortTask<String> sortingTask = ExternalMargeSortTask.creatStringSortingTask(inputFolder, new File("temp"), 2);    			
		File outputFile = sortingTask.executeSort();       
        assertTrue( TestUtils.isStringSorted(outputFile.getPath()));      	
    	    	
    	
    	StringLineReader aSimpleLineReader = StringLineReader.createLineReader(outputFile);
	    System.out.println("\n");
        while (aSimpleLineReader.hasNextLine()) {
            System.out.print(" "+aSimpleLineReader.nextLine());
        }     
        
        
        System.out.println("\nis sorted: "+TestUtils.isStringSorted(outputFile.getPath()));
        assertTrue( TestUtils.isStringSorted(outputFile.getPath()));    	
    }
    

}
