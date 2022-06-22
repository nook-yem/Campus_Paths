package performance;

import org.junit.Test;
import static org.junit.Assert.*;

public class MarvelPathsTest
{
	MarvelPaths path;
//	path = new MarvelPaths();
//	path.createNewGraph("./data/marvel.csv");
	@Test
	public void SmallFileConstruction() throws RuntimeException
	{
		path = new MarvelPaths();
		path.createNewGraph("./data/family.csv");
	}
	
	@Test
	public void reflexiveTest()
	{
		path = new MarvelPaths();
		path.createNewGraph("./data/family.csv");
		assertEquals("path from Ray to Ray:\n", path.findPath("Ray", "Ray"));
	}
	
	@Test
	public void unknownTest()
	{
		path = new MarvelPaths();
		path.createNewGraph("./data/family.csv");
		assertEquals("unknown character nofacenocase\n", path.findPath("nofacenocase", "Ray"));
		assertEquals("unknown character BillyBobJoey\n", path.findPath("Henry", "BillyBobJoey"));
		assertEquals("unknown character JimmyJohn\nunknown character BillyBobJoey\n", path.findPath("JimmyJohn", "BillyBobJoey"));
		assertEquals("unknown character JimmyJohn\n", path.findPath("JimmyJohn", "JimmyJohn")); //reflexive, one line case

	}
	
	@Test
	public void standardFindPathSmaller()
	{
		path = new MarvelPaths();
		path.createNewGraph("./data/family.csv");
		assertEquals("path from Henry to Ray:\nHenry to Ray via Cullom\n", path.findPath("Henry", "Ray"));
		assertEquals("path from Adeline to Kevin:\nAdeline to Little via Cullom\nLittle to Kevin via Ruddy\n", path.findPath("Adeline", "Kevin"));
		assertEquals("path from Henry to Nook:\nno path found\n", path.findPath("Henry", "Nook"));
	}
	
	
	
	@Test
	public void LargeFileConstructionAndFindPath() throws RuntimeException
	{
		path = new MarvelPaths();
		path.createNewGraph("./data/marvel.csv");
		assertEquals("path from PETERS, SHANA TOC to SEERESS:\nPETERS, SHANA TOC to KNIGHT, MISTY via M/CP 80/3\nKNIGHT, MISTY to ALEXANDER, CALEB via N 17\nALEXANDER, CALEB to HULK/DR. ROBERT BRUC via N@ 1/3\nHULK/DR. ROBERT BRUC to RAVAGE/PROF. GEOFFRE via RH2 2\nRAVAGE/PROF. GEOFFRE to SEERESS via M/CP 117/4\n", path.findPath("PETERS, SHANA TOC", "SEERESS"));
		assertEquals("path from GOOM to HOFFMAN:\n"
				+ "no path found\n", path.findPath("GOOM", "HOFFMAN"));
		assertEquals("unknown character BATMAN\n", path.findPath("BATMAN", "CAPTAIN AMERICA"));		
		assertEquals("unknown character BATMAN\nunknown character GREEN LANTERN\n", path.findPath("BATMAN", "GREEN LANTERN"));
		assertEquals("path from SEERESS to SEERESS:\n", path.findPath("SEERESS", "SEERESS"));
		assertEquals("unknown character BATMAN\n", path.findPath("BATMAN", "CAPTAIN AMERICA"));
		assertEquals("unknown character BATMAN\n", path.findPath("BATMAN", "BATMAN"));
	}	
}