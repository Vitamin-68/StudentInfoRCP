package com.luxoft.vmosin.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import com.luxoft.vmosin.parts.StudentEditInfo;

public class EditHandler {

	@Execute
	public void execute(EPartService partService, EModelService modelService, MApplication application) {
		MPartStack stack = (MPartStack) modelService.find("studentinforcp.partstack.editor", application);
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setContributionURI("bundleclass://StudentInfoRCP/com.luxoft.vmosin.parts.StudentEditInfo");
		part.setCloseable(true);
		part.setLabel("New person");
		stack.getChildren().add(part);
//		StudentEditInfo editor = (StudentEditInfo) part.getObject();
		partService.showPart(part, PartState.ACTIVATE);
	}
}
