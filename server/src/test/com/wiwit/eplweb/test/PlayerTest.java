package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Player;
import com.wiwit.eplweb.service.PlayerService;

public class PlayerTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		PlayerService sd = context.getBean(PlayerService.class);
		for(Player s : sd.getSquadsByTeamId(20)){
			System.out.println(s.getName() + " - " + s.getTeam().getName());
		}
		context.close();
	}
}
