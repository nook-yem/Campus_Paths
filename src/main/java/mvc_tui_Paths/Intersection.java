package mvc_tui_Paths;;

/*
* 	Abstraction Function: 	Intersection represents a type of location, a non-building location, in
* 						the real world that is simulated in Quadrant IV of a Cartesian grid with
* 						cooridnates x and y (stored in x and y). It will have a unique ID number
* 						to distinguish it from others (stored in id).
* 
* 	RI: 	x >= 0, y >= 0, id >= 0, name != null, name != ""
*/

public class Intersection extends Location
{
	/*
	 * constructor (same as Location superclass)
	 * 
	 * 	@param 		id - building id
	 * 				x - x-coordinate
	 * 				y - y-coordinate
	 * 	@modifies 	this.name, this.id (super), this.x (super), this.y (super)
	 *	@effects 	initializes them to respective parameters
	 *	@throws 	RuntimeException if x < 0 || y < 0 || id < 0
	 */	
	public Intersection(int id, int x, int y) 
	{
		super(id, x, y);
		checkRep();
	}
	
	// @throws RuntimeException if rep. invariant (defined above) is violated
	private void checkRep() throws RuntimeException
	{
		if(getID() < 0 || getX() < 0 || getY() < 0)
			throw new RuntimeException("Rep. Invariant violated.");
	}
}
