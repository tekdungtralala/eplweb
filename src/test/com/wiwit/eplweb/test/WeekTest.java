package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.WeekDAO;
import com.wiwit.eplweb.model.Week;

public class WeekTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		WeekDAO wd = context.getBean(WeekDAO.class);
		for(Week w:wd.getLastFiveWeek()){
			System.out.println("week : "+w.getWeekNumber());
			System.out.println(w.getId());
			System.out.println(w.getStartDay());
			System.out.println(w.getSeason().getYears());
		}
		context.close();
	}
}
