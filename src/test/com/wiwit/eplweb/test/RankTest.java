package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.model.Rank;

public class RankTest {
	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		RankDAO rd = context.getBean(RankDAO.class);
		for (Rank r : rd.getFiveHighestRank()) {
			System.out.println("Rank : " + r.getTeam().getName() + ", points="
					+ r.getPoints());
		}
		context.close();
	}
}
