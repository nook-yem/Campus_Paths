package mvc_tui_Paths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import adt.*;
import generic.DijkstrasSearch;

//	Main model class for MVC program.
// 	CampusPaths uses Graph to store locations, edges between them, and the weights of those edges.
//	Weight of an edge is calculated as the Euclidean distance between the source and receiver Locations.
//	CampusModel is not an ADT.

public class CampusModel
{
	// Instance fields
	private Graph<Location,Double> graph;
	
	// Constructor
	/*
	 * 	@modifies 	graph
	 * 	@effects 	instantiates graph as Graph object
	 */
	public CampusModel()
	{
		 graph = new Graph<Location,Double>();
	}
	
	// Methods
	
/*	initializes reads/parses data from file and store data in graph member variable
 * 	@param: 	filename - name of the file we're parsing for data
 * 	@requires: 	filename != null
 * 	@modifies:	graph
 * 	@effects:	graph is initialized to contain/represent all of the data
 *  		from file of name filename		 
 * 	@throws: 	IOException if file cannot be read of file not a CSV file following
 *          the proper format.
	@returns: 	None
	
	Edge weight is the inverse of the # of books the characters appear in together
	We decrease the edge weight, x, for each newly discovered edge using: x = x/(x+1)
	
 *  */
	public void createNewGraph(String nodeFileName, String edgeFileName)
	{		
		graph.clear();		
		Map<Location, Set<Location>> parentChildrenMap= new HashMap<Location, Set<Location>>();
		
		try 
		{
			CampusParser.readData(nodeFileName, edgeFileName, parentChildrenMap);
		}
		catch (IOException e) 
		{
    		e.printStackTrace();
    	}
		
		// NOTE: we assume readData fills parentChildrenMap with non-repeating undirected associations
			// and no reflexive edges		
		Double weight;
		double dx;
		double dy;
		
		// edges are bi-directional, so we'll have to add two directed edges for each parent,child pair
		for(Location parent : parentChildrenMap.keySet())
		{
			// add node to graph if it doesn't exist yet
			if(!graph.contains(parent))
				graph.addNode(parent);
			for(Location child : parentChildrenMap.get(parent))
			{
				// add node to graph if it doesn't exist yet
				if(!graph.contains(child))
					graph.addNode(child);
				//add an edge from both parent -> child and child -> parent
				dx = parent.getX() - child.getX();
				dy = parent.getY() - child.getY();
				weight = Double.valueOf(Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
				graph.addEdge(parent, child, weight);
				graph.addEdge(child, parent, weight);
			}			
		}		
	}
	
/*	Finds shortest path between two nodes and returns an ordered ArrayList of the nodes forming the location
 * 
 * 	@param:		s - source Location node we're finding shortest path from;
 * 				r - receiver Location  we're finding shortest path to
 * 	@requires: 	s != null, r != null, s and r exist in graph
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	none
 * 	@return:	an ordered ArrayList of the edges connecting from s to r, forming the shortest possible path
 * 			(includes reflexive edge first) if one exists; null if no path is found. 
 */
	public ArrayList<Edge<Location,Double>> findPath(Building s, Building r)
	{		
		ArrayList<Edge<Location,Double>> path = DijkstrasSearch.findMinPath(s, r, graph); // handles reflexive case as well
		if (path == null) // no path found between the two nodes (both nodes exist)
			return null;	
		else // return the shortest path
			return path;
	}
	
/*
 * 	Find the Building with name/ID nameOrID 
 * 	@param 		nameOrID - String name or String ID of Building we are looking for
 *	@requires 	nameOrID != null
 *	@modifies	n.a
 *	@effects	n.a.
 *	@throws		n.a.
 *	@return 	Building in graph with matching name/ID; null if no such Building exists 
 *			(an Intersection is a not Building)
 */
	public Building getBuilding(String nameOrID)
	{
		if(nameOrID.length() == 0)
			return null;
		// check if String is a parseable ID
		boolean parseable = true;
		for(int i=0; i<nameOrID.length(); i++)
		{
			if(!Character.isDigit(nameOrID.charAt(i)))
			{
				parseable = false;
				break;
			}
		}
		Iterator<Location> itr = graph.listNodes(); // list is sorted
		Location loc;
		if(parseable) // try to find building with ID: nameOrID
		{
			int id = Integer.parseInt(nameOrID);
			while(itr.hasNext())
			{
				loc = itr.next();
				if(loc.getID() == id)
				{
					if(loc.getName().length() > 0)
						return (Building) loc;
					else // this location is an intersection, not a building
						return null;
				}
			}
		}
		else // try to find Building with name: nameOrID
		{
			while(itr.hasNext())
			{
				loc = itr.next();
				if(loc.getName().equals(nameOrID))
					return (Building) loc;
			}
		}
		return null; // there is no Building with name or ID nameOrID
	}
	
/* 	
 * 	@return Iterator over an alphabetically sorted (by name) ArrayList of each Location*/
	public Iterator<Location> listNodes()
	{
		return graph.listNodes();
	}
	
}