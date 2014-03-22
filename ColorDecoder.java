import java.awt.Color;
public class ColorDecoder {
	private static boolean equalsIgnoreCase(String s1, String s2)
	{
		return (s1.compareToIgnoreCase(s2) == 0);
	}
	public static Color getColor(String colorName)
	{
		if (equalsIgnoreCase(colorName, "black"))
		   return Color.black;	
		else if (equalsIgnoreCase(colorName, "blue"))
			   return Color.blue;
		else if (equalsIgnoreCase(colorName, "cyan"))
			   return Color.cyan;
		else if (equalsIgnoreCase(colorName, "darkGray"))
			   return Color.darkGray;
		else if (equalsIgnoreCase(colorName, "gray"))
			   return Color.gray;
		else if (equalsIgnoreCase(colorName, "green"))
			   return Color.green;
		else if (equalsIgnoreCase(colorName, "lightGray"))
			   return Color.lightGray;
		else if (equalsIgnoreCase(colorName, "magenta"))
			   return Color.magenta;
		else if (equalsIgnoreCase(colorName, "orange"))
			   return Color.orange;
		else if (equalsIgnoreCase(colorName, "pink"))
			   return Color.pink;
		else if (equalsIgnoreCase(colorName, "red"))
			   return Color.red;
		else if (equalsIgnoreCase(colorName, "white"))
			   return Color.white;
		else if (equalsIgnoreCase(colorName, "yellow"))
			   return Color.yellow;
		return null;
	}
}
