package functions;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.saucerest.SauceREST;

public class SauceIntegratioPage 
{ 

	public static  ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>(); 
	public static WebDriver getWebDriver() 
	{ 
		return webDriver.get(); 
	} 

	public String getSessionId() 
	{ 
		return sessionId.get(); 
	} 

	/** 
	 * ThreadLocal variable which contains the Sauce Job Id. 
	 */ 
	public static ThreadLocal<String> sessionId = new ThreadLocal<String>(); 
	public static  WebDriver createDriver(String browserOrPalfor, String version, String osOrdevice, String methodName) throws IOException 
	{ 
		DesiredCapabilities capabilities = new DesiredCapabilities(); 

		// set desired capabilities to launch appropriate browser on Sauce 
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browserOrPalfor); 
		capabilities.setCapability(CapabilityType.VERSION, version); 
		capabilities.setCapability(CapabilityType.PLATFORM, osOrdevice); 
		capabilities.setCapability("screenResolution", "1920x1080");
		capabilities.setCapability("name", methodName); 
		String username = "VBethanboien";
		String accesskey = "dd72d21b-e04a-46b4-9896-72678bd0b5a9";//Utilities.getPropertyValue("sauceAccessKey", "CONFIG.properties");

		webDriver.set(new RemoteWebDriver(new URL("http://" +username + ":" + accesskey + "@ondemand.saucelabs.com:80/wd/hub"),  capabilities)); 
		// set current sessionId         
		String id = ((RemoteWebDriver) getWebDriver()).getSessionId().toString(); 
		sessionId.set(id); 
		System.out.println(String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", id, methodName)); 
		return webDriver.get(); 
	} 

	public static void UpdateResults(String Id, boolean testResults) throws IOException
	{
		String username = Utilities.getPropertyValue("sauceUserName", "CONFIG.properties");
		String accesskey = Utilities.getPropertyValue("sauceAccessKey", "CONFIG.properties");
		SauceREST saucerest = new SauceREST(username, accesskey);
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("passed", testResults);
		saucerest.updateJobInfo(Id, updates);
	}
}
