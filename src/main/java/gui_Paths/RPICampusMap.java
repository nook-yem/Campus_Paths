package gui_Paths;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;
import javax.swing.*;
import adt.Edge;
import mvc_tui_Paths.*;
;

// "Canvas" class for our RPI campus map

public class RPICampusMap extends JPanel implements Scrollable
{
	private static final long serialVersionUID = 1L;
	private int xStart, yStart, dragStartX, dragStartY, numSelected = 0;
	private double zoomRatio;
	private boolean firstRender, pathDrawn = false;
	private ArrayList<BuildingButton> selectedButtons;	
	private Set<BuildingButton> buildings;
	private Image image;
	private double widthScale, heightScale = 1.0;
    private static final Stroke POINTER_STROKE = new BasicStroke(4);
    private static final Color POINTER_COLOR = Color.BLUE;
    private CampusModel model;
	
	private int windowDimButtonScale = 35; // scale for buttonSize // may be specific to my desktop.
		
// Constructor
	
/*	@param		m - model containing map data
 * 	@requires 	m != null
 * 	@modifies 	this, (model, image, buildings, selectedButtons)
 * 	@effects 	initializes the members listed above; loads data from model to fill buildings
 * 				with a BuildingButton for each building node in the dataset; adds mouseListener
 * 				and mouseMotionListener to check for/handle user dragging to scroll; adds
 * 				componentListener to re-scale building buttons every time the panel/window is resized
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	public RPICampusMap(CampusModel m) 
	{
		super(null);
		ImageIcon icon = new ImageIcon("data/RPI_campus_map_2010_extra_nodes_edges.png");
		image = icon.getImage();
		model = m;	
		buildings = new HashSet<BuildingButton>();
		loadLocations(model.listNodes());
		selectedButtons = new ArrayList<BuildingButton>();
		
//	    mouse wheel listener to zoom with mouse wheel
	    addMouseWheelListener(new MouseWheelListener() 
	    {
	        public void mouseWheelMoved(MouseWheelEvent e) {
	            updatePreferredSize(-e.getWheelRotation());
	        }
	    });
	    // add mouse listener to drag scroll map
	    addMouseListener(new MouseListener() 
	    {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void mousePressed(MouseEvent e) 
			{				
				dragStartX = e.getPoint().x;
				dragStartY = e.getPoint().y;
			}
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				dragStartX = 0;
				dragStartY = 0;			
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// Idea: create a JLabel extension for each building (just need the name), that is 
					// only visible when the mouse enters the appropriate region				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub				
			}	        
	    });
	    // add mouse listener to drag scroll map
	    addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				dragCoords(e.getPoint().x,e.getPoint().y);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub				
			}	
		});	    
	    
	    addComponentListener(new ComponentAdapter() 
	    {  
	            public void componentResized(ComponentEvent evt) 
	            {
	                reScaleBuildings();
	            }
	    });	 
	}	
	
/*	
 * 	Sets the size of the panel to specified Dimension
 * 
 * 	@param		d - desired Dimension for the panel
 * 	@requires 	d != null
 * 	@modifies 	zoomRatio, buildings
 * 	@effects 	adjusts zoomRatio to be consistent so image zoom appears unchanged as panel size changes,
 * 				BuildingButtons in buildings are resized accordingly
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	@Override
	public void setSize(Dimension d)
	{		
		
		double oldStdXZoom = (double)getWidth()/(image.getWidth(this)*widthScale);
        double oldStdYZoom = (double)getHeight()/((image.getHeight(this)-1400)*heightScale);
        double zoomScale;
        if(oldStdXZoom < oldStdYZoom)
        	zoomScale = zoomRatio/oldStdXZoom;
        else
        	zoomScale = zoomRatio/oldStdYZoom;
		widthScale = (double)this.getSize().width / image.getWidth(this);
        heightScale = (double)this.getSize().height / image.getHeight(this);
        widthScale = widthScale < heightScale ? widthScale : heightScale;
        heightScale = heightScale < widthScale ? heightScale : widthScale; 
        super.setSize(d);         
		widthScale = (double)this.getSize().width / image.getWidth(this);
        heightScale = (double)this.getSize().height / image.getHeight(this);
        widthScale = widthScale < heightScale ? widthScale : heightScale;
        heightScale = heightScale < widthScale ? heightScale : widthScale; 		       

		double stdXZoom = (double)getWidth()/(image.getWidth(this)*widthScale);
        double stdYZoom = (double)getHeight()/((image.getHeight(this)-1400)*heightScale);
        if(stdXZoom < stdYZoom)
        	zoomRatio = stdXZoom*zoomScale;
        else
        	zoomRatio = stdYZoom*zoomScale;
        reScaleBuildings();		
	}	
	
/*	
 * 	Image is repainted with starting coordinates for adjusted starting coordinates for
 * 		painted image to account for user drag to simulate scrolling.
 * 
 * 	@param		x - new x-coordinate of the mouse when method is called
 * 				y - new y-coordinate of the mouse when method is called
 * 	@requires 	n.a.
 * 	@modifies 	xStart, ySytart, buildings, this
 * 	@effects 	Adjusts xStart and yStart to account for user drag scroll, buildingButton locations are
 * 			shifted accordingly. Image is repainted.
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void dragCoords(int x, int y)
	{
		boolean newRender = false;
		if(x - dragStartX > 0)
			newRender = true;
		else if (x - dragStartX < 0)
			newRender = true;
		if(y - dragStartY > 0)
			newRender = true;
		else if (y - dragStartY < 0)
			newRender = true;	
		if(newRender)
		{					
			int dx = (int)((x - dragStartX)*.025);
			int dy = (int)((y - dragStartY)*.025);
			xStart += dx;
			yStart += dy;
			reScaleBuildings();
			repaint();
		}		
	}
	
	
/*	
 * 	Creates control panel for this canvas panel with zoom and reset buttons, for user to interact with
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	this
 * 	@effects 	adds JPanel with interactable zoom and reset buttons to this
 * 	@throws 	n.a.
 * 	@return		JPanel that was just added
 */
	public JPanel createZoomPanel()
	{
		// Zoom In
		JButton zoomIn = new JButton("+");
		zoomIn.setActionCommand("Zoom In");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePreferredSize(2);
			}
		});	
		// Zoom Out
		JButton zoomOut = new JButton("-");
		zoomOut.setActionCommand("Zoom In");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePreferredSize(-2);
			}
		});	
		// Center (but don't reset zoom)
		JButton center = new JButton("Center");
		center.setActionCommand("Center View");
		center.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centerImage(); // should return shift for dragX and dragY
			}
		});		
		// Zoom Reset
		JButton zoomReset = new JButton("Reset view");
		zoomReset.setActionCommand("Standard View");
		zoomReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setViewToJustMap();
			}
		});		
		// HARD reset: resets GUI to initial state
		JButton hardReset = new JButton("Restart");
		hardReset.setActionCommand("Full reset");
		hardReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fullReset();
			}
		});
		
		// create and add Zoom Panel
		JPanel ctrlPanel = new JPanel(new GridLayout());
		ctrlPanel.add(zoomIn);
		ctrlPanel.add(zoomOut);
		ctrlPanel.add(center);
		ctrlPanel.add(zoomReset, BorderLayout.NORTH);
		ctrlPanel.add(new JLabel("Zoom"));
		ctrlPanel.add(hardReset);
		return ctrlPanel;
	}
	
/*	
 * 	Sets coordinates for the image to be drawn so that the view is centered
		with respect to the map portion of the PNG image.
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	xStart, ySytart, buildings
 * 	@effects 	adjusts xStart and yStart to center image view when repainted, buildingButtons re-located accordingly
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void centerCoords()
	{
		// center with respect to drawn path (by re-drawing) if there is a path drawn
		if(numSelected == 2)
			pathDrawn = false;
		else
		{
			xStart = (int)((getWidth()-image.getWidth(this)*widthScale*zoomRatio)/2);
			yStart = (int)((getHeight()-(image.getHeight(this) - 1400)*heightScale*zoomRatio)/2); // we subtract by 1400 so view is centered on map portion of png
		}
		reScaleBuildings();
	}	
	
/*	
 * 	Repaints image with centered view. Basically centerCoords() with repaint()
 *  
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	xStart, ySytart, buildings, this
 * 	@effects 	adjusts xStart and yStart to center image view, buildingButtons re-located accordingly.
 * 			Image is repainted
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */		
	private void centerImage()
	{
		centerCoords();	
		repaint();
	}
	
/*	
 * 	Repaints image with position and zoom centered so that only the map portion
 * 		of the view is displayed at the perfect size.
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	zoomRatio, xStart, ySytart, buildings, this
 * 	@effects 	Adjusts zoomRatio to perfectly contain map portion of PNG and xStart and yStart to center image view.
 * 			BuildingButtons re-located accordingly.	Image is repainted.
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void setViewToJustMap()
	{
		double xZoom = (double)getWidth()/(image.getWidth(this)*widthScale);
        double yZoom = (double)getHeight()/((image.getHeight(this)-1400)*heightScale);
        if(xZoom < yZoom)
        	zoomRatio = xZoom;
        else
        	zoomRatio = yZoom;
        xStart = ((int)((getWidth()-image.getWidth(this)*widthScale*zoomRatio)/2));
		yStart = 0;
		reScaleBuildings();
		repaint();
	}	
	
/*	
 * 	Paints or repaints image with width, height, and position determined by scaling member fields.
 * 	Also draws the shortest path connecting two selectied buildings, if tow buildings are selected. 
 * 
 * 	@param		g - Graphics object given by JPanel when paintComponent() is privately called out of this class's scope
 * 	@requires 	requirements of super.paintComponent()
 * 	@modifies 	this, (technically also: zoomRatio, xStart, ySytart, buildings)
 * 	@effects 	repaints image on panel as specified above. Will setViewToMap() on very first image render
 * 			hence, zoomRatio, xStart, ySytart, and buildings will be modified as detailed above in setViewToMap()
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	@Override
	public void paintComponent(Graphics g)
	{		
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		widthScale = (double)this.getSize().width / image.getWidth(this);
        heightScale = (double)this.getSize().height / image.getHeight(this);
        widthScale = widthScale < heightScale ? widthScale : heightScale;
        heightScale = heightScale < widthScale ? heightScale : widthScale;
        
		if(!firstRender)
		{
			firstRender = true;
			// sets zoom to fit only the actual map part of the png
			setViewToJustMap();
		}        
		
		// fit zoom to path, if we are drawing one
		ArrayList<Edge<Location,Double>> path = null;
		if(numSelected == 2)
			path = findPath();
		else
			pathDrawn = false;
        int width = (int) (image.getWidth(this) * widthScale * zoomRatio);
        int height = (int) (image.getHeight(this) * heightScale * zoomRatio);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, xStart, yStart, width, height, this);
		
		if(numSelected == 2) // draw path
			drawPath(g2, path);
	}
	
/*	
 * 	Draws a line forming the path that is specified by path argument. 
 * 
 * 	@param		g2 - Graphics object from paintComponent()
 * 				path - shortest path that we're drawing connecting buildings in selectedButtons
 * 	@requires 	g2 != null
 * 	@modifies 	this
 * 	@effects 	draws line forming the path, path, connecting the two user selected buildings
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void drawPath(Graphics2D g2, ArrayList<Edge<Location,Double>> path)
	{
		if(path == null) // no path between the two buildings exist, unclear how graders want this case handled
			return;		
		// now that the zoom has been fit, we can draw the path
		g2.setColor(POINTER_COLOR);
		g2.setStroke(POINTER_STROKE);
		for(int i=0; i<path.size()-1; i++)
		{
			int x = (int)(path.get(i).getReceiver().getX()*widthScale*zoomRatio) + xStart;
			int y = (int)(path.get(i).getReceiver().getY()*heightScale*zoomRatio) + yStart;
			int nextX = (int)(path.get(i+1).getReceiver().getX()*widthScale*zoomRatio) + xStart;
			int nextY = (int)(path.get(i+1).getReceiver().getY()*heightScale*zoomRatio) + yStart;
			g2.drawLine(x,y, nextX, nextY);
		}		
	}
	
/*	
 * 	Adjusts zoom and image position to fit the drawn path near/on the borders of the panel (maximize path view)
 * 
 * 	@param		path - path that zoom needs to be adjusted to fit
 * 	@requires 	path != null
 * 	@modifies 	zoomRatio, xStart, yStart, buildings
 * 	@effects 	zoomRatio, xStart, and yStart are adjusted to fit path zoom and position as described above.
 * 			buildingButtons are rescaled and re-located accordingly
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void fitZoomToPath(ArrayList<Edge<Location,Double>> path)
	{
		// get building locations (on original map)
		pathDrawn = true;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int x,y= 0;
		double minXIsEnd = 0; // false
		double maxXIsEnd = 0; // false
		double minYIsEnd = 0; // false
		double maxYIsEnd = 0; // false
		for(int i=0; i<path.size(); i++)
		{
			x = (int)(path.get(i).getReceiver().getX());
			y = (int)(path.get(i).getReceiver().getY());
			// update min/max coords
			minX = Math.min(minX,  x);
			maxX = Math.max(maxX,  x);
			minY = Math.min(minY,  y);
			maxY = Math.max(maxY,  y);			
		}
		Dimension buttonDim = selectedButtons.get(0).getSize();
		if(minX >= path.get(0).getReceiver().getX() - buttonDim.width*(zoomRatio*widthScale/2.0) || minX >= path.get(path.size()-1).getReceiver().getX() - buttonDim.width*(zoomRatio*widthScale/2.0))
			minXIsEnd = 0.5;
		if(minY >= path.get(0).getReceiver().getY() - buttonDim.height*(zoomRatio*heightScale/2.0) || minY == path.get(path.size()-1).getReceiver().getY() - buttonDim.height*(zoomRatio*heightScale/2.0))
			minYIsEnd = 0.5;
		if(maxX <= path.get(0).getReceiver().getX() + buttonDim.width*(zoomRatio*widthScale/2.0)|| maxX <= path.get(path.size()-1).getReceiver().getX() + buttonDim.width*(zoomRatio*widthScale/2.0))
			maxXIsEnd = 0.5;
		if(maxY <= path.get(0).getReceiver().getY() + buttonDim.height*(zoomRatio*heightScale/2.0)|| maxY <= path.get(path.size()-1).getReceiver().getY() + buttonDim.height*(zoomRatio*heightScale/2.0))
			maxYIsEnd = 0.5;		
		// update zoomRatio, xStart, and yStart accordingly
		centerViewAndZoomToPath(minX, minY, maxX, maxY, minXIsEnd, minYIsEnd, maxXIsEnd, maxYIsEnd);		
	}	
	
	
/*	
 * 	Helper for fitZoomToPath(). Does most of the work once min max ranges are found by aforementioned function
 * 	Adjusts zoom and image position to fit the drawn path near/on the borders of the panel (maximize path view).
 * 		coordinates passed in parameters are relative scaled as those in the model (2057x1921 map image)
 * 
 * 	@param		minX - smallest x-coordinate of path
 * 				minY - smallest y-coordinate of path
 * 				maxX - largest x-coordinate of path
 * 				maxY - largest y-coordinate of path
 * 				minXIsEnd - 0.5 if the location with smallest x-coordinate is the source/receiver of the path, 0 otherwise
 * 				minYIsEnd - 0.5 if the location with smallest y-coordinate is the source/receiver of the path, 0 otherwise
 * 				maxXIsEnd - 0.5 if the location with largest x-coordinate is the source/receiver of the path, 0 otherwise
 * 				maxYIsEnd - 0.5 if the location with largest y-coordinate is the source/receiver of the path, 0 otherwise
 * 	@requires 	(params follow criteria detailed above)
 * 	@modifies 	zoomRatio, xStart, yStart, buildings
 * 	@effects 	zoomRatio, xStart, and yStart are adjusted to fit path zoom and position as described
 * 			at the top of this comment block. buildingButtons are rescaled and re-located accordingly
 * 	@throws 	n.a.
 * 	@return		n.a.	
	 */
	private void centerViewAndZoomToPath(int minX, int minY, int maxX, int maxY, double minXIsEnd, double minYIsEnd, double maxXIsEnd, double maxYIsEnd)
	{
			// idea is to treat (maxX - minX) as image width
		minXIsEnd = 0.5;
		minYIsEnd = 0.5;
		maxXIsEnd = 0.5;
		maxYIsEnd = 0.5;
		int buttonSizeConstant = 30;
		Dimension buttonSize = new Dimension((int)((minXIsEnd+maxXIsEnd)*buttonSizeConstant), (int)(buttonSizeConstant*(minYIsEnd+maxYIsEnd)));
		double xZoom = (double)getWidth()/(widthScale*(maxX - minX + buttonSize.width));
		double yZoom = (double)getHeight()/(heightScale*(maxY - minY + buttonSize.height));	
		double dx = 0;
		double dy = 0;
		if(xZoom < yZoom)
		{
			updatePreferredSize(xZoom);
			dy = getHeight() - (maxY - minY)*heightScale*zoomRatio;
		}
		else
		{
			updatePreferredSize(yZoom);
			dx = getWidth() - (maxX - minX)*widthScale*zoomRatio;			
		}
		xStart = (int)( -minX*widthScale*zoomRatio + dx/2.0 + (double)selectedButtons.get(0).getWidth()*(minXIsEnd));
		yStart = (int)( -minY*heightScale*zoomRatio  + dy/2.0 + (double)selectedButtons.get(0).getHeight()*(minYIsEnd));
		reScaleBuildings();
	}
	
/*	
 * 	Resets GUI to initial state.
 * 
 * 	@param		n.a.
 * 	@requires 	n.a.
 * 	@modifies 	zoomRatio, xStart, ySytart, buildings, this
 * 	@effects 	De-selects all buttons. Adjusts zoomRatio to perfectly contain map portion of PNG and 
 * 			xStart and yStart to center image view.	buildingButtons are re-located accordingly.	Image is repainted.
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */	
	private void fullReset()
	{
		deselectAll();
		setViewToJustMap();
	}
	
/*	
 * 	Calculates and returns re-scaled position coordinates as a Dimension object
 * 
 * 	@param		x - x-coordinate to re-scale
 * 				y - y-coordinate to re-scale
 * 	@requires 	n.a.
 * 	@modifies 	n.a.
 * 	@effects 	n.a.
 * 	@throws 	n.a.
 * 	@return		Dimension containing rescaled coordinates
 */	
	private Dimension reScaleCoords(int x, int y)
	{
		int newX = (int)((getWidth()-image.getWidth(this)*zoomRatio*widthScale)/2) + xStart;
		int newY = (int)((getHeight()-image.getHeight(this)*zoomRatio*heightScale)/2) + yStart;		
		return new Dimension(newX,newY);
	}
	
	
/*	
 * 	Loads location data from model via passed in list iterator.
 * 
 * 	@param		locItr - list iterator of Locations traversing through list of locations we are loading
 * 	@requires 	locItr != null
 * 	@modifies 	
 * 	@effects 	
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void loadLocations(Iterator<Location> locItr)
	{
		while(locItr.hasNext())
		{
			processNewLocation(locItr.next());
		}
	}
	
/*	
 * 	Helper for loadLocations(), parses Location and adds buildingButton to buildings if loc is a Building
 * 
 * 	@param		loc - Location we are parsing
 * 	@requires 	loc != null
 * 	@modifies 	buildings (potentially)
 * 	@effects 	parses location and adds BuildingButton to buildings, if loc is a building
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */	
	private void processNewLocation(Location loc)
	{
		if(loc.getName().length() != 0) // building
		{
			BuildingButton locButton = new BuildingButton((Building)loc, this);
			Dimension d = reScaleCoords(loc.getX(), loc.getY());
			locButton.setBounds(d.width, d.height, 8, 8);
			// make button transparent
			locButton.setOpaque(false);
			locButton.setContentAreaFilled(false);
			locButton.setBorderPainted(true);
			// add to this canvas panel and set of building buttons
			add(locButton);
			buildings.add(locButton);
		}			
	}	
	
	
/*	
 * 	Helper for reScalBuildings(). Re-scales BuildingButton to match new image dimensions/zoom and position
 * 
 * 	@param		button - BuildingButton in buildings to re-scale
 * 	@requires 	button != null
 * 	@modifies 	button, buildings
 * 	@effects 	button is resized and re-position to match new view dimesnions, zoom, and position.
 * 			This method will only be called buttons inside buildings, so building will technically be modifies as well.
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	
	private void scaleButton(BuildingButton button)
	{
		Building b = button.getBuilding();
		int buttonBaseSize;
		if(getWidth() < getHeight())
			buttonBaseSize = (int)(widthScale*windowDimButtonScale);
		else
			buttonBaseSize = (int)(heightScale*windowDimButtonScale);
		int newWidth = (int)(buttonBaseSize*zoomRatio);
		int newHeight = (int)(buttonBaseSize*zoomRatio);
		int newX = (int)(b.getX()*widthScale*zoomRatio + xStart - (double)newWidth/2.0);
		int newY = (int)(b.getY()*heightScale*zoomRatio + yStart - (double)newHeight/2.0);
		button.setBounds(newX, newY, newWidth, newHeight);
	}
	
/*	
 * 	Re-sizes and re-positions all BuildingButtons in buildings to fit new dimensions, zoom, position.
 * 
 * 	@param		n.a.
 * 	@requires 	buildings != null
 * 	@modifies 	buildings
 * 	@effects 	Re-scales buttons in buildings as described above
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */	
	private void reScaleBuildings()
	{
		BuildingButton button;
		Iterator<BuildingButton> buttonItr = buildings.iterator();
		while(buttonItr.hasNext())
		{
			button = buttonItr.next();
			scaleButton(button);
		}
	}
	
/*	
 * 	Updates image position to account for new zoom ratio
 * 
 * 	@param		zoomScale - scale of new zoom ratio over the old zoom ratio the current coordinates are based on
 * 				n - int that is > 0 if the zoomRatio has increased, or < 0 if the zoomRatio has decreased
 * 	@requires  	zoomScale != 0
 * 	@modifies 	xStart, yStart
 * 	@effects 	Adjusts xStart and yStart to maintain image view after zoomRatio is modified
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void zoomUpdateCoords(double zoomScale, int n) // n == 1 or -1
	{
		double dx = xStart - getWidth()/2; // distance from center
		double dy = yStart - getHeight()/2;
		if(n < 0)
			zoomScale = 1.0/zoomScale;
		xStart = (int)(getWidth()/2 + (dx*(zoomScale)));
		yStart = (int)(getHeight()/2 + (dy*(zoomScale)));
	}		
	
/*	
 * 	Updates zoom and positioning data as specified by parameters
 * 
 * 	@param		n - int we shift our zoom ratio with respect to a tenth of (sign determines pos. or neg. change)
 * 	@requires  	n.a.
 * 	@modifies 	xStart, yStart
 * 	@effects 	Updates zoomRatio by shift detailed in @param. Adjusts xStart and yStart to maintain image
 * 			view after zoomRatio is modified. Buildings are resized and re-position accordingly.
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void updatePreferredSize(int n) 
	{
		double zoomShift = (double)(n)/10;
		double zoomScale = 1.0 + Math.abs(zoomShift);
		if(n > 0)
	    {	    	
	    	if(zoomRatio*zoomScale <= 80.0)
	    		zoomRatio *= zoomScale;
	    	else
	    	{
	    		zoomScale = 80.0/zoomRatio;    	
	    		zoomRatio = 80.0;
	    	}
	    }
	    else if (n < 0)
	    {
	    	if(zoomRatio/zoomScale >= 1.0)
	    		zoomRatio /= zoomScale;
	    	else
	    	{
	    		zoomScale = 1.0/zoomRatio;
	    		zoomRatio = 1.0;
	    	}
	    }
	    else
	    	return;
	    zoomUpdateCoords(zoomScale, n);
	    reScaleBuildings();
	    repaint();
	}
	
	/*	
	 * 	Overloaded to take exact value of newly desired zoomRatio. Updates zoom and positioning data as such.
	 * 		Increases zoo
	 * 
	 * 	@param		newZoomRatio - newly desired zoom ratio
	 * 	@requires  	n.a.
	 * 	@modifies 	n.a.
	 * 	@effects 	Updates zoomRatio to tnewZoomRatio. Buildings are resized and re-position accordingly.
	 * 	@throws 	n.a.
	 * 	@return		n.a.	
	 */
	private void updatePreferredSize(double newZoomRatio) 
	{
		double zoomScale = newZoomRatio/zoomRatio;
		if(zoomScale > 1.0)
	    {	    	
	    	if(newZoomRatio <= 80.0)
	    		zoomRatio = newZoomRatio;
	    	else
	    		zoomRatio = 80.0;	    		
	    }
	    else if (zoomScale < 1.0)
	    {
	    	if(newZoomRatio >= 1.0)
	    		zoomRatio = newZoomRatio;
	    	else
	    		zoomRatio = 1.0;
	    }
	    else
	    	return;
	    reScaleBuildings();
	    repaint();
	}
	
	
/*	
 * 	Selects BuildingButton specifies in parameter. Repaints with new selected button(s)
 * 
 * 	@param		b - button user is selecting
 * 	@requires  	b != null (we assume b is in buildings), selectedButtons != null
 * 	@modifies 	selectedButtons, numSelected, buildings, this
 * 	@effects 	First, resets numSelected and clears selectedButtons if 2 buttons are already selected.
 * 			Adds the selected button to selectedButtons, increments numSelected, modifies state of button in buildings
 * 			by calling b.select() so button will be marked selected and filled in with red. Image is repainted
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	public void selectButton(BuildingButton b)
	{
		if(numSelected == 2) // deselect other buttons, clear path drawing, select new button
		{
			deselectAll();			
		}			
		// select new button
		b.select();
		selectedButtons.add(b);
		numSelected++;
		// check if we need to find path
		if (numSelected == 2)
		{
			// find and draw shortest path (will need to zoom perfectly)
			repaint();
		}
	}

/*	
 * 	De-selects specified button
 * 
 * 	@param		b - button user is de-selecting
 * 	@requires  	b != null, b is an element of, selectedButtons != null
 * 	@modifies 	selectedButtons, numSelected, buildings, this
 * 	@effects 	Decrements numSelected and removes b from selectedButtons. Modifies state of button in buildings
 * 			by calling b.deselect() so button will be marked as not selected and made transparent. Image is repainted		
*/		
	public void deselectButton(BuildingButton b)
	{
		selectedButtons.remove(b);
		b.deselect();
		numSelected--;
		if(numSelected == 1)
			repaint(); // remove path drawing
	}
	
/*	
 * 	De-selects all selected buttons
 * 
 * 	@param		n.a.
 * 	@requires  	selectedButtons != null
 * 	@modifies 	selectedButtons, numSelected, buildings, this
 * 	@effects 	Resets numSelected to 0 and clears selectedButtons.	 Modifies state of selected buttons button in buildings
 * 			by calling b.deselect() so all buttons will be marked as not selected and made transparent. Image is repainted
 * 	@throws 	n.a.
 * 	@return		n.a.	
 */
	private void deselectAll()
	{		
		for(BuildingButton b : selectedButtons)
		{
			b.deselect();			
		}
		selectedButtons.removeAll(selectedButtons);
		numSelected = 0;	
		repaint(); // assuming deselectAll() is only called when a 3rd node is selected, we will always need to remove a path drawing
	}
	
/*	
 * 	Finds shortest path and sets zoom and image position to fit it perfectly (if not already done)
 * 
 * 	@param		n.a.
 * 	@requires  	selectedButtons != null, model != null, selectedButtons contains two distinct BuildingButtons
 * 	@modifies 	zoomRatio, xStart, yStart, buildings
 * 	@effects 	If necssary, zoomRatio, xStart, and yStart are adjusted to fit path zoom and position as described above,
 * 			and BuildingButtons in buildings are rescaled and re-located accordingly
 * 	@throws 	n.a.
 * 	@return		shortest path connecting the to BuildingButtons in selectedButtons as an ArrayList of Edges
 */
	private ArrayList<Edge<Location,Double>> findPath()
	{
		Building source = selectedButtons.get(0).getBuilding();
		Building receiver = selectedButtons.get(1).getBuilding();
		ArrayList<Edge<Location,Double>> path = model.findPath(source,receiver);
		if(path != null && !pathDrawn) // fit zoom and image start coordinates to path
			fitZoomToPath(path);
		return path;
	}
	
	// @returns projected size of the image accounting for zoom and scaling to panel dimensions
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension((int)(image.getWidth(this)*zoomRatio*widthScale), (int)(image.getHeight(this)*zoomRatio*heightScale));
	}
	
// The following methods are overridden to implement Scrollable. They are unused, and the 
		// un-described ones are implemented arbitrarily.
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension((int)(image.getWidth(this)*zoomRatio*widthScale), (int)(image.getHeight(this)*zoomRatio*heightScale));
	}
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 5;
	}
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 5;
	}
	// Set to true so we don't use scroll bar to scroll. 
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	// Set to true so we don't use scroll bar to scroll.
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return true;
	}	
}
