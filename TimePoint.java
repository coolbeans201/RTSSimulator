//Create the TimePoint subclass
public class TimePoint extends BusStop {
	private static final long serialVersionUID = 10;
	
	public TimePoint () {
		super ();
	}
	
	public TimePoint (String n, int x, int y) {
		super (n, x, y);
	}
	
	public TimePoint (String n, int x, int y, Line [] l, int length) {
		super (n, x, y, l, length);
	}
}
