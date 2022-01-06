package com.luxoft.vmosin.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.parts.StudentEditInfo;
import com.luxoft.vmosin.parts.TreeGroupView;
import com.luxoft.vmosin.utils.Const;

public class EditHandler {

	@CanExecute
	public boolean canExecute(EPartService partService) {
		return partService.getActivePart() != null && partService.getActivePart().getObject() instanceof TreeGroupView;
	}

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application) {
		TreeViewer treeViewer = ((TreeGroupView) partService.getActivePart().getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		if (!(selection.getFirstElement() instanceof Person)) {
			return;
		}
		Person person = (Person) selection.getFirstElement();
		MPartStack stack = (MPartStack) modelService.find(Const.PART_STACK_EDITOR, application);
		List<MStackElement> childrens = stack.getChildren();
		if (childrens != null) {
			for (MStackElement child : childrens) {
				MPart childPart = (MPart) child;
				if (childPart.getLabel() != null && isPartExist (childPart, person)) {
					return;
				}
			}
		}
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI(Const.BUNDLE_STUDENT_INFO);
		part.setCloseable(true);
		part.setLabel(person.getName());
		stack.getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);
		StudentEditInfo studentInfo = (StudentEditInfo) part.getObject();
		studentInfo.setPerson(person);
	}

	private boolean isPartExist(MPart childPart, Person person) {
		boolean alreadyOpened = childPart.getLabel().equals(person.getName());
		boolean notClosed = childPart.getObject() != null;
		return alreadyOpened && notClosed;
	}

}
