package mvc_tui_Paths;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

/*
 * 	Series of tests for MVC program
 * 	Generally, inputs are taken from specified test file, output is sent to
 *  	a text file and compared to a text file with the expected output
 */

public class CampusPathsTest 
{ 

	/**
	 * @param file1 
	 * @param file2
	 * @return true if file1 and file2 have the same content, false otherwise
	 * @throws IOException
	 */	
	/* compares two text files, line by line */
	private static boolean compare(String file1, String file2) throws IOException 
	{
		BufferedReader is1 = new BufferedReader(new FileReader(file1)); // Decorator design pattern!
		BufferedReader is2 = new BufferedReader(new FileReader(file2));
		String line1, line2;
		boolean result = true;
		while ((line1=is1.readLine()) != null) 
		{			
			line2 = is2.readLine();
			if (line2 == null) 
			{
				System.out.println(file1+" longer than "+file2);
				result = false;
				break;
			}
			if (!line1.equals(line2)) 
			{
				System.out.println("Lines: "+line1+" and "+line2+" differ.");
				result = false;
				break;
			}
		}
		if (result && is2.readLine() != null) 
		{
			System.out.println(file1+" shorter than "+file2);
			result = false;
		}
		is1.close();
		is2.close();
		return result;
	}
	
	// 	helper method that directs input to specified file, directs output to text file
	// 		and compares it to specified expected file
	private void runTest(String filename) throws IOException 
	{
		InputStream in = System.in;
		PrintStream out = System.out;
		String inFilename = "data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out 
		String args[] = {"data/RPI_map_data_Nodes.csv","data/RPI_map_data_Edges.csv"}; // line added by student
		CampusPaths.main(args); // arguments modifed/added modified by student
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output
		assertTrue(compare(expectedFilename, outFilename)); 		
	}
	
	@Test
	public void testListBuildings() throws IOException 
	{
		runTest("test1");
	}
	
	@Test
	public void testMenu() throws IOException 
	{
		runTest("test2");
	}
	@Test
	public void testPath() throws IOException 
	{
		runTest("test3");
	}	
	@Test
	public void testReflexivePath() throws IOException 
	{
		runTest("test5");
	}	
	@Test
	public void testNoPathFound() throws IOException 
	{
		runTest("test6");
	}		
	@Test
	public void testUnknownBuildingCases() throws IOException 
	{
		runTest("test7");
	}	
	@Test
	public void testSameVertical() throws IOException 
	{
		runTest("test8");
	}
	@Test
	public void testSeveralCommands() throws IOException 
	{
		runTest("test4");
	}
	@Test
	public void testGetBuildingEmptyString()
	{
		CampusModel model = new CampusModel();
		model.createNewGraph("data/RPI_map_data_Nodes.csv","data/RPI_map_data_Edges.csv");
		assertEquals(null, model.getBuilding(""));
	}
	@Test
	public void testReadData()
	{
		try 
		{
			Map<Location, Set<Location>> parentChildrenMap = new HashMap<Location, Set<Location>>();
			CampusParser.readData("data/RPI_map_data_Nodes.csv", "data/RPI_map_data_Edges.csv", parentChildrenMap);
			assertEquals(parentChildrenMap.keySet().size(), 132);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidBuildingCondtruction1()
	{
        Location building = new Building("",0,0,0);
	}	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidBuildingCondtruction2()
	{
		Location building = new Building("fail1",-1,0,0);
	}	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidBuildingCondtruction3()
	{
		Location building = new Building("fail2",6,-20,25);
	}	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidBuildingCondtruction4()
	{
		Location building = new Building("",78,67,-3);
	}	    
	@Test (expected=IOException.class)
	public void testInvalidInvalidCSVFormat1() throws IOException
	{
		CampusParser.readData("data/bad_csv1_mvc.csv", "data/bad_csv1_mvc.csv", new HashMap<Location,Set<Location>>());
	}
	@Test (expected=IOException.class)
	public void testInvalidInvalidCSVFormat2() throws IOException
	{
		CampusParser.readData("data/bad_csv2_mvc.csv", "data/bad_csv1_mvc.csv", new HashMap<Location,Set<Location>>());
	}
	@Test (expected=IOException.class)
	public void testInvalidInvalidCSVFormat3() throws IOException
	{
		CampusParser.readData("data/bad_csv3_mvc.csv", "data/bad_csv1_mvc.csv", new HashMap<Location,Set<Location>>());
	}
	@Test (expected=IOException.class)
	public void testInvalidInvalidCSVFormat4() throws IOException
	{
		CampusParser.readData("data/bad_csv4_mvc.csv", "data/RPI_map_data_Nodes.csv", new HashMap<Location,Set<Location>>());
	}
	@Test (expected=IOException.class)
	public void testInvalidInvalidCSVFormat5() throws IOException
	{
		CampusParser.readData("data/RPI_map_data_Nodes.csv", "data/bad_csv4_mvc.csv", new HashMap<Location,Set<Location>>());
	}	
	
	@Test
	public void testCampusPathsWithoutArgs() throws IOException
	{
		String filename = "test4"; // pretty good coverage test
		InputStream in = System.in;
		PrintStream out = System.out;
		String inFilename = "data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out 		
		CampusPaths.main(new String [0]); // arguments modifed/added modified by student
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output 
		assertTrue(compare(expectedFilename, outFilename));		
	}
}
