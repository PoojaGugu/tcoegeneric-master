package functions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.testng.Reporter;

public class ClipBoardUtil implements ClipboardOwner{

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}
	/**
	    * Place a String on the clipboard, and make this class the
	    * owner of the Clipboard's contents.
	    */
	    public void setClipboardContents(String aString){
	      StringSelection stringSelection = new StringSelection(aString);
	      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	      clipboard.setContents(stringSelection, this);
	    }
	    public String getClipboardContents() {
	        String result = "";
	        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	        //odd: the Object param of getContents is not currently used
	        Transferable contents = clipboard.getContents(null);
	        boolean hasTransferableText =
	          (contents != null) &&
	          contents.isDataFlavorSupported(DataFlavor.stringFlavor)
	        ;
	        if (hasTransferableText) {
	          try {
	            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
	          }
	          catch (UnsupportedFlavorException  ex){
	            System.out.println(ex);
	            ex.printStackTrace();
	          }
	          catch ( IOException ex){
		            System.out.println(ex);
		            ex.printStackTrace();
		          }
	        }
	        return result;
	      }
	    
	    public static String getCurrentDate()
	    {
	    	Date date = new Date(System.currentTimeMillis());
		    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		    String s = formatter.format(date);
		 	return s ;
	    } 
	    public static String getCurrentDatePlusdays(int days)
	    {
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    	Calendar c = Calendar.getInstance();
	    	c.setTime(new Date()); // Now use today date.
	    	c.add(Calendar.DATE, days); // Adding 5 days
	    	String output = sdf.format(c.getTime());
	    	System.out.println(output);

			return output;
	    } 
	    
	    
	    public static String getDataFromCSV(String strHeader,String strFilePath, String strFileName)
	    {
	    String csvFile =strFilePath+"\\"+strFileName;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String strDataList="" ;
		String[] Data = null;
		System.out.println("File Name -->"+csvFile);

		try {

			br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {
			Data = line.split(cvsSplitBy);
//			System.out.println("Data "+Data.length);
			
			
			if (Data[0].equalsIgnoreCase(strHeader))
			{
				//System.out.println("The header "+strHeader);
				for (String strEachData : Data)
				{
					
					if (strDataList.isEmpty())
					{
						strDataList = strEachData ;
					}
					else
					{	
						strDataList =strDataList +"\n" +strEachData ;
					}
				}
			}	
		}
			
		System.out.println(strDataList + "\n And Number of "+ strHeader+" are \t"+ (Data.length-1));
		Reporter.log("List of Data\n"+ strDataList);
			

		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return strDataList;
	  }
	    
	    
	    
}

