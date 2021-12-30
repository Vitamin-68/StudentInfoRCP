 
package com.luxoft.vmosin.popupmenu;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Evaluate;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.parts.TreeGroupView;

public class PopupMenuStudentImperative {
	@Evaluate
	public boolean evaluate(@Named(IServiceConstants.ACTIVE_PART) MPart part) {
		TreeViewer treeViewer = ((TreeGroupView) part.getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		return selection.getFirstElement() instanceof Person;
	}
}
