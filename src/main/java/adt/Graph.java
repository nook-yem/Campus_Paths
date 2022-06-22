package adt;
import java.util.*;

/*	
 * 	Overview: 				
 * 		A representation of a directed labeled multigraph.
 * 		Object will contain a list of nodes and a list of
 * 		edges mapping between nodes. Each edge is distinct.
 * 		Two edges are distinct as long as they don't share
 * 		the same receiver node and label.
 *
 *	Abstraction Function: 	
 *		A graph is a list of nodes, of some generic type U
 *		(used to be Strings) and the connecting edges
 *		between them
 *
 *	Rep. invariant: 		
 *		source and receiver nodes for all edges exist in
 *		the map/adjacency-list
 *							
 *	Regarding use of Generic Typing:
 * 		U is the type for the nodes in the graph, V is the type for the label
 */

public class Graph<U extends Comparable<U>,V extends Comparable<V>> 
{
	private Map<U, Set<Edge<U,V>>> adjList; // Node -> Set of Edges the node is a source for
	
// constructor	
//	@modifies adjList
//	@effects instantiates the Map (empty)
	public Graph()
	{
		adjList = new HashMap<U, Set<Edge<U,V>>>();	
		checkRep();
	}
		
/*	@requires 	parent != null
 * 	@param 		parent node whose children we're finding
 * 	@returns 	Iterator<String> on an ArrayList of each
 * 		child_node(edge_label) of parent 
 */
	public Iterator<String> listChildren(U parent)
	{
		Set<Edge<U,V>> seeds = adjList.get(parent);
		List<Edge<U,V>> sortedSeeds = new ArrayList<Edge<U,V>>();
		sortedSeeds.addAll(seeds);
		Collections.sort(sortedSeeds, new SortbyChild<U,V>()); //sort edges to children
		ArrayList<String> kids = new ArrayList<String>(); //child(label) format
		String s;
		for (Edge<U,V> edge : sortedSeeds)
		{
			s = edge.getReceiver() + "(";
			if(edge.getLabel() != null)
				s = s + edge.getLabel() + ")";
			else
				s = s + ")";
			kids.add(s);
		}
		return kids.listIterator();				
	}
	
/* 	@return Iterator<String> on an ArrayList of each node
 * 		in alphabetical (lexicographical) order		
 */
	public Iterator<U> listNodes()
	{
		List<U> nodes = new ArrayList<U>(); 
		nodes.addAll(adjList.keySet());
		Collections.sort(nodes);
		return nodes.listIterator();
	}
	
/*	@requires 	parent != null
 * 	@param 		parent node whose children we're finding
 * 	@return		ArrayList of children nodes				 
 */
	public ArrayList<String> getChildren(U parent)
	{
		Set<Edge<U,V>> seeds = adjList.get(parent);
		List<Edge<U,V>> sortedSeeds = new ArrayList<Edge<U,V>>();
		sortedSeeds.addAll(seeds);
		Collections.sort(sortedSeeds, new SortbyChild<U,V>()); //sort edges to children
		ArrayList<String> kids = new ArrayList<String>(); //child(label) format
		String s;
		for (Edge<U,V> edge : sortedSeeds)
		{
			s = edge.getReceiver() + "(" + edge.getLabel() + ")";
			kids.add(s);
		}
		return kids;	
	}
	
/*	@requires 	child != null
 * 	@param 		child node whose parents we're finding
 * 	@return 	ArrayList of parent nodes				 
 */
	public ArrayList<U> getParents(U child)
	{		
		ArrayList<U> parents = new ArrayList<U>();
		for (U node : adjList.keySet())
		{
			for(Edge<U,V> edge : adjList.get(node))
			{
				if(edge.getReceiver().equals(child))
				{
					parents.add(node);
					break;
				}
			}			
		}
		Collections.sort(parents);
		return parents;
	}

/*	@modifies	adjList
 * 	@effects 	sets it to empty
 */
	public void clear()
	{
		adjList.clear();
		checkRep();
	}
	
/*	@requires 	node != null
 *	@param 		node we are adding to this Graph
 *	@effects 	adjList
 *	@modifies 	we add node as a key in the adjList if it doesn't
 *		already exist
 *	@return 	false if node was already in adjList, true if node
 *		was not already in adjList and we just added it	
 */
	public boolean addNode(U node)
	{
		if(contains(node))
			return false;
		else
		{
			adjList.put(node, new HashSet<Edge<U,V>>());
			checkRep();
			return true;
		}
	}
	
/*	@requires 	n.a. (exception handles null parameters)
 *	@param 		source node s, receiver node r, label l
 *	@modifies 	adjList
 *	@effects 	Add an edge to the set of edges mapped to
 * 		in adjList from source node s, if the set doesn't
 *		already contain that edge and both the nodes exist
 *		in the graph
 *	@throws 	RuntimeException if s == null or r == null
 *	@return 	true if the edge is added or if it already
 *		exists, false if one of the nodes don't exist in the
 *		graph, and thus, the edge couldn't be added		
 */	
	public boolean addEdge(U s, U r, V l)
	{
		if(s == null || r == null)
			throw new RuntimeException("At least one of first two arguments are null.");
		if (!contains(s) || !contains(r))	//if either of the nodes don't exist
			return false;
		Edge<U,V> new_edge = new Edge<U,V>(r,l);
		Set<Edge<U,V>> currentEdges = adjList.get(s);
		//check if edge exists already
		for (Edge<U,V> edge : currentEdges)
		{
			if(edge.equals(new_edge))
				return true; // NEW CHANGE, so we track edge weight in hw6
		}
		currentEdges.add(new_edge);
		return true;		
	}
	
/*	Removes an edge with same contents as the one 
 * 	passed in as an argument from the graph, if such
 * 	an edge exists. 	
 * 	
 * 	@requires 	n.a. (exception handles null parameters)
 *	@param 		source - source node s; edge - Edge object 
 *		identical to the one we wish to remove, that is 
 *		directed from the source
 *	@modifies 	list of edges
 *	@effects 	add an edge to the list of edges, if it
 *		doesn't already contain that edge and both the
 *		nodes exist in the graph
 *	@throws 	RuntimeException if edge == null
 *	@return 	true if an edge was removed, and false if
 * 		there was never a matching edge to remove.
 */		
	public boolean remove(U source, Edge<U,V> edge)
	{
		if (edge == null)
			throw new RuntimeException("Argument is null.");
		Set<Edge<U,V>> currentEdges = adjList.get(source);
		for (Edge<U,V> tempEdge : currentEdges)
		{
			if(edge.equals(tempEdge))
			{
				currentEdges.remove(tempEdge);
				return true;
			}				
		}
		return false;	
	}	
	
/*	Written specifically for Graph with only a single
 *  directed edge between characters, regardless of label.
 *  Returns the edge from source node s, to receiver node r.
 *  If one does not exist yet, returns null.
 * 
 * 	@requires 	n.a. (exception handles null parameters)
 *	@param 		source node s, receiver node r
 *	@modifies 	n.a.
 *	@effects 	n.a.
 *	@throws 	RuntimeException if s == null, r == null, or
 *		either of the nodes don't exist in the graph
 *	@return 	the directed Edge from s to r, if it exists,
 *		and null if it does not
 */	
	public Edge<U,V> getEdge(U s, U r)
	{
		if(s == null || r == null)
			throw new RuntimeException("At least one of the arguments is null.");			
		if (!contains(s) || !contains(r))	//if either of the nodes don't exist
			throw new RuntimeException("At least one of the nodes don't exist.");
		//try and get the edge connecting the two
		Set<Edge<U,V>> currentEdges = adjList.get(s);
		for (Edge<U,V> edge : currentEdges)
			if(edge.getReceiver().equals(r))
				return edge;
		return null;		
	}	

/*
 *	@requires 	node != null
 *	@param 		node - node were searching for
 *	@return 	true if node exists in list of nodes, false if not
*/
	public boolean contains(U node)
	{
		if (adjList.keySet().contains(node))
			return true;
		else
			return false;
	}
		
/* 	@requires 	parent and child != null
 *	@param 		parent, child; supposed parent and child whose 
 *		relationship we're confirming
 *	@return 	true if child (param) is a child of parent
 *		(param), false otherwise 													
 */
	public boolean hasChild(U parent, U child)
	{
		if(!contains(parent) || !contains(child))
			return false;
		for(Edge<U,V> edge : adjList.get(parent))
		{
			if(edge.getReceiver().equals(child))
				return true;
		}
		return false;
	}
	
//	@return list of edges in String format
	public ArrayList<String> getEdges()
	{
		ArrayList<String> list = new ArrayList<String>();
		for (U parent : adjList.keySet())
		{
			for (Edge<U,V> edge : adjList.get(parent))
			{
				 list.add(parent + ", " + edge.getReceiver() + ", " + edge.getLabel());
			}
		}
		return list;
	}	
	
/*
 * 	Return a set of all the edges with source node parent
 * 
 * 	@param		parent - source node for all the edges in the set we'll return
 * 	@requires 	n.a.
 * 	@modifies 	n.a.
 * 	@effects 	n.a.
 * 	@throws 	RuntimeException if parent == null or if parent doesn't exist in Graph
 * 	@return 	set of edges with source node parent
 */
	public Set<Edge<U,V>> getEdges(U parent)
	{
		return adjList.get(parent);
	}	
	
	
//	@throws RuntimeException if the rep. invariant is violated, but rep will never be violated and can't be tested w/o a remove function
	private void checkRep() throws RuntimeException
	{
		for(U parent : adjList.keySet())
		{
			for(Edge<U,V> edge : adjList.get(parent))
			{
				if(!contains(edge.getReceiver()))
					throw new RuntimeException("Can't have edge with unadded nodes.");
			}
		}
	}
}
