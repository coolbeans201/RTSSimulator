//Create the PointOfInterest class
public class PointOfInterest extends Location {
	private static final long serialVersionUID = 10;
	protected String description;
	
	public PointOfInterest () {
		super ();
		description = "";
	}
	
	public PointOfInterest (String n, int x, int y, String d) {
		super (n, x, y);
		description = d;
	}
	
	public String getDescription () {
		return description;
	}
	
	public void setDescription (String d) {
		description = d;
	}
}
