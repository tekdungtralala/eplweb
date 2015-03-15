package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.BestWeekSquad;
import com.wiwit.eplweb.service.BestWeekSquadService;

public class BestWeekSquadTest {

	public static void main(String[] asfd) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		BestWeekSquadService bs = context.getBean(BestWeekSquadService.class);
		for (BestWeekSquad b : bs.findBestSquadByWeekId(23)) {
			System.out.println(b.getNumber() + " - " + b.getPlayer().getName()
					+ " - " + b.getPlayer().getTeam().getName());
		}
		context.close();
	}
}
