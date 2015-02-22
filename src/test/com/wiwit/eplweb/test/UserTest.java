package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.User;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.service.UserService;
import com.wiwit.eplweb.service.UserSessionService;

public class UserTest {
	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
//		UserService us = context.getBean(UserService.class);
//		System.out.println(us.findUserByEmail("admin@eplweb.com").getUserSessions().size());
//		
//		UserSessionService uss = context.getBean(UserSessionService.class);
//		for(UserSession ses: uss.findAll()){
//			System.out.println(ses.getUser().getEmail());
//		}		
		
		testLogin(context);
		
		context.close();
	}
	
	public static void testLogin(ClassPathXmlApplicationContext context){
		UserService userService = context.getBean(UserService.class);
		UserSessionService sessionService = context.getBean(UserSessionService.class);
		
		User u = userService.findUserByEmail("admin@eplweb.com");
		sessionService.doLogin(u);
		
	}
}
