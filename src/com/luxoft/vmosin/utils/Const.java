package com.luxoft.vmosin.utils;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import com.luxoft.vmosin.handlers.EditHandler;

public class Const {
	
	public static final String TREE_ROOT = "hidden";
	public static final String TREE_FOLDER = "Folder";
	public static final String PART_STACK_VIEWER = "studentinforcp.partstack.viewer";
	public static final String PART_STACK_EDITOR = "studentinforcp.partstack.editor";
	public static final String PART_TREE_VIEW = "studentinforcp.part.groupview";
	public static final String BUNDLE_STUDENT_INFO = "bundleclass://StudentInfoRCP/com.luxoft.vmosin.parts.StudentEditInfo";
	public static final String BUNDLE_BLANK_EDITOR = "bundleclass://StudentInfoRCP/com.luxoft.vmosin.parts.BlankPartEditor";
	public static final String NAME_EDIT_COMMAND = "studentinforcp.ui.edit.editCommand";
	public static final String POPUP_MENU = "studentinforcp.popupmenu.popupmenu";
	public final static String PHOTO_PATH = "/resources/photo/";
	public final static String IMAGE_GROUP = "/icons/group_icon.png";
	public final static String IMAGE_STUDENT= "/icons/student_icon.png";

	public static void createDropTarget(Composite parent, ECommandService commandService, EHandlerService service) {
		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		DropTarget target = new DropTarget(parent, operations);
		target.setTransfer(types);
		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event){
				if (event.data != null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				event.detail = DND.DROP_COPY;
				try {
					Command command = commandService.getCommand(Const.NAME_EDIT_COMMAND);
					if (!command.isDefined())
						return;
					ParameterizedCommand myCommand = commandService.createCommand(Const.NAME_EDIT_COMMAND, null);
					service.activateHandler(Const.NAME_EDIT_COMMAND, new EditHandler());
					if (!service.canExecute(myCommand))
						return;
					service.executeHandler(myCommand);
				} catch (Exception ex) {
					throw new RuntimeException(String.format("command with id \"%s\" not found", Const.NAME_EDIT_COMMAND));
				}
			}
		});
	}
}
