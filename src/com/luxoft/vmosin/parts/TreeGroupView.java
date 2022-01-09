
package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.handlers.EditHandler;
import com.luxoft.vmosin.utils.Const;

@Singleton
public class TreeGroupView extends SelectionAdapter {

	private TreeViewer treeViewer;
	private PersonGroup root;

	@Inject
	private MPart part;
	@Inject
	private ECommandService commandService;
	@Inject
	private EHandlerService service;

	@PostConstruct
	public void createComposite(Composite parent, EMenuService menuService) {
		treeViewer = new TreeViewer(parent);
		treeViewer.setLabelProvider(new MyLabelProvider());
		treeViewer.setContentProvider(new MyContentProvider());
		createPersonsModel();

		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		int operations = DND.DROP_MOVE | DND.DROP_COPY;
		treeViewer.addDragSupport(operations, transfers, new DragSourceAdapter() {
			@Override
			public void dragStart(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if (!(selection.getFirstElement() instanceof Person)) {
					event.doit = false;
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				treeViewer.refresh();
			}
		});

		treeViewer.addDropSupport(operations, transfers, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				movePersonToOtherGroup(event);
			}

			private void movePersonToOtherGroup(DropTargetEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if ((event.item != null && event.item.getData() instanceof PersonGroup
						&& !((PersonGroup) event.item.getData()).getName().equals(Const.TREE_FOLDER))) {
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
		});
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
				if (selection.isEmpty())
					return;
				createEditPart();
			}
		});

		menuService.registerContextMenu(treeViewer.getControl(), Const.POPUP_MENU);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		treeViewer.setInput(root);
	}

	private void createEditPart() {
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

	private void createPersonsModel() {
		root = new PersonGroup(null, "init");
		PersonGroup folder = new PersonGroup(root, Const.TREE_FOLDER);
		root.addPerson(folder);
		PersonGroup gr1 = new PersonGroup(folder, "Group 1");
		PersonGroup gr2 = new PersonGroup(folder, "Group 2");
		PersonGroup gr3 = new PersonGroup(folder, "Group 3");
		folder.addPerson(gr1);
		folder.addPerson(gr2);
		folder.addPerson(gr3);
		gr1.addPerson(new Person("Bart Simpson", gr1, "addr 1", "city 1", "S_01.png", 5));
		gr1.addPerson(new Person("Homer Simpson", gr1, "addr 2", "city 2", "S_02.png", 4));
		gr2.addPerson(new Person("Kearney Zzyzwicz", gr2, "addr 3", "city 3", "S_03.png", 3));
		gr3.addPerson(new Person("Lisa Simpson", gr3, "addr 4", "city 4", "S_04.png", 2));
		gr3.addPerson(new Person("Marge Simpson", gr3, "addr 5", "city 5", "S_05.png", 1));
		gr3.addPerson(new Person("Kirk Van Houten", gr3, "addr 6", "city 6", "S_07.png", 4));

	}

	@Focus
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	@Persist
	public void save() {
		part.setDirty(false);
	}

	@PreDestroy
	public void preDestroy() {
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