package TestCase;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.codeborne.selenide.WebDriverRunner;
import com.lb.common.reporting.Reporting;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import static com.codeborne.selenide.Selenide.$;
import functions.InitBrowser;
import functions.Utilities;

public class SimpleTestCase {

	WebDriver driver = null;
	ExtentTest test=null;
	ExtentReports extent=null;
	Reporting report = new Reporting();
	String path = "C:\\Test";
	String ScreenshotFileName="";
	@BeforeTest
	public void Setup() throws IOException
	{

		try {
			//Initialize the report
			File directory = new File("C:\\Test\\Reports\\screenshots");
	  	    if (! directory.exists()){
	  	        directory.mkdir();
	  	    }
	}catch (Exception e) {
		System.out.println("Directory not exists");
	}
	  	  extent = new ExtentReports("C:\\Test\\Reports\\LiveLink_"+report.GetDateTime() +".html", true);
	  	  ScreenshotFileName ="C:\\Test\\Reports\\screenshots\\Livelink_";
	  	//Start the test extent report
			test = extent.startTest("Live Link Download","");
			driver =InitBrowser.chooseBrowser("chrome");
			WebDriverRunner.setWebDriver(driver);

	
	}
	@Test
	public void Login() throws Exception
	{
		driver.navigate().to(Utilities.getPropertyValue("URL", "config.properties"));
		$(By.xpath("//*[@id='lst-ib']")).wait(3000);
		if($(By.xpath("//*[@id='lst-ib']")).isDisplayed())
		{
			report.ReportPassAttachScreenshot(test, "Verifying the search textbox ", "GoogleHomePage");
		}
			
	}
	
	
@AfterTest
public void TearDown()
{
	extent.endTest(test);
	extent.flush();}
}
