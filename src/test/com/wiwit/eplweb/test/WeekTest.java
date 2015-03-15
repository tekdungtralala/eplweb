package com.wiwit.eplweb.test;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Week;
import com.wiwit.eplweb.service.WeekService;

public class WeekTest {
	public static void showLastFiveWeek(WeekService wd, ObjectWriter ow) throws Exception {
		for (Week w : wd.findLastFiveWeek()) {
			System.out.println("week : " + w.getWeekNumber());
			System.out.println(w.getId());
			System.out.println(w.getStartDay());
			System.out.println(w.getSeason().getYears());

			String json = ow.writeValueAsString(w);
			System.out.println(json);
		}		
	}
	
	public static void showAllPassedWeek(WeekService wd, ObjectWriter ow) throws Exception {
		String json = ow.writeValueAsString(wd.findAllPassedWeek());
		System.out.println(json);
	}

	public static void main(String[] asdf) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("servlet-context.xml");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		WeekService wd = context.getBean(WeekService.class);
		
		showAllPassedWeek(wd, ow);

		context.close();
	}
}
