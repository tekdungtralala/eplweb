package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.wiwit.eplweb.service.RankService;

public class RankTest {

	public static void showHighestFive(RankService rs, ObjectWriter ow) throws Exception {
		String json = ow.writeValueAsString(rs.getFiveHighestLastRank());
		System.out.println(json);
	}

	public static void showLatestRank(RankService rs, ObjectWriter ow)
			throws JsonGenerationException, JsonMappingException, IOException {
		String json = ow.writeValueAsString(rs.findLatestRank());
		System.out.println(json);
	}

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		RankService rs = context.getBean(RankService.class);
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		showHighestFive(rs, ow);
		context.close();
	}
}
