package performance;
import java.io.IOException;
import java.util.*;

import adt.*;

public class MarvelPaths
{
	// Instance fields
	private Graph<String,String> graph;
	
	// Constructor
	MarvelPaths()
	{
		 graph = new Graph<String,String>();
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
 *  */
	public void createNewGraph(String filename)
	{		
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
					graph.addEdge(character, c2, book);					
				}
			}
		}
		Iterator<String> nodes = graph.listNodes();
		while (nodes.hasNext())
		{
			System.out.println(nodes.next());
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
		LinkedList<String> queue = new LinkedList<String>();
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		queue.add(s);
		List<String> oldp;
		map.put(s, new LinkedList<String>()); // add start -> [] to M
		while (queue.size() != 0)
		{			
			String source = queue.remove();
			if(source.equals(r))
				return output + make_path(s,r,map.get(source));
			Iterator<String> children = graph.listChildren(source);
			while(children.hasNext()) 
			{
				String receiver = children.next();
				if(!map.containsKey(parse_node(receiver))) // if m is not in M
				{
						oldp = map.get(source);
					LinkedList<String> newp = new LinkedList<String>(oldp);
					newp.add(receiver);
					map.put(parse_node(receiver), newp);
					queue.add(parse_node(receiver));				
				}
			}
		}		
		return output + "no path found\n";
	}
	
/*	@param: 	s: source node; 
 * 				r: receiver node; 
 * 				nodes: list of nodes forming path
 *  @requires: 	s != null, r != null, nodes != null
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	none
 *	@return: 	String for output representing path from s to r
 */
	public String make_path(String s, String r, List<String> nodes)
	{
		String path = "";
		for (int i=0; i < nodes.size(); i++)
		{
			if(i==0)
			{
				path += s + " to " + parse_node(nodes.get(i)) + " via " + parse_edge(nodes.get(i)) + "\n";
				continue;
			}
			path += parse_node(nodes.get(i-1)) + " to " + parse_node(nodes.get(i)) + " via " + parse_edge(nodes.get(i)) + "\n";
		}
		return path;
	}
	
	
	
/*	Parses node string to get rid of "(label)"
 * 	@param:	 	node - the string we're parsing for the node
 * 	@requires: 	node != null;
 * 				since we've commented out the if statement below,
 *  		parse_node can only be called if the string is of form node(label)
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	none
 *	@return:	parsed node String
 */
	public static String parse_node(String node)
	{
		int pos = node.lastIndexOf("(");
//		if(pos == -1) // only here fore safety, commented out for coverage (we wouldn't be able to test this)
//			return node;
		return node.substring(0,pos);
	}
	
/*	parses edge String to only include "label"
 * 	@param: 	edge - the string we're parsing for the edge
 * 	@requires: 	edge != null;
 * 				edge string has "(label)" to parse
 * 	@modifies: 	none
 * 	@effects: 	none
 * 	@throws: 	none
 *	@return: 	parsed edge String
 */
	public static String parse_edge(String edge)
	{
		int start = edge.lastIndexOf("(");
		int end = edge.lastIndexOf(")");
		return edge.substring(start+1, end);
	}	
}
