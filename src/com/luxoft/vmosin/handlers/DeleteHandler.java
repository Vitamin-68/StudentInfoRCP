 
package com.luxoft.vmosin.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.parts.TreeGroupView;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class DeleteHandler {
	
	@CanExecute
	public boolean canExecute(EPartService partService) {
		if (partService.getActivePart() == null || !(partService.getActivePart().getObject() instanceof TreeGroupView)) {
			return false;
		}
		return true;
	}
		
	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application) {
		TreeViewer treeViewer = ((TreeGroupView) partService.getActivePart().getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		if (selection.getFirstElement() instanceof Person && isConfirmDelete("this Student")) {
			Person person = (Person) selection.getFirstElement();
			person.getParent().removePerson(person);
			partService.getActivePart().setDirty(true);
			treeViewer.refresh();
			return;
		} else if ((selection.getFirstElement() instanceof PersonGroup) && !((PersonGroup) selection.getFirstElement()).getName().equals("Folder") && isConfirmDelete("this Group")) {
			PersonGroup group = (PersonGroup) selection.getFirstElement();
			group.getParent().removePerson(group);
			partService.getActivePart().setDirty(true);
			treeViewer.refresh();
			return;
		} else if ((selection.getFirstElement() instanceof PersonGroup) && ((PersonGroup) selection.getFirstElement()).getName().equals("Folder") && isConfirmDelete("all Groups")) {
			PersonGroup group = (PersonGroup) selection.getFirstElement();
			for (PersonAbstr person : group.getPersons()) {
				group.removePerson(person);
			}
			partService.getActivePart().setDirty(true);
			treeViewer.refresh();
		}
	}

	private boolean isConfirmDelete(String str) {
		return MessageDialog.openConfirm(null, null, String.format("Delete %s?", str));
	}
}