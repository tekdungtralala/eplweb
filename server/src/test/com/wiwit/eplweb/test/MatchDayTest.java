package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Matchday;
import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.view.MatchdayModelView;
import com.wiwit.eplweb.service.MatchdayRatingService;
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
	
	public static void showMatchdays(ClassPathXmlApplicationContext context) {
		MatchdayService md = context.getBean(MatchdayService.class);
		MatchdayRatingService mds = context.getBean(MatchdayRatingService.class);
		
		for(Matchday m : md.findClosestMatch(19)){
			System.out.println("id : " + m.getId());
			System.out.println(m.getMatchdayRating());
			System.out.println("_");
		}
		
		User u = new User();
		u.setId(3);
		Matchday m = new Matchday();
		m.setId(643);
		
//		System.out.println("isMatchdayExist : " + mds.isMatchdayExist(u, m));
	} 

	public static void main(String[] asdf) throws JsonGenerationException, JsonMappingException, IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		
		showMatchdays(context);

//		MatchdayService md = context.getBean(MatchdayService.class);
//		ObjectWriter ow = new ObjectMapper().writer()
//				.withDefaultPrettyPrinter();
		
//		System.out.println(ow.writeValueAsString(md.findMatchtdayOnCurrWeek()));
//		showMatchdayOnCurrWeek(md.getMatchtdayOnCurrWeek());
		
		context.close();
	}
}
