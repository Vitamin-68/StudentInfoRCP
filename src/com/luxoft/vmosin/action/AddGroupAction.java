package com.luxoft.vmosin.action;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;

import com.luxoft.vmosin.entity.PersonGroup;

public class AddGroupAction extends Action {

	@Inject
	List<PersonGroup> persons;
	
	@Inject
	PersonGroup gr;

//	public AddGroupAction() {
//		
//	}

	public void run() {
		persons.add(new PersonGroup(persons.get(0).getParent(), "Group 4"));
	}
}
