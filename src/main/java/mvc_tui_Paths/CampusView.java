package mvc_tui_Paths;

import java.util.Iterator;
import java.util.List;
import adt.*;

// 	Class handling view for CampusPaths; basically a Wrapper class of methods 
//		formatting and presenting output to user
// 	System.out will be directed to the appropriate ostream outside of this class
// 	CampusView is not an ADT.

public class CampusView 
{
	// Constructor
		// we don't actually have any unique data for an individual CampusView object to store
	public CampusView() 
	{
		
	}
	
/*
 * 	Prints path specified in argument in desired format with desired info
 * 		Does end up doing computation for direction and total distance
 * 	@param 		path - List of edges that form the path we are formatting
 *				s - source node (Building) for path
 *				r - receiver node (Building) for path
 *	@requires 	formatting of path matches the description in the @return specs of CampusModel.findPath();
 *			s != null, r != null, s is the Location in first Edge of path, r is the location in the
 *			last Edge of path
 *	@modifies	n.a
 *	@effects	n.a.
 *	@throws		n.a.
 *	@return 	n.a.
 */	
	public void printPath(List<Edge<Location, Double>> path, Building s, Building r)
	{
		// check for no path found (nodes exist)
		if(path == null)
		{
			System.out.println("There is no path from " + s.getName() + " to " + r.getName() + ".");
			return;
		}
		
		System.out.println("Path from " + s.getName() + " to " + r.getName() + ":");	
		Double totalDistance = Double.valueOf(0);
		Location first = s;
		Location second;
		double dx;
		double dy;
		String direction;
		String distStr;
		for(Edge<Location,Double> edge : path)
		{
			if(edge.getReceiver().getName().equals(s.getName()))// skip reflexive edge
				continue;					
			totalDistance += edge.getLabel(); // update total distance of path
			// calculate direction (first will be the origin, convert Quadrant IV coords. of
				// Buildings to Quadrant I in dx,dy computing)
			second  = edge.getReceiver();
			dx = second.getX() - first.getX(); // side adjacent to angle
			dy = first.getY() - second.getY(); // Quad IV -> Quad I, side opposite of angle;
			direction = findDirection(dx,dy);
			// output next direction
			if(second.getName().length() == 0)
				System.out.println("\tWalk " + direction + " to " + "(Intersection " + second.getID() + ")");
			else
				System.out.println("\tWalk " + direction + " to " + "(" + second.getName() + ")");
			first = second; // update first
		}
		// output total distance of path
		distStr = String.format("%.3f", totalDistance);
		System.out.println("Total distance: " + distStr + " pixel units.");
	}
	
/*
 * 	Checks if Locations we want to find a path between exist before we try.
 * 	Outputs to the user appropiately if at least one of them does not.
 * 	@param		source - Building that would be the source of the desired path;
 * 			null means it does not exist
 * 				receiver - Building that would be the destination of the desired path;
 * 			null means it does not exist
 * 				s - user given name/ID of source Building
 * 				r - user given name/ID of destination Building
 * 	@requires	s != null, r != null, s and r are not empty Strings
 * 	@return		true if both Buildings exist and we can attempt to find the shortest path;
 * 			false otherwise
 */
	public boolean checkNodes(Building source, Building receiver, String s, String r)
	{
		if (source == null || receiver == null) // one or both nodes doesn't exist
		{
			if (s.equals(r)) // reflexive case, node doesn't exist
				System.out.println("Unknown building: [" + s + "]");
			else if (receiver != null) // r exists, but s does not
				System.out.println("Unknown building: [" + s + "]");
			else if (source != null) // s exists, but r does not
				System.out.println("Unknown building: [" + r + "]");
			else // two are distinct and neither exist 
				System.out.println("Unknown building: [" + s + "]\nUnknown building: [" + r + "]");
			return false;
		}
		return true;
	}
	
/*
 *	Helper method for printPath to compute direction of an edge
 *	@param		dx - double holding x-coordinate differential in Quadrant I form
 *				dy - double holding x-coordinate differential in Quadrant I form
 *	@returns 	String name of direction (1 of 8) for the location differentials given
 */
	private String findDirection(double dx, double dy)
	{
		double angle;
		String direction;
		if (dx == 0) // can't divide by 0, so handle dx == 0 case before calling arctan(dy/dx)
		{
			if(dy > 0)
				angle = 0;
			else
				angle = 180;
		}			
		else
		{
			angle = Math.abs(Math.atan(dy/dx)*180/Math.PI); // # of degrees away from horizontal (0-90)
			// Use signs to determine if should be in Quadrants I,II,III, or IV and adjust accordingly
			if(dx > 0 && dy >= 0) // Quad I
				angle = 90 - angle;
			else if (dx > 0 && dy <= 0) // Quad II
				angle = 90 + angle;
			else if (dx < 0 && dy <= 0) //Quad III
				angle = 270 - angle;
			else // (dx < 0 && dy > 0) // Quad IV
				angle = 270 + angle;
		}
		// Interpret angle into one of 8 directions
		if (angle < 22.5 || (337.5 <= angle && angle < 360))
			direction = "North";
		else if (angle < 67.5)
			direction = "NorthEast";
		else if (angle < 112.5)
			direction = "East";
		else if (angle < 157.5)
			direction = "SouthEast";
		else if (angle < 202.5)
			direction = "South";
		else if (angle < 247.5)
			direction = "SouthWest";
		else if (angle < 292.5)
			direction = "West";
		else // angle < 337.5
			direction = "NorthWest";
		return direction;
	}
	
/*
 * 	Print all the Buildings (only Buildings) in the list that the iterator
 *  	is traversing in the desired format
 * 	@param 		locItr - Iterator over List of Locations in model
 *	@requires 	nameOrID != null
 *	@modifies	n.a
 *	@effects	n.a.
 *	@throws		n.a.
 *	@return 	n.a.
 */
	public void printBuildings(Iterator<Location> locItr)
	{
		Location location;
		while(locItr.hasNext())
		{
			location = locItr.next();
			if(location.getName().length() > 0) // if location is nameless then this is not a Building
				System.out.println(location.getName() + "," + location.getID());
		}
	}
	
	// Methods below are simple print statements, requiring no data. Constant output.
	
	// Print menu options available to the user
	public void printMenu()
	{
		System.out.println("b lists all buildings\n"
				+ "r prints directions for the shortest route between any two buildings\n"
				+ "q quits the program\n" + "m prints a menu of all commands");
	}
	
	// prompt user to input name/ID of source node building to form path
	public void requestFirstBuilding()
	{
		System.out.print("First building id/name, followed by Enter: ");
	}
	
	// prompt user to input name/ID of source node building to form path
	public void requestSecondBuilding()
	{
		System.out.print("Second building id/name, followed by Enter: ");
	}	
	
	// print that the command entered does not exist
	public void printBadCommand()
	{
		System.out.println("Unknown option");
	}	
}
