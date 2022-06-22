package gui_Paths;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import mvc_tui_Paths.*;

// JFrame class for our main program

public class RPICampusView extends JFrame
{
	private static final long serialVersionUID = -4725346878809211984L;
	private MyJScrollPane campusMapScrollPane; //JPanel extension
	private CampusModel model;
	
	public RPICampusView(String title, CampusModel m) 
	{
		super(title);
		setLayout(new FlowLayout());
			// dimensions of png are 2175x3400 -> 87x136 ratio
				// dimensions of actual map are 2057x1921
		model = m;
		campusMapScrollPane = new MyJScrollPane(new RPICampusMap(/*87, 136, 6, */model), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); 	
		pack();
	    this.setContentPane(campusMapScrollPane); 
	    
	    int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setSize(screenWidth,(int)(screenHeight*0.95)); // leave a bit of room for start menu bar on some desktops
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);	
	    
	    // want this to resize panel when window is resized
	    addComponentListener(new ComponentAdapter() 
	    {  
	            public void componentResized(ComponentEvent evt) 
	            {
	                Component c = (Component)evt.getSource();	                
	                campusMapScrollPane.setSize(new Dimension(c.getWidth() - 12,c.getHeight() - 35)); // left room for scroll bars and title bar
	            }
	    });	    
	}		
}
