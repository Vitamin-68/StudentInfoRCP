 
package com.luxoft.vmosin.parts;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.swt.widgets.Composite;

import com.luxoft.vmosin.utils.Const;

public class BlankPartEditor {
	@Inject
	private ECommandService commandService;
	@Inject
	private EHandlerService service;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Const.createDropTarget(parent, commandService, service);
	}
	
	
	
	
}