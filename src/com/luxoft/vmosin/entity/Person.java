package com.luxoft.vmosin.entity;

public class Person extends PersonAbstr {

	private String name;
	private PersonGroup group;
	private String address;
	private String city;
	private int result;

	public Person(String name, PersonGroup group, String address, String city, int result) {
		super();
		this.name = name;
		this.group = group;
		this.address = address;
		this.city = city;
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PersonGroup getParent() {
		return group;
	}

	public void setGroup(PersonGroup group) {
		this.group = group;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return name;
	}

}
