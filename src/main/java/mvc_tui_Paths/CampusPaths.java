package mvc_tui_Paths;

// 	CampusPaths is the driver class for the Text-based User Interface CampusPaths MVC

public class CampusPaths
{	
	public static void main(String[] args) 
	{	
		String nodeFile, edgeFile;
		if(args == null || args.length < 2)
		{
			nodeFile = "data/RPI_map_data_Nodes.csv";
			edgeFile = "data/RPI_map_data_Edges.csv";
		}
		else
		{
			nodeFile = args[0];	
			edgeFile = args[1];
		}
		CampusController controller = new CampusController(nodeFile, edgeFile);//instantiate controller
		controller.readCommands(); // loop through commands until user quits
	}
}