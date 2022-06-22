package adt;

import java.util.ArrayList;
import java.util.Iterator;

/*	Overview: The purpose of GraphWrapper is to allow us
 *  to test our Graph ADT through a common interface.
 */
public class GraphWrapper
{
	private Graph<String,String> graph;
	
//	constructor	
/*	@modifies 	graph
 * 	@effects 	instantiates graph
 */
	public GraphWrapper()
	{
		graph = new Graph<String,String>(); 
	}
	
/*	@requires 	nodeData != null
 *	@param 		nodeData (node we are adding)
 *	@effects 	graph
 *	@modifies 	we add node to the graph.nodes; if it 
 *	isn't already inside the list, we add it to the list											
 */
	public void addNode(String nodeData)
	{
		graph.addNode(nodeData);
	}
/*	@requires 	parentNode and childNode != null
 *	@param 		parentNode (source), childNode (receiver), edgeLabel (label)
 *	@effects 	graph
 *	@modifies 	we add edge to the graph.adjList, IFF nodes exist
 *		in the graph and this particular edge does not											*/	
	public boolean addEdge(String parentNode, String childNode, String edgeLabel)
	{
		return graph.addEdge(parentNode, childNode, edgeLabel);
	}
	
/* 	@return 	Iterator<String> on an ArrayList of each node in alphabetical
 *		(lexicographical) order		
 */
	public Iterator<String> listNodes()
	{
		return graph.listNodes();
	}
	
/*	@requires 	parentNode != null
 * 	@param 		parentNode - node whose children we're finding
 * 	@return 	Iterator<String> on an ArrayList of each 
 * 		child_node(edge_label) of parent 
 * */
	public Iterator<String> listChildren(String parentNode)
	{
		return graph.listChildren(parentNode);
	}
	
/*	@effects 	graph
 * 	@modifies 	sets all data fields to empty
 */
	public void clear()
	{
		graph.clear();
	}
	
	
//the following methods are for TESTING:	
/*
	@requires 	node != null
	@param 		node - node were searching for
	@return 	true if node exists in graph; false if not	
*/
	public boolean contains(String node)
	{
		return graph.contains(node);
	}	
	
/*  
 *	@requires 	parent and child != null
 *	@param 		parent, child - supposed parent and child
 *		nodes whose relationship is being confirming
 *	@return 	true if child (param) is a child of parent 
 *		(param), false otherwise 													
 */
	public boolean hasChild(String parent, String child)
	{
		return graph.hasChild(parent, child);
	}
	
/*	
 * @requires 	parent != null
 * 	@param 		parent node whose children we're finding
 *  @return 	ArrayList of children nodes				 
 */
	public ArrayList<String> getChildren(String parent)
	{
		return graph.getChildren(parent);
	}	
	
/*	
 * 	@requires 	child != null
 * 	@param 		child node whose parents we're finding
 *  @return 	ArrayList of parent nodes				 
 */
	public ArrayList<String> getParents(String child)
	{
		return graph.getParents(child);
	}		
	
// 	@return 	list of edges in graph in String format
	public ArrayList<String> getEdges()
	{
		return graph.getEdges();
	}
	
//	@param list - list of Strings being printing
	public void print (ArrayList<String> list)
	{
		for (String s : list)
			System.out.println(s);
	}	
}