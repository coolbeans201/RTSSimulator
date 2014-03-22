//Create the test class
import java.util.HashMap;
public class TestProject1 {
	public static void main (String [] args) {
		// The bus stops with names BSi are meant to be regular bus stops for
		// which time information is not displayed in the schedule.
		// The others are meant to be time points for which time information
		// is printed in the schedule.
		// Note: (x,y) coordinates are not used in this project but will be
		// useful once we start incorporating graphics.
		String[] busStopNames = {"Rawling", "BS1", "Reitz", "BS2", "Macguire",
				"BS3", "SweetBay", "BS4", "BS5", "BS6", "OaksMall"};
		HashMap<String,BusStop> busStops = new HashMap<String, BusStop>();
		busStops.put(busStopNames[0], new TimePoint(busStopNames[0],70,10));
		busStops.put(busStopNames[1], new BusStop(busStopNames[1],65,15));
		busStops.put(busStopNames[2], new TimePoint(busStopNames[2],55,20));
		busStops.put(busStopNames[3], new BusStop(busStopNames[3],50,20));
		busStops.put(busStopNames[4], new TimePoint(busStopNames[4],45,45));
		busStops.put(busStopNames[5], new BusStop(busStopNames[5],20,50));
		busStops.put(busStopNames[6], new TimePoint(busStopNames[6],10,60));
		busStops.put(busStopNames[7], new BusStop(busStopNames[7],10,65));
		busStops.put(busStopNames[8], new BusStop(busStopNames[8],8,55));
		busStops.put(busStopNames[9], new BusStop(busStopNames[9],6,45));
		busStops.put(busStopNames[10], new TimePoint(busStopNames[10],3,35));
		// So line 20 starts from bus stop Rawling and heads towards SweetBay
		// bus stop. It starts the first service at 6:00 and does not serve
		// after 08:00. There is a bus that operates as line 20 every 15 minutes.
		HashMap<String,Line> lines = new HashMap<String, Line>();
		lines.put("20", new Line("20",busStops.get("Rawling"),
				busStops.get("SweetBay"), new Time(6,0), new Time(8,0), 15));
		Line line20 = lines.get("20");
		// Since Rawling is the first bus top the duration parameter is 0
		line20.addNextBusStop(busStops.get("Rawling"),0);
		// Since BS1 is not a TimePoint the duration parameter is 0
		line20.addNextBusStop(busStops.get("BS1"),0);
		// It takes 7 minutes for the bus to reach Reitz from the
		// most recent time point type of bus stop (Rawlings)
		line20.addNextBusStop(busStops.get("Reitz"),7);
		// Since BS2 is not a TimePoint the duration parameter is 0
		line20.addNextBusStop(busStops.get("BS2"),0);
		// It takes 8 minutes for the bus to reach Macguire from the
		// most recent time point type of bus stop (Reitz)
		line20.addNextBusStop(busStops.get("Macguire"),8);
		// Since BS3 is not a TimePoint the duration parameter is 0
		line20.addNextBusStop(busStops.get("BS3"),0);
		// It takes 9 minutes for the bus to reach SweetBay from the
		// most recent time point type of bus stop (Macguire)
		line20.addNextBusStop(busStops.get("SweetBay"),9);
		System.out.print(line20.getRoute());
		System.out.print(line20.getSchedule());
		lines.put("21", new Line("21",busStops.get("Macguire"),
				busStops.get("OaksMall"), new Time(9,30), new Time(13,15), 25));
		Line line21 = lines.get("21");
		line21.addNextBusStop(busStops.get("Macguire"),0);
		line21.addNextBusStop(busStops.get("BS3"),0);
		line21.addNextBusStop(busStops.get("SweetBay"),11);
		line21.addNextBusStop(busStops.get("BS4"),0);
		line21.addNextBusStop(busStops.get("BS5"),0);
		line21.addNextBusStop(busStops.get("BS6"),0);
		line21.addNextBusStop(busStops.get("OaksMall"),14);
		System.out.print(line21.getRoute());
		System.out.print(line21.getSchedule());
		for(int i=0; i < busStopNames.length; i++) {
			System.out.print(busStops.get(busStopNames[i]).getLineInfo());
		}
	}
}

