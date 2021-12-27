
package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;
import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.handlers.EditHandler;
import com.luxoft.vmosin.handlers.NewHandler;

@Singleton
public class TreeGroupView extends SelectionAdapter {

	private TreeViewer treeViewer;
	private PersonGroup root;
	private final String nameEditCommand = "studentinforcp.ui.edit.editCommand";

	@Inject	private MPart part;
	@Inject private ECommandService commandService;
	@Inject private EHandlerService service;
	
	@PostConstruct
	public void createComposite(Composite parent, EMenuService menuService) {
		treeViewer = new TreeViewer(parent);
		treeViewer.setLabelProvider(new MyLabelProvider());
		treeViewer.setContentProvider(new MyContentProvider());
		createPersonsModel();

		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		treeViewer.addDragSupport(operations, transfers, new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if (!(selection.getFirstElement() instanceof Person)) {
					event.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				
				if (event.detail == DND.DROP_MOVE) {
					System.out.println("move");
				} else if (event.detail == DND.DROP_COPY) {
					System.out.println("copy");
				}
				
				treeViewer.refresh();
			}

		});
		treeViewer.addDoubleClickListener(event-> {
		        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
		        if (selection.isEmpty()) return;
		        try {
	                Command command = commandService.getCommand(nameEditCommand);
	                if( !command.isDefined() )
	                    return;
	                ParameterizedCommand myCommand = commandService.createCommand(nameEditCommand, null);
	                service.activateHandler(nameEditCommand, new EditHandler());
	                if( !service.canExecute(myCommand ))
	                    return;
	                service.executeHandler( myCommand );
	            } catch (Exception ex) {
	                throw new RuntimeException(String.format("command with id \"%s\" not found", nameEditCommand));
	            } 
		        
		    }
		);
		treeViewer.addDropSupport(DND.DROP_MOVE, transfers, new DropTargetAdapter() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
				try {
	                Command command = commandService.getCommand(nameEditCommand);
	                if( !command.isDefined() )
	                    return;
	                ParameterizedCommand myCommand = commandService.createCommand(nameEditCommand, null);
	                service.activateHandler(nameEditCommand, new EditHandler());
	                if( !service.canExecute(myCommand ))
	                    return;
	                service.executeHandler( myCommand );
	            } catch (Exception ex) {
	                throw new RuntimeException(String.format("command with id \"%s\" not found", nameEditCommand));
	            }   

			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void drop(DropTargetEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if ((event.item != null && event.item.getData() instanceof PersonGroup
						&& !((PersonGroup) event.item.getData()).getName().equals("Folder"))) {
					Person p = (Person) selection.getFirstElement();
					PersonGroup pg = (PersonGroup) event.item.getData();
					if (p.getParent() != pg) {
						p.getParent().removePerson(p);
						pg.addPerson(p);
						p.setGroup(pg);
						part.setDirty(true);
					}
				}
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
			}

		});
		
		menuService.registerContextMenu(treeViewer.getControl(), "studentinforcp.popupmenu.popupmenu");
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		treeViewer.setInput(root);
	}

	private void createPersonsModel() {
		root = new PersonGroup(null, "ppp");
		PersonGroup folder = new PersonGroup(root, "Folder");
		root.addPerson(folder);
		PersonGroup gr1 = new PersonGroup(folder, "Group 1");
		PersonGroup gr2 = new PersonGroup(folder, "Group 2");
		PersonGroup gr3 = new PersonGroup(folder, "Group 3");
		folder.addPerson(gr1);
		folder.addPerson(gr2);
		folder.addPerson(gr3);
		gr1.addPerson(new Person("Nick", gr1, "addr 1", "city 1", "", 5));
		gr1.addPerson(new Person("John", gr1, "addr 2", "city 2", "", 4));
		gr2.addPerson(new Person("Anna", gr2, "addr 3", "city 3", "", 3));
		gr3.addPerson(new Person("Jack", gr3, "addr 4", "city 4", "", 2));
		gr3.addPerson(new Person("Liza", gr3, "addr 5", "city 5", "", 1));
		gr3.addPerson(new Person("Elvis", gr3, "addr 6", "city 6", "", 4));

	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Persist
	public void save() {
		part.setDirty(false);
	}

	public PersonGroup getRoot() {
		return root;
	}

	public void setRoot(PersonGroup root) {
		this.root = root;
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

}