package gui_Paths;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;

// JScrollPane to hold canvas panel

public class MyJScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 2L;
	RPICampusMap canvasPanel;

	public MyJScrollPane(Component view, int vsbPolicy, int hsbPolicy) 
	{
		super(view, vsbPolicy, hsbPolicy);
		canvasPanel = (RPICampusMap)view;
		setColumnHeaderView(canvasPanel.createZoomPanel());
		setViewportView(canvasPanel);
	    addComponentListener(new ComponentAdapter() 
	    {  
	            public void componentResized(ComponentEvent evt) 
	            {
	                Component c = (Component)evt.getSource();
	                canvasPanel.setSize(new Dimension(c.getWidth(),c.getHeight())); // left room for scroll bars and title bar
//	                setSize(new Dimension(c.getWidth(), c.getHeight()));
	            }
	    });	  
	}
}
