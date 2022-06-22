package gui_Paths;

import javax.swing.JFrame;
import mvc_tui_Paths.CampusModel;

public class RPICampusPathsMain 
{
	public static void main(String[] args) 
	{			
		CampusModel model = new CampusModel();
		model.createNewGraph("data/RPI_map_data_Nodes.csv","data/RPI_map_data_Edges.csv");
		// Location coordinates are relative to a 2057x1921 Quadrant IV Cartesian grid
		RPICampusView view = new RPICampusView("RPI Campus Paths", model);
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view.setVisible(true);				
	}
}