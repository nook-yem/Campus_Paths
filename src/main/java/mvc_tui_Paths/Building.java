package mvc_tui_Paths;

/*
* 	Abstraction Function: Building represents a type of location, specfically a building,
* 						in the real world that is simulated in Quadrant IV of a Cartesian grid with
* 						cooridnates x and y (stored in x and y). A building will have a unique ID
* 						number to distinguish it from others (stored in id), just as any other 
* 						location, but will also have name (stored in name).
* 
* 	RI: 	x >= 0, y >= 0, id >= 0, name != null, name is not an empty String
*/

public class Building extends Location
{
	String name; // name of building
	
	/*
	 * constructor
	 * 
	 * 	@param 		name - building name
	 * 				id - building id
	 * 				x - x-coordinate
	 * 				y - y-coordinate
	 * 	@modifies 	this.name, this.id (super), this.x (super), this.y (super)
	 *	@effects 	initializes them to respective parameters
	 *	@throws 	IllegalArgumentException if x < 0 || y < 0 || id < 0 || name == null || name is not an empty String
	 */		
	public Building(String name, int id, int x, int y) 
	{
		super(id,x,y);
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException("Invalid argument. Building must have a name.");
		this.name = name;
		checkRep();
	}
		
	// @returns this.name
	@Override
	public String getName()
	{
		return name;
	}
	
	// @throws RuntimeException if rep. invariant (defined above) is violated
	private void checkRep() throws RuntimeException
	{
		if(name == null || name.length() == 0 || getID() < 0 || getX() < 0 || getY() < 0)
			throw new RuntimeException("Rep. Invariant violated.");
	}
}