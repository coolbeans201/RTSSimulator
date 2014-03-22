//Create the Line class
import java.util.ArrayList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
public class Line implements Serializable{
	private static final long serialVersionUID = 10;
	protected String name;
	protected Location departure;
	protected Location destination;
	protected Time first;
	protected Time last;
	protected int period;
	protected ArrayList <BusStop> route;
	protected ArrayList <Integer> timePoints;
	protected ArrayList<Time> times;
	protected int timePointAmount;
	public Line () {
		name = "";
		departure = null;
		destination = null;
		first = null;
		last = null;
		period = 0;
		route = new ArrayList<BusStop> ();
		timePoints = new ArrayList<Integer> ();
		times = new ArrayList<Time> ();
		timePointAmount = 0;
	}

	public Line (String n, Location dep, Location des, Time f, Time l, int p) {
		name = n;
		departure = dep;
		destination = des;
		first = f;
		last = l;
		period = p;
		route = new ArrayList<BusStop> ();
		timePoints = new ArrayList<Integer> ();
		times = new ArrayList<Time> ();
		timePointAmount = 0;
	}

	public void addNextBusStop (BusStop bs, int x) {
		bs.addLine(this);
		route.add (bs);
		if(bs instanceof TimePoint) {
			timePoints.add(x);
			timePointAmount ++;
		}
	}
	
	public void printSchedule (OutputStream o) throws IOException {
		o.write(getSchedule().getBytes ());
	}
	
	public String getSchedule () {
		String s = "SCHEDULE FOR LINE " + name + "\n";
		for (int i = 0; i< route.size (); i ++) {
			if (route.get(i) instanceof TimePoint) {
				s += route.get(i).getName() + "           ";
			}
		}
		s += "\n";
		while (first.compareTo(last) <= 0) {
			int index = 0;
			Time first1 = first.clone ();
			for (int i = 0; i < route.size (); i ++) {
				if (route.get(i) instanceof TimePoint) {
					if (index == 0) {
						s += first1 + "              ";
						Time x = first1.clone ();
						times.add(x);
					}
					else 
					{
						first1 = first1.advanceMinutes(timePoints.get (index));
						times.add(first1);
						s += first1 + "              ";
					}
					index ++;
				}
			}
			s += "\n";
			first = first.advanceMinutes(period);
		}
		return s;
	}

	public void printRoute (OutputStream o) throws IOException {
		o.write(getRoute ().getBytes ());
	}
	
	public String getRoute () {
		String s = "ROUTE FOR LINE " + name + "\n";
		for (int i = 0; i < route.size () - 1; i ++) {
			s += route.get(i).getName () + ", ";
		}
		s += route.get(route.size () - 1).getName () + "\n";
		return s;
	}
}
