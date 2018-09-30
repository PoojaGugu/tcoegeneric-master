package functions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class InitBrowser {
	static  WebDriver browser = null;
	
		/****************************************************************************
		 * Function Name : chooseBrowser
		 * Purpose : Creating  driver object based on Browser String 
		 * Created By :  
		 * Created On : 
		 * Comments :We can execute remote [Selenium hub, sauce lab and local]
		 * Updates :
		 * @throws Exception 
		 ******************************************************************************/ 

	
	public static WebDriver chooseBrowser(String Browser) throws IOException{
//Getting Execution mode 
		String executionMode=Utilities.getPropertyValue("executionMode", "CONFIG.properties");
		String serverip=Utilities.getPropertyValue("serverip", "CONFIG.properties");
		
		if(Browser.equalsIgnoreCase("ff")) 
		{
			
			
			if(executionMode.equalsIgnoreCase("local"))
				
			{
				DesiredCapabilities capability = DesiredCapabilities.firefox();
				capability.setCapability("screenResolution", "1280x1024" );
				browser = new FirefoxDriver(capability);
			}
			else
			{
				 DesiredCapabilities capability = DesiredCapabilities.firefox();
				 capability.setCapability("screenResolution", "1280x1024" );
				 browser =new RemoteWebDriver(new URL("http://"+serverip+":4444/wd/hub"), capability);
			}
			browser.manage().window().maximize();
			browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			browser.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			return browser;

		}
		else if (Browser.equalsIgnoreCase("ie")) 
		{
			if(executionMode.equalsIgnoreCase("local"))
			{
				File file = new File("IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				browser = new InternetExplorerDriver();
			}
			{
				 DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
				 browser =new RemoteWebDriver(new URL("http://"+serverip+":4444/wd/hub"), capability);
			}
			
			browser.manage().window().maximize();
			browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			return browser;
		}
		else if (Browser.equalsIgnoreCase("chrome")) 
		{
			if(executionMode.equalsIgnoreCase("local"))
			{
			File file = new File("chromedriver.exe");
			System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			browser =new ChromeDriver();
			}
			else
			{
				DesiredCapabilities capability = DesiredCapabilities.chrome();
				browser =new RemoteWebDriver(new URL("http://"+serverip+":4444/wd/hub"), capability);
			}
			browser.manage().window().maximize();
			browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);



			return browser;
		}

		else 
		{
			browser = new FirefoxDriver();
			browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			return browser;
		}
	}

}
