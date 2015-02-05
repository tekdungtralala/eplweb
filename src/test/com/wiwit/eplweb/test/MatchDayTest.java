package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.MatchdayDAO;
import com.wiwit.eplweb.model.Matchday;

public class MatchDayTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		MatchdayDAO md = context.getBean(MatchdayDAO.class);
		for (Matchday m : md.getMatchDayOnLastWeek()) {
			System.out.println(m.getId() + " - " + m.getDate() + m.getTime());
			System.out.println(m.getHomeTeam().getName() + " vs " +m.getAwayTeam().getName());
			System.out.println("");
		}
	}
}
