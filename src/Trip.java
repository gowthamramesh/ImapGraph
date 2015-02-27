import java.util.ArrayList;


public class Trip
{

	int tripId = 0;
	private ArrayList<Integer> speedList = new ArrayList<Integer>();
	
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
	
}
