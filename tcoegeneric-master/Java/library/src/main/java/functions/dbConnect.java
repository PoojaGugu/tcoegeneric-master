package functions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public class dbConnect {


		/*Get the property from the file 
		 * FirstParameter : Filename
		 * Second Parameter :  Name of the Property
		 *Author :Venkata 
		 */
	public static String getProperty(String filename, String  nameofpropertie)
	{		String host = null;
		
			try {
		  
			
					File configFile = new File(filename);
					FileReader reader = new FileReader(configFile.getAbsoluteFile());
			 		Properties props = new Properties();
			 		props.load(reader);
			 		host = props.getProperty(nameofpropertie);
		            return host;
			} catch (Exception e) {
			}
			return host;
		}
	/*Set the property from the file 
	 * FirstParameter : Filename
	 * Second Parameter :  Name of the Property
	 * Third Parameter : Values Set to the property
	 *Author :Venkata 
	 */
	public static void setProperty(String filename,String Propertiename , String value)
	{
		
		 
		try {
			File configFile = new File(filename);
		    Properties props = new Properties();
		    
		    //Read file 
		    FileReader reader = new FileReader(configFile.getAbsoluteFile());
			props.load(reader);
			props.setProperty(Propertiename, value);
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "set vale " + Propertiename + value );
		    writer.close();
		
		} catch (IOException ex) {
			System.out.println(ex.getLocalizedMessage());
		    
		}
	}

	/*
	 * Creating the connection and return the Connection Object
	 * [Oracle Connection Object]
	 */

	public static Connection ConnectDB() throws IOException, ClassNotFoundException
	{
		Properties prop = new Properties();
		InputStream input = null;
		File file = new File("DB.properties");
		input = new FileInputStream(file.getAbsolutePath());
		
		// load a properties file
		prop.load(input);
		// get the property value and print it out
		String driver = prop.getProperty("DB_DRIVER");
		  Class.forName(driver);
      String url = prop.getProperty("DB_URL");
      String username = prop.getProperty("DB_USER");
      String pwd = prop.getProperty("DB_PWD");
      System.out.println("Connecting to database...");

		 Log.info("-------- Oracle JDBC Connection Testing ------");
		 
			try {

				Class.forName(driver);

			} catch (ClassNotFoundException e) {

				Log.info("Where is your Oracle JDBC Driver?");
				e.printStackTrace();
				

			}

			Log.info("Oracle JDBC Driver Registered!");

			Connection connection = null;

			try {
				connection = DriverManager.getConnection(url,username,pwd);

				if (connection != null) {
					Log.info("You made it, take control your database now!");
				} else {
					Log.info("Failed to make connection!");
				}

				return connection;
			} catch (SQLException e) {

				Log.info("Connection Failed! Check output console");
				e.printStackTrace();
				

			}
			return connection;
		 
	 }
	/*
	 * Creating the connection and return the Connection Object
	 * [SQL Connection Object]
	 */
	public static Connection SQLConnectDB() throws IOException, ClassNotFoundException {
		Properties prop = new Properties();
		InputStream input = null;
		Connection conn = null;
		   
		try {
			File file = new File("DB.properties");
		
			input = new FileInputStream(file.getAbsolutePath());
			
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			String driver = prop.getProperty("SQL_DB_DRIVER");
			
	      Class.forName(driver);
	      String url = prop.getProperty("SQL_DB_URL");
	      String username = prop.getProperty("SQL_DB_USER");
	      String pwd = prop.getProperty("SQL_DB_PWD");
	      System.out.println("Connecting to database...");
	      conn = DriverManager.getConnection(url, username,pwd);
	 
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 return conn;
		
	}
	
}
