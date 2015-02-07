package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.RankDAO;
import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.service.RankService;

public class RankTest {
	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		
		RankService rs = context.getBean(RankService.class);
		for (Rank r : rs.getFiveHighestLastRank()) {
			System.out.println("Rank : " + r.getTeam().getName() + ", points="
					+ r.getPoints());
		}
		context.close();
	}
}
