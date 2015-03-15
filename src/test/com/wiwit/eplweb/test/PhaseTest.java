package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.PhaseDAO;

public class PhaseTest {

	public static void main(String[] asdf) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");
		PhaseDAO pd = context.getBean(PhaseDAO.class);
		System.out.println("CURRENT MATCHDAY : ");
		System.out.println(pd.findCurrentMatchday().getValue());
		
		System.out.println("CURRENT SEASON : ");
		System.out.println(pd.findCurrentSeason().getValue());
		context.close();
	}
}
