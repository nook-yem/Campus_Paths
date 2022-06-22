package mvc_tui_Paths;

/*
 * 	Abstraction Function: Location represents a location in the real world that is simulated
 * 						Quadrant IV of a Cartesian grid with cooridnates x and y. Each location
 * 						should have a unique ID number to distinguish it from others (stored in id).
 * 
 * 	RI: 		x >= 0, y >= 0, id >= 0
 */

public abstract class Location implements Comparable<Location>
{
	private int id, x, y; // unique building id, x-coordinate, and y-coordinate in a Quadrant IV cartesian grid
	// *name of Location will be empty String unless instantiated as a Building; member declared in subclasses
	
	/*
	 * constructor
	 * 
	 * 	@param 		id - building id
	 * 				x - x-coordinate
	 * 				y - y-coordinate
	 * 	@modifies 	this.id, this.x, this.y
	 *	@effects 	initializes them to respective parameters
	 *	@throws 	IllegalArgumentException if x < 0 || y < 0 || id < 0
	 */	
	Location (int id, int x, int y)
	{
		if (id < 0 || x < 0 || y < 0)
			throw new IllegalArgumentException("ID, x-coordinate, and y-coordinate must be non-negative.");
		this.id = id;
		this.x = x;
		this.y = y;
		checkRep();
	}
	
	
	// Observer methods	
	
	// @returns this.id
	public int getID()
	{
		return id;
	}
	
	// @returns this.x
	public int getX()
	{
		return x;
	}
	
	// @returns this.y
	public int getY()
	{
		return y;
	}
	
	// @returns empty String
	public String getName()
	{
		return "";
	}
	
	// @throws RuntimeException if rep. invariant (defined above) is violated
	private void checkRep() throws RuntimeException
	{
		if(getID() < 0 || getX() < 0 || getY() < 0)
			throw new RuntimeException("Rep. Invariant violated.");
	}
	
/*
 * 	Intersection objects are sorted come after all Building objects
 * 	No need to compare two Intersection objects, so that comparison is not defined
 * 	@param b - Location we are comparing this to
 * 	@requires 	b != null
 * 	@modifies 	n.a.
 * 	@effects	n.a.
 * 	@throws		n.a.
 * 	@returns 	0 if this and b are both intersections or both buildings with the same name (won't happen);
 * 				-1 if this is a building and b is an intersection, or if both are buildings and this.name
 * 				comes lexicographically first;
 * 				1, if this is an intersection and b is a building, or if both are buildings and this.name
 * 				comes lexicographically second to b.name;
 */
	@Override
	public int compareTo(Location b)
	{
		//repeats aren't allowed in a Graph, so no two buildings will share the same name
		String aName = getName();
		String bName = b.getName();
		if (aName.length() == 0)
		{
			if(bName.length() == 0)
				return 0;
			else
				return 1;
		}
		else if (bName.length() == 0)
			return -1;
		return aName.compareTo(bName);		
	}	
}

