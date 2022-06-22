package generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import adt.*;

// Wrapper class for Dijkstra's search algorithm (class method)
public abstract class DijkstrasSearch <U extends Comparable<U>>
{
	// Generic Typing: U is the data type of the nodes of the graph we are traversing
	
	/*
	 * Dijkstra's search algorithm
	 * Assumes we use a graph with all nonnegative weights for edge labels
	 * Finds least expensive path from starting node, to destination node
	 * 
	 * 	@param 		start - node the path will start from;
	 * 				dest - destination node for the path
	 * 				graph - graph containing all nodes and edges
	 * 	@requires 	n.a. 
	 * 	@modifies 	n.a.
	 * 	@effects	n.a.
	 * 	@throws 	RuntimeException if start, dest, or graph is null
	 * 				RuntimeException if start and/or dest don't exist in graph
	 * 	@returns 	an ordered List of Edges representing the minimum-cost
	 * 		path from start to dest, if a path connecting the two exists;
	 * 		returns null if such a path does not exist. Path starts with
	 * 		reflexive edge to start
	*/
	
	public static <U extends Comparable<U>> ArrayList<Edge<U,Double>> findMinPath(U start, U dest, Graph<U,Double> graph) // problem must be something in here
	{
		if(start == null || dest == null || graph == null)
			throw new RuntimeException("At least one of the arguments is null.");
		if(!graph.contains(start) || !graph.contains(dest))
			throw new RuntimeException("Start and or destination node don't exist.");
		PriorityQueue<ArrayList<Edge<U,Double>>> active = new PriorityQueue<ArrayList<Edge<U,Double>>>(20, new SortPathByWeight<U>());
		List<U> finished = new ArrayList<U>(); // set won't instantiate for generics
		
		//add path from start to itself (weight of 0)
		ArrayList<Edge<U,Double>> newPath = new ArrayList<Edge<U,Double>>();
		newPath.add(new Edge<U,Double>(start,Double.valueOf(0)));
		active.add(newPath);
		
		//declarations
		ArrayList<Edge<U,Double>> minPath;
		U minDest;
		while(active.size() != 0)
		{
			minPath = active.poll();
			minDest = minPath.get(minPath.size() - 1).getReceiver();
			
			if(minDest.equals(dest)) //successfully found min-cost path
				return minPath;
			
			if(containsEquals(finished, minDest))
				continue;
			
			for(Edge<U,Double> edge : graph.getEdges(minDest))
			{
				if(!containsEquals(finished, edge.getReceiver()))
				{
					newPath = new ArrayList<Edge<U,Double>>(minPath);
					newPath.add(edge);
					active.add(newPath);
				}
			}
			finished.add(minDest);			
		}
		return null; // there is no path connecting source to dest		
	}	
	
/*
 * 	Collection.contains() compares the addresses of object rather than using .equals()
 * 	This method will provide that missing functionality.
 * 
 * 	@param		group - collection of objects we are traversing
 * 			i	tem - object we are comparing objects of item to for equality
 * 	@requires 	n.a.
 * 	@modifies 	n.a.
 * 	@effects 	n.a.
 * 	@throws 	RuntimeException if any argument is null
 * 	@returns 	true if T object with same contents as item exists in group,
 * 		false otherwise 
 * 
 */
	public static <T> boolean containsEquals(Collection<T> group, T item)
	{
		for(T tempItem : group)
			if(item.equals(tempItem))
				return true;
		return false;
	}			
}
