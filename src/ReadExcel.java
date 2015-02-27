import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ReadExcel
{
	
	private static LinkedHashMap<Integer, Trip>	tripList	= new LinkedHashMap<Integer, Trip>();
	
	/**
	 * @param args
	 */
	public static void readFile(File file)
	{
		
		boolean eof = false;
		try
		{
			
			// Create a workbook object from the file at specified location.
			// Change the path of the file as per the location on your computer.
			Workbook wrk1 = Workbook.getWorkbook(file);
			
			// Obtain the reference to the first sheet in the workbook
			Sheet sheet1 = wrk1.getSheet(0);
			int rows = sheet1.getRows();
			int row = 1;
			int col = 0;
			while (!eof)
			{
				if (row >= rows)
				{
					eof = true;
					break;
				}
				Cell cellVal = sheet1.getCell(col, row);
				if (cellVal == null)
				{
					eof = true;
				}
				int tripId = Integer.parseInt(cellVal.getContents());
				Trip trip = tripList.get(tripId);
				if (trip == null)
				{
					int speed = 0;
					Trip tripObj = new Trip(tripId);
					speed = Integer.parseInt(sheet1.getCell(14, row)
							.getContents());
					tripObj.addSpeed(speed);
					tripList.put(tripId, tripObj);
				} else
				{
					trip.addSpeed(Integer.parseInt(sheet1.getCell(14, row)
							.getContents()));
				}
				row++;
			}
			
			writeTripstoFile(file);
			
		} catch (BiffException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (WriteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void writeTripstoFile(File file) throws BiffException,
			IOException, RowsExceededException, WriteException
	{
		Workbook wb = Workbook.getWorkbook(file);
		WritableWorkbook writableWorkbook = Workbook.createWorkbook(file, wb);
		int index = 1;
		for (Map.Entry<Integer, Trip> entry : tripList.entrySet()) {
		    int tripId = entry.getKey();
		    Trip trip = entry.getValue();
		    
		    WritableSheet writableSheet = writableWorkbook.createSheet(Integer.toString(tripId), index);
		    Label second = new Label(0, 0, "SECOND");
		    writableSheet.addCell(second);
		    Label speed = new Label(1, 0, "SPEED");
		    writableSheet.addCell(speed);
		    
		    ArrayList<Integer> speedList = trip.getSpeedList();
		    for(int j=0;j<speedList.size();j++)
		    {
		    	Number sec = new Number(0,j+1,j);
		    	writableSheet.addCell(sec);
		    	Number spd = new Number(1,j+1,speedList.get(j));
		    	writableSheet.addCell(spd);
		    }
		    index++;
		}
		// Write and close the workbook
		writableWorkbook.write();
		writableWorkbook.close();
		
	}
}
