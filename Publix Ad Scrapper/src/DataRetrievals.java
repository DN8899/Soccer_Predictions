import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataRetrievals extends ConnectDatabase {
	PrivateVariables myVariables = new PrivateVariables();
	ConnectDatabase connection = new ConnectDatabase();
	
	public DataRetrievals() {
	}
	
	
	/* Get XAG for each premier league team 
	 * put it into the database
     */
	public void retrieveXAG() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		try {
			Document statDoc = Jsoup.connect(statsUrl).get();
			String[] xAFinal = new String[20];
			for (Element row : statDoc.select("#stats_squads_standard_for"))			
			{
				String xGName = row.select("[data-stat='xg_assist_per90']").text();	
				xAFinal = xGName.split(" ");
				System.out.println(xGName);
			}
			for (int i = 1; i < 21; i++) {
				System.out.println(xAFinal[i]);
				String sql = "UPDATE englandteams" + 
						" SET xAG = " + xAFinal[i] +
						" WHERE teamID = " + i;
				databaseConnection.returnStmt(sql);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			databaseConnection.closeStatement();
			databaseConnection.closeConnection();
		}
	}
	
	/* Get XG for each premier league team 
	 * put it into the database
     */
	public void retrieveXG() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		try {
			Document statDoc = Jsoup.connect(statsUrl).get();
			String[] xGFinal = new String[20];
			for (Element row : statDoc.select("#stats_squads_standard_for"))			
			{
				String xGName = row.select("[data-stat='xg_per90']").text();	
				xGFinal = xGName.split(" ");
			}
			
			for (int i = 1; i < 21; i++) {
				System.out.println(xGFinal[i]);
				String sql = "UPDATE englandteams" + 
						" SET xG = " + xGFinal[i] +
						" WHERE teamID = " + i;
				databaseConnection.returnStmt(sql);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			databaseConnection.closeStatement();
			databaseConnection.closeConnection();
		}
	}
	
	public LocalDate getDate() {
		LocalDate date = LocalDate.now();
		return date;
	}
	
	public LocalDate getSevenDaysDate() {
		LocalDate date = LocalDate.now();
		LocalDate addedDate = date.plusDays(7);
		return addedDate;
	}
	
	public String getGameName() throws ClassNotFoundException, SQLException {
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		
		String sql = "select teamHome, teamAway from englandmatchmain \r\n"
				+ "		where gametime between '2023-02-26' and '2023-03-05'\r\n"
				+ "		AND teamHome = 'Arsenal' ";
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		databaseConnection.returnStmt(sql);
		
		//temp
		return null;
	}
	

}
