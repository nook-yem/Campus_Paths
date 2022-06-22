package mvc_tui_Paths;

import java.util.List;
import java.util.Scanner;
import adt.*;

/*
 * 	Controller class for MVC program.
 * 	Used by driver class, CampusPaths, to handle user input and respond accordingly.
 * 	Delegates to internal model and view objects appropriately. 
 * 	CampusController is not an ADT.
 */

public class CampusController
{
	private CampusModel model; // CampusModel object containing data
	private CampusView view; // CampusView object handling output to the user
	
/*	
 * 	Constructor
 * 	@param 		nodeFile - String name of file that's holding node data to be parsed
 * 				edgeFile - String name of file that's holding edge data to be parsed
 * 	@requires 	nodeFile != null, edgeFile !- null
 * 	@modifies	model, view
 * 	@effects	model and view are instantiated; model.graph is filled with data parsed
 * 			from nodeFile and edgeFile
 * 	@throws 	IOException if a file cannot be read or a file is not a CSV file following the proper format.
 * 	@returns 	n.a.
 */	
	public CampusController(String nodeFile, String edgeFile) 
	{
		model = new CampusModel();
		model.createNewGraph(nodeFile, edgeFile);
		view = new CampusView();
	}
	
/*
 * 	This method runs for the majority of the program. It takes user input and directs program to respond
 *  	accordingly, delegating to model and view where appropriate.
 * 	@param 		n.a.
 * 	@requires 	n.a.
 * 	@modifies	n.a.
 * 	@effects	n.a.
 * 	@throws 	n.a. 
 * 	@returns 	n.a.
 */

	public void readCommands()
	{
		Scanner scan = new Scanner(System.in);
		String command = scan.next();
		String s = ""; // source name/ID
		String r = ""; // source name/ID
		int i = 0;
		while(!command.equals("q"))
		{		
			if (command.equals("b"))
				view.printBuildings(model.listNodes()); // may want to write model method to return map.keySet()				
			else if (command.equals("r"))
			{
				s = "";
				r = "";
				// take source and receiver building from input
				view.requestFirstBuilding();
				while(s.length() == 0)
					s = scan.nextLine();
				Building source = model.getBuilding(s);				
				view.requestSecondBuilding();
				scan.hasNextLine(); // block Scanner until user enters input
				while(r.length() == 0)
					r = scan.nextLine();				
				Building receiver = model.getBuilding(r);
				List<Edge<Location,Double>> minPath;
				// check if source and receiver exist
				if(view.checkNodes(source, receiver,s,r))
				{
					minPath = model.findPath(source, receiver);
					view.printPath(minPath, source, receiver); //parse minPath for view
				}
			}
			else if(command.equals("m")) // print menu
				view.printMenu(); // we'll allow CampusView to know about menu options by default
			else // unknown command
				view.printBadCommand(); 
			command = scan.nextLine();
			if(i==0 && command.length() == 0) // set pattern
				command = scan.nextLine();
			i++;
		}
		scan.close();
	}
}
