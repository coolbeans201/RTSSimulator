//Create the BusStop subclass
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
public class BusStop extends Location {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10;
	protected ArrayList <Line> line;

	public BusStop () {
		super ();
		line = new ArrayList <Line> ();
	}

	public BusStop (String n, int x, int y) {
		super (n, x, y);
		line = new ArrayList <Line> ();
	}

	public BusStop (String n, int x, int y, Line [] l, int length) {
		super (n, x, y);
		line = new ArrayList <Line> (length);

		for (int i = 0; i < l.length; i ++) {
			line.add(l[i]);
		}
	}

	public void addLine (Line l) {
		line.add (l);
	}

	public void printLineInfo (OutputStream o) throws IOException {
		o.write (getLineInfo().getBytes());
	}
	public String getLineInfo () {
		String s = super.toString () + "\n";
		s += "Following lines stop at this bus stop: " + "\n";
		for (int i = 0; i < line.size () - 1; i ++) {
			s += line.get(i).name + ", ";
		}		
		s += line.get(line.size () - 1).name + "\n";
		return s;
	}
}
