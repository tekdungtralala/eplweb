package com.wiwit.eplweb.test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class RankQueryGenerator {

	public static void sout(Object s) {
		System.out.println(s);
	}

	public static void main(String[] asfd) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("database/rank.sql", "UTF-8");
		for (int weekId =1; weekId <=23; weekId++){
			for (int teamId = 1; teamId <= 20; teamId++) {
				StringBuilder sb = new StringBuilder();
				sb.append("insert into rank values (null, ");
				sb.append(weekId);
				sb.append(", ");
				sb.append(teamId);
				
				// game_won start
				sb.append(", (");
				sb.append(" select count(*) from matchday where ");
				sb.append(" week_id <=" + weekId + " and ");
				sb.append(" ( (home_team_id = " + teamId + " and home_goal > away_goal) or ");
				sb.append("   (away_team_id = " + teamId + " and away_goal > home_goal))");
				sb.append("), ");
				// game_won end
	
				// games_drawn start
				sb.append("(");
				sb.append(" select count(*) from matchday where ");
				sb.append(" week_id <=" + weekId + " and ");
				sb.append(" ( ( home_team_id = " + teamId + " and home_goal = away_goal) or ");
				sb.append(" (away_team_id = " + teamId + " and away_goal = home_goal))");
				sb.append("), ");
				// games_drawn start
	
				// games_lost start
				sb.append("(");
				sb.append(" select count(*) from matchday where ");
				sb.append(" week_id <=" + weekId + " and ");
				sb.append(" ( ( home_team_id = " + teamId + " and home_goal < away_goal) or ");
				sb.append(" (away_team_id = " + teamId + " and away_goal < home_goal))");
				sb.append("), ");
				// games_lost end
	
				// goals_scored
				sb.append(" /*goals_scored*/( ");
				sb.append("   (");
				sb.append("     select ifnull(sum(away_goal),0) from matchday where ");
				sb.append("     week_id <=" + weekId + " and away_team_id = " + teamId);
				sb.append("   )");
				sb.append(" + ");
				sb.append("   (");
				sb.append("     select ifnull(sum(home_goal),0) from matchday where ");
				sb.append("     week_id <=" + weekId + " and home_team_id = " + teamId);
				sb.append("   )");
				sb.append(" ) ");
				
				sb.append(" , ");
	
				// goals_against
				sb.append(" /*goals_against*/( ");
				sb.append("   (");
				sb.append("     select ifnull(sum(home_goal),0) from matchday where ");
				sb.append("     week_id <=" + weekId + " and away_team_id = " + teamId);
				sb.append("   )");
				sb.append(" + ");
				sb.append("   (");
				sb.append("     select ifnull(sum(away_goal),0) from matchday where ");
				sb.append("     week_id <=" + weekId + " and home_team_id = " + teamId);
				sb.append("   )");
				sb.append(" ) ");
				
				sb.append(" , ");
	
				// points
				sb.append("/*points*/((");
				sb.append(" select count(*) from matchday where ");
				sb.append(" week_id <=" + weekId + " and ");
				sb.append(" ( (home_team_id = " + teamId + " and home_goal > away_goal) or ");
				sb.append("   (away_team_id = " + teamId + " and away_goal > home_goal))");
				sb.append(") * 3)");
				sb.append("+");
				sb.append("(");
				sb.append(" select count(*) from matchday where ");
				sb.append(" week_id <=" + weekId + " and ");
				sb.append(" ( ( home_team_id = " + teamId + " and home_goal = away_goal) or ");
				sb.append(" (away_team_id = " + teamId + " and away_goal = home_goal))");
				sb.append(")");
				sb.append(");");
				sout(sb.toString());
				sout("");
				writer.println(sb.toString());
			}
		}
		writer.close();
	}
}





