package com.luxoft.vmosin.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.parts.StudentEditInfo;
import com.luxoft.vmosin.parts.TreeGroupView;

public class EditHandler {

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application) {
		
		if (!(partService.getActivePart().getObject() instanceof TreeGroupView)) {
			return;
		}
		TreeViewer treeViewer = ((TreeGroupView) partService.getActivePart().getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		if (!(selection.getFirstElement() instanceof Person)) {
			return;
		}
		Person person = (Person) selection.getFirstElement();
		MPartStack stack = (MPartStack) modelService.find("studentinforcp.partstack.editor", application);
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI("bundleclass://StudentInfoRCP/com.luxoft.vmosin.parts.StudentEditInfo");
		part.setCloseable(true);
		part.setLabel(person.getName());
		stack.getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);
		StudentEditInfo studentInfo = (StudentEditInfo) part.getObject();
		studentInfo.setPerson(person);
//		part.setObject(studentInfo);
		
	}

	@CanExecute
	public boolean canExecute(IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof Person) {
			return true;
		}
		return false;
	}
}
