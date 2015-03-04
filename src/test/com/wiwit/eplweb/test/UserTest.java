package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.filter.SessionService;



public class UserTest {
	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"filter-context.xml");
		testLogin(context);
		
		context.close();
	}
	
	public static void testLogin(ClassPathXmlApplicationContext context){
//		UserSessionService userSessionService = context.getBean(UserSessionService.class);
//		System.out.println(userSessionService.findBySession("6a7b0cf80adf4eafb174cb75aa1e4c43").getLoginTime());
		
//		PersonService ps = context.getBean(PersonService.class);
//		System.out.println(ps.listPersons().size());
		
	}
}
