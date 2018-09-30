package com.lb.common.reporting;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reporting {
	
	String screentshotPath ;
	File scrFile;
	private ExtentReports extent;
	String ScreenshotLocation;
	public String ExtentPath;
	public String ResultStatus;
	
	public String GetTestStatus()
	{
		if (ResultStatus.contains("Fail"))
			return ResultStatus;
		else
			return "Pass";
		
	}
	
	public ExtentReports initialize(ExtentTest test,Reporting report,String scenarioName)
	{
		ResultStatus = "";
		Random ranNum=new Random();
		//Check if reports folder exists or else create
		File directoryR = new File(System.getProperty("user.dir")+"\\Reports");
	    if (! directoryR.exists()){
	        directoryR.mkdir();
	    }
		File directory = new File(System.getProperty("user.dir")+"\\Reports\\"+scenarioName);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    //Check if screenshots folder within reports folder exists or else create
		File directorySS = new File(System.getProperty("user.dir")+"\\Reports\\"+scenarioName+"\\screenshots");
	    if (! directorySS.exists()){
	        directorySS.mkdir();
	    }
	    ExtentPath = System.getProperty("user.dir") + "\\Reports\\"+scenarioName+"\\"+scenarioName+"_"+ranNum.nextLong() +".html";
        extent = new ExtentReports(ExtentPath, true);

		extent.loadConfig(new File(System.getProperty("user.dir")+"\\"+"extent-config.xml"));
		return extent;
		
	}	

	
	public void endTest(ExtentTest test)
	{
		extent.endTest(test);
		extent.flush();
	}
	
	public void ReportPasswithscreenshot(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		   Random ranNum=new Random();
	        screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
	        takeScreenShot(screentshotPath);
	        Reporter.log(StepName, true);
	        test.log(LogStatus.PASS,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
	}
	
	
	public void ReportPassEmbedDocument(ExtentTest test, String StepName,String DocumentPath) throws Exception
	{
		Reporter.log(StepName, true);
		test.log(LogStatus.PASS, "<a href='file:///"+DocumentPath+"'>Link to the docment</a>");
	}
	
	/*
	 *  Below function without take screen shot attaching the existing screen shot to extend report 
	 *  
	 */
	public void ReportPassAttachScreenshot(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		Reporter.log(StepName, true);
		test.log(LogStatus.PASS,StepName,test.addScreenCapture(ScreenShotname.replace(System.getProperty("user.dir"), "..")));
	}
	/*
	 * Below function without take screen shot attaching the existing screen shot to extend report
	 */
	public void ReportFailAttachScreenshot(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		
		Reporter.log(StepName, false);
		test.log(LogStatus.FAIL,StepName,test.addScreenCapture(ScreenShotname.replace(System.getProperty("user.dir"), "..")));
		ResultStatus="Fail";
	}
	public void ReportPasswithscreenshotForSap(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShotRobot(screentshotPath);
		Reporter.log(StepName, true);
		test.log(LogStatus.PASS,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
	}
	
	public void ReportWarningwithscreenshotForSap(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShotRobot(screentshotPath);
		Reporter.log(StepName, true);
		test.log(LogStatus.WARNING,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
	}
	
	public void ReportPass(ExtentTest test, String StepName){
		Reporter.log(StepName, true);
		test.log(LogStatus.PASS, StepName,"");
	}
	
	public void ReportFailwithscreenshotForScenario(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{ 
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShot(screentshotPath);
		Reporter.log(StepName, false);
		test.log(LogStatus.FAIL,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
		ResultStatus="Fail";
	}
	
	public void ReportFailwithscreenshot(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{ 
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShot(screentshotPath);
		Reporter.log(StepName, false);
		test.log(LogStatus.FAIL,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
		ResultStatus="Fail";
	}
	
	public void ReportPasswithscreenshotForScenario(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{ 
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShot(screentshotPath);
		Reporter.log(StepName, false);
		test.log(LogStatus.PASS,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
	}
	
	public void ReportFailwithscreenshotForSap(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		Random ranNum=new Random();
		screentshotPath= ScreenShotname+ranNum.nextLong()+".png";
		takeScreenShotRobot(screentshotPath);
		Reporter.log(StepName, false);
		test.log(LogStatus.FAIL,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..\\..")));
		ResultStatus="Fail";
	}
	
	public void ReportFail(ExtentTest test, String StepName){
		Reporter.log(StepName, false);
		test.log(LogStatus.FAIL, StepName,"");
		ResultStatus="Fail";
	}
	
	public void takeScreenShot (String fileName) throws Exception
	{
		Thread.sleep(3000);
	
		scrFile  = ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile,new File(fileName));
	 
	}
	
	public void takeScreenShotRobot (String fileName) throws Exception
	{
		Robot robot = new Robot();
		BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(screenShot, "JPG", new File(fileName));
	}
	public String GetDateTime ()
	{
		      int day, month, year;
		      int second, minute, hour;
		      GregorianCalendar date = new GregorianCalendar();
		 
		      day = date.get(Calendar.DAY_OF_MONTH);
		      month = date.get(Calendar.MONTH);
		      year = date.get(Calendar.YEAR);
		 
		      second = date.get(Calendar.SECOND);
		      minute = date.get(Calendar.MINUTE);
		      hour = date.get(Calendar.HOUR);
		 return Integer.toString(day)+Integer.toString(month+1)+Integer.toString(year)+Integer.toString(hour)+Integer.toString(minute)+Integer.toString(second);
	}
	
    public  void assertEquals(Reporting report,String actual, String expected, String ScreenshotLocation, ExtentTest test) throws Exception{
        if(actual.equalsIgnoreCase(expected))
               report.ReportPasswithscreenshot(test, "Expected - "+expected+" and actuals - "+actual+" are matching",ScreenshotLocation);
        
        else
               report.ReportFailwithscreenshot(test, "Expected - "+expected+" and actuals - "+actual+" are not matching", ScreenshotLocation);
 }
 
    /****************************************************************************
	 * Function Name : assertContains
	 * Purpose : Compares whether two strings equal or not without screenshot
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public void assertContains(String expected, String actual,String message,ExtentTest test, Reporting report,String screenFilename) throws Exception
	{
		//Validate the purchasing group		
		if(expected.contains(actual))
			report.ReportPasswithscreenshotForSap(test, message +" : Actual: " + actual  +" - Expected: " + expected,screenFilename);			
		else
			report.ReportFailwithscreenshotForSap(test, "FAILED : " + message +" : Actual: " + actual  +" - Expected: " + expected ,screenFilename);

	}
	
	/****************************************************************************
	 * Function Name : assertTrue
	 * Purpose : 
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public void asserttrue(Boolean expected, String message, Reporting report,ExtentTest test) {
		// TODO Auto-generated method stub
		if (expected==true)
        	report.ReportPass(test, message+ " is displayed and its working as expected");
         else
            report.ReportFail(test, message+ "is not displayed and its not working as expected");
	}
	
	/****************************************************************************
	 * Function Name : assertEquals
	 * Purpose : Compares whether two strings equal or not without screenshot
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public void assertEquals(ExtentTest test,Reporting report,String actual, String expected, String message) throws Exception {
        if (expected.equalsIgnoreCase(actual))
        	report.ReportPass(test, message+ " is displayed and its working as expected. Expected:"+expected+". And Actual:"+actual);
           else
                 report.ReportFail(test, message+ "is not displayed and its not working as expected. Expected:"+expected+". And Actual:"+actual);
      }

	/****************************************************************************
	 * Function Name : assertContains
	 * Purpose : Compares whether two strings equal or not without screenshot
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
 public void assertContains(String expected, String actual,String message,ExtentTest test, Reporting report) throws Exception
	{
		//Validate the purchasing group		
		if(expected.contains(actual))
			report.ReportPass(test, message +" : Actual: " + actual  +" - Expected: " + expected);			
		else
			report.ReportFail(test, "FAILED : " + message +" : Actual: " + actual  +" - Expected: " + expected);

	}
 
 /****************************************************************************
	 * Function Name : 
	 * Purpose : 
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public  void assertEquals(ExtentTest test,Reporting report,String ScreenShotname,Object actual, Object expected, String message) throws Exception {
     if (expected.equals(actual))
     	report.ReportPasswithscreenshotForSap(test, message+ " and its working as expected. Expected:"+expected+". And Actual:"+actual, ScreenShotname);
     
        else
              report.ReportFailwithscreenshotForSap(test, message+ " and its not working as expected. Expected:"+expected+". And Actual:"+actual, ScreenShotname);
   }
    
	
	/****************************************************************************
	 * Function Name : 
	 * Purpose : 
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public void assertEquals(ExtentTest test,Reporting report,String ScreenShotname,String actual, String expected, String message) throws Exception {
     if (expected.equalsIgnoreCase(actual))
     	report.ReportPasswithscreenshotForSap(test, message+ " is displayed and its working as expected. Expected:"+expected+". And Actual:"+actual,ScreenShotname);
        else
              report.ReportFailwithscreenshotForSap(test, message+ "is not displayed and its not working as expected. Expected:"+expected+". And Actual:"+actual, ScreenShotname);
   }
    
    
	/****************************************************************************
	 * Function Name : 
	 * Purpose : 
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public  void assertEquals(ExtentTest test, Reporting report,String ScreenShotname,boolean actual, boolean expected, String message) throws Exception {
     if(actual == expected)
     	report.ReportPasswithscreenshotForSap(test, message+ " is displayed and its working as expected. Expected:"+expected+". And Actual:"+actual, ScreenShotname);
        else
              report.ReportFailwithscreenshotForSap(test, message+ " is displayed and its not working as expected. Expected:"+expected+". And Actual:"+actual, ScreenShotname);
   }
     
	
 public  void assertEquals(Reporting report,int actual, int expected, String ScreenshotLocation, ExtentTest test) throws Exception{
        if(actual == expected)
               report.ReportPass(test, "Expected - "+expected+" and actuals - "+actual+" are matching");
        else
               report.ReportFailwithscreenshot(test, "Expected - "+expected+" and actuals - "+actual+" are not matching", ScreenshotLocation);
 }

 
 public  boolean assertGreater(Reporting report,Double actual, Double expected, String ScreenshotLocation, ExtentTest test) throws Exception{
	 boolean flag;
	 if(actual > expected)
         {
    	 report.ReportPass(test, "Expected - "+expected+" is greater than actuals - "+actual+" ");
    	flag =true;
         }
     		
     else
     {
    	report.ReportFailwithscreenshot(test, "Expected - "+expected+" is not greater than  "+actual+" ", ScreenshotLocation);
    	flag =false;
     }
     return flag;
}

 
 public  boolean assertEquals(Reporting report,Double actual, Double expected, String ScreenshotLocation, ExtentTest test) throws Exception{
    boolean flag= actual.equals(expected);
	 if(flag)
            report.ReportPasswithscreenshot(test, "Expected - "+expected+" and actuals - "+actual+" are matching",ScreenshotLocation);
     
     else
            report.ReportFailwithscreenshot(test, "Expected - "+expected+" and actuals - "+actual+" are not matching", ScreenshotLocation);
	 return flag;
 }
 public  void assertNull(Reporting report,String actual, String ScreenshotLocation, ExtentTest test) throws Exception{
        if(actual.equalsIgnoreCase(""))
               report.ReportFailwithscreenshot(test, "Actuals - "+actual+" is Null", ScreenshotLocation);
        else
               report.ReportPass(test, "Actuals - "+actual);
               
 }

 
 public  void assertEquals(Reporting report, boolean expected, boolean actual, String ScreenshotLocation, ExtentTest test) throws Exception{
        if(expected)
        {
               if(expected == actual)
                      report.ReportPass(test, "Label/Option is displayed");
               else
                      report.ReportFailwithscreenshot(test, "Label/Option is not displayed",ScreenshotLocation);
        }
        else
        {
               if(expected == actual)
                      report.ReportPass(test, "Label/Option is not displayed");
               else
                      report.ReportFailwithscreenshot(test, "Label/Option is displayed",ScreenshotLocation);
        }
 }


 
 public  String ScreenshotfilePath(String scenarioID, ExtentTest test,Reporting report) 
 {
        String scenario = scenarioID.replace(".0", "").replace("-", "_");
        String ScreenshotFileName = System.getProperty("user.dir") +"\\Reports\\"+scenario+"\\screenshots\\" + scenario+"_";
        return ScreenshotFileName;
 }
 
	public void ReportWarningwithscreenshot(ExtentTest test, String StepName,String ScreenShotname) throws Exception
	{
		screentshotPath= ScreenShotname+GetDateTime()+".png";
		takeScreenShot(screentshotPath);
		Reporter.log(StepName, false);
		test.log(LogStatus.WARNING,StepName,test.addScreenCapture(screentshotPath.replace(System.getProperty("user.dir"), "..")));
		ResultStatus = "WARN";
	}
	public ExtentReports newinitialize(ExtentTest test,Reporting report,String scenarioName)
    {
		Random ranNum=new Random();
    //Check if reports folder exists or else create
           File directoryR = new File(System.getProperty("user.dir")+"\\Reports");
        if (! directoryR.exists()){
            directoryR.mkdir();
        }
           File directory = new File(System.getProperty("user.dir")+"\\Reports\\"+scenarioName);
        if (! directory.exists()){
            directory.mkdir();
        }
        //Check if screenshots folder within reports folder exists or else create
           File directorySS = new File(System.getProperty("user.dir")+"\\Reports\\"+scenarioName+"\\screenshots");
        if (! directorySS.exists()){
            directorySS.mkdir();
        }
               
           extent = new ExtentReports(System.getProperty("user.dir") + "\\Reports\\"+scenarioName+"\\"+scenarioName+"_"+ranNum.nextLong() +".html", true);
           extent.loadConfig(new File(System.getProperty("user.dir")+"\\"+"extent-config.xml"));
           return extent;
    }
	

	public void ReportWarning(ExtentTest test, String StepName){
		Reporter.log(StepName, true);
		test.log(LogStatus.WARNING, StepName,"");
	}

}
	
	
