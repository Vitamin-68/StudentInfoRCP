package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.handlers.SaveHandler;

public class StudentEditInfo {

	private Person inputPerson;
	private Text fieldName;
	private Canvas photo;
	private Text fieldGroup;
	private Text fieldAddress;
	private Text fieldCity;
	private Text fieldResult;
	private KeyAdapter txtFieldListener;

	@Inject
	public MPart part;

	@PostConstruct
	public void postConstruct(Composite parent, EPartService partService) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginWidth = 30;
		gridLayout.marginHeight = 30;
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 20;
		parent.setLayout(gridLayout);
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("Name:*");
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		fieldName = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldName.setLayoutData(gridData);
		fieldName.addKeyListener(getTextFieldListener(partService));
//				new KeyAdapter() {
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				partService.getActivePart().setDirty(true);
//				super.keyPressed(e);
//			}
//		});
		photo = new Canvas(parent, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.widthHint = 60;
		gridData.heightHint = 60;
		gridData.horizontalSpan = 3;
		gridData.verticalSpan = 6;
		photo.setLayoutData(gridData);
		photo.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
//	        if (dogImage != null) 
//	        {
//	          event.gc.drawImage(dogImage, 0, 0);
//	        }
			}
		});
		label = new Label(parent, SWT.RIGHT);
		label.setText("Group:*");
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		fieldGroup = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldGroup.setLayoutData(gridData);
		fieldGroup.addKeyListener(getTextFieldListener(partService));
		label = new Label(parent, SWT.RIGHT);
		label.setText("Address:");
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		fieldAddress = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldAddress.setLayoutData(gridData);
		fieldAddress.addKeyListener(getTextFieldListener(partService));
		label = new Label(parent, SWT.RIGHT);
		label.setText("City:");
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		fieldCity = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldCity.setLayoutData(gridData);
		fieldCity.addKeyListener(getTextFieldListener(partService));
		label = new Label(parent, SWT.RIGHT);
		label.setText("Result:");
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		fieldResult = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldResult.setLayoutData(gridData);
		fieldResult.addKeyListener(getTextFieldListener(partService));
		fieldResult.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				StringBuilder builder = new StringBuilder(fieldResult.getText());
				builder.insert(fieldResult.getCaretPosition(), e.text);

				if (!e.text.matches("[0-9]")) {
					e.doit = false;
				}
//				partService.getActivePart().setDirty(true);
			}
		});
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		label = new Label(parent, SWT.LEFT);
		label.setText("* – required fields");
		label.setLayoutData(gridData);
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 6;
		Button saveButton = new Button(parent, SWT.PUSH);
		saveButton.setLayoutData(gridData);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fieldName.getText().trim().isEmpty() || fieldGroup.getText().trim().isEmpty()) {
					MessageDialog.openInformation(parent.getShell(), "Warning!", "Fields \"Name\" and \"Group?\" not allow be empty!");
					return;
				}
				MPart part = partService.findPart("studentinforcp.part.groupview");
				PersonGroup root = ((TreeGroupView) part.getObject()).getRoot();
				PersonGroup folder = (PersonGroup) root.getPersons()[0];
				TreeViewer treeViewer = ((TreeGroupView) part.getObject()).getTreeViewer();
				if (!fieldGroup.getText().equals(inputPerson.getParent().getName())) {
					inputPerson.setGroup(moveToOtherGroup(folder));
				}
				inputPerson.setName(fieldName.getText());
				inputPerson.setAddress(fieldAddress.getText());
				inputPerson.setCity(fieldCity.getText());
				inputPerson.setResult(Integer.parseInt(fieldResult.getText()));
				partService.findPart("studentinforcp.part.groupview").setDirty(true);
				partService.getActivePart().setDirty(false);
				partService.getActivePart().setLabel(fieldName.getText());
				((TreeGroupView) part.getObject()).setRoot(root);
				treeViewer.setInput(root);
				treeViewer.refresh();
			}

			private PersonGroup moveToOtherGroup(PersonGroup folder) {
				inputPerson.getParent().removePerson(inputPerson);
				PersonGroup newGroup = null;
				PersonAbstr[] arr = inputPerson.getParent().getParent().getPersons();
				for (PersonAbstr obj : arr) {
					if (obj.getName().equals(fieldGroup.getText())) {
						newGroup = (PersonGroup) obj;
						break;
					}
				}
				if (newGroup == null) {
					newGroup = new PersonGroup(folder, fieldGroup.getText());
					folder.addPerson(newGroup);
				}
				newGroup.addPerson(inputPerson);
				return newGroup;
			}
		});
	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void onFocus() {
		fieldName.setFocus();
	}

	@Persist
	public void save() {

	}

	public void setPerson(Person person) {
		this.inputPerson = person;
		this.fieldName.setText(inputPerson.getName());
//		this.photo = photo;
		this.fieldGroup.setText(inputPerson.getParent().getName());
		this.fieldAddress.setText(inputPerson.getAddress());
		this.fieldCity.setText(inputPerson.getCity());
		this.fieldResult.setText(String.valueOf(inputPerson.getResult()));
	}

	private KeyAdapter getTextFieldListener(EPartService partService) {
		if (txtFieldListener == null) {
			txtFieldListener = new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {
					partService.getActivePart().setDirty(true);
					super.keyPressed(e);
				}
			};
		}
		return txtFieldListener;
	}
}
