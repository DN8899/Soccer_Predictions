import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

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
				databaseConnection.returnUpdateStmt(sql);
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
				databaseConnection.returnUpdateStmt(sql);
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
	
	
	public String[] getGameName() throws ClassNotFoundException, SQLException {
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		String theTeam = userInput();
		int count = 0;
		
		String[] outTeams = new String[2];
		String sql = "select teamHome, teamAway from englandmatchmain \r\n"
				+ "		where gametime between '"+getDate()+"' and '"+getSevenDaysDate()+"'\r\n"
				+ "		AND (teamHome = '"+theTeam+"' OR teamAway = '"+theTeam+"') ";
		
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		ResultSet rs = databaseConnection.returnStmt().executeQuery(sql);
		
		/*
		 * Search through both home and away columns of game database 
		 * returns the team that was asked for
		 */
		while (rs.next()) {
			if (rs.getString("teamHome").equals(theTeam)) {
				//call on prediction function here
				outTeams[count] = theTeam;
				count++;
				//Need to get against team
				outTeams[count] = rs.getString("teamAway");
				break;
			}
			
			if (rs.getString("teamAway").equals(theTeam)) {
				//TEMP
				//out = "AWAY GOOO";
				break;
			}
		}
		
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
		
		return outTeams;
	}
	
	/*
	 * Helper method for getGameName() method
	 */
	private String userInput() throws ClassNotFoundException, SQLException {
		Scanner scan = new Scanner(System.in);
		System.out.println("This is a list of the current Premier League teams");
		
		String[] teams = teamList();
		for (int i = 0; i < teams.length; i++) {
			System.out.print(teams[i] + " | ");
			if (i == 19) {
				System.out.print("\n");
			}
		}
		
		System.out.println("From these teams, choose one to predict their next match");
		String userTeam = scan.nextLine();
		
		scan.close();
		
		return userTeam;
	}
	
	/*
	 * Gets an array of the teams from the englandteams database
	 */
	private String[] teamList() throws SQLException, ClassNotFoundException {
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		int i = 0;
		
		String[] teams = new String[20];
		
		String sql = "select teamName from englandteams";
			
		ResultSet rs = databaseConnection.returnStmt().executeQuery(sql);
		while (rs.next()) {
			teams[i] = rs.getString("teamName");
			i++;
		}
		
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
		
		return teams;
	}
	
	
}
