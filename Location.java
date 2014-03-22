import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
//Create the Location superclass
public class Location implements Drawable, Serializable{
	private static final long serialVersionUID = 10;
	protected String name;
	protected int x;
	protected int y;
	protected static Image image;
	public Location (){
		name = "";
		x = 0;
		y = 0;
	}

	public Location (String n, int x, int y) {
		setName (n);
		setX (x);
		setY (y);
	}

	public String getName () {
		return name;
	}

	public int getX () {
		return x;
	}

	public int getY () {
		return y;
	}

	public void setName (String n) {
		this.name = n;
	}

	public void setX (int x) {
		this.x = x;
	}

	public void setY (int y) {
		this.y = y;
	}

	public String toString () {
		String s = name + " at (" + x + "," + y + ")";
		return s;
	}

	public void setImage (Image i) {
		image = i;
	}
	
	public Image  getImage () {
		return image;
	}
	
	public boolean covers (int x, int y) {
	       return (this.x <= x && x <= this.x + 60 && this.y <= y && y <= this.y + 60);
	}
	public void draw(Graphics g, int width, int height) {
		g.drawImage(image, x, y, width, height, null);
	}
	
	public int distanceFrom(Location other)
	{
		double xdiff = this.x - other.x;
		double ydiff = this.y - other.y;
		return (int)Math.sqrt(xdiff*xdiff + ydiff*ydiff);
	}
}
