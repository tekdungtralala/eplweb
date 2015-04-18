package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.MatchdayComment;
import com.wiwit.eplweb.service.MatchdayCommentService;

public class MatchdayCommentTest {

	public static void showComment(ClassPathXmlApplicationContext context) {
		MatchdayCommentService sr = context.getBean(MatchdayCommentService.class);
		
//		System.out.println(sr.findByMathdayId(246).size());
		for(MatchdayComment mc : sr.findByParentId(9, 2, 100)){
			System.out.println(mc.getId());
		}
	}

	public static void main(String[] asdf) throws JsonGenerationException,
			JsonMappingException, IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");

		showComment(context);

		context.close();
	}
}
