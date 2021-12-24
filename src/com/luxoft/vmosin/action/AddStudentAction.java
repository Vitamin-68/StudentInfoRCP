package com.luxoft.vmosin.action;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.jface.action.Action;

import com.luxoft.vmosin.handlers.EditHandler;

public class AddStudentAction extends Action {
	
	@Inject ECommandService commandService;
	@Inject EHandlerService service;
	
	public AddStudentAction(String string) {
		super.setText(string);
	}

	public void run() {
		try {
            Command command = commandService.getCommand("studentinforcp.ui.edit.newStudentCommand");
            if( !command.isDefined() )
                return;
            ParameterizedCommand myCommand = commandService.createCommand("studentinforcp.ui.edit.newStudentCommand", null);
            service.activateHandler("studentinforcp.ui.edit.newStudentCommand", new EditHandler());
            if( !service.canExecute(myCommand ))
                return;
            service.executeHandler( myCommand );
        } catch (Exception ex) {
            throw new RuntimeException("command with id \"studentinforcp.ui.edit.newStudentCommand\" not found");
        }  
	}
}
