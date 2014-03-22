import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.*;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import java.util.Map;
import java.util.Set;
public class RTSMapApp extends JFrame implements ActionListener {
	private static final long serialVersionUID = 10;
	JPanel actionListPanel;
	JPanel actionPanel;
	JPanel customPanel;
	JPanel drawingPanel; 
	JPanel selectionPanel;
	JPanel dPanel;
	static ArrayList<Line> lines1 = new ArrayList<Line> ();
	HashMap<String, Location> labels1 = new HashMap<String, Location> ();
	HashMap<String, BusStop> visibleBuses1 = new HashMap<String, BusStop>();
	HashMap<String, PointOfInterest> visiblePointsOfInterest1 = new HashMap<String, PointOfInterest> ();
	HashMap<Polygon, Color> colors = new HashMap<Polygon, Color>();
	HashMap<Line, Polygon> drawings = new HashMap<Line, Polygon> ();
	HashMap<String, ArrayList<JComponent>> actionComponents = new HashMap<String,ArrayList<JComponent>>();
	HashMap<String, Object> allObjects = new HashMap<String, Object>();
	HashMap<String, Location> labels = new HashMap<String, Location> ();
	static HashMap<String, Line> lines = new HashMap<String,Line>();
	HashMap<String, BusStop> points = new HashMap<String,BusStop>();	 
	HashMap<String, PointOfInterest> pointsOfInterest = new HashMap<String, PointOfInterest> ();
	HashMap<String, BusStop> visibleBuses = new HashMap<String,BusStop>();
	HashMap<String, Location> visibleLocations = new HashMap<String, Location>();
	HashMap<String, PointOfInterest> visiblePointsOfInterest = new HashMap<String, PointOfInterest> ();
	String[] lineActions = {"New", "Add BusStop", "Write Schedule"};
	String[] locationActions = {"New", "Show", "Hide", "Move", "Write Line Info"};
	String [] pointOfInterestActions = {"New", "Move", "Show", "Hide", "Search For Line"};
	String actionListKeyword = "ActionList";
	String actionMode = "New";
	String rtsItem = "BusStop";
	JTextField description;
	JTextField duration;
	JTextField firstH, firstM, lastH, lastM;
	JTextField name;
	JTextField period;
	JTextField x;
	JTextField y;
	JTextField toBeDone;
	JTextField arrivalH;
	JTextField arrivalM;
	JCheckBox timePoint;
	JComboBox<String> busStopList;
	JComboBox<String> busStopListDep;
	JComboBox<String> busStopListDest;
	JComboBox<String> lineActionList;
	JComboBox<String> locationActionList;	
	JComboBox<String> pointOfInterestActionList; 
	JComboBox<String> lineList;
	JComboBox<String> pointOfInterestList;
	JComboBox<String> pointOfInterestDepList;
	JComboBox<String> pointOfInterestDestList;
	BufferedImage busImage;
	BufferedImage busImage1;
	BufferedImage mapImage;
	BufferedImage poiImage;
	ArrayList<Location> movingLocations = new ArrayList<Location> ();
	int lastX;
	int lastY;
	String fileName = null;
	JFileChooser fileChooser = new JFileChooser ();
	JFrame frame2;
	String theSchedule;
	Line l1;
	JRadioButton moveMode;
	JRadioButton drawMode;
	ArrayList<Integer> lineUpperX = new ArrayList<Integer>();
	ArrayList<Integer> lineUpperY = new ArrayList<Integer>();
	ArrayList<Integer> lineBottomX = new ArrayList<Integer>();
	ArrayList<Integer> lineBottomY = new ArrayList<Integer>();
	ArrayList<Polygon> polygons = new ArrayList<Polygon>();
	boolean drawingStarted = false;
	static QueryResult q;
	static QueryResult2 z;
	static List<String> namesList = new ArrayList<String> ();
	JComboBox<String> colorList;
	Polygon p;
	class BusStopComboBoxListener implements ItemListener {
		public void itemStateChanged (ItemEvent e) {
			if (points.get((String)(busStopList.getSelectedItem())) instanceof TimePoint) {
				dPanel.setVisible (true);
			}
			else {
				dPanel.setVisible(false);
			}
		}
	}
	class LineComboBoxListener implements ItemListener {
		public void itemStateChanged (ItemEvent e) {
			toBeDone.setText("To be added after " + lines.get((String)lineList.getSelectedItem()).departure.getName ());
		}
	}
	void clearPolygon()
	{
		lineUpperX.clear();
		lineUpperY.clear();
		lineBottomX.clear();
		lineBottomY.clear();
		drawingStarted = false;
		drawingPanel.repaint();
	}
	static class DistanceCompare implements Comparable<DistanceCompare>
	{
		Location l;
		BusStop bs;
		int distance;

		public DistanceCompare(Location l, BusStop bs)
		{
			this.l = l;
			this.bs = bs;
			distance = l.distanceFrom(bs);
		}

		public int compareTo(DistanceCompare dc)
		{
			if (this.distance < dc.distance) {
				return -1;
			}
			else if (this.distance == dc.distance) {
				return 0;
			}
			else {
				return 1;
			}
		}
	}
	public static class TimeCompare implements Comparable<TimeCompare> {
		Time t;
		Time z;
		int difference;

		public TimeCompare (Time t, Time z) {
			this.t = t;
			this.z = z;
			difference = t.compareTo(z);
		}

		public int compareTo(TimeCompare tc) {
			if (this.difference < tc.difference) {
				return -1;
			}
			else if (this.difference == tc.difference) {
				return 0;
			}
			else {
				return 1;
			}
		}
	}
	public static class QueryResult
	{
		Line line;
		Location departure;
		Location destination;

		public QueryResult(Line line, Location departure, Location destination)
		{
			this.line = line;
			this.departure = departure;
			this.destination = destination;
		}

		public String toString()
		{
			return "The best line to take to go from "  + departure + " to " + destination + " is "+ line.name;
		}
	}
	public static class QueryResult2
	{
		Line line;
		Location departure;
		Location destination;
		Time arrivalTime;

		public QueryResult2(Line line, Location departure, Location destination, Time arrivalTime)
		{
			this.line = line;
			this.departure = departure;
			this.destination = destination;
			this.arrivalTime = arrivalTime;
		}

		public String toString()
		{
			return "The best line to take to go from "  + departure + " at " + arrivalTime + " to " + destination + " is "+ line.name;
		}
	}
	public static QueryResult search(Collection<BusStop> locations, Location departure, Location destination)
	{
		PriorityQueue<DistanceCompare> sortedList1 = new PriorityQueue<DistanceCompare>(locations.size ());
		for(BusStop b2: locations)
			sortedList1.offer(new DistanceCompare(departure, b2));
		PriorityQueue<DistanceCompare> sortedList2 = new PriorityQueue<DistanceCompare> (locations.size ());
		for (BusStop b3: locations) 
			sortedList2.offer(new DistanceCompare(destination, b3));
		BusStop closestDestination = sortedList2.poll().bs;
		for (int i = 0; i < lines1.size (); i ++) {
			Line l = lines1.get(i);
			for (int j = 0; j < l.route.size (); j ++) {
				if (l.route.get(j).getName ().equals(closestDestination.getName ())) {
					while (sortedList1.peek() != null) {
						BusStop closestDeparture = sortedList1.poll().bs;
						for (int k = 0; k < l.route.size (); k ++) {
							if (l.route.get(k).getName().equals(closestDeparture.getName ())) {
								q = new QueryResult(l, closestDeparture, closestDestination);
								return q;
							}
						}
					}
				}
			}
		}
		return null;
	}
	public static QueryResult2 search2 (Collection<BusStop> locations, Location departure, Location destination, Time departureTime) {
		int index = 0;
		int durationAmount = 0;
		PriorityQueue<DistanceCompare> sortedList1 = new PriorityQueue<DistanceCompare>(locations.size ());
		for(BusStop b2: locations)
			sortedList1.offer(new DistanceCompare(departure, b2));
		PriorityQueue<DistanceCompare> sortedList2 = new PriorityQueue<DistanceCompare> (locations.size ());
		for (BusStop b3: locations) 
			sortedList2.offer(new DistanceCompare(destination, b3));
		BusStop closestDestination = sortedList2.poll().bs;
		for (int i = 0; i < lines1.size (); i ++) {
			Line l = lines1.get(i);
			for (int j = 0; j < l.route.size (); j ++) {
				if (l.route.get(j).getName ().equals(closestDestination.getName ())) {
					while (sortedList1.peek() != null) {
						BusStop closestDeparture = sortedList1.poll().bs;
						for (int k = 0; k < l.route.size (); k ++) {
							if (l.route.get(k).getName().equals(closestDeparture.getName ())) {
								index = k;
								int s = 0;
								for (int m = 0; m <= index; m ++) {
									if (l.route.get(m) instanceof TimePoint) {
										durationAmount += l.timePoints.get(s);
										s++;
									}
								}
								PriorityQueue<TimeCompare> sortedList3 = new PriorityQueue<TimeCompare> ();
								Time theTimes = l.first.advanceMinutes(durationAmount);
								Time finale = l.last.advanceMinutes(durationAmount);
								while (theTimes.compareTo(finale) <= 0) {
									sortedList3.offer(new TimeCompare (theTimes, departureTime));
									theTimes = theTimes.advanceMinutes(l.period);
								}
								Time closestTime = sortedList3.poll().t;
								z = new QueryResult2(l, closestDeparture, closestDestination, closestTime);
								return z;
							}
						}
					}
				}
			}
		}
		return null;
	}
	class RequestFocusListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			System.out.println("Requesting focus for the JFrame object"); 
			requestFocusInWindow();
		}
	}
	class RTSWindowListener extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			try {
				if (fileName != null) 
					writeLocations(fileName);
				System.out.println("Bye!");
			} 
			catch(IOException exc)
			{
				exc.printStackTrace();
				System.out.println("Problem writing Location and Line objects");
			}
		}
	}
	class RTSSaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try { 
				int value = fileChooser.showSaveDialog(RTSMapApp.this);
				if (value == JFileChooser.APPROVE_OPTION)
				{
					System.out.println("Writing Locations and Lines to " + fileChooser.getSelectedFile().getName());
					writeLocations(fileChooser.getSelectedFile().getName());
				}
			}
			catch(IOException exc)
			{
				System.out.println("Error saving Locations and Lines");
				exc.printStackTrace();
			}
		}
	}
	class RTSLoadButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int value = fileChooser.showOpenDialog(RTSMapApp.this);
			if (value == JFileChooser.APPROVE_OPTION)
			{
				System.out.println("Reading Locations and Lines from " + fileChooser.getSelectedFile().getName());
				readLocations(fileChooser.getSelectedFile().getName());
				drawingPanel.repaint ();
			}
		}
	}
	class okButtonListener implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			if (e.getActionCommand().equals("OK")) {
				Line l = lines.get((String)lineList.getSelectedItem ());
				Color c = ColorDecoder.getColor((String)colorList.getSelectedItem());
				drawings.put(l, p);
				colors.put(p, c);
			}
		}
	}
	class BusStopMouseListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			lastX = e.getX();
			lastY = e.getY();
			if (moveMode.isSelected()) {
				System.out.println("Let's move starting at (" + lastX + "," + lastY + ")");
				Collection<Location> locations = visibleLocations.values (); 
				for (Location l: locations) {
					if (l.covers(lastX, lastY)) {
						movingLocations.add(l);
						if (l instanceof BusStop) {
							busStopList.setSelectedItem(l.getName ());
						}
					}
				}
				Collection<PointOfInterest> pointsOfInterest1 = visiblePointsOfInterest.values ();
				for (PointOfInterest p: pointsOfInterest1) {
					if (p.covers(lastX, lastY)) {
						JOptionPane.showMessageDialog(null, p.getDescription (), "Desciption of " + p.getName (), JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			else {
				System.out.println("BUTTON1=" + MouseEvent.BUTTON1 + " currently=" + e.getButton()); 
				if (e.getButton() == MouseEvent.BUTTON1)
				{	
					drawingStarted = true;	
					System.out.println("Drawing starts at (" + lastX + "," + lastY + ")");
					lineUpperX.add(lastX); 
					lineUpperY.add(lastY - 5);
					//lineX.add(lastX);
					//lineY.add(lastY);
					lineBottomX.add(lastX);
					lineBottomY.add(lastY + 5);
				} 
			}
		}
		public void mouseReleased(MouseEvent e)
		{
			if (moveMode.isSelected()) {
				System.out.println("Move ended at (" + e.getX() + "," + e.getY() + ")");
				movingLocations.clear ();
			}
			else if (e.getButton() == MouseEvent.BUTTON1){
				clearPolygon ();
			}
		}
		public void mouseClicked(MouseEvent e)
		{
			if (drawingStarted && e.getButton() == MouseEvent.BUTTON3)
			{
				JFrame frame4 = new JFrame ("Color and Line choosing");
				JPanel listPanel = new JPanel ();
				listPanel.add(lineList);
				colorList = new JComboBox<String> ();
				colorList.addItem("Black");
				colorList.addItem("Blue");
				colorList.addItem("Cyan");
				colorList.addItem("darkGray");
				colorList.addItem("Gray");
				colorList.addItem("Green");
				colorList.addItem("lightGray");
				colorList.addItem("Magenta");
				colorList.addItem("Orange");
				colorList.addItem("Pink");
				colorList.addItem("Red");
				colorList.addItem("White");
				colorList.addItem("Yellow");
				listPanel.add(colorList);
				JButton ok = new JButton ("OK");
				ok.setBackground(Color.blue);
				ok.addActionListener(new okButtonListener ());
				frame4.setLayout(new BorderLayout ());
				frame4.add(listPanel, BorderLayout.NORTH);
				frame4.add(ok, BorderLayout.SOUTH);
				frame4.setSize(800, 400);
				frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame4.setVisible(true);
				p = generatePolygon ();
				polygons.add(p);
				clearPolygon();
			}
		}
	}
	class BusStopMouseMotionListener extends MouseMotionAdapter
	{
		public void mouseDragged(MouseEvent e)
		{
			int x1 = e.getX();
			int y1 = e.getY();
			if (moveMode.isSelected ()) {
				for(int i=0; i < movingLocations.size(); i++)
				{
					Location l = movingLocations.get(i);
					l.setX(l.getX() + x1 - lastX);
					l.setY(l.getY() + y1 - lastY);
				}
			}
			else if (drawingStarted) {
				System.out.println("Dragging at (" + x + "," + y);
				if (Math.abs(lastX - x1) == 0)
				{
					lineUpperX.add(x1 + 5);
					lineUpperY.add(y1);
					lineBottomX.add(x1 - 5);
					lineBottomY.add(y1);					 
				}
				else {
					lineUpperX.add(x1);
					lineUpperY.add(y1 - 5);
					lineBottomX.add(x1);
					lineBottomY.add(y1 + 5);
				}	
			}
			lastX = x1;
			lastY = y1;
			drawingPanel.repaint();
		}
		public void mouseMoved(MouseEvent e)
		{
			lastX = e.getX();
			lastY = e.getY();
			if (moveMode.isSelected ()) {
				Set<Map.Entry<String, Location>> locationTupleSet = visibleLocations.entrySet();
				int change = 0;
				for(Map.Entry<String, Location> m: locationTupleSet)
					if (((Location)m.getValue()).covers(lastX, lastY))
					{  
						change++;  
						labels.put((String)m.getKey(), (Location)m.getValue());
					}  
					else 
					{		 
						if (labels.remove(m.getKey()) != null)
							change++; 
					}		 
				if (change > 0)
					drawingPanel.repaint(); 
			}
		}
	}
	class RtsItemListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (rtsItem.compareTo(e.getActionCommand()) != 0)
			{	 
				actionMode = "New";
				if (rtsItem.equals("BusStop"))
					locationActionList.setSelectedIndex(0);
				if (rtsItem.equals("Line"))
					lineActionList.setSelectedIndex(0);	
				if (rtsItem.equals("PointOfInterest"))
					pointOfInterestActionList.setSelectedIndex(0);
			}		
			rtsItem = e.getActionCommand();
			System.out.println(rtsItem);
			updateCustomComponent();
		}
	}
	class RtsItemActionListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getStateChange() == ItemEvent.SELECTED) 
			{	
				actionMode = (String) e.getItem();
				System.out.println(actionMode);
				updateCustomComponent();
			}   
		}
	}
	Polygon generatePolygon()
	{
		if (lineUpperX.size() > 0)
		{	   
			int[] xP = new int[2 * lineUpperX.size() + 1];
			int[] yP = new int[2 * lineUpperY.size() + 1];
			int i;
			for(i=0; i < lineUpperX.size(); i++)
			{
				xP[i] = lineUpperX.get(i);
				yP[i] = lineUpperY.get(i);       		
			}
			for(int j=i=lineBottomX.size() - 1; i >= 0; i--)
			{
				xP[j + lineBottomX.size() - i] = lineBottomX.get(i);
				yP[j + lineBottomX.size() - i] = lineBottomY.get(i); 
			}
			xP[2 * lineUpperX.size()] = xP[0];
			yP[2 * lineUpperX.size()] = yP[0];

			return new Polygon(xP, yP, xP.length);
		}
		else return null;
	}
	public RTSMapApp(String s)  {
		super(s);
		fileChooser.setCurrentDirectory(new File("I:\\BRIDGE MAN\\Freshman Year\\Second Semester\\Programming for CIS Majors 2\\Programming for CIS Majors 2\\RTS.java"));
		selectionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		selectionPanel.setBorder(new TitledBorder("Items"));
		JRadioButton busStopButton = new JRadioButton("BusStop", true);
		busStopButton.setMnemonic('B');
		JRadioButton lineButton = new JRadioButton("Line", false);
		JRadioButton pointsOfInterestButton = new JRadioButton("PointOfInterest", false);
		pointsOfInterestButton.setMnemonic('P');
		lineButton.setMnemonic('L');
		ButtonGroup group = new ButtonGroup();
		group.add(busStopButton);
		group.add(lineButton);
		group.add(pointsOfInterestButton);
		RtsItemListener rtsItemListener = new RtsItemListener();
		busStopButton.addActionListener(rtsItemListener);
		lineButton.addActionListener(rtsItemListener);
		pointsOfInterestButton.addActionListener(rtsItemListener);
		selectionPanel.add(busStopButton); 
		selectionPanel.add(lineButton);
		selectionPanel.add(pointsOfInterestButton);
		try {
			mapImage  = ImageIO.read(new File("Campus Map.gif"));
		}
		catch (IOException e) { 
			System.out.println("Could not load the map image");
		}
		drawingPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				int width = getWidth();
				int height = getHeight(); 
				if (width == 0 || height == 0)
				{
					width = (int)getPreferredSize().getWidth();
					height = (int)getPreferredSize().getHeight();
				}
				g.clearRect(0,  0,  getWidth(),  getHeight());
				g.drawImage(mapImage, 0, 0, width, height, null);
				Collection<BusStop> busStops = visibleBuses.values  ();
				for (BusStop b: busStops) {
					try {
						busImage = ImageIO.read(new File("bus.jpg"));
						busImage1 = ImageIO.read(new File("Bus1.jpg"));
						if (b instanceof TimePoint) {
							b.setImage(busImage);
						}
						else {
							b.setImage(busImage1);
						}
						b.draw(g, 60, 60);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Collection<PointOfInterest> pointsOfInterest1 = visiblePointsOfInterest.values ();
				for (PointOfInterest p: pointsOfInterest1) {
					try {
						poiImage = ImageIO.read(new File("poi.gif"));
						p.setImage(poiImage);
						p.draw(g, 60, 60);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				//for(int i=0; i < lineX.size() - 1; i++)
				// g.drawLine(lineX.get(i), lineY.get(i), lineX.get(i+1), lineY.get(i+1));
				Set<Map.Entry<Polygon, Color>> colorSet = colors.entrySet();
				for (Map.Entry<Polygon, Color> c: colorSet) {
					g.setColor(c.getValue());
					g.fillPolygon(c.getKey());
				}
				Polygon currentPolygon = generatePolygon();
				if (currentPolygon != null)
					g.fillPolygon(currentPolygon);
				if (moveMode.isSelected ()) {
					FontMetrics fontMetrics = g.getFontMetrics();
					Color prev = g.getColor();
					Set<Map.Entry<String, Location>> labelSet = labels.entrySet();
					for(Map.Entry<String, Location> m: labelSet)
					{ 	 
						int height1 = fontMetrics.getHeight();
						int width1 = fontMetrics.stringWidth((String)m.getKey());
						int x = ((Location)m.getValue()).getX();
						int y = ((Location)m.getValue()).getY();
						if (y > getHeight())
							y =  getHeight();
						else if (y - height1 < 0)
							y = height1;
						if (x + width1 > getWidth())
							x = getWidth() - width1;
						else if (x < 0)
							x = 0;
						g.setColor(new Color(255, 215, 0));
						g.fillRect(x, y - height1, width1, height1);
						g.setColor(Color.black);
						g.drawString((String)m.getKey(), x, y);
					} 	 
					g.setColor(prev);
				}
			}
			public Dimension getPreferredSize()
			{
				return new Dimension(500, 400);
			}
		};
		drawingPanel.setBorder(new TitledBorder("Map"));
		drawingPanel.add(new JLabel("Map"));
		drawingPanel.setPreferredSize(new Dimension(50, 50)); 
		drawingPanel.addMouseListener(new BusStopMouseListener ());
		drawingPanel.addMouseMotionListener(new BusStopMouseMotionListener ());
		moveMode = new JRadioButton ("Move & Show Mode");
		drawMode = new JRadioButton ("Draw Mode");
		ButtonGroup drawingMoveGroup = new ButtonGroup ();
		drawingMoveGroup.add(moveMode);
		drawingMoveGroup.add(drawMode);
		moveMode.setSelected(true);
		JPanel buttonPanel = new JPanel ();
		buttonPanel.add(moveMode);
		buttonPanel.add(drawMode);
		JPanel outerDrawPanel = new JPanel ();
		outerDrawPanel.setLayout (new BorderLayout ());
		outerDrawPanel.add(drawingPanel, BorderLayout.NORTH);
		outerDrawPanel.add(buttonPanel, BorderLayout.SOUTH);
		actionPanel = new JPanel(new BorderLayout(5,5));
		actionPanel.setBorder(new TitledBorder("Actions"));
		actionListPanel = new JPanel();
		customPanel = new JPanel();
		actionPanel.add(actionListPanel, BorderLayout.NORTH);
		actionPanel.add(customPanel, BorderLayout.CENTER);
		makeCustomPanelComponents();                  
		setLayout(new BorderLayout(5,5));
		JPanel IOPanel = new JPanel ();
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new RTSLoadButtonListener());
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new RTSSaveButtonListener());
		IOPanel.add(loadButton);
		IOPanel.add(saveButton);
		add(IOPanel, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel (new BorderLayout (5,5));
		centerPanel.add(outerDrawPanel, BorderLayout.EAST);
		centerPanel.add(selectionPanel, BorderLayout.WEST); 
		centerPanel.add(actionPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
		setFocusable(true);
		addMouseListener(new RequestFocusListener ());
		addWindowListener(new RTSWindowListener ());
	}
	public void makeCustomPanelComponents()
	{	
		locationActionList = new JComboBox<String>(locationActions); 
		locationActionList.addItemListener(new RtsItemActionListener());
		JPanel locActionPanel = new JPanel();
		locActionPanel.add(locationActionList);
		ArrayList<JComponent> bsActionList = new ArrayList<JComponent>();
		bsActionList.add(locActionPanel);        
		actionComponents.put("BusStop" + actionListKeyword, bsActionList);
		lineActionList = new JComboBox<String>(lineActions);
		lineActionList.addItemListener(new RtsItemActionListener());
		JPanel lineActionPanel = new JPanel();
		lineActionPanel.add(lineActionList);
		ArrayList<JComponent> lnActionList = new ArrayList<JComponent>();
		lnActionList.add(lineActionPanel);
		actionComponents.put("Line" + actionListKeyword, lnActionList);
		pointOfInterestActionList = new JComboBox<String>(pointOfInterestActions);
		pointOfInterestActionList.addItemListener(new RtsItemActionListener ());
		JPanel pointOfInterestActionPanel = new JPanel ();
		pointOfInterestActionPanel.add(pointOfInterestActionList);
		ArrayList<JComponent> poiActionList = new ArrayList<JComponent>();
		poiActionList.add(pointOfInterestActionPanel);
		actionComponents.put("PointOfInterest" + actionListKeyword, poiActionList);
		JLabel nLabel = new JLabel("Name");
		name = new JTextField(10);
		JPanel nPanel = new JPanel();
		nPanel.add(nLabel);
		nPanel.add(name);
		JLabel dLabel = new JLabel("Duration");
		duration = new JTextField("Duration", 3);
		dPanel = new JPanel();
		dPanel.add(dLabel);
		dPanel.add(duration);
		JLabel xLabel = new JLabel("x");
		x = new JTextField(4);
		JPanel xPanel = new JPanel();
		xPanel.add(xLabel);
		xPanel.add(x);
		JLabel yLabel = new JLabel("y");
		y = new JTextField(4);
		JPanel yPanel = new JPanel();
		yPanel.add(yLabel);
		yPanel.add(y);
		firstH = new JTextField(4);
		firstM = new JTextField(4);
		JPanel firstPanel = new JPanel();
		firstPanel.add(new JLabel("First"));
		firstPanel.add(firstH);
		firstPanel.add(new JLabel(":"));
		firstPanel.add(firstM);
		lastH = new JTextField(4);
		lastM = new JTextField(4);
		JPanel lastPanel = new JPanel();
		lastPanel.add(new JLabel("Last"));
		lastPanel.add(lastH);
		lastPanel.add(new JLabel(":"));
		lastPanel.add(lastM);
		period = new JTextField(4);
		JPanel periodPanel = new JPanel();
		periodPanel.add(new JLabel("Period"));
		periodPanel.add(period);
		JLabel timePointLabel = new JLabel("Time Point?");
		timePoint = new JCheckBox("", false);
		timePoint.addActionListener((ActionListener)this);
		JPanel tPanel = new JPanel();
		tPanel.add(timePointLabel);
		tPanel.add(timePoint);
		JLabel desLabel = new JLabel("Description");
		description = new JTextField(10);
		JPanel desPanel = new JPanel ();
		desPanel.add(desLabel);
		desPanel.add(description);
		JButton search = new JButton("Search");
		search.setBackground(Color.blue);
		search.addActionListener((ActionListener)this);
		JButton ok = new JButton("OK");
		ok.setBackground(Color.blue);
		ok.addActionListener((ActionListener)this);
		String[] busStopNames = {};
		busStopList = new JComboBox<String>(busStopNames);
		busStopList.addItemListener(new BusStopComboBoxListener ());
		busStopListDep = new JComboBox<String>(busStopNames);
		busStopListDest = new JComboBox<String>(busStopNames);
		String[] lineNames = {};
		lineList = new JComboBox<String>(lineNames);
		lineList.addItemListener(new LineComboBoxListener ());
		String [] pointOfInterestNames = {};
		pointOfInterestList = new JComboBox<String> (pointOfInterestNames);
		pointOfInterestDepList = new JComboBox<String> (pointOfInterestNames);
		pointOfInterestDestList = new JComboBox<String> (pointOfInterestNames);
		JPanel bsListPanel = new JPanel();
		bsListPanel.add(new JLabel("BusStop"));
		bsListPanel.add(busStopList);
		JPanel bsListPanelDep = new JPanel();
		bsListPanelDep.add(new JLabel("Departure"));
		bsListPanelDep.add(busStopListDep);
		JPanel bsListPanelDest = new JPanel();
		bsListPanelDest.add(new JLabel("Destination"));
		bsListPanelDest.add(busStopListDest); 
		JPanel lnListPanel = new JPanel();
		lnListPanel.add(new JLabel("Line"));
		lnListPanel.add(lineList);
		JPanel poiListPanel = new JPanel ();
		poiListPanel.add(new JLabel("PointOfInterest"));
		poiListPanel.add(pointOfInterestList);
		toBeDone = new JTextField (13);
		JPanel toBeDonePanel = new JPanel ();
		toBeDonePanel.add(toBeDone);
		JPanel poiDepPanel = new JPanel ();
		poiDepPanel.add(new JLabel("Departure"));
		poiDepPanel.add(pointOfInterestDepList);
		JPanel poiDestPanel = new JPanel ();
		poiDestPanel.add(new JLabel ("Destination"));
		poiDestPanel.add(pointOfInterestDestList);
		JPanel arrivalPanel = new JPanel ();
		arrivalH = new JTextField (4);
		arrivalM = new JTextField (4);
		arrivalPanel.add(new JLabel("Departure"));
		arrivalPanel.add(arrivalH);
		arrivalPanel.add(new JLabel(":"));
		arrivalPanel.add(arrivalM);
		ArrayList<JComponent> newBSComponents = new ArrayList<JComponent>();
		newBSComponents.add(nPanel);
		newBSComponents.add(xPanel);
		newBSComponents.add(yPanel);
		newBSComponents.add(tPanel);      
		newBSComponents.add(ok);
		actionComponents.put("BusStopNew", newBSComponents);
		ArrayList<JComponent> moveBSComponents = new ArrayList<JComponent>();
		moveBSComponents.add(bsListPanel);
		moveBSComponents.add(xPanel);
		moveBSComponents.add(yPanel);
		moveBSComponents.add(ok);
		actionComponents.put("BusStopMove", moveBSComponents);     
		ArrayList<JComponent> showBSComponents = new ArrayList<JComponent>();
		showBSComponents.add(bsListPanel);
		showBSComponents.add(ok);
		actionComponents.put("BusStopShow", showBSComponents);
		ArrayList<JComponent> hideBSComponents = new ArrayList<JComponent>();
		hideBSComponents.add(bsListPanel);
		hideBSComponents.add(ok);
		actionComponents.put("BusStopHide", hideBSComponents);
		ArrayList<JComponent> showLnsComponents = new ArrayList<JComponent>();
		showLnsComponents.add(bsListPanel);
		showLnsComponents.add(ok);
		actionComponents.put("BusStopWrite Line Info", showLnsComponents);       
		ArrayList<JComponent> newLNComponents = new ArrayList<JComponent>();
		newLNComponents.add(nPanel);
		newLNComponents.add(bsListPanelDep);
		newLNComponents.add(bsListPanelDest);
		newLNComponents.add(firstPanel);
		newLNComponents.add(lastPanel);
		newLNComponents.add(periodPanel);
		newLNComponents.add(ok);
		actionComponents.put("LineNew", newLNComponents);
		ArrayList<JComponent> addBSComponents = new ArrayList<JComponent>();
		addBSComponents.add(toBeDonePanel);
		addBSComponents.add(lnListPanel);
		addBSComponents.add(bsListPanel);
		addBSComponents.add(dPanel);
		addBSComponents.add(ok);
		actionComponents.put("LineAdd BusStop", addBSComponents); 
		ArrayList<JComponent> writeSchComponents = new ArrayList<JComponent>();
		writeSchComponents.add(lnListPanel);
		writeSchComponents.add(ok);
		actionComponents.put("LineWrite Schedule", writeSchComponents);  
		ArrayList<JComponent> newPOIComponents = new ArrayList<JComponent> ();	
		newPOIComponents.add(nPanel);
		newPOIComponents.add(xPanel);
		newPOIComponents.add(yPanel);
		newPOIComponents.add(desPanel);
		newPOIComponents.add(ok);
		actionComponents.put("PointOfInterestNew", newPOIComponents);
		ArrayList<JComponent> movePOIComponents = new ArrayList<JComponent> ();
		movePOIComponents.add(poiListPanel);
		movePOIComponents.add(xPanel);
		movePOIComponents.add(yPanel);
		movePOIComponents.add(ok);
		actionComponents.put("PointOfInterestMove", movePOIComponents);
		ArrayList<JComponent> showPOIComponents = new ArrayList<JComponent> ();
		showPOIComponents.add(poiListPanel);
		showPOIComponents.add(ok);
		actionComponents.put("PointOfInterestShow", showPOIComponents);
		ArrayList<JComponent> hidePOIComponents = new ArrayList<JComponent> ();
		hidePOIComponents.add(poiListPanel);
		hidePOIComponents.add(ok);
		actionComponents.put("PointOfInterestHide", hidePOIComponents);
		ArrayList<JComponent> searchPOIComponents = new ArrayList<JComponent> ();
		searchPOIComponents.add(poiDepPanel);
		searchPOIComponents.add(poiDestPanel);
		searchPOIComponents.add(arrivalPanel);
		searchPOIComponents.add(search);
		actionComponents.put("PointOfInterestSearch For Line", searchPOIComponents);
	}
	private void resetFields()
	{
		name.setText("");
		x.setText("");
		y.setText("");
		duration.setText("0");
		period.setText("");
		firstH.setText("");
		firstM.setText("");
		lastH.setText("");
		lastM.setText("");
		description.setText("");
		arrivalH.setText("");
		arrivalM.setText("");
		timePoint.setSelected(false);
		dPanel.setVisible(false);
	}
	private void updateCustomComponent()
	{
		resetFields();	

		actionListPanel.removeAll();
		actionListPanel.add(actionComponents.get(rtsItem + actionListKeyword).get(0), BorderLayout.NORTH);
		actionListPanel.revalidate();
		actionListPanel.repaint();

		customPanel.removeAll();
		ArrayList<JComponent> list = actionComponents.get(rtsItem + actionMode);
		for(int i=0; i < list.size(); i++)
			customPanel.add(list.get(i));
		customPanel.revalidate();
		customPanel.repaint();
	}
	private void doAction () throws IOException {
		String actionCommand = rtsItem + actionMode;
		if (actionCommand.equals("BusStopNew")) {
			BusStop b = null;
			String n = name.getText();
			int x1 = Integer.parseInt(x.getText ());
			int y1 = Integer.parseInt (y.getText ());
			if (timePoint.isSelected()) {
				b = new TimePoint(n, x1, y1);
			}
			else {
				b = new BusStop (n, x1, y1);
			}
			points.put(n, b);
			allObjects.put(n, (Object)b);
			busStopList.addItem(n);
			busStopListDep.addItem(n);
			busStopListDest.addItem(n);
		}
		else if (actionCommand.equals("LineNew")) {
			Line l = null;
			String n = name.getText ();
			BusStop b = points.get((String)busStopListDep.getSelectedItem ());
			String n1 = b.getName();
			int x1 = b.getX();
			int y1 = b.getY();
			TimePoint departure1 = new TimePoint(n1, x1, y1);
			BusStop c = points.get((String)busStopListDest.getSelectedItem ());
			String n2 = c.getName();
			int x2 = c.getX ();
			int y2 = c.getY ();
			BusStop destination1 = new BusStop (n2, x2, y2);
			int startMin = Integer.parseInt(firstM.getText());
			int startH = Integer.parseInt(firstH.getText ());
			Time startTime = new Time (startH, startMin);
			int endH = Integer.parseInt(lastH.getText ());
			int endMin = Integer.parseInt(lastM.getText());
			Time endTime = new Time (endH, endMin);
			int period1 = Integer.parseInt(period.getText ());
			l = new Line (n, departure1, destination1, startTime, endTime, period1);
			l.addNextBusStop(departure1, 0);
			lines1.add(l);
			lines.put(n, l);
			lineList.addItem(n);
			allObjects.put(n, (Object)l);
		}
		else if (actionCommand.equals("BusStopShow")) {
			BusStop b = points.get((String)busStopList.getSelectedItem ());
			if(!visibleBuses.containsValue(b)) {
				visibleBuses.put(b.getName(), b);
				visibleLocations.put(b.getName(), b);
			}
		}
		else if (actionCommand.equals("BusStopHide")) {
			BusStop b = points.get((String)busStopList.getSelectedItem ());
			if (visibleBuses.containsValue(b)) {
				visibleBuses.remove(b.getName());
				visibleLocations.remove(b.getName());
			}
		}
		else if (actionCommand.equals("BusStopMove")) {
			BusStop b = points.get((String)busStopList.getSelectedItem ());
			int x1 = Integer.parseInt(x.getText());
			int y1 = Integer.parseInt(y.getText ());
			if (visibleBuses.containsValue(b)) {
				if((x1 <= 500 && x1 >= 0) && (y1 >= 0  && y1 <= 400))  {
					b.setX(x1);
					b.setY(y1);
				}
			}
		}
		else if (actionCommand.equals("BusStopWrite Line Info")) {
			BusStop b = points.get((String)busStopList.getSelectedItem());
			b.printLineInfo(System.out);
			String x = b.getLineInfo();
			JTextArea lineArea = new JTextArea (x);
			lineArea.setSize(200, 100);
			lineArea.setLineWrap(true);
			lineArea.setWrapStyleWord(true);
			JScrollPane linePane = new JScrollPane(lineArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			JOptionPane.showMessageDialog(null, linePane, "Line Info", JOptionPane.INFORMATION_MESSAGE);
		}
		else if (actionCommand.equals("LineAdd BusStop")) {
			Line l = lines.get((String)lineList.getSelectedItem ());
			BusStop b = points.get((String)busStopList.getSelectedItem ());
			if (b instanceof TimePoint) {
				int duration1 = Integer.parseInt(duration.getText ());
				l.addNextBusStop(b, duration1);
			}
			else {
				l.addNextBusStop(b, 0);
			}
		}
		else if (actionCommand.equals("LineWrite Schedule")) {
			class RTSRefreshButtonListener implements ActionListener {
				String lineName;
				JTextArea newTextArea;
				JFrame frame3;
				JScrollPane pane1;
				public RTSRefreshButtonListener (String s, JTextArea t, JFrame f, JScrollPane p) {
					lineName = s;
					newTextArea = t;
					frame3 = f;
					pane1 = p;
				}
				public void actionPerformed (ActionEvent e) {
					Line l2 = lines.get(lineName);
					String schedule = l2.getSchedule();
					ArrayList<Time> times = l2.times;
					for (int i = 0; i < times.size (); i ++) {
						int amount = l2.timePointAmount;
						if ((i + 1) % amount != 0) {
							schedule += times.get(i) + "        ";
						}
						else {
							schedule += times.get(i) + "\n";
						}
					}
					newTextArea.setText(schedule);
					frame3.remove(pane1);
					pane1 = new JScrollPane(newTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					frame3.add(pane1, BorderLayout.CENTER);
					frame3.revalidate ();
					frame3.repaint();
				}
			}
			l1 = lines.get((String)lineList.getSelectedItem());
			theSchedule = l1.getSchedule();
			frame2 = new JFrame("The Schedule for Line " + l1.name);
			frame2.setVisible(true);
			frame2.setSize(800, 400);
			frame2.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			JButton refreshButton = new JButton("Refresh");
			JTextArea scheduleArea = new JTextArea (theSchedule);
			scheduleArea.setSize (200, 100);
			scheduleArea.setWrapStyleWord(true);
			scheduleArea.setLineWrap(true);
			JScrollPane schedulePane = new JScrollPane(scheduleArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			refreshButton.addActionListener(new RTSRefreshButtonListener (l1.name, scheduleArea, frame2, schedulePane));
			frame2.add(refreshButton, BorderLayout.NORTH);
			frame2.add(schedulePane, BorderLayout.CENTER);
		}
		else if (actionCommand.equals("PointOfInterestNew")) {
			PointOfInterest p = null;
			String n = name.getText();
			int x1 = Integer.parseInt(x.getText ());
			int y1 =  Integer.parseInt(y.getText());
			String d = description.getText();
			p = new PointOfInterest(n, x1, y1, d);
			pointsOfInterest.put(n, p);
			allObjects.put(n, (Object)p);
			pointOfInterestList.addItem(n);
			pointOfInterestDepList.addItem(n);
			pointOfInterestDestList.addItem(n);
		}
		else if (actionCommand.equals("PointOfInterestShow")) {
			PointOfInterest p = pointsOfInterest.get((String)pointOfInterestList.getSelectedItem ());
			if (!visiblePointsOfInterest.containsValue(p)) {
				visiblePointsOfInterest.put(p.getName (), p);
				visibleLocations.put(p.getName (), p);
			}
		}
		else if (actionCommand.equals("PointOfInterestHide")) {
			PointOfInterest p = pointsOfInterest.get((String)pointOfInterestList.getSelectedItem ());
			if (visiblePointsOfInterest.containsValue(p)) {
				visiblePointsOfInterest.remove(p.getName());
				visibleLocations.remove(p.getName());
			}
		}
		else if (actionCommand.equals("PointOfInterestMove")) {
			PointOfInterest p = pointsOfInterest.get((String)pointOfInterestList.getSelectedItem ());
			int x1 = Integer.parseInt(x.getText());
			int y1 = Integer.parseInt(y.getText ());
			if (visiblePointsOfInterest.containsValue(p)) {
				if((x1 <= 500 && x1 >= 0) && (y1 >= 0  && y1 <= 400))  {
					p.setX(x1);
					p.setY(y1);
				}
			}
		}
		else {
			final PointOfInterest departure = pointsOfInterest.get((String)pointOfInterestDepList.getSelectedItem ());
			final PointOfInterest destination = pointsOfInterest.get((String)pointOfInterestDestList.getSelectedItem ());
			if (arrivalH.getText ().equals("") && arrivalM.getText ().equals("")) {
				final QueryResult result = search (points.values (), departure, destination);
				if (result != null) {
					JPanel answerPanel = new JPanel ();
					answerPanel.add(new JTextArea(result.toString ()));
					for (int i = 0; i < result.line.route.size (); i ++) {
						visibleBuses1.put(result.line.route.get(i).getName(), result.line.route.get(i));
					}
					visiblePointsOfInterest1.put(departure.getName(), departure);
					visiblePointsOfInterest1.put(destination.getName (), destination);
					labels1.put(destination.getName (), destination);
					labels1.put(departure.getName (), departure);
					labels1.put(result.departure.getName(), result.departure);
					labels1.put(result.destination.getName (), result.destination);
					JPanel drawPanel = new JPanel () {
						private static final long serialVersionUID = 1L;
						public void paintComponent (Graphics g) {
							super.paintComponent(g);
							int width = getWidth();
							int height = getHeight(); 
							if (width == 0 || height == 0)
							{
								width = (int)getPreferredSize().getWidth();
								height = (int)getPreferredSize().getHeight();
							}
							g.clearRect(0,  0,  getWidth(),  getHeight());
							try {
								BufferedImage mapImage2 = ImageIO.read(new File("Campus Map.gif"));
								g.drawImage (mapImage2, 0, 0, width, height, null);
							} catch (IOException e) {
								e.printStackTrace();
							}
							Collection<BusStop> busStops = visibleBuses1.values  ();
							for (BusStop b: busStops) {
								try {
									busImage = ImageIO.read(new File("bus.jpg"));
									busImage1 = ImageIO.read(new File("Bus1.jpg"));
									if (b instanceof TimePoint) {
										b.setImage(busImage);
									}
									else {
										b.setImage(busImage1);
									}
									b.draw(g, 60, 60);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							Collection<PointOfInterest> pointsOfInterest1 = visiblePointsOfInterest1.values ();
							for (PointOfInterest p: pointsOfInterest1) {
								try {
									poiImage = ImageIO.read(new File("poi.gif"));
									p.setImage(poiImage);
									p.draw(g, 60, 60);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
							}
							Set<Map.Entry<Line,Polygon>> lineSet = drawings.entrySet ();
							for (Map.Entry<Line,Polygon> l1: lineSet) {
								if (l1.getKey().name.equals(result.line.name)) {
									Polygon p = l1.getValue();
									Color c = colors.get(p);
									g.setColor(c);
									g.fillPolygon(p);
								}
							}
							FontMetrics fontMetrics = g.getFontMetrics();
							Color prev = g.getColor();
							Set<Map.Entry<String, Location>> labelSet = labels1.entrySet();
							for(Map.Entry<String, Location> m: labelSet)
							{ 	 
								int height1 = fontMetrics.getHeight();
								int width1 = fontMetrics.stringWidth((String)m.getKey());
								int x = ((Location)m.getValue()).getX();
								int y = ((Location)m.getValue()).getY();
								if (y > getHeight())
									y =  getHeight();
								else if (y - height1 < 0)
									y = height1;
								if (x + width1 > getWidth())
									x = getWidth() - width1;
								else if (x < 0)
									x = 0;
								g.setColor(new Color(255, 215, 0));
								g.fillRect(x, y - height1, width1, height1);
								g.setColor(Color.black);
								g.drawString((String)m.getKey(), x, y);
							} 	 
							g.setColor(prev);
						}
						public Dimension getPreferredSize () {
							return new Dimension(500,400);
						}
					};
					JFrame frame3 = new JFrame ("Answer");
					frame3.setSize(850, 500);
					frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame3.setVisible (true);
					frame3.add(answerPanel, BorderLayout.WEST);
					frame3.add(drawPanel, BorderLayout.EAST);
				}
				else {
					JOptionPane.showMessageDialog(null, "No answer could be found", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else {
				int arrivalHours = Integer.parseInt(arrivalH.getText ());
				int arrivalMinutes = Integer.parseInt(arrivalM.getText());
				Time arrivalTime = new Time(arrivalHours, arrivalMinutes);
				final QueryResult2 result = search2(points.values (), departure, destination, arrivalTime);
				if (result != null) {
					JPanel answerPanel = new JPanel ();
					answerPanel.add(new JTextArea(result.toString ()));
					for (int i = 0; i < result.line.route.size (); i ++) {
						visibleBuses1.put(result.line.route.get(i).getName(), result.line.route.get(i));
					}
					visiblePointsOfInterest1.put(departure.getName(), departure);
					visiblePointsOfInterest1.put(destination.getName (), destination);
					labels1.put(destination.getName (), destination);
					labels1.put(departure.getName (), departure);
					labels1.put(result.departure.getName(), result.departure);
					labels1.put(result.destination.getName (), result.destination);
					JPanel drawPanel = new JPanel () {
						private static final long serialVersionUID = 1L;
						public void paintComponent (Graphics g) {
							super.paintComponent(g);
							int width = getWidth();
							int height = getHeight(); 
							if (width == 0 || height == 0)
							{
								width = (int)getPreferredSize().getWidth();
								height = (int)getPreferredSize().getHeight();
							}
							g.clearRect(0,  0,  getWidth(),  getHeight());
							try {
								BufferedImage mapImage2 = ImageIO.read(new File("Campus Map.gif"));
								g.drawImage (mapImage2, 0, 0, width, height, null);
							} catch (IOException e) {
								e.printStackTrace();
							}
							Collection<BusStop> busStops = visibleBuses1.values  ();
							for (BusStop b: busStops) {
								try {
									busImage = ImageIO.read(new File("bus.jpg"));
									busImage1 = ImageIO.read(new File("Bus1.jpg"));
									if (b instanceof TimePoint) {
										b.setImage(busImage);
									}
									else {
										b.setImage(busImage1);
									}
									b.draw(g, 60, 60);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							Collection<PointOfInterest> pointsOfInterest1 = visiblePointsOfInterest1.values ();
							for (PointOfInterest p: pointsOfInterest1) {
								try {
									poiImage = ImageIO.read(new File("poi.gif"));
									p.setImage(poiImage);
									p.draw(g, 60, 60);
								}
								catch (IOException e) {
									e.printStackTrace();
								}
							}
							Set<Map.Entry<Line,Polygon>> lineSet = drawings.entrySet ();
							for (Map.Entry<Line,Polygon> l1: lineSet) {
								if (l1.getKey().name.equals(result.line.name)) {
									Polygon p = l1.getValue();
									Color c = colors.get(p);
									g.setColor(c);
									g.fillPolygon(p);
								}
							}
							FontMetrics fontMetrics = g.getFontMetrics();
							Color prev = g.getColor();
							Set<Map.Entry<String, Location>> labelSet = labels1.entrySet();
							for(Map.Entry<String, Location> m: labelSet)
							{ 	 
								int height1 = fontMetrics.getHeight();
								int width1 = fontMetrics.stringWidth((String)m.getKey());
								int x = ((Location)m.getValue()).getX();
								int y = ((Location)m.getValue()).getY();
								if (y > getHeight())
									y =  getHeight();
								else if (y - height1 < 0)
									y = height1;
								if (x + width1 > getWidth())
									x = getWidth() - width1;
								else if (x < 0)
									x = 0;
								g.setColor(new Color(255, 215, 0));
								g.fillRect(x, y - height1, width1, height1);
								g.setColor(Color.black);
								g.drawString((String)m.getKey(), x, y);
							} 	 
							g.drawString("Departure time: " + result.arrivalTime.toString(), 100, 100);
							g.setColor(prev);
						}
						public Dimension getPreferredSize () {
							return new Dimension(500,400);
						}
					};
					JFrame frame3 = new JFrame ("Answer");
					frame3.setSize(850, 500);
					frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame3.setVisible (true);
					frame3.add(answerPanel, BorderLayout.WEST);
					frame3.add(drawPanel, BorderLayout.EAST);
				}
				else {
					JOptionPane.showMessageDialog(null, "No answer could be found", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		drawingPanel.repaint ();
		resetFields ();
	}
	public void writeLocations(String fileName) throws IOException
	{
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
		Set<Map.Entry<String,Object>> allObjects1 = allObjects.entrySet();
		for(Map.Entry<String,Object> m: allObjects1)
		{	 
			output.writeUTF(m.getKey());
			output.writeObject(m.getValue());
		}	
		output.close();
	}
	public void readLocations(String fileName) 
	{
		try { 
			this.fileName = fileName;
			if (!new File(fileName).exists())
			{	 
				new File(fileName).createNewFile();			   	
				return;
			}	
			else {			    
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
				while (true)
					try {	
						String name = input.readUTF();
						Object object1 = input.readObject();
						allObjects.put(name, object1);
						if (object1 instanceof BusStop) {
							points.put(name, (BusStop)object1);
							busStopList.addItem(name);
							busStopListDep.addItem(name);
							busStopListDest.addItem(name);
						}
						else if (object1 instanceof Line) {
							lines.put(name, (Line)object1);
							lineList.addItem(name);
						}
						else {
							pointsOfInterest.put(name, (PointOfInterest)object1);
							pointOfInterestList.addItem(name);
						}
					}     
				catch(EOFException e) 
				{
					System.out.println("EOF reached!");
					input.close();
					break;
				}	
			}
		}
		catch(IOException e) {
			e.printStackTrace(); 
			System.out.println("Problem reading Locations and  Lines");
		}
		catch(ClassNotFoundException e) { 
			System.out.println("Class couldn't be loaded when reading Location and Line objects");
		}
	}
	public void actionPerformed (ActionEvent e) {
		try {
			String command = e.getActionCommand();
			if (command.equals("OK") || command.equals("Search")) {
				doAction ();
			}
		}
		catch (Exception f) {
			System.out.print(f);
		}
	}
	public static void main(String[] args) {
		RTSMapApp myapp = new RTSMapApp("The BusStop on the Map!"); 
		myapp.setResizable(false);
		myapp.setSize(850, 500);
		myapp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myapp.setVisible(true);
		myapp.updateCustomComponent();
	}
}

