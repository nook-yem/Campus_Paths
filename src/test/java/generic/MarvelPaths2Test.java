package generic;

import org.junit.Test;
import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.BeforeClass; // cause initilization error


public class MarvelPaths2Test
{
	MarvelPaths2 path1;
	MarvelPaths2 path2;
	
//	@Before
//	public void create()
//	{
//		path1 = new MarvelPaths2();
//		path2 = new MarvelPaths2();
//		path1.createNewGraph("./data/lineage-map.csv");
//		path2.createNewGraph("./data/marvel.csv");		
//	}

//	@Test
//	public void SmallFileConstruction() throws RuntimeException
//	{
//		path = new MarvelPaths2();
//		path.createNewGraph("./data/lineage-map.csv");
//	}
	
	@Test
	public void reflexiveTest()
	{
		path1 = new MarvelPaths2();
		path1.createNewGraph("./data/lineage-map.csv");
		assertEquals("path from Nook Yemane to Nook Yemane:\ntotal cost: 0.000\n", path1.findPath("Nook Yemane", "Nook Yemane"));
	}
	
	@Test
	public void unknownTest()
	{
		path1 = new MarvelPaths2();
		path1.createNewGraph("./data/lineage-map.csv");
		assertEquals("unknown character nofacenocase\n", path1.findPath("nofacenocase", "Henry Cullom"));
		assertEquals("unknown character BillyBobJoey\n", path1.findPath("Niko Mikluscak", "BillyBobJoey"));
		assertEquals("unknown character JimmyJohn\nunknown character BillyBobJoey\n", path1.findPath("JimmyJohn", "BillyBobJoey"));
		assertEquals("unknown character JimmyJohn\n", path1.findPath("JimmyJohn", "JimmyJohn")); //reflexive, one line case
	}
	
	@Test
	public void standardFindPathSmaller()
	{		
		path1 = new MarvelPaths2();
		path1.createNewGraph("./data/lineage-map.csv");
		assertEquals("path from Henry Cullom to Isayah Tejada:\nHenry Cullom to Isayah Tejada with weight 0.333\n"
				+ "total cost: 0.333\n", path1.findPath("Henry Cullom", "Isayah Tejada"));
		assertEquals("path from Henry Cullom to Joel Sunny:\nno path found\n", path1.findPath("Henry Cullom", "Joel Sunny"));
		assertEquals("path from Niels Mandrus to Joel Sunny:\n"
				+ "Niels Mandrus to Joel Sunny with weight 1.000\n"				
				+ "total cost: 1.000\n", path1.findPath("Niels Mandrus", "Joel Sunny"));
	}	
	
	@Test
	public void LargeFileConstructionAndFindPath() throws RuntimeException
	{
		path2 = new MarvelPaths2();
		path2.createNewGraph("./data/marvel.csv");
		assertEquals("path from PETERS, SHANA TOC to SEERESS:\nPETERS, SHANA TOC to KNIGHT, MISTY with weight 1.000\nKNIGHT, MISTY to CAGE, LUKE/CARL LUCA with weight 0.017\nCAGE, LUKE/CARL LUCA to HULK/DR. ROBERT BRUC with weight 0.032\nHULK/DR. ROBERT BRUC to RAVAGE/PROF. GEOFFRE with weight 0.500\nRAVAGE/PROF. GEOFFRE to SEERESS with weight 1.000\ntotal cost: 2.549\n", path2.findPath("PETERS, SHANA TOC", "SEERESS"));
		assertEquals("path from GOOM to HOFFMAN:\n"
				+ "no path found\n", path2.findPath("GOOM", "HOFFMAN"));
		assertEquals("unknown character BATMAN\n", path2.findPath("BATMAN", "CAPTAIN AMERICA"));		
		assertEquals("unknown character BATMAN\nunknown character GREEN LANTERN\n", path2.findPath("BATMAN", "GREEN LANTERN"));
		assertEquals("path from SEERESS to SEERESS:\ntotal cost: 0.000\n", path2.findPath("SEERESS", "SEERESS"));
		assertEquals("unknown character BATMAN\n", path2.findPath("BATMAN", "CAPTAIN AMERICA"));
		assertEquals("unknown character BATMAN\n", path2.findPath("BATMAN", "BATMAN"));
		// test fromWisdomPeterToWisdomRomany
		assertEquals("path from WISDOM, PETER to WISDOM, ROMANY:\n"
				+ "WISDOM, PETER to WISDOM, ROMANY with weight 0.200\n"
				+ "total cost: 0.200\n", path2.findPath("WISDOM, PETER", "WISDOM, ROMANY"));
		assertEquals("path from NIGHTCRAWLER/KURT WA to CAPTAIN BRITAIN/BRIA:\n"
				+ "NIGHTCRAWLER/KURT WA to CAPTAIN BRITAIN/BRIA with weight 0.009\n"
				+ "total cost: 0.009\n", path2.findPath("NIGHTCRAWLER/KURT WA", "CAPTAIN BRITAIN/BRIA"));
	}	
}