package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.MachtdayDAO;
import com.wiwit.eplweb.model.Machtday;

public class MachtDayTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		MachtdayDAO md = context.getBean(MachtdayDAO.class);
		for (Machtday m : md.getMatchDayOnLastWeek()) {
			System.out.println(m.getId() + " - " + m.getDate() + m.getTime());
			System.out.println(m.getHomeTeam().getName() + " vs " +m.getAwayTeam().getName());
			System.out.println("");
		}
	}
}
