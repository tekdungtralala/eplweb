package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.service.MatchdayService;

public class MatchDayTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");

		MatchdayService md = context.getBean(MatchdayService.class);

		MatchdayModelView mv = md.getMatchtdayOnCurrWeek();
		for (String key : mv.getModel().keySet()) {
			System.out.println("key : " + key);
			for (Matchday m : mv.getModel().get(key)) {
				System.out.println(m.getTime() + " : " + m.getHomeTeam().getName() + " vs "
						+ m.getAwayTeam().getName());
			}
		}
	}
}
