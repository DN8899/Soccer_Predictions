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
	
	private String teamOneGoalsAgainst = "";
	private String teamOneGamesPlayed = "";
	private String teamTwoGoalsAgainst = "";
	private String teamTwoGamesPlayed = "";
	private String teamOneName = "";
	private String teamTwoName = "";
	private double teamOneGlsConP90 = 0.0;
	private double teamTwoGlsConP90 = 0.0;
	private String teamOnexG = "";
	private String teamTwoxG = "";
	private String teamOnexAG = "";
	private String teamTwoxAG = "";
	private long finishedTeamOneScore;
	private long finishedTeamTwoScore;
	
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
			}
			for (int i = 1; i < 21; i++) {
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
	
	/*
	 * Gets the name of the match day game
	 * Uses user's team name
	 */
	public void getGameName() throws ClassNotFoundException, SQLException {
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
				outTeams[count] = theTeam;
				count++;
				outTeams[count] = rs.getString("teamAway");
				break;
			}
			else if  (rs.getString("teamAway").equals(theTeam)) {
				outTeams[count] = theTeam;
				count++;
				outTeams[count] = rs.getString("teamHome");
				break;
			}
			
			
		}
	
		teamOneName = outTeams[0];
		teamTwoName = outTeams[1];
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
	}
	
	/*
	 * Helper method for getGameName() method 
	 * Gets the users team
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
	
	
	/*
	 * Retrieves the updated goals conceded per game for each team
	 * Used to update what already exists in the database
	 */
	public void retrieveGoalsAgainst() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		String goalAgainstFinal[] = new String[21];
		
		try {
			Document statDoc = Jsoup.connect(statsUrl).get();
			for (Element row : statDoc.select("#results2022-202391_overall")) {
				String goalAgainst = row.select("[data-stat='goals_against']").text();
				goalAgainstFinal = goalAgainst.split(" ");
				
			}
			
		 } catch (Exception e) {
			e.printStackTrace();
		}
		
		int count = 1;
		for (int i = 1; i < goalAgainstFinal.length; i++) {
			
			
			String sql = "update englandteams \n" + 
						 "set goalsAgainst ='" +goalAgainstFinal[i]+ "' \n" + 
						 "where teamID =" + count;
						 
			databaseConnection.returnUpdateStmt(sql);
			count++;
		}
		
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
	}
	
	/*
	 * Web scrapes the current data from the URL and stores it into the database
	 * Updates what is in the database
	 */
	public void retrieveGamesPlayed() throws ClassNotFoundException, SQLException {
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		String gamesPlayedFinal[] = new String[21];
		
		try {
			Document statDoc = Jsoup.connect(statsUrl).get();
			for (Element row : statDoc.select("#results2022-202391_overall")) {
				String gamesPlayed = row.select("[data-stat='games']").text();
				gamesPlayedFinal = gamesPlayed.split(" ");
				
			}
			
		 } catch (Exception e) {
			e.printStackTrace();
		}
		
		
		int count = 1;
		
		/*
		 * Store matches played into database
		 */
		for (int i = 1; i < gamesPlayedFinal.length; i++) {
			
			
			String sql = "update englandteams \n" + 
						 "set gamesPlayed ='" +gamesPlayedFinal[i]+ "' \n" + 
						 "where teamID =" + count;
						 
			databaseConnection.returnUpdateStmt(sql);
			
			count++;
		}
		
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
	}
	
	/*
	 * Used to create the score line prediction
	 * Gets the goals against and matches played for each team
	 */
	
	public void getGoalsAndMatches() throws ClassNotFoundException, SQLException {
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		

		String sqlOneGoalsAgainst = "select teamName, goalsAgainst from englandteams \n" + 
					  "where teamName = '"+teamOneName+"'";
		
		try {
			
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlOneGoalsAgainst);
			while (rs.next()) {
				if (teamOneName.equals(rs.getString("teamName"))) {
					teamOneGoalsAgainst = rs.getString("goalsAgainst");
					break;
				}
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sqlOneGamesPlayed = "select teamName, gamesPlayed from englandteams \n" + 
				  "where teamName = '"+teamOneName+"'";
		
		try {
			
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlOneGamesPlayed);
			while (rs.next()) {
				if (teamOneName.equals(rs.getString("teamName"))) {
					this.teamOneGamesPlayed = rs.getString("gamesPlayed");
					break;
				}
				rs.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		String sqlTwoGoalsAgainst = "select teamName, goalsAgainst from englandteams \n" +
					  "where teamName = '"+teamTwoName+"'";
		
		try {
			
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTwoGoalsAgainst);
			while (rs.next()) {
				if (teamTwoName.equals(rs.getString("teamName"))) {
					teamTwoGoalsAgainst = rs.getString("goalsAgainst");
					break;
				}
				rs.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		String sqlTwoGamesPlayed = "select teamName, gamesPlayed from englandteams \n" +
				  "where teamName = '"+teamTwoName+"'";
		
		try {
			
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTwoGamesPlayed);
			while (rs.next()) {
				if (teamTwoName.equals(rs.getString("teamName"))) {
					teamTwoGamesPlayed = rs.getString("gamesPlayed");
					break;
				}
				rs.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}					
		
		
		databaseConnection.closeStatement();
		databaseConnection.closeConnection();
	}
	
	
	/*
	 * Gathers the xG and xAG that is in the database
	 */
	public void pullXGAndXAG() throws ClassNotFoundException, SQLException {
		String dataUrl = myVariables.getUrl();
		String userName = myVariables.getUserName();
		String password = myVariables.getPassword();
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		/*
		 * Updates the values in the database with the ones from the website
		 */
		retrieveXG();
		retrieveXAG();
		
		String sqlTeamOneXG = "select teamName, xG from englandteams " +
					"where teamName ='" +teamOneName+"'"; 
		
		try {
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTeamOneXG);
			while (rs.next()) {
				if (teamOneName.equals(rs.getString("teamName"))) {
					teamOnexG = rs.getString("xG");
				}
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String sqlTeamTwoXG = "select teamName, xG from englandteams " +
				"where teamName ='" +teamTwoName+"'"; 
		
		try {
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTeamTwoXG);
			while (rs.next()) {
				if (teamTwoName.equals(rs.getString("teamName"))) {
					teamTwoxG = rs.getString("xG");
					break;
				}
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		String sqlTeamTwoXAG = "select teamName, xAG from englandteams " +
				"where teamName ='" +teamTwoName+"'"; 
		
		try {
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTeamTwoXAG);
			while (rs.next()) {
				if (teamTwoName.equals(rs.getString("teamName"))) {
					teamTwoxAG = rs.getString("xAG");
					break;
				}
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		String sqlTeamOneXAG = "select teamName, xAG from englandteams " +
				"where teamName ='" +teamOneName+"'"; 
		
		try {
			ResultSet rs = databaseConnection.returnStmt().executeQuery(sqlTeamOneXAG);
			while (rs.next()) {
				if (teamOneName.equals(rs.getString("teamName"))) {
					teamOnexAG = rs.getString("xAG");
					break;
				}
				
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/*
	 * Gets the amount of goals conceded per match
	 */
	public void calGlsConP90() {
		
		int tempOneGlsAgainst = Integer.parseInt(teamOneGoalsAgainst);
		int tempOneGamesPlayed = Integer.parseInt(teamOneGamesPlayed);
		
		teamOneGlsConP90 = tempOneGlsAgainst / tempOneGamesPlayed;
		
		int tempTwoGlsAgainst = Integer.parseInt(teamTwoGoalsAgainst);
		int tempTwoGamesPlayed = Integer.parseInt(teamTwoGamesPlayed);
		
		teamTwoGlsConP90 = tempTwoGlsAgainst / tempTwoGamesPlayed;
		
	}
	
	public void calScores() {
		double teamOneScore = 0;
		double teamTwoScore = 0;
		
		double tempTeamOnexG = Double.parseDouble(teamOnexG);
		double tempTeamTwoxG = Double.parseDouble(teamTwoxG);
		
		double tempTeamOnexAG = Double.parseDouble(teamOnexAG);
		double tempTeamTwoxAG = Double.parseDouble(teamTwoxAG);
		
		teamOneScore = teamTwoGlsConP90 * tempTeamOnexG * tempTeamOnexAG;
		teamTwoScore = teamOneGlsConP90 * tempTeamTwoxG * tempTeamTwoxAG;
		
		finishedTeamOneScore = Math.round(teamOneScore);
		finishedTeamTwoScore = Math.round(teamTwoScore);
		
	}
	
	public void displayScores() throws ClassNotFoundException, SQLException {
		/*
		getGoalsAndMatches();
		pullXGAndXAG();
		calGlsConP90();
		calScores();
		*/
		
		System.out.println(teamOneName + " " + finishedTeamOneScore + "-" + finishedTeamTwoScore + " " + teamTwoName);
	}
	
}
