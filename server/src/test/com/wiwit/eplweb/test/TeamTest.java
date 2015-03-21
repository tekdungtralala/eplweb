package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Team;
import com.wiwit.eplweb.service.TeamService;

public class TeamTest {

	public static void main(String []asfd){
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		TeamService ts = context.getBean(TeamService.class);
		
		for(Team t : ts.findAll()){
			System.out.println(" - " + t.getName());
		}
		context.close();
	}
}
