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
	
	public static void main(String[] args) throws Exception {
		DataRetrievals retrieve = new DataRetrievals();
		
		//retrieve.retrieveXG();
		//retrieve.retrieveXAG();
		//System.out.println(retrieve.getDate());
		//System.out.println(retrieve.getSevenDaysDate());
		
		/*
		String[] team = retrieve.getGameName();
		for (int i = 0; i < team.length; i++) {
			System.out.println(team[i]);
		}
		*/
	//	retrieve.getGameName();
	//	retrieve.getGoalsAndMatches();
	//	retrieve.getGoalsConcededP90();
		retrieve.getGameName();
		retrieve.getGoalsAndMatches();
		retrieve.pullXGAndXAG();
		retrieve.calGlsConP90();
		retrieve.calScores();
		retrieve.displayScores();
		
		/*
		 * NEXT need to fetch the XG and XAG for both teams
		 * Create a formula to calculate the goals they will score
		 * Display the score accordingly for each team
		 * 
		 * MAYBE: Fetch XG and XAG for first team, calculate that team
		 * Then move onto second team
		 */
			
	}
}
