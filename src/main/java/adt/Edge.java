package adt;
import java.util.*;

/*	
 * 	Overview: 
 * 		A representation of an edge in a directed labeled
 * 		multigraph. Contains a receiver node, and an edge
 * 		label.
 *
 *	Abstraction Function: 
 *		An Edge maps from start to end and includes label,
 *		which contains some information on the Edge

 *	Rep Invariant: 
 *		end != null
 *
 *	Regarding use of Generic Typing:
 * 		U is the type for the nodes in the graph, V is the type for the label
*/
public class Edge<U,V> 
{
	private final U end;	//receiver node; source node points to this node
	private final V label;		//label w/ info on edge
	
// constructor	
/*	@requires 	s != null and r != null
 *	@param 		source node, receiver node, and label
 *	@modifies 	start, end, label
 *	@effects 	start = s, end = r, label = l 		*/
	public Edge(U r, V l)
	{
		end = r;
//		if (l == null)	// useless after ADT went generic
//			label = "";
//		else
			label = l;		
		checkRep();
	}
		
//	@return end (receiver node)
	public U getReceiver()
	{
		return end;
	}
	
//	@return label
	public V getLabel()
	{
		return label;
	}
	
//	@param edge that is being compared to this object
//	@return true if they hold all the same info, else false
	public boolean equals(Edge<U,V> e)
	{
		if (end == e.getReceiver() && label == e.getLabel())
			return true;
		else
			return false;
	}
	
//	@throws a RuntimeException if the rep. invariant
	private void checkRep() throws RuntimeException
	{
		if (end == null)
			throw new RuntimeException("Need a source node and a receiver node.");
	}
	
}

class SortbyChild<U extends Comparable<U>,V extends Comparable<V>> implements Comparator<Edge<U,V>>
{
	// Override
	// only guaranteed to work for listChildren()
/*	@requires 	a and b != null
 *	@param 		Edge objects a and b
 *	@return 	-1 if a < b; 1 if a > b  
 */
	public int compare(Edge<U,V> a, Edge<U,V> b)
	{
		//repeats aren't allowed in Graph, so a can't equal b
		if (a.getReceiver().compareTo(b.getReceiver()) == 0) //if both edges map same nodes
			return a.getLabel().compareTo(b.getLabel()); //compare labels
		else //compare children
			return a.getReceiver().compareTo(b.getReceiver());		
	}
}
