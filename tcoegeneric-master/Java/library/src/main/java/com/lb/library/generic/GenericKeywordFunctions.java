package com.lb.library.generic;

import java.util.Set;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import org.openqa.selenium.WebDriver;

public class GenericKeywordFunctions {
	public static void switchWindowToTitleContaining(WebDriver driver,
			String titleSubString) throws InterruptedException {

		Set<String> allWindows = driver.getWindowHandles();
		for(String curWindow : allWindows){
			driver.switchTo().window(curWindow);
			Thread.sleep(3000);
			//	String currTitle = driver.getTitle() ;
			if(driver.getTitle().contains("Salary Adjustment Approval Requested for")){
				break;
			}

		}

	}
	public static WebDriver switchWindow(String title)
    {
      
         Set<String> windowIterator = getWebDriver().getWindowHandles();
         int count =0;
         WebDriver popup = null;
         for (String s : windowIterator) {
             String windowHandle = s; 
             if(count==0)
             {
               popup =  getWebDriver().switchTo().window(windowHandle);
             }
             if(!title.equals(""))
              {
               popup =  getWebDriver().switchTo().window(windowHandle);
               if(popup.getTitle().contains(title))
               {
                      return popup;
                     
               }
               count++;
             }
             
         
       }
         return popup;
    }
}
