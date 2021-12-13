 
package com.luxoft.vmosin.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class NewHandler {
	
	@Inject
	EPartService partService;
	
	@Inject
	EModelService modelService;
	
	@Inject
	MApplication application;
	
	@Execute
	public void execute(Shell shell) {
		MPartStack stack = (MPartStack) modelService.find("studentinforcp.partstack.editor", application);
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI("bundleclass://StudentInfoRCP/com.luxoft.vmosin.parts.StudentEditInfo");
		part.setCloseable(true);
		part.setLabel("New person");
		stack.getChildren().add(part);
		partService.showPart(part, PartState.ACTIVATE);
	}
}