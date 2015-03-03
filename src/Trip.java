import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class Trip
{

	int tripId = 0;
	private ArrayList<Integer> speedList = new ArrayList<Integer>();
	private GregorianCalendar	timeVal;
	
	public Trip(int tripIdVal)
	{
		tripId = tripIdVal;
	}

	public void addSpeed(int speed)
	{
		speedList.add(speed);
	}

	public ArrayList<Integer> getSpeedList()
	{
		return speedList;
		
	}

	public void setTime(GregorianCalendar calendar)
	{
		timeVal = calendar;
	}

	public Date getDateTime()
	{
		return timeVal.getTime();
	}
	
}
