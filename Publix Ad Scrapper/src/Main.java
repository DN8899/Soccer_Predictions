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
		
		retrieve.retrieveXG();
		retrieve.retrieveXAG();
		System.out.println(retrieve.getDate());
		System.out.println(retrieve.getSevenDaysDate());
		
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
			
					
		
	}
}
