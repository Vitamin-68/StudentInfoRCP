package com.luxoft.vmosin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonGroup;

public class ContextMenuHandler {
	
	@Execute
	public static void execute(IStructuredSelection selection, MenuManager mgr, Menu menu) {
		final Action a = new Action("") {
		};
		
		
		if (!selection.isEmpty() && selection.getFirstElement() instanceof PersonGroup) {
			if (((PersonGroup) selection.getFirstElement()).getName().equals("Folder")) {
				a.setText("Action for folder " + selection.getFirstElement());
				
				MenuItem addGroupItem = new MenuItem(menu, SWT.PUSH);
				addGroupItem.setText("Add new Group");
				MenuItem delAllGroupItem = new MenuItem(menu, SWT.PUSH);
				delAllGroupItem.setText("Delete all Group");
//				addGroupItem.
				mgr.add(a);
			} 
			else {
				a.setText("Action for group " + selection.getFirstElement());
				mgr.add(a);
			}
		}
		if (!selection.isEmpty() && selection.getFirstElement() instanceof Person) {
			a.setText("Action for person " + selection.getFirstElement());
			mgr.add(a);
		}
	}

}
