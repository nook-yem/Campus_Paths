package adt;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Iterator;

public class GraphWrapperTest
{
	GraphWrapper graph = new GraphWrapper();
	
	@Test
	public void testAddNode()
	{
		graph.clear();
		graph.addNode("A");
		Iterator<String> itr = graph.listNodes();
		assertEquals("A", itr.next());
		assertEquals(false, itr.hasNext());
	}

	@Test
	public void testContains()
	{
		graph.clear();
		graph.addNode("A");
		assertEquals(false, graph.contains("B"));
		assertEquals(true, graph.contains("A"));
	}
	
	@Test
	public void testHasChild()
	{
		graph.clear();
		assertEquals(false, graph.hasChild("A","B"));
		graph.addNode("A");
		assertEquals(false, graph.hasChild("A","B"));
		graph.addNode("B");
		assertEquals(false, graph.hasChild("A","B"));
		graph.addEdge("A", "B", "a");
		assertEquals(true, graph.hasChild("A","B"));
	}
	
	@Test
	public void testListNodeOrder()
	{
		graph.clear();
		graph.addNode("N");
		graph.addNode("W");
		graph.addNode("C");
		graph.addNode("G");
		graph.addNode("B");
		graph.addNode("X");
		graph.addNode("D");
		graph.addNode("A");
		graph.addNode("T");
		graph.addNode("O");
		Iterator<String> itr = graph.listNodes();
		assertEquals("A", itr.next());
		assertEquals("B", itr.next());
		assertEquals("C", itr.next());
		assertEquals("D", itr.next());
		assertEquals("G", itr.next());
		assertEquals("N", itr.next());
		assertEquals("O", itr.next());
		assertEquals("T", itr.next());
		assertEquals("W", itr.next());
		assertEquals("X", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testTestMethods() //mostly for EclEmma; these functions only exist for testing
	{
		graph.clear();
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addEdge("A", "B", "a");
		graph.addEdge("B", "B", "b");
		graph.addEdge("A", "B", "c");
		graph.addEdge("A", "C", "a");
		graph.print(graph.getEdges());
	/*	should print:
		("A", "B", "a")
		("B", "B", "b")
		("A", "B", "c")
		("A", "C", "a")		*/
		graph.print(graph.getChildren("A"));
	/*	should print:
		"B"
		"C"		*/
		graph.print(graph.getParents("B"));
	/*	should print:
		"A"
		"B"		*/	
		
	}
	
	@Test
	public void testAddDuplicates()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("A");
		Iterator<String> itr = graph.listNodes();
		assertEquals("A", itr.next());
		assertEquals(false, itr.hasNext());	
	}
	
	@Test
	public void testListWhenNoChildren()
	{
		graph.clear();
		Iterator<String> itr = graph.listNodes();
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testAddFirstEdge()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addEdge("A", "B", "a");
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testAddMultEdges()
	{
		graph.clear();
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addNode("D");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "C", "b");
		graph.addEdge("A", "D", "c");
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("D(c)", itr.next());
	}
	
	@Test
	public void testListChildren()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addNode("C"); 
		graph.addNode("Q");
		graph.addNode("V"); 
		graph.addNode("X");
		graph.addNode("W"); 
		graph.addNode("Z");
		graph.addEdge("A", "W", "a");
		graph.addEdge("A", "W", "a"); // test repeating edge branch case
		graph.addEdge("W", "X", "a"); // test non-child of A branch case
		graph.addEdge("A", "C", "b");
		graph.addEdge("A", "W", "c");		
		graph.addEdge("A", "V", "d");
		graph.addEdge("A", "X", "e");
		graph.addEdge("A", "X", "f");
		graph.addEdge("A", "Q", "g");		
		graph.addEdge("A", "Z", "h");
		graph.addEdge("A", "A", "z");
		graph.addEdge("A", "X", "i");
		graph.addEdge("A", "C", "j");
		graph.addEdge("A", "B", "k");
		Iterator<String> itr = graph.listChildren("A");
		//checks sort too
		assertEquals("A(z)", itr.next());
		assertEquals("B(k)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("C(j)", itr.next());
		assertEquals("Q(g)", itr.next());		
		assertEquals("V(d)", itr.next());
		assertEquals("W(a)", itr.next());
		assertEquals("W(c)", itr.next());
		assertEquals("X(e)", itr.next());
		assertEquals("X(f)", itr.next());
		assertEquals("X(i)", itr.next());
		assertEquals("Z(h)", itr.next());		
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testAddMultSourceEdges()
	{
		graph.clear();
		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addNode("D");
		graph.addEdge("A", "B", "a");
		graph.addEdge("B", "C", "b");
		graph.addEdge("B", "D", "c");
		graph.addEdge("A", "B", "d");
		graph.addEdge("B", "C", "e");
		graph.addEdge("A", "D", "f");
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals("B(d)", itr.next());
		assertEquals("D(f)", itr.next());
		assertEquals(false, itr.hasNext());
		Iterator<String> itr2 = graph.listChildren("B");
		assertEquals("C(b)", itr2.next());
		assertEquals("C(e)", itr2.next());
		assertEquals("D(c)", itr2.next());
		assertEquals(false, itr2.hasNext());
		
	}
	
	@Test
	public void testInvalidEdge()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "C", "b"); //there is no node "C"
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test(expected = RuntimeException.class)
	public void testNullEdgeConstruction()
	{
		graph.clear();
		graph.addEdge(null, null, null);	
	}
	
	@Test
	public void testNullLabel()
	{
		graph.addNode("A");
		graph.addEdge("A", "A", null);
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("A()", itr.next());
		assertEquals(false, itr.hasNext());	
	}

	@Test
	public void testEmptyLabelEdges()
	{
		graph.addNode("A");
		graph.addNode("B");
		graph.addEdge("A", "A", "");
		graph.addEdge("A", "B", "");
		graph.addEdge("A", "A", "");
		graph.print(graph.getEdges());
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("A()", itr.next());
		assertEquals("B()", itr.next());
		assertEquals(false, itr.hasNext());	
	}
		
	@Test
	public void testUnrelatedEdges()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addNode("C"); 
		graph.addNode("D");
		graph.addNode("E"); 
		graph.addNode("F");
		graph.addEdge("A", "B", "e");
		graph.addEdge("B", "E", "f");
		graph.addEdge("A", "C", "b");
		graph.addEdge("B", "D", "g");
		graph.addEdge("A", "F", "a");
		graph.addEdge("B", "A", "d");
		graph.addEdge("B", "C", "c");
		assert(graph.hasChild("B", "A"));
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(e)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("F(a)", itr.next());
		assertEquals(false, itr.hasNext());
		Iterator<String> itr2 = graph.listChildren("B");
		assertEquals("A(d)", itr2.next());
		assertEquals("C(c)", itr2.next());
		assertEquals("D(g)", itr2.next());
		assertEquals("E(f)", itr2.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testDuplicateEdge()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addNode("C"); 
		graph.addNode("D");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "C", "b");
		graph.addEdge("A", "D", "c");
		graph.addEdge("A", "B", "a"); //duplicate
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("D(c)", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testFakeDuplicateEdge()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addNode("C"); 
		graph.addNode("D");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "C", "b");
		graph.addEdge("A", "D", "c");
		graph.addEdge("A", "C", "d"); //different label so NOT a duplicate
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("C(d)", itr.next());
		assertEquals("D(c)", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testFakeDuplicateEdge2()
	{
		graph.clear();
		graph.addNode("A"); 
		graph.addNode("B");
		graph.addNode("C"); 
		graph.addNode("E");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "E", "a"); //different child so NOT a duplicate		
		graph.addEdge("A", "C", "b");
		graph.addEdge("B", "C", "b"); //different parent so NOT duplicate
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("B(a)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals("E(a)", itr.next());
		assertEquals(false, itr.hasNext());
		Iterator<String> itr2 = graph.listChildren("B");
		assertEquals("C(b)", itr2.next());
		assertEquals(false, itr2.hasNext());
	}	
	
	@Test
	public void testReflexiveEdge()
	{
		graph.addNode("A"); 
		graph.addNode("B"); 
		graph.addNode("C");
		graph.addEdge("A", "B", "a");
		graph.addEdge("A", "C", "b");
		graph.addEdge("A", "A", "d");	//reflexive
		graph.addEdge("A", "A", "e");	//reflexive
		graph.addEdge("A", "A", "c");	//reflexive
		Iterator<String> itr = graph.listChildren("A");
		assertEquals("A(c)", itr.next());
		assertEquals("A(d)", itr.next());
		assertEquals("A(e)", itr.next());
		assertEquals("B(a)", itr.next());
		assertEquals("C(b)", itr.next());
		assertEquals(false, itr.hasNext());
	}
		
	@Test
	public void testListNodeOrder2()
	{
		graph.clear();
		graph.addNode("Q"); 
		graph.addNode("B");
		graph.addNode("Z"); 
		graph.addNode("D");
		graph.addNode("B"); //duplicate
		graph.addEdge("B", "Krabby Patty", "invalid"); //random invalid edge
		graph.addEdge("B", "B", "B"); //random reflexive edge
		graph.addNode("E"); 
		graph.addNode("R");
		graph.addNode("H"); 
		graph.addNode("G");
		Iterator<String> itr = graph.listNodes();
		assertEquals("B", itr.next());
		assertEquals("D", itr.next());
		assertEquals("E", itr.next());
		assertEquals("G", itr.next());
		assertEquals("H", itr.next());
		assertEquals("Q", itr.next());
		assertEquals("R", itr.next()); 
		assertEquals("Z", itr.next());
		assertEquals(false, itr.hasNext());
	}
	
	@Test
	public void testListMultReflexiveEdges()
	{
		graph.addNode("A"); 
		graph.addNode("B"); 
		graph.addNode("C");		
		graph.addEdge("C", "A", "label1"); //reflexive
		graph.addEdge("A", "A", "label1"); //reflexive repeat
		graph.addEdge("A", "A", "label1"); //reflexive repeat
		graph.addEdge("C", "C", "label2");
		graph.addEdge("C", "B", "label3"); 
		Iterator<String> itr = graph.listChildren("C");
		System.out.println("here");
		System.out.println(itr.next());
		System.out.println(itr.next());
		System.out.println(itr.next());		
		/*	should print:
			("A", "A", "label1")
			("B", "B", "label3")
			("C", "C", "label2")	*/
		itr = graph.listChildren("C");
		assertEquals("A(label1)", itr.next());
		assertEquals("B(label3)", itr.next());
		assertEquals("C(label2)", itr.next());
		assertEquals(false, itr.hasNext());

	}
	
}