 
package com.luxoft.vmosin.popupmenu;

import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.parts.TreeGroupView;

public class PopupMenuStudentImperative {
	@Evaluate
	public boolean evaluate(EPartService partService) {
		TreeViewer treeViewer = ((TreeGroupView) partService.getActivePart().getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		if (selection.getFirstElement() instanceof Person) {
			return true;
		}
		return false;
	}
}
