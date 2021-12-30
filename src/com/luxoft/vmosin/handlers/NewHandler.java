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
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.parts.StudentEditInfo;
import com.luxoft.vmosin.parts.TreeGroupView;
import com.luxoft.vmosin.utils.Const;

public class NewHandler {

	@CanExecute
	public boolean canExecute(EPartService partService) {
		return partService.getActivePart() != null && partService.getActivePart().getObject() instanceof TreeGroupView;
	}

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application, Shell shell) {
		TreeViewer treeViewer = ((TreeGroupView) partService.getActivePart().getObject()).getTreeViewer();
		IStructuredSelection selection = treeViewer.getStructuredSelection();
		if ((selection.getFirstElement() instanceof PersonGroup)
				&& ((PersonGroup) selection.getFirstElement()).getParent().getName().equals("Folder")) {
			MPartStack stack = (MPartStack) modelService.find(Const.PART_STACK_EDITOR, application);
			MPart part = MBasicFactory.INSTANCE.createPart();
			part.setContributionURI(Const.BUNDLE_STUDENT_INFO);
			part.setCloseable(true);
			part.setLabel("New student");
			PersonGroup pg = (PersonGroup) selection.getFirstElement();
			Person p = new Person("", pg, "", "", "", 0);
			pg.addPerson(p);
			stack.getChildren().add(part);
			partService.showPart(part, PartState.ACTIVATE);
			((StudentEditInfo) part.getObject()).setPerson(p);
		} else if ((selection.getFirstElement() instanceof PersonGroup)
				&& ((PersonGroup) selection.getFirstElement()).getName().equals("Folder")) {
			PersonGroup folder = (PersonGroup) selection.getFirstElement();
			final InputDialog id = new InputDialog(shell, "Create new Group", "Enter new Group Name:", "<Group Name>",
					new IInputValidator() {
						public String isValid(String newText) {
							for (PersonAbstr p : folder.getPersons()) {
								if (p.getName().equals(newText)) {
									return "This name alredy exist!";
								}
							}
							return null;
						}
					});

			id.open();
			if (id.getValue() != null) {
				folder.addPerson(new PersonGroup(folder, id.getValue()));
				treeViewer.refresh();
			}
		}
	}
}