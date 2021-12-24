package com.luxoft.vmosin.action;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.jface.action.Action;

import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.handlers.EditHandler;

public class AddAction extends Action {

	@Inject ECommandService commandService;
	@Inject EHandlerService service;
	
	public AddAction(String string) {
		super.setText(string);
	}

	public void run() {
		try {
            Command command = commandService.getCommand("com.luxoft.vmosin.handler.newCommand");
            if( !command.isDefined() )
                return;
            ParameterizedCommand myCommand = commandService.createCommand("com.luxoft.vmosin.handler.newCommand", null);
            service.activateHandler("com.luxoft.vmosin.handler.newCommand", new EditHandler());
            if( !service.canExecute(myCommand ))
                return;
            service.executeHandler( myCommand );
        } catch (Exception ex) {
            throw new RuntimeException("command with id \"com.luxoft.vmosin.handler.newCommand\" not found");
        } 
	}
}
