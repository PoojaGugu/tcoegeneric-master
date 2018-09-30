package functions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.screenshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.openqa.selenium.By;
import org.testng.SkipException;





public class Utilities {
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";

/*
 * Assigning the  Reporting file Path
 */
	public static String strReportFilepath=GetReportFilePath();
	
	/****************************************************************************
	 * Constructor : GetReportFilePath
	 * Purpose : Get reporting file path
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/



	public static String GetReportFilePath(){

		Date date = new Date();
		SimpleDateFormat ft =  new SimpleDateFormat ("Eyyyy.MM.ddhhmmssazzz");
		String folderName = ft.format(date);
		//System.setProperty("selenide.screenshots", "false") ;
		//System.setProperty("selenide.reports", "C:\\Results\\Reports_" +  folderName + "\\Screenshots") ;

		new File("C:\\Results\\Reports_" +  folderName + "\\Screenshots").mkdirs() ;

		return "C:\\Results\\Reports_" +  folderName  ; 

	}


	/****************************************************************************
	 * Constructor : getToday
	 * Purpose : Get today's date specified format
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/

	public static String getToday(String format){
		Date date = new Date();
		return new SimpleDateFormat(format).format(date);
	}

	/****************************************************************************
	 * Constructor : addDayTodaysdate
	 * Purpose : Adding days to date [MM/DD/YY]
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public static String  addDayTodaysdate(String date_string , int days ) throws ParseException
	{



		String DATE_FORMAT = "MM/dd/yy";

		java.text.SimpleDateFormat sdf =
				new java.text.SimpleDateFormat(DATE_FORMAT);
		Date date = (Date)sdf.parse(date_string);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date); 
		c1.add(Calendar.DAY_OF_MONTH,days);

		return sdf.format(c1.getTime());

	}
 

	/****************************************************************************
	 * Constructor : returnCurrentUTCTime
	 * Purpose : Give UTC time in 12 hour format converted to decimal
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/

	// 

	public static String returnCurrentUTCTime(){


		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));

		//Local time zone   
		SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

		//Time in GMT
		if(Integer.parseInt(dateFormatGmt.format(new Date()).split(" ")[1].split(":")[0])>12){

			return (Integer.parseInt(dateFormatGmt.format(new Date()).split(" ")[1].split(":")[0]) - 12)  + "." +  Integer.parseInt(dateFormatGmt.format(new Date()).split(" ")[1].split(":")[0]) ;
		} else

			return Integer.parseInt(dateFormatGmt.format(new Date()).split(" ")[1].split(":")[0]) + "." + Integer.parseInt(dateFormatGmt.format(new Date()).split(" ")[1].split(":")[0]);


	}
	/****************************************************************************
	 * Constructor : compareDate
	 * Purpose : Comparing the UI date with excel data sheets date 
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	public static boolean compareDate(String dateToCompareUI,String dateToCompareExcel)
	{
		String[] dateUI = dateToCompareUI.split("/");
		String[] dateExcel = dateToCompareExcel.split("/");
		boolean flag = false;
		for(int i=0;i<dateUI.length;i++)
		{
			if(Integer.parseInt(dateUI[i])<10)
			{
				if(dateUI[i].replace("0", "").equals(dateExcel[i].replace("0", "")))
					flag=true;
			}
			else
			{
				if(dateUI[i].equals(dateExcel[i]))
					flag=true;
			}
		}
		return flag;
	}
	
	/****************************************************************************
	 * Constructor : generateRandomString
	 * Purpose : This method generates random string
	 * Created By :  
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/
	 
		static String CHAR_LIST ="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
		public static String generateRandomString(int RANDOM_STRING_LENGTH){
	        StringBuffer randStr = new StringBuffer();
	        
	        
	        
	        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
	            int number = getRandomNumber();
	            char ch = CHAR_LIST.charAt(number);
	            randStr.append(ch);
	        }
	        return randStr.toString();
	    }
	
		/****************************************************************************
		 * Constructor : getRandomNumber
		 * Purpose : This method generates random numbers
		 * Created By :  
		 * Created On : 
		 * Comments :
		 * Updates :
		 * @throws Exception 
		 ******************************************************************************/
	    private static int getRandomNumber() {
	        int randomInt = 0;
	        Random randomGenerator = new Random();
	        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
	        if (randomInt - 1 == -1) {
	            return randomInt;
	        } else {
	            return randomInt - 1;
	        }
	    }

	    /****************************************************************************
		 * Constructor : getPropertyValue
		 * Purpose : Return value for properties file
		 * Created By :  
		 * Created On : 
		 * Comments :
		 * Updates :
		 * @throws Exception 
		 ******************************************************************************/
			
		public static String getPropertyValue(String propertyName,String propertyFile) throws IOException{
			
			Properties prop = new Properties();
			InputStream input = null;
			File file = new File(propertyFile);
			input = new FileInputStream(file.getAbsolutePath());

			// load a properties file
			prop.load(input);
			// get the property value and return
			return  prop.getProperty(propertyName);
			
			
						
			
		}
		  /****************************************************************************
				 * Constructor : isProcessRunning
				 * Purpose :  Find the running process and Kill
				 * Created By :  
				 * Created On : 
				 * Comments :
				 * Updates :
				 * @throws Exception 
				 ******************************************************************************/
		
		public static boolean isProcessRunning(String serviceName) throws Exception {

		       
		       Process p = Runtime.getRuntime().exec(TASKLIST);
		       BufferedReader reader = new BufferedReader(new InputStreamReader(
		          p.getInputStream()));
		       String line;
		       while ((line = reader.readLine()) != null) {

		         System.out.println(line);
		         if (line.contains(serviceName)) {
		          return true;
		         }
		       }

		       return false;

		       }
		public static void killProcess(String serviceName) throws Exception {

			  Runtime.getRuntime().exec(KILL + serviceName);

			}
		/*
		 * Donated by Mano
		 * Sending mailing with Attachment 
		 * Parameters  : host ,EmailUser,EmailPassword, fromAddress,toAddress,msgSubject, msgBody attachFiles 
		 * 
		 * 	String[] attachFiles =  {"file1","file2"}
		 * without attachment file  attachFiles =null
		 * Ex :sendMailWithAttachment("webmail.limitedbrands.com","limited/user","password","test@mast.com","test@mast.com","TEST TEST","Testing mail Appi",attachFiles);
		 * Tested and modified by venkata
		 */
		
		public static void sendMailWithAttachment(String host,final String EmailUser, final String EmailPassword,String fromAddress,String toAddress,String msgSubject,String msgBody,String[] attachFiles) throws UnsupportedEncodingException, MessagingException
		{
			Properties props = new Properties();
		
			
			props.put("mail.smtp.host",host);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.ssl.trust", host);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.connectiontimeout", "10000");
			
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(EmailUser, EmailPassword);
				}
			});
			
			
			InternetAddress fromAddressObj = new InternetAddress(fromAddress, "");
			InternetAddress toAddressObj = null;
			
			Address[] TO =null;
			Message msg = new MimeMessage(session);
			/*
			 * if want to send multiple recipients 
			 */
			if (toAddress.contains(";") )
			{
				String[] toAddressary = toAddress.split(";");
				int toAddresslen = toAddressary.length;
				
				TO = new Address[toAddresslen];
				for ( int i=0;i<toAddresslen;i++)
				{
					TO[i] =  new InternetAddress(toAddressary[i]);
				}
				/* Address[] cc = new Address[] {
	                    ,
	                     new InternetAddress("sWER@gmail.com")};*/
				
				msg.addRecipients(Message.RecipientType.TO,TO);
			}
			else
			{
				toAddressObj =new InternetAddress(toAddress, "");
				msg.addRecipient(Message.RecipientType.TO,toAddressObj);
			}
			
			msg.setFrom(fromAddressObj);
			//msg.setRecipients(Message.RecipientType.TO , "");
		
			msg.setSubject(msgSubject);
			msg.setSentDate(new Date());
			
			// creates message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(msgBody, "text/html");
			
			// creates multi-part
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			
			if (attachFiles != null && attachFiles.length > 0) 
			{
				for (String filePath : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();

					try {
						attachPart.attachFile(filePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(attachPart);
				}
			}
			// sets the multi-part as e-mail's content
						msg.setContent(multipart);

						Transport transport = session.getTransport("smtp");
						transport.connect();
						transport.sendMessage(msg, msg.getAllRecipients());
	
		
}
}