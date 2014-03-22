import java.io.Serializable;

//Create the time class
public class Time implements Serializable {
	private static final long serialVersionUID = 10;
	private int hours;
	private int minutes;

	public Time () {
		hours = 0;
		minutes = 0;
	}

	public Time (int h, int m) {
		set (h, m);
	}

	public int getHours () {
		return hours;
	}

	public int getMinutes () {
		return minutes;
	}

	public void set (int h, int m) {
		hours = h;
		minutes = m;
	}

	public Time clone () {
		Time t = new Time (hours, minutes);
		return t;
	}

	public boolean equals (Time l) {
		if (hours == l.getHours() && minutes == l.getMinutes()) {
			return true;
		}
		else {
			return false;
		}
	}

	public int compareTo (Time l) {
		int totalMinutes = this.getHours() * 60 + this.getMinutes();
		int totalMinutes2 = l.getHours() * 60 + l.getMinutes();
		return totalMinutes - totalMinutes2;
	}

	public String toString () {
		return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes;
	}

	public Time advanceMinutes (int minutes) {
		int totalMinutes = hours * 60 + this.minutes;
		totalMinutes += minutes;
		hours = totalMinutes / 60;
		if (hours > 24) {
			hours -= 24;
		}
		this.minutes = totalMinutes % 60;
		Time t = new Time (hours, this.minutes);
		return t;
	}
}
