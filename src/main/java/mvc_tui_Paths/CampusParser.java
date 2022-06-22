package mvc_tui_Paths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// 	Provides readData() class method that will parse data from "CSV" files
// 	Some code only existed for safety and was commented out

public class CampusParser 
{
	/**
	 * 	@param: 	nodeFileName     The path to a "CSV" file that contains the
	 *                      "buildings" and "intersections" (Form: name, id, x-coord, y-coord).
	 *                      Intersections won't be named (Form: "", id, x-coord, y-coord)
	 *	@param: 	edgeFileName     The path to a "CSV" file that contains the
	 *                      "building"/"intersection", "building"/"intersection" id pairs
	 *                      that represent bi-directional edges (Form: id,id)
	 *	@param: 	parentChildrenMap		The Map that stores all parsed locations as keys 
	 *							with an associated Set of all child Locations; usually passed in empty           
	 * 	@requires: 	nodeFileName != null  && parentChildrenMap != null
	 * 	@modifies: 	parentChildrenMap
	 * 	@effects: 	adds parsed <Location, Set of immediately-connected Locations> pairs to parentChildrenMap
	 * 	@throws: 	IOException if a file cannot be read or a file is not a CSV file following the proper format.
	 * 				
	 * 	@returns: None
	 */
	public static void readData(String nodeFileName, String edgeFileName, Map<Location, Set<Location>> parentChildrenMap)
			throws IOException/*, RuntimeException*/
	{
		// first read nodeFileName and populate map with all Location nodes (they'll point to empty sets)
		try (BufferedReader reader = new BufferedReader(new FileReader(nodeFileName))) 
		{
			String line = null;
			int id,x,y;
			while ((line = reader.readLine()) != null) 
			{
				int i = line.indexOf(",");
				
				if (i == -1)
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");
				String name = line.substring(0, i);
				int j = line.substring(i+1, line.length()).indexOf(",") + i+1;				
				if (j == i) // this is the -1 (not found) case
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");				
				try
				{
					id = Integer.parseInt(line.substring(i + 1, j));
				}
				catch(NumberFormatException nfe)
				{
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");
				}
				i = line.substring(j+1,line.length()).indexOf(",") + j+1;				
				if (i == j) // this is the -1/not found case
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");
				try
				{
					x = Integer.parseInt(line.substring(j+1, i));
					y = Integer.parseInt(line.substring(i+1,line.length()));	
				}
				catch(NumberFormatException nfe)
				{
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");
				}

				// Add Location to parentChildrenMap
				if (name.length() > 0)
					parentChildrenMap.put(new Building(name,id,x,y), new HashSet<Location>());
				else
					parentChildrenMap.put(new Intersection(id,x,y), new HashSet<Location>());
			}			
		}
		// second read edgeFileName and fill the set of children nodes of appropriate key Location nodes
		int childID, parentID;
		try (BufferedReader reader = new BufferedReader(new FileReader(edgeFileName))) 
		{
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				int i = line.indexOf(","); 
				if (i == -1)
					throw new IOException("File " + edgeFileName + " not a CSV (id,id) file.");
				try
				{
					parentID = Integer.parseInt(line.substring(0, i));
					childID = Integer.parseInt(line.substring(i + 1, line.length()));
				}
				catch(NumberFormatException nfe)
				{
					throw new IOException("File " + nodeFileName + " not a CSV (Name, id, x, y) file.");

				}
				
				// Add Location with id childID to the set of children belonging to Location with id parentID
				// find child with childID
				Location child = null;
//				boolean found = false;
				for(Location c : parentChildrenMap.keySet())
				{
					if(c.getID() == childID)
					{
						child = c;
//						found = true;
						break;
					}
				}
//				if (!found)
//					throw new RuntimeException("Error: child not found in map");
//				found = false;
				for(Location parent : parentChildrenMap.keySet())
				{
					if(parent.getID() == parentID)
					{
//						found = true;
						parentChildrenMap.get(parent).add(child);
						break;
					}
				}
//				if (!found)
//					throw new RuntimeException("Error: parent not found in map");
			}			
		}		
	}
	
//	public static void main(String[] args) 
//	{	
//		String nodeFile = args[0];	
//		String edgeFile = args[1];
//		try 
//		{
//			Map<Location, Set<Location>> parentChildrenMap = new HashMap<Location, Set<Location>>();
//			readData(nodeFile, edgeFile, parentChildrenMap);
//			System.out.println("Read " + parentChildrenMap.keySet().size() + " buildings and intersections.");
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
//	}
}
