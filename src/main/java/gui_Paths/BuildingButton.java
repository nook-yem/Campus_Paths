package gui_Paths;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import mvc_tui_Paths.Building;

public class BuildingButton extends JButton 
{
	// MyButtonListener is a Listener class for BuildingButton
	private static class MyButtonListener implements ActionListener 
	{
		RPICampusMap canvas;
		public MyButtonListener(RPICampusMap canvas) {
			this.canvas = canvas;
		}
		// if a button is pressed by the user, we call selectButton() or deselectButton() (whichever changes button's state)
		@Override
		public void actionPerformed(ActionEvent e) {
			BuildingButton button = (BuildingButton)e.getSource();
			if(!button.isSelected())
			{
				canvas.selectButton(button);
			}
			else
			{
				canvas.deselectButton(button);
			}
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	// important data fields
	private boolean selected;
	private Building building;
	private RPICampusMap canvas; // needed to instantiate MyButtonLister
	public BuildingButton(Building b, RPICampusMap canvas) 
	{
	    super();
	    building = b;
	    this.canvas = canvas;
	    selected = false;
	    // add listener for button
	    addActionListener(new MyButtonListener(canvas));
    }
		
/*	
 * 	Selects this button
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	selected, this
 * 	@effects 	sets selected to true, sets background to be red and NOT transparent
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	public void select()
	{
		selected = true;        
		setBackground(new Color (255,0,0));        
		setContentAreaFilled(true);     
	}
	
/*	
 * 	De-selects this button
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	selected, this
 * 	@effects 	sets selected to false, sets this button to be transparent
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	public void deselect()
	{
		selected = false;        
		setContentAreaFilled(false);
	}
	
	// Accessors
	
// 	@return building
	public Building getBuilding()
	{
		return building;
	}
	
// 	@return selcted
	public boolean isSelected()
	{
		return selected;
	}
}