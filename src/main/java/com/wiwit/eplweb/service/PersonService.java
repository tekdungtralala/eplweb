package com.wiwit.eplweb.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wiwit.eplweb.model.Person;

public interface PersonService {

	public void addPerson(Person p);
	public void updatePerson(Person p);
	public List<Person> listPersons();
	public Person getPersonById(int id);
	public void removePerson(int id);
	
}
