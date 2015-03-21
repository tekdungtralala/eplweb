package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.model.Season;
import com.wiwit.eplweb.service.SeasonService;

public class SeasonTest {
	
	public static ClassPathXmlApplicationContext getContext(){
		return new ClassPathXmlApplicationContext("servlet-context.xml");
	}
	
	public static void getAllSeason(SeasonService sd){
		for(Season s: sd.findAllSeason()){
			System.out.println(s.getId());
			System.out.println(s.getYears());
			System.out.println(s.getWeeks().size());
		}		
	}
	
	public static void getSeasonById(SeasonService sd){
		System.out.println(sd.findSeasonById(1).getYears());
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = getContext();
		SeasonService sd = context.getBean(SeasonService.class);
		
		//getAllSeason(sd);
		getSeasonById(sd);
		
		context.close();
	}
}
