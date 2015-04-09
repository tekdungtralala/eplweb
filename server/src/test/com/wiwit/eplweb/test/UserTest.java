package com.wiwit.eplweb.test;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.UserNetwork;
import com.wiwit.eplweb.model.UserSession;
import com.wiwit.eplweb.model.input.UserNetworkModelInput;
import com.wiwit.eplweb.service.UserNetworkService;
import com.wiwit.eplweb.service.UserSessionService;
import com.wiwit.eplweb.util.UserNetworkType;

public class UserTest {
	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		// testLogin(context);
//		userSessionTest(context);
		createUsrNetwork(context);

		context.close();
	}
	
	public static void createUsrNetwork(ClassPathXmlApplicationContext context) {
		UserNetworkService userService = context.getBean(UserNetworkService.class);
		UserSessionService sessionService = context.getBean(UserSessionService.class);
		
		UserNetworkModelInput model = new UserNetworkModelInput();
		model.setEmail("email");
//		model.setName("name");
		model.setUserNetworkID("3234234234");
		
		UserNetworkType type = UserNetworkType.FACEBOOK; //
		
		model.setType(type.getValue());
		
		UserNetwork user = userService.findByEmailAndType(model.getEmail(), type);
		if (user == null) {
			user = new UserNetwork(model);
//			userService.create(user);
		}
		UserSession session = sessionService.doLogin(user);
		
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		try {
			String value = ow.writeValueAsString(model);
			System.out.println(value);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void userSessionTest(ClassPathXmlApplicationContext context) {
		// TeamService ts = context.getBean(TeamService.class);

		UserSessionService uss = context.getBean(UserSessionService.class);
		for (UserSession us : uss.findAll()) {
			System.out.println(us.getUser().getId());
//			System.out.println(us.getUserNetwork().getName());
		}
	}

	public static void testLogin(ClassPathXmlApplicationContext context) {
		// UserSessionService userSessionService =
		// context.getBean(UserSessionService.class);
		// System.out.println(userSessionService.findBySession("6a7b0cf80adf4eafb174cb75aa1e4c43").getLoginTime());

		// PersonService ps = context.getBean(PersonService.class);
		// System.out.println(ps.listPersons().size());

	}
}
