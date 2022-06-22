package generic;
import adt.*;
import performance.*;

import java.io.IOException;
import java.util.*;


/*
 * Will store only one directed edge per pair of characters
 * Will track number of books characters appear in together as inverse of the edge weight
 */
public class MarvelPaths2
{
	// Instance field
	private Graph<String,Double> graph;
	
	// Constructor
	MarvelPaths2()
	{
		 graph = new Graph<String,Double>();
	}
	
	// Methods
	
/*	initializes reads/parses data from file and store data in graph member variable
 * 
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
	public void createNewGraph(String filename)
	{		
		graph.clear();
		Map<String, Set<String>> bookCharsMap = new HashMap<String, Set<String>>();
		Set<String> charSet = new HashSet<String>();
		try 
		{
			MarvelParser.readData(filename, bookCharsMap, charSet);
		}
		catch (IOException e) 
		{
    		e.printStackTrace();
    	}
		
		Set<String> books = bookCharsMap.keySet();
		Set<String> chars;
		Edge<String, Double> edge;
		Double weight;
		for (String character : charSet)
			graph.addNode(character);
		for (String book : books)
		{
			chars = bookCharsMap.get(book);
			for (String character : chars)
			{
				for (String c2 : chars)
				{
					if (c2 == character)
						continue;
					//find edge from character to c2, if it exists
					//update the edge weight (x = x/(x+1))
					edge = graph.getEdge(character, c2);
					if(edge == null)
						graph.addEdge(character, c2, Double.valueOf(1));
					else //update edge weight
					{
						weight = edge.getLabel();
						graph.remove(character, edge);
						graph.addEdge(character, c2, weight/(weight+1));
					}
				}
			}
		}
	}	
	
/*	@param:		s - source node we're finding shortest path from;
 * 				r - receiver node we're finding shortest path to
 * 	@requires: 	s != null, r != null
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	none
 * 	@return:	String for output representing path from s to r
 */
	public String findPath(String s, String r)
	{		
		boolean exists1 = true;
		boolean exists2 = true;
		if(!graph.contains(s))
			exists1 = false;
		if(!graph.contains(r))
			exists2 = false;
		if (!exists1 || !exists2)
		{
			if (exists2 || s == r) // s == r included for reflexive case, in which we only need one line
				return "unknown character " + s + "\n";
			else if (exists1)
				return "unknown character " + r + "\n";
			else
				return "unknown character " + s + "\nunknown character " + r + "\n";
		}
		String output = "path from " + s + " to " + r + ":\n";
		//reflexive case first
		if(s == r)
			return output + "total cost: 0.000\n";		
		ArrayList<Edge<String,Double>> path = DijkstrasSearch.findMinPath(s, r, graph);
		if (path == null)
			return output + "no path found\n";		
		return output + makePath(path);
	}
	
/*	@param: 	path - list of edges that form path from source to destination node; 
 * 					first Edge is reflexive from source node
 *  @requires: 	n.a.
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	RuntimeException if path is null or empty
 *	@return: 	String for output representing path from s to r
 */
	public String makePath(List<Edge<String,Double>> path)
	{
		if(path == null)
			throw new RuntimeException("Null argument.");
		if(path.size() == 0)
			throw new RuntimeException("Function was called with empty path");
		String pathOutput = "";
		Double totalCost = Double.valueOf(0);
		Double cost;
		for (int i=1; i < path.size(); i++)
		{
			cost = path.get(i).getLabel();
			pathOutput += (path.get(i-1).getReceiver()) + " to " + (path.get(i).getReceiver()) + String.format(" with weight %.3f", cost) + "\n";
			totalCost += cost;
		}
		return pathOutput + String.format("total cost: %.3f\n", totalCost);
	}	
}

// 	Defines Comparator to compare ArrayList of Edges representing a path
//		by the sum weight of the edges of a list
class SortPathByWeight<U> implements Comparator<ArrayList<Edge<U,Double>>>
{
	// Generic Typing: U is the data type of the nodes of 
	// the Edges we are comparing

/*	@requires 	a and b != null
 *	@param 		Edge objects a and b
 *	@return 	-1 if a has smaller weight, 1 if a has larger weight,
 *		0 if and b have the same weight
 */
	@Override
	public int compare(ArrayList<Edge<U,Double>> a, ArrayList<Edge<U,Double>> b) // not the problem
	{
		Double weightA = getPathWeight(a);
		Double weightB = getPathWeight(b);
		return weightA.compareTo(weightB);	
	}
	
/*
 * 	@param 		path - ArrayList of Edges we want the total weight of
 *	@requires 	Edge labels represent the weight of the path the edge represents
 * 	@modifies 	n.a.
 * 	@effects 	n.a.
 * 	@throws 	RuntimeException if path == null
 * 	@returns 	the sum total of weights of all the Edges in path 
 */
	public Double getPathWeight(ArrayList<Edge<U,Double>> path) // not the problem
	{
		if(path == null) throw new RuntimeException("Null argument.");
		Double sum = Double.valueOf(0);
		for (Edge<U,Double> edge : path)
		{
			sum += edge.getLabel();
		}
		return sum;
	}
}
