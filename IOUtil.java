//Create the IO functionality
import java.util.ArrayList;
import java.lang.Exception;
import java.util.Scanner;
import java.io.*;
public class IOUtil {
	public IOUtil () {

	}
	public static ArrayList<Object> readBusStopAndLineInfo (String fileName) throws Exception {
		ArrayList<BusStop> busStops = new ArrayList<BusStop> ();
		ArrayList<Line> lines = new ArrayList<Line> ();
		ArrayList<Integer> durations = new ArrayList<Integer> ();
		ArrayList<Object> objectList = new ArrayList<Object> ();
		ArrayList<String> theRoute = new ArrayList<String> ();
		int period1;
		Location departure = null;
		Location destination = null;
		String lineName;
		Time startTime1 = null;
		Time endTime1 = null;
		Scanner input = new Scanner (new File (fileName));
		while (input.hasNext()) {
			String line1 = input.nextLine ();
			Scanner input1 = new Scanner (line1);
			input1.useDelimiter("[=]+");
			String busStopList = input1.next ();
			if (!busStopList.equals("busstops")) {
				input1.close ();
				throw new Exception ("Invalid bus stop name.");	
			}
			String listOfBusStops = input1.next ();
			Scanner input2 = new Scanner (listOfBusStops);
			input2.useDelimiter("[\\s=<>]+");
			while (input2.hasNext()) {
				String theList = input2.next ();
				Scanner input3 = new Scanner (theList);
				input3.useDelimiter("[\\s=,<>]+");
				String busStopName = input3.next ();
				int x = input3.nextInt ();
				int y = input3.nextInt ();
				if (busStopName.startsWith ("[") && busStopName.endsWith ("]")) {
					busStops.add (new TimePoint(busStopName.substring(1, busStopName.length () - 1), x, y));
				}
				else {
					busStops.add (new BusStop(busStopName, x, y));
				}
				input3.close ();
			}
			input1.close ();
			input2.close ();
			while (input.hasNextLine ()) {
				String lineLine = input.nextLine ();
				Scanner input4 = new Scanner (lineLine);
				input4.useDelimiter("[=]+");
				String lineList = input4.next ();
				if (!lineList.equals("line")) {
					input4.close ();
					throw new Exception ("Invalid line name.");
				}
				lineName = input4.next ();
				input4.close ();
				String routeLine = input.nextLine ();
				Scanner input5 = new Scanner (routeLine);
				input5.useDelimiter ("[=]+");
				String routeList = input5.next ();
				if (!routeList.equals ("route")) {
					input5.close ();
					throw new Exception ("Invalid route name.");
				}
				String listofRoutes = input5.next ();
				input5.close ();
				Scanner input6 = new Scanner (listofRoutes);
				input6.useDelimiter("[\\s=<>]+");
				while (input6.hasNext ()) {
					String theList2 = input6.next ();
					Scanner input7 = new Scanner (theList2);
					input7.useDelimiter("[\\s=,<>]+");
					String busName = input7.next ();
					theRoute.add(busName);
					String durationString = input7.next ();
					int duration = Integer.parseInt (durationString);
					theRoute.add(durationString);
					durations.add(duration);
					input7.close ();
				}
				input6.close ();
				String startLine = input.nextLine ();
				Scanner input8 = new Scanner (startLine);
				input8.useDelimiter("[=]+");
				String start = input8.next ();
				if (!start.equals ("start")) {
					input8.close ();
					throw new Exception ("Invalid start name.");
				}
				String startTime = input8.next ();
				input8.close ();
				Scanner input9 = new Scanner(startTime);
				input9.useDelimiter("[\\s:]+");
				int hours = input9.nextInt ();
				int minutes = input9.nextInt ();
				startTime1 = new Time (hours, minutes);
				input9.close();
				String lastLine = input.nextLine ();
				Scanner input10 = new Scanner(lastLine);
				input10.useDelimiter("[=]+");
				String last = input10.next ();
				if (!last.equals("last")) {
					input10.close ();
					throw new Exception ("Invalid last name.");
				}
				String endTime = input10.next ();
				input10.close ();
				Scanner input11 = new Scanner (endTime);
				input11.useDelimiter("[\\s:]+");
				int hours2 = input11.nextInt ();
				int minutes2 = input11.nextInt ();
				endTime1 = new Time (hours2, minutes2);
				input11.close ();
				String periodLine = input.nextLine ();
				Scanner input12 = new Scanner (periodLine);
				input12.useDelimiter("[=]+");
				String period = input12.next ();
				if (!period.equals ("period")) {
					input12.close ();
					throw new Exception ("Invalid period name.");
				}
				period1 = input12.nextInt ();
				if (period1 <= 0) {
					input12.close ();
					throw new Exception ("Period is not positive.");
				}
				input12.close ();
				String departureName = theRoute.get (0);
				String destinationName = theRoute.get(theRoute.size () - 2);
				for (int m = 0; m < objectList.size (); m ++) {
					if (objectList.get(m) instanceof TimePoint) {
						TimePoint tp1 = (TimePoint)(objectList.get(m));
						if (departureName.equals(tp1.getName())) {
							departure = new Location (departureName, tp1.getX(), tp1.getY());
						}
						else if (destinationName.equals(tp1.getName())) {
							destination = new Location (destinationName, tp1.getX(), tp1.getY());
						}
					}
				}
				Line line = new Line (lineName, departure, destination, startTime1, endTime1, period1);
				lines.add(line);
				for (int i = 0; i < lines.size (); i ++)
				for (int j = 0; j < theRoute.size (); j += 2) {
					for (int k = 0; k < busStops.size (); k++) {
						if (theRoute.get(j).equals(busStops.get(k).getName ())) {
							lines.get(i).addNextBusStop(busStops.get(k),Integer.parseInt(theRoute.get(j+1)));
						}
					}
				}
			}
			for (int i = 0; i < busStops.size (); i++) {
				objectList.add(busStops.get(i));
			}
			for (int i = 0; i < lines.size (); i ++) {
				objectList.add(lines.get(i));
			}
			}
		input.close ();
		return objectList;
	}
}
