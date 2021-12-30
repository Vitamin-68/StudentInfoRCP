package com.luxoft.vmosin.entity;

import java.util.ArrayList; 
import java.util.List;

public class PersonGroup extends PersonAbstr {
	
	private PersonGroup parent;
	private String name;
	private List<PersonAbstr> persons;

	public PersonGroup(PersonGroup parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public PersonGroup getParent() {
		return parent;
	}

	public void rename(String newName) {
		this.name = newName;
	}
	
	public void addPerson(PersonAbstr person) {
		if (persons == null)
			persons = new ArrayList<PersonAbstr>();
		persons.add(person);
	}
	
	public void removePerson(PersonAbstr person) {
		if (persons != null) {
			persons.remove(person);
			if (persons.isEmpty())
				persons = null;
		}
	}

	public PersonAbstr[] getPersons() {
		if (persons != null)
			return (PersonAbstr[]) persons.toArray(new PersonAbstr[persons.size()]);
		return new PersonAbstr[0];
	}

	@Override
	public String toString() {
		return name;
	}
	
}
