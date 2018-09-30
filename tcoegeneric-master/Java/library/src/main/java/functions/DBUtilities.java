package functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.testng.log4testng.Logger;

import com.microsoft.sqlserver.jdbc.SQLServerException;

public class DBUtilities {

	
	static PreparedStatement preparedStatement = null;
	static final Logger logger = Logger.getLogger(DBUtilities.class);
	String className = this.getClass().getName();
	public static String dbPropertiefilename;
	
	
	/****************************************************************************
	 * Function Name : ConnectDB
	 * Purpose : Creating the connection and return the Connection Object
	 * Created By :  [Oracle Connection Object]
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 


	public static Connection ConnectDB() throws IOException, ClassNotFoundException {
		Properties prop = new Properties();
		InputStream input = null;
		File file;

		if (null == dbPropertiefilename)
			file = new File("DB.properties");
		else
			file = new File(dbPropertiefilename);

		input = new FileInputStream(file.getAbsolutePath());

		// load a properties file
		prop.load(input);
		// get the property value and print it out
		String driver = prop.getProperty("DB_DRIVER");
		Class.forName(driver);
		String url = prop.getProperty("DB_URL");
		String username = prop.getProperty("DB_USER");
		String pwd = prop.getProperty("DB_PWD");
		// System.out.println("Connecting to database...");
		logger.info("-------- Oracle JDBC Connection Testing ------");

		try {

			Class.forName(driver);

		} catch (ClassNotFoundException e) {

			logger.debug("Where is your Oracle JDBC Driver?");
			e.printStackTrace();

		}

		logger.info("Oracle JDBC Driver Registered!");

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, pwd);

			if (connection != null) {
				logger.info("You made it, take control your database now!");
			} else {
				logger.info("Failed to make connection!");
			}

			return connection;
		} catch (SQLException e) {

			logger.debug("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return connection;

	}
	/****************************************************************************
	 * Function Name : ConnectDB
	 * Purpose : Creating the connection and return the Connection Object
	 * Created By :  [SQL Connection Object]
	 * Created On : 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 
	
	public static Connection SQLConnectDB() throws IOException, ClassNotFoundException {
		Properties prop = new Properties();
		InputStream input = null;
		Connection conn = null;

		File file;
		try {

			if (null == dbPropertiefilename)
				file = new File("DB.properties");
			else
				file = new File(dbPropertiefilename);

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
			conn = DriverManager.getConnection(url, username, pwd);

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
	/****************************************************************************
	 * Function Name : getCombinationID
	 * Purpose :  SRP E2E get CombinationID base on Test Case Name 
	 * Created By :  [SQL Connection Object]
	 * Created On : 
	 * Parameter :TestCase name 
	 *  returns the Combination ID 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 

	public static String getCombinationID(String testCaseName)
			throws ClassNotFoundException, IOException, SQLException {
		String QueryStr = "select depends_on_id from tgdependency where test_group_id in ( select tg.id    from combination comb inner join test_case tcase on tcase.id = comb.testcase_id INNER JOIN test testtble  ON   testtble.combination_id = comb.id inner join  test_group tg on tg.tests_id = testtble.id where tcase.name  like (SELECT name  FROM test_case WHERE name like '%"
				+ testCaseName + "'))";
		int TestGroupID[] = DBUtilities.getIDs(QueryStr);
		String DepIds = null;

		if (TestGroupID.length == 1) {
			DepIds = Integer.toString(TestGroupID[0]);
		} else {
			for (int row = 0; row < TestGroupID.length; row++) {
				if (row == 0)

				{
					DepIds = Integer.toString(TestGroupID[row]);
				} else {
					DepIds = DepIds + "," + Integer.toString(TestGroupID[row]);
				}
			}
		}
		return DepIds;
	}
	/****************************************************************************
	 * Function Name : getCombinationID
	 * Purpose :  SRP E2E get CombinationID base on Test Case Name 
	 * Created By :  [SQL Connection Object]
	 * Created On : 
	 * Parameter :TestCase name  and scenariosID
	 *  returns the Combination ID 
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 
	public static String getCombinationID(String testCaseName, String scenarioID)
			throws ClassNotFoundException, IOException, SQLException {

		String QueryStr = "select depends_on_id from tgdependency where test_group_id in ( select tg.id    from combination comb inner join test_case tcase on tcase.id = comb.testcase_id INNER JOIN test testtble  ON   testtble.combination_id = comb.id inner join  test_group tg on tg.tests_id = testtble.id where tcase.name  like (SELECT name  FROM test_case WHERE name like '%"
				+ testCaseName + "') and comb.scenario_id = " + scenarioID + ")";
		int TestGroupID[] = DBUtilities.getIDs(QueryStr);
		String DepIds = null;

		if (TestGroupID.length == 1) {
			DepIds = Integer.toString(TestGroupID[0]);
		} else {
			for (int row = 0; row < TestGroupID.length; row++) {
				if (row == 0)

				{
					DepIds = Integer.toString(TestGroupID[row]);
				} else {
					DepIds = DepIds + "," + Integer.toString(TestGroupID[row]);
				}
			}
		}
		return DepIds;
	}
	/****************************************************************************
	 * Function Name : FireInsertQuery
	 * Purpose :   Update query[update records in table   [SQL Connection Object]
	 * Created On : 
	 * Parameter:String query
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 
	public static void FireInsertQuery(String Query) throws ClassNotFoundException, IOException, SQLException {
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement();

		System.out.println(Query);

		// execute insert SQL stetement
		stmt.executeUpdate(Query);

	}
	/****************************************************************************
	 * Function Name : getDaataFromDB
	 * Purpose :  SRP E2E getting data in List   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query 
	 *  returns  List of record [single ]
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 

	public static List<String> getDaataFromDB(String Query) throws ClassNotFoundException, IOException, SQLException {
		String name, value, Actualstring;
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);
		ResultSetMetaData rsmd = resultset.getMetaData();
		resultset.beforeFirst();
		List<String> list = new ArrayList<>();
		while (resultset.next()) {
			name = null;
			value = null;
			Actualstring = null;
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {

				if (i == 1) {
					name = rsmd.getColumnName(i);
					value = resultset.getString(i);
					Actualstring = name + "~" + value;
				} else {
					name = rsmd.getColumnName(i);
					value = resultset.getString(i);
					Actualstring = Actualstring + ";" + name + "~" + value;
				}

			}
			list.add(Actualstring);
		}
		return list;

	}
	/****************************************************************************
	 * Function Name : getDaataFromDB
	 * Purpose : Generic single record in HashMap   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query 
	 *  returns  List of record [single ]
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 

	public static HashMap<String, String> getDataFromdb(String Query)
			throws ClassNotFoundException, IOException, SQLException {

		HashMap<String, String> dataMap = new HashMap<String, String>();
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);
		resultset.beforeFirst();
		List<String> list = new ArrayList<>();
		while (resultset.next()) {
			list.add(resultset.getString(1));
		}
		System.out.println(list.size());
		System.out.println(list);
		resultset.beforeFirst();

		List<String> list1 = new ArrayList<>();
		while (resultset.next()) {
			list1.add(resultset.getString(2));
		}
		System.out.println(list1.size());
		System.out.println(list1);

		for (int i = 0; i < list.size(); i++) {
			if (dataMap.containsKey(list.get(i))) {
				if (dataMap.get(list.get(i)).equals("")) {
					dataMap.remove(list.get(i));
				}
				System.out.println("Key allready exsitst");
			} else {
				dataMap.put(list.get(i), list1.get(i));
			}
		}

		return dataMap;
	}
	/****************************************************************************
	 * Function Name : getDaataFromDB
	 * Purpose : Generic function to get single record in HashMap   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query 
	 *  returns  List of record [single ]
	 * Comments :
	 * Updates : Optimized function above function 
	 * @throws Exception 
	 ******************************************************************************/ 
	public static HashMap<String, String> getDataFromdbSingleRow(String Query)
			throws ClassNotFoundException, IOException, SQLException {

		HashMap<String, String> dataMap = new HashMap<String, String>();
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);

		ResultSetMetaData rsmd = resultset.getMetaData();
		resultset.beforeFirst();
		while (resultset.next())
		{
			for (int i = 1; i <= rsmd.getColumnCount(); i++) 
			{
				
				dataMap.put(rsmd.getColumnName(i), resultset.getString(rsmd.getColumnName(i)));
			}
		}
		
		return dataMap;
	}
	/****************************************************************************
	 * Function Name : getDaataFromDB
	 * Purpose : Generic function get multiple rows data in hash table   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query 
	 *  returns  HashTable of records [Multiple rows ]
	 * Comments :
	 * Updates : 
	 * @throws Exception 
	 ******************************************************************************/ 
	public static Object[][] getDataFromdbMultipleRowS(String Query)
			throws ClassNotFoundException, IOException, SQLException {

		HashMap<String, String> dataMap = new HashMap<String, String>();
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);

		ResultSetMetaData rsmd = resultset.getMetaData();
		resultset.beforeFirst();
		int rowCount =0;
		while (resultset.next())
		{
			rowCount++;
		}
		Object[][] data = new Object[rowCount][1];
		resultset.beforeFirst();
		int row=0;
		while (resultset.next())
		{
		
			for (int i = 1; i <= rsmd.getColumnCount(); i++) 
			{
			
				dataMap.put(rsmd.getColumnName(i), resultset.getString(rsmd.getColumnName(i)));
			}
			data[row][0] = dataMap.clone();
			row++;
		}
		
		return data;
	}
	
	/****************************************************************************
	 * Function Name : getDaataFromDB
	 * Purpose : Generic function get multiple rows data in hash table   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query  and splitting  columns values 
	 *  returns  HashTable of records [Multiple rows ]
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/ 
	
	public static Object[][] getDBValues(String Query, String SpiltVaiabel)
			throws ClassNotFoundException, IOException, SQLException {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);
		resultset.beforeFirst();
		List<String> list = new ArrayList<>();
		while (resultset.next()) {
			// System.out.println(resultset.getString(0));
			list.add(resultset.getString(1));
		}
		System.out.println(list.size());
		System.out.println(list);
		resultset.beforeFirst();
		List<String> list1 = new ArrayList<>();

		while (resultset.next()) {
			list1.add(resultset.getString(2));
		}
		System.out.println(list1.size());
		System.out.println(list1);
		HashMap<String, String> dataMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			dataMap.put(list.get(i), list1.get(i));
		}
		String[] SplitRows = dataMap.get(SpiltVaiabel).split("\\|");
		String[][] multi = new String[SplitRows.length + 1][list.size()];

		for (int i = 0; i < list.size(); i++) {
			multi[0][i] = list.get(i);

		}
		for (int i = 0; i < list1.size(); i++) // 10
		{
			System.out.println(SplitRows.length);
			for (int j = 1; j <= SplitRows.length; j++) {

				System.out.println(list1.get(i));
				if (list1.get(i).contains("|")) // 4
				{

					String[] arrayData = list1.get(i).split("\\|");

					multi[j][i] = arrayData[j - 1]; // 1,0= dd

				} else {

					multi[j][i] = list1.get(i);

				}

			}
		}

		Object[][] data = new Object[SplitRows.length][1];

		Hashtable<String, String> table = new Hashtable<String, String>();

		for (int row = 0; row < SplitRows.length; row++) {

			for (int col = 0; col < list.size(); col++) {

				data[row][0] = list.get(col);
				table.put(list.get(col), multi[row + 1][col]);
				System.out.println(list.get(col) + " " + multi[row + 1][col]);

			}
			data[row][0] = table.clone();

			System.out.println("Before ******" + data[row][0]);
		}
		System.out.println("Afte completion row#############");
		System.out.println("Before ******" + data);
		return data;

	}

	/****************************************************************************
	 * Function Name : GetDbValuesOfStringType
	 * Purpose : Generic function get multiple rows data  in for Single Column   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query  
	 *  returns  String array of records [Multiple rows ]
	 * Comments :
	 * Updates : 
	 * @throws Exception 
	 ******************************************************************************/ 
	public static String[] GetDbValuesOfStringType(String Query)
			throws ClassNotFoundException, IOException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		ResultSet resultset;
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultset = stmt.executeQuery(Query);
		int RowCount = 0;
		while (resultset.next()) {
			RowCount++;
		}
		resultset.beforeFirst();

		resultset.beforeFirst();
		String data[] = new String[RowCount];
		int i = 0;
		while (resultset.next()) {

			data[i] = (resultset.getString(1));
			i++;
		}
		return data;
	}

	/****************************************************************************
	 * Function Name : GetDbValuesAsString[converts number to String
	 * Purpose : Generic function get multiple rows data  in for Single Column   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query  
	 *  returns  String array of records [Multiple rows ]
	 * Comments :
	 * Updates : 
	 * @throws Exception 
	 ******************************************************************************/

	public static String[] GetDbValuesAsString(String Query) throws ClassNotFoundException, IOException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		ResultSet resultset;
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultset = stmt.executeQuery(Query);
		int RowCount = 0;
		while (resultset.next()) {
			RowCount++;
		}
		resultset.beforeFirst();

		resultset.beforeFirst();
		String data[] = new String[RowCount];
		int i = 0;
		while (resultset.next()) {

			data[i] = String.valueOf(resultset.getInt(1));
			i++;
		}
		return data;
	}
	/****************************************************************************
	 * Function Name : getIDs
	 * Purpose : Generic function get multiple rows data  in for Single Column   [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query  
	 *  returns  : int array of records [Multiple rows ]
	 * Comments :
	 * Updates :
	 * @throws Exception 
	 ******************************************************************************/

	public static int[] getIDs(String Query) throws ClassNotFoundException, IOException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		ResultSet resultset;
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		resultset = stmt.executeQuery(Query);
		int RowCount = 0;
		while (resultset.next()) {
			RowCount++;
		}
		resultset.beforeFirst();

		resultset.beforeFirst();
		int data[] = new int[RowCount];
		int i = 0;
		while (resultset.next()) {

			data[i] = resultset.getInt(1);
			i++;
		}
		return data;
	}

	
	/****************************************************************************
	 * Function Name : deleteTable
	 * Purpose :drop Table  [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :TableName  
	 *  returns  : 
	 * Comments :
	 * Updates :  
	 * @throws Exception 
	 ******************************************************************************/

	public static void deleteTable(String tableName) throws ClassNotFoundException, IOException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DBUtilities.SQLConnectDB();
		StringBuilder droptbl = new StringBuilder("drop table ").append(tableName);
		try {
			preparedStatement = con.prepareStatement(droptbl.toString());
			preparedStatement.executeUpdate();

		} catch (SQLServerException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/****************************************************************************
	 * Function Name : createTable
	 * Purpose :drop Table  [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :TableName ,Excel file name and sheet name  
	 *  returns  : 
	 * Comments :
	 * Updates :  
	 * @throws Exception 
	 ******************************************************************************/
	public static void createTable(String tableName, String excelFileName, String SheetName) throws Exception {
		Object[][] data = ExcelDataProvider.dataProvider(
				System.getProperty("user.dir") + "\\src\\test\\resources\\vss\\" + excelFileName + ".xlsm", SheetName);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DBUtilities.SQLConnectDB();
		StringBuilder createTbl = new StringBuilder("Create  table ").append(tableName).append(" (");
		Hashtable<String,String> tblCol  = (Hashtable<String, String>) data[0][0];
		for (Iterator<String> iter = tblCol.keySet().iterator(); iter.hasNext();)
			 
		{	
			createTbl.append(iter.next()).append(" varchar(255)");
			if (iter.hasNext()) 
			{
				createTbl.append(",");
			}
		}
		createTbl.append(")");
		try {
			preparedStatement = con.prepareStatement(createTbl.toString());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/****************************************************************************
	 * Function Name : insertTable
	 * Purpose :Create Table  [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :TableName ,Excel file name and sheet name  
	 *  returns  : 
	 * Comments :
	 * Updates :  
	 * @throws Exception 
	 ******************************************************************************/
	public static void insertTable(String tableName, String excelFileName, String SheetName) throws Exception {
		Object[][] data = ExcelDataProvider.dataProvider(System.getProperty("user.dir") + "\\src\\test\\resources\\vss\\" + excelFileName + ".xlsm", SheetName);
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DBUtilities.SQLConnectDB();
		for (int i = 0; i < data.length; i++) {
			StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
			StringBuilder placeholders = new StringBuilder();
			Hashtable<String, String> table = (Hashtable<String, String>) data[i][0];
			System.out.println(data[i][0]);
			for (Iterator<String> iter = table.keySet().iterator(); iter.hasNext();) {
				sql.append("[").append(iter.next()).append("]");
				placeholders.append("?");
				if (iter.hasNext()) {
					sql.append(",");
					placeholders.append(",");
				}
			}
			for (String value : table.values()) {
				System.out.println(value);
			}
			sql.append(") VALUES (").append(placeholders).append(")");
			preparedStatement = con.prepareStatement(sql.toString());
			int y = 1;

			for (String value : table.values()) {
				preparedStatement.setObject(y++, value);
			}
			try {
				preparedStatement.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}
	/****************************************************************************
	 * Function Name : getDataInHashTable
	 * Purpose :Get the table data in Hash table  [MSSQL Connection Object]
	 * Created By :
	 * Created On : 
	 * Parameter :Query 
	 *  returns  : 
	 * Comments :
	 * Updates :  
	 * @throws Exception 
	 ******************************************************************************/

	public static Object[][] getDataInHashTable(String Query) throws ClassNotFoundException, IOException, SQLException {
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = SQLConnectDB();
		Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = stmt.executeQuery(Query);

		ResultSetMetaData rsmd = resultset.getMetaData();
		int columnCount = rsmd.getColumnCount();

		List<String> list = new ArrayList<>();

		for (int i = 1; i <= columnCount; i++) {
			list.add(rsmd.getColumnName(i));

		}

		resultset.beforeFirst();
		int rowCount = 0;
		while (resultset.next()) {
			rowCount++;
		}
		System.out.println(list.size());
		System.out.println(list);
		resultset.beforeFirst();

		String[][] multi = new String[rowCount][list.size()];

		for (int i = 0; i < list.size(); i++) {
			multi[0][i] = list.get(i);

		}

		resultset.beforeFirst();

		int i = 0;

		while (resultset.next()) {
			for (int j = 0; j < list.size(); j++) {

				System.out.println(list.get(j));

				multi[i][j] = resultset.getString(list.get(j));

			}
			i++;
		}

		Object[][] data = new Object[rowCount][1];

		Hashtable<String, String> table = new Hashtable<String, String>();

		for (int row = 0; row < rowCount; row++) {

			for (int col = 0; col < list.size(); col++) {

				data[row][0] = list.get(col);
				table.put(list.get(col), multi[row][col]);
				System.out.println(list.get(col) + " " + multi[row][col]);

			}
			data[row][0] = table.clone();

			System.out.println("Before ******" + data[row][0]);
		}
		System.out.println("Afte completion row#############");
		System.out.println("Before ******" + data);
		return data;

	}

}
