import java.util.ArrayList;
public class TestProject2
{
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Inputfilename expected as the commandline parameter");
			System.exit(1);
		}
		try {
			ArrayList<Object> list = IOUtil.readBusStopAndLineInfo(args[0]);
			for(int i=0; i < list.size(); i++)
			{
				Object o = list.get(i);
				if (o instanceof Line)
				{
					((Line)o).printRoute(System.out);
					((Line)o).printSchedule(System.out);
				}
				else if (o instanceof BusStop){
					((BusStop)o).printLineInfo(System.out);
				}
				
			}
		}
		catch(Exception e) { 
			System.out.println(e); 
		}
	}
}
