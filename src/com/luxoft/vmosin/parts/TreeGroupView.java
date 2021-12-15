
package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.luxoft.vmosin.action.AddGroupAction;
import com.luxoft.vmosin.action.DelAllGroupAction;
import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.handlers.ContextMenuHandler;

public class TreeGroupView extends SelectionAdapter {

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

//		final Action a = new Action("") {
//		};
//		IStructuredSelection selection = treeViewer.getStructuredSelection();
//		Menu menuFolder = new Menu(treeViewer.getControl());
//		menu.addMenuListener(new MenuListener() {
//			IStructuredSelection selection = treeViewer.getStructuredSelection();
//			ContextMenuHandler.execute(selection, mgr, menu);
//		});
//		MenuItem addGroupItem = new MenuItem(menuFolder, SWT.PUSH);
//		addGroupItem.setText("Add new Group");
//		addGroupItem.addSelectionListener(this);
//		MenuItem delAllGroupItem = new MenuItem(menuFolder, SWT.PUSH);
//		delAllGroupItem.setText("Delete all Group");
//		treeViewer.getTree().setMenu(menuFolder);

//		mgr.addMenuListener(manager -> {
//			IStructuredSelection selection = treeViewer.getStructuredSelection();
//			ContextMenuHandler.execute(selection, mgr, menu);
//		});

		MenuManager mgr = new MenuManager();
		Menu menu = mgr.createContextMenu(treeViewer.getControl());
		mgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}
				if (treeViewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
					PersonAbstr object = (PersonAbstr) selection.getFirstElement();

					if (object instanceof PersonGroup) {
						manager.add(new AddGroupAction());
						manager.add(new DelAllGroupAction());
					} else if (object instanceof Person) {

//                        manager.add(new OtherAction());

					}
				}
			}
		});
		mgr.setRemoveAllWhenShown(true);
		treeViewer.getControl().setMenu(menu);
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