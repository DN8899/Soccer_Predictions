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
		
		retrieve.getGameName();
		retrieve.getGoalsAndMatches();
		retrieve.pullXGAndXAG();
		retrieve.calGlsConP90();
		retrieve.calScores();
		retrieve.displayScores();
		
			
	}
}
