package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Squad;
import com.wiwit.eplweb.service.SquadService;

public class SquadTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		SquadService sd = context.getBean(SquadService.class);
		for(Squad s : sd.getSquadsByTeamId(20)){
			System.out.println(s.getName());
		}
		context.close();
	}
}
