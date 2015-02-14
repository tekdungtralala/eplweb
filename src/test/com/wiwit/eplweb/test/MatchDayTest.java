package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.service.MatchdayService;

public class MatchDayTest {
	
	public static void showMatchdayOnCurrWeek(MatchdayModelView mv){
		for (String key : mv.getModel().keySet()) {
			System.out.println("key : " + key);
			for (Matchday m : mv.getModel().get(key)) {
				System.out.println(m.getTime() + " : " + m.getHomeTeam().getName() + " vs "
						+ m.getAwayTeam().getName());
			}
		}		
	}

	public static void main(String[] asdf) throws JsonGenerationException, JsonMappingException, IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");

		MatchdayService md = context.getBean(MatchdayService.class);
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		
		System.out.println(ow.writeValueAsString(md.getMatchtdayOnCurrWeek()));
//		showMatchdayOnCurrWeek(md.getMatchtdayOnCurrWeek());
		

		
		context.close();
	}
}
