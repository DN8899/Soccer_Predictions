import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataRetrievals extends ConnectDatabase {
	PrivateVariables myVariables = new PrivateVariables();
	ConnectDatabase connection = new ConnectDatabase();
	
	public DataRetrievals() {
		
	}
	
	
	public void retrieveXAG() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		int count = 1;
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		try {
			Document statDoc = Jsoup.connect(statsUrl).get();
			String[] xAFinal = new String[20];
			for (Element row : statDoc.select("#stats_squads_standard_for"))			
			{
				String xGName = row.select("[data-stat='xg_assist_per90']").text();	
				xAFinal = xGName.split(" ");
				System.out.println(xGName + "HI");
			}
			for (int i = 1; i < 21; i++) {
				System.out.println(xAFinal[i]);
				String sql = "UPDATE englandteams" + 
						" SET xAG = " + xAFinal[i] +
						" WHERE teamID = " + i;
				databaseConnection.returnStmt(sql);
				count++;
			
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			databaseConnection.closeStatement();
			databaseConnection.closeConnection();
		}
	}
	
	
	public void retrieveXG() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		int count = 1;
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
				count++;
			
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			databaseConnection.closeStatement();
			databaseConnection.closeConnection();
		}
	}
	
}
