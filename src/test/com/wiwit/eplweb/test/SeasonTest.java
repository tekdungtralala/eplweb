package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.SeasonDAO;
import com.wiwit.eplweb.model.Season;

public class SeasonTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		SeasonDAO sd = context.getBean(SeasonDAO.class);
		for(Season s: sd.getAllSeason()){
			System.out.println(s.getId());
			System.out.println(s.getYears());
			System.out.println(s.getWeeks().size());
		}
		context.close();
	}
}
