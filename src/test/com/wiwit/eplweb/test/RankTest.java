package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Rank;
import com.wiwit.eplweb.service.RankService;

public class RankTest {
	
	public static void showHighestFive(RankService rs) throws Exception{
		for (Rank r : rs.getFiveHighestLastRank()) {
			System.out.println("Rank : " + r.getTeam().getName() + ", points="+ r.getPoints());
		}
	}
	
	public static void showJson(RankService rs) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(rs.getLatestRank());
		System.out.println(json);
	}

	public static void main(String[] args) throws Exception{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		RankService rs = context.getBean(RankService.class);
		showJson(rs);
		context.close();
	}
}
