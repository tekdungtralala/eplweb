package com.wiwit.eplweb.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wiwit.eplweb.dao.PersonDAO;
import com.wiwit.eplweb.model.Person;

public class DatabaseConnect {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"servlet-context.xml");

		PersonDAO pd = context.getBean(PersonDAO.class);

		for (Person p : pd.listPersons()) {
			System.out.println(p.getId() + " ,name=" + p.getName());
		}
		context.close();
	}
}