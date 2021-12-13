
package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonGroup;

public class TreeGroupView {

	private TreeViewer treeViewer;
	PersonGroup root;

	@Inject
	private MPart part;

	@PostConstruct
	public void createComposite(Composite parent) {
		treeViewer = new TreeViewer(parent);
		treeViewer.setLabelProvider(new MyLabelProvider());
		treeViewer.setContentProvider(new MyContentProvider());
		createPersonsModel();
		treeViewer.setInput(root);
	}

	private void createPersonsModel() {
		root = new PersonGroup(null, "");
		PersonGroup folder = new PersonGroup(root, "Folder");
		root.addPerson(folder);
		PersonGroup gr1 = new PersonGroup(root, "Group 1");
		PersonGroup gr2 = new PersonGroup(root, "Group 2");
		PersonGroup gr3 = new PersonGroup(root, "Group 3");
		folder.addPerson(gr1);
		folder.addPerson(gr2);
		folder.addPerson(gr3);
		gr1.addPerson(new Person("Nick", gr1, "addr 1", "city 1", 5));
		gr1.addPerson(new Person("John", gr1, "addr 2", "city 2", 4));
		gr2.addPerson(new Person("Anna", gr2, "addr 3", "city 3", 3));
		gr3.addPerson(new Person("Jack", gr3, "addr 4", "city 4", 2));
		gr3.addPerson(new Person("Liza", gr3, "addr 5", "city 5", 1));
		gr3.addPerson(new Person("Elvis", gr3, "addr 6", "city 6", 4));

	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Persist
	public void save() {
		part.setDirty(false);
	}

}