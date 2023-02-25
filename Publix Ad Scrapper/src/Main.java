import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
	private static String dataUrl = "jdbc:mysql://localhost:3306/soccer_leagues";
	private static String userName = "root";
	private static String password = "DandB#8899";
	
	
	
	public static void main(String[] args) throws Exception {
		DataRetrievals retrieve = new DataRetrievals();
		/*
		//SQL Variables
		String dataUrl = "jdbc:mysql://localhost:3306/soccer_leagues";
		String userName = "root";
		String password = "DandB#8899";
		
		//Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(dataUrl, userName, password);
		Statement stmt = con.createStatement();

		*/
		
		
		
		
		//-------------------------------------FIRST TEST AREA-------------------------------------
		/*
		try 
		{
			final Document doc = Jsoup.connect(url).get();
			
			
			for (Element row : doc.select("table.shsTable.shsBorderTable tr"))
			{
				if (row.select("td.shsNamD:nth-of-type(1)").text().equals("")) 
				{
					continue;
				}
				else 
				{
					String result = row.select("td.shsNamD:nth-of-type(1)").text();
					System.out.print("\n" + result);
					//String times = row.select(".shsETZone.shsTimezone").text();
					//System.out.println(times);
					String nameHome = row.select("td.shsNamD:nth-of-type(2)").text();
					System.out.print(" " + nameHome);
					String nameAway = row.select("td.shsNamD:nth-of-type(3)").text();
					System.out.print("-" + nameAway);
				}
			}
			*/
			//-------------------------------------FIRST TEST AREA ABOVE-------------------------------------
			
		
		//final String bet = "https://fbref.com/en/comps/9/schedule/Premier-League-Scores-and-Fixtures";
		String statsUrl = "https://fbref.com/en/comps/9/Premier-League-Stats";
		
		//Create database connection 
		ConnectDatabase databaseConnection = new ConnectDatabase(dataUrl, userName, password);
		
		try {
				
			final Document statDoc = Jsoup.connect(statsUrl).get();
			
			//---------------------------------------Premier League Group-------------------------------------
			
			int count = 1; 	
			
			
			//---------------------------------------XG AGAINST RETRIVIAL---------------------------------------
		
			/*
			for (int i = 1; i < 21; i++) {
				System.out.println(xGAgainstFinal[i]);
				String sql = "UPDATE englandteams" + 
						" SET xGAgainst = " + xGAgainstFinal[i] +
						" WHERE teamID = " + i;
				//stmt.executeUpdate(sql);
				count++;
			
			}
			*/
			
			
			
			//--------------------------------------XA RETRIVIAL----------------------------------
			
			
			retrieve.retrieveXAG();
			
			
			//--------------------------------------------XG RETRIVIAL-------------------------------------------------------------
			
			
			String[] xGFinal = new String[20];
			for (Element row : statDoc.select("#stats_squads_standard_for"))			
			{
				String xGName = row.select("[data-stat='xg_per90']").text();	
				xGFinal = xGName.split(" ");
			}
			/*
			for (int i = 1; i < 21; i++) {
				System.out.println(xGFinal[i]);
				String sql = "UPDATE englandteams" + 
						" SET xG = " + xGFinal[i] +
						" WHERE teamID = " + i;
				stmt.executeUpdate(sql);
				count++;
			
			}
			*/		
			
					
					//----------------------HOME CODE-----------------------------
					/*
					StringBuilder home = new StringBuilder();
					
					String homeTemp = row.select("td.right:nth-of-type(4)").text();
					String homeReplace = homeTemp.replaceAll("'", "");
					home.append('\'');
					home.append(homeReplace);
					home.append('\'');
					System.out.println(homeReplace);
					
					
					
					String sql = "INSERT INTO englandGames" + 
								"(gameID ,teamHome) " +
								"VALUES (" + count + ","+ home +");";
					
					System.out.println(sql);
					stmt.executeUpdate(sql);
					count++;
					*/
					
					
					
					
					//-----------------------------AWAY CODE-------------------------------------
					/*StringBuilder away = new StringBuilder();
					
					String awayTemp = row.select("td.right:nth-of-type(8)").text();
					String awayReplace = awayTemp.replaceAll("'", "");
					away.append('\'');
					away.append(awayReplace);
					away.append('\'');
					System.out.println(awayReplace);*/
					
					
					
					
					
					
					/*------------------------------RESULT CODE---------------------------------------
					String result = row.select("td.left:nth-of-type(2)").text();
					System.out.println(result);
					StringBuilder resultReplace = new StringBuilder();
					resultReplace.append('\'');
					resultReplace.append(result);
					resultReplace.append('\'');
					//Use update to input away and results
					String sql = "UPDATE englandgames" + 
								" SET gameTime = str_to_date("  + resultReplace + ", '%Y-%m-%d')" +
								" WHERE gameID = " + count;
					
					System.out.println(sql);
					stmt.executeUpdate(sql);
					count++;
					
					//-------------------------------RESULT CODE------------------------------------------
				}*/
			
		}
		
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			databaseConnection.closeStatement();
			databaseConnection.closeConnection();
		} 
		
	}

}
