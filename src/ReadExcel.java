import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
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
			
			int year = 1900;
			int month = 0;
			int date = 0;
			int hour = 0;
			int min = 0;
			int sec = 0;
			while (!eof)
			{
				if (row >= rows)
				{
					eof = true;
					break;
				}
				Cell cellVal = sheet1.getCell(0, row);
				if (cellVal == null)
				{
					eof = true;
				}
				String[] contents = cellVal.getContents().split(";");
				int tripId = Integer.parseInt(contents[0]);
				Trip trip = tripList.get(tripId);
				if (trip == null)
				{
					int speed = 0;
					
					String dateVal = contents[1];
					String timeVal = contents[2];
					String dateArray[] = dateVal.split("/");
					String timeArray[] = timeVal.split(":");
					GregorianCalendar calendar = new GregorianCalendar(
							Integer.parseInt(dateArray[2]),
							Integer.parseInt(dateArray[0]),
							Integer.parseInt(dateArray[1]),
							Integer.parseInt(timeArray[0]), 
							Integer.parseInt(timeArray[1]),
							Integer.parseInt(timeArray[2]));
					Trip tripObj = new Trip(tripId);
					tripObj.setTime(calendar);
					speed = (int)Float.parseFloat(contents[14]);
					tripObj.addSpeed(speed);
					tripList.put(tripId, tripObj);
				} else
				{
					trip.addSpeed((int)Float.parseFloat(contents[14]));
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
		File tempfile = new File("output1.xls");
		WritableWorkbook writableWorkbook = Workbook.createWorkbook(tempfile);
		for (int k = 1; k < writableWorkbook.getNumberOfSheets(); k++)
		{
			writableWorkbook.removeSheet(k);
		}
		DateFormat customDateFormat = new DateFormat ("hh:mm:ss"); 
		WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
		int index = 1;
		for (Map.Entry<Integer, Trip> entry : tripList.entrySet())
		{
			int tripId = entry.getKey();
			Trip trip = entry.getValue();

		
			WritableSheet writableSheet = writableWorkbook.createSheet(
					Integer.toString(tripId), index);
			Label second = new Label(0, 0, "SECOND");
			writableSheet.addCell(second);
			Label speed = new Label(1, 0, "SPEED");
			writableSheet.addCell(speed);
			Label speedft = new Label(2, 0, "SPEED(FT/S)");
			writableSheet.addCell(speedft);
			Label distance = new Label(3, 0, "DISTANCE");
			writableSheet.addCell(distance);
			
			Label time = new Label(4, 0, "TIME");
			writableSheet.addCell(time);
			
			float dist = 0;
			ArrayList<Integer> speedList = trip.getSpeedList();
			Date now = trip.getDateTime(); 
			for (int j = 0; j < speedList.size(); j++)
			{
				
				Number sec = new Number(0, j + 1, j + 1);
				writableSheet.addCell(sec);
				Number spd = new Number(1, j + 1, speedList.get(j));
				writableSheet.addCell(spd);
				
				float spdinFt = (float) (speedList.get(j) * 0.911344);
				Number spdft = new Number(2, j + 1, spdinFt);
				writableSheet.addCell(spdft);
				
				if (j > 0)
				{
					dist += spdinFt;
				}
				
				Number distVal = new Number(3, j + 1, dist);
				writableSheet.addCell(distVal);
				
				now.setSeconds(now.getSeconds() + 1);

				DateTime dateCell = new DateTime(4, j+1, now, dateFormat); 
				writableSheet.addCell(dateCell); 
			   
			}
			index++;
		}
		// Write and close the workbook
		writableWorkbook.write();
		writableWorkbook.close();
		System.exit(0);
	}
}
