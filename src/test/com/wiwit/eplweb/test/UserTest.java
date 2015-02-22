package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.service.UserService;
import com.wiwit.eplweb.service.UserSessionService;

public class UserTest {
	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		UserService us = context.getBean(UserService.class);
		System.out.println(us.findUserByEmail("admin@eplweb.com").getUserSessions().size());
		
		UserSessionService uss = context.getBean(UserSessionService.class);
		for(UserSession ses: uss.findAll()){
			System.out.println(ses.getUser().getEmail());
		}
		
		
		context.close();
	}
}
