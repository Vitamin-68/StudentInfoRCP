package com.luxoft.vmosin.parts;

import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.entity.PersonAbstr;
import com.luxoft.vmosin.entity.PersonGroup;

public class StudentEditInfo {

	private Person inputPerson;
	private Text fieldName;
	private Canvas studentPhoto;
	private Image studentImage;
	private String photoName;
	private Text fieldGroup;
	private Text fieldAddress;
	private Text fieldCity;
	private Text fieldResult;
	private KeyAdapter txtFieldListener;
	private final static String PHOTO_PATH = "/resources/photo/";

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
		studentPhoto = new Canvas(parent, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.widthHint = 60;
		gridData.heightHint = 60;
		gridData.horizontalSpan = 3;
		gridData.verticalSpan = 6;
		if (photoName != null) {
//			studentImage = ImageDescriptor.createFromURL(getClass().getResource(PHOTO_PATH + photoName)).createImage();
			studentImage = ImageDescriptor.createFromURL(getClass().getResource(Path.of(PHOTO_PATH, photoName).toString())).createImage();
		}
		studentPhoto.setLayoutData(gridData);
		studentPhoto.addPaintListener(new PaintListener() {
			public void paintControl(final PaintEvent event) {
				if (studentImage != null) {
					event.gc.drawImage(studentImage, 0, 0);
				}
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
			}
		});
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		label = new Label(parent, SWT.LEFT);
		label.setText("* – required fields");
		label.setLayoutData(gridData);
		gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 3;
		Button saveButton = new Button(parent, SWT.PUSH);
		saveButton.setLayoutData(gridData);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fieldName.getText().trim().isEmpty() || fieldGroup.getText().trim().isEmpty()) {
					MessageDialog.openInformation(parent.getShell(), "Warning!",
							"Fields \"Name\" and \"Group?\" not allow be empty!");
					return;
				}
				MPart groupViewPart = partService.findPart("studentinforcp.part.groupview");
				PersonGroup root = ((TreeGroupView) groupViewPart.getObject()).getRoot();
				PersonGroup folder = (PersonGroup) root.getPersons()[0];
				TreeViewer treeViewer = ((TreeGroupView) groupViewPart.getObject()).getTreeViewer();
				if (!fieldGroup.getText().equals(inputPerson.getParent().getName())) {
					inputPerson.setGroup(moveToOtherGroup(folder));
				}
				inputPerson.setName(fieldName.getText());
				inputPerson.setAddress(fieldAddress.getText());
				inputPerson.setCity(fieldCity.getText());
				inputPerson.setPhotoName(photoName);
				inputPerson.setResult(Integer.parseInt(fieldResult.getText()));
				partService.findPart("studentinforcp.part.groupview").setDirty(true);
				partService.getActivePart().setDirty(false);
				partService.getActivePart().setLabel(fieldName.getText());
				((TreeGroupView) groupViewPart.getObject()).setRoot(root);
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
		Button addPhotoButton = new Button(parent, SWT.PUSH);
		addPhotoButton.setLayoutData(gridData);
		addPhotoButton.setText("Add photo...");
		addPhotoButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String fileName = new FileDialog(parent.getShell()).open();
				if (fileName != null) {
					studentImage = new Image(parent.getDisplay(), fileName);
					photoName = getNameFromPath(fileName);
					partService.getActivePart().setDirty(true);
				}
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
		this.photoName = inputPerson.getPhotoName();
		this.fieldGroup.setText(inputPerson.getParent().getName());
		this.fieldAddress.setText(inputPerson.getAddress());
		this.fieldCity.setText(inputPerson.getCity());
		this.fieldResult.setText(String.valueOf(inputPerson.getResult()));
		studentImage = ImageDescriptor.createFromURL(getClass().getResource(Path.of(PHOTO_PATH, photoName).toString())).createImage();
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
	
	private String getNameFromPath(String path) {
		int idx = path.replaceAll("\\\\", "/").lastIndexOf("/");
		return idx >= 0 ? path.substring(idx + 1) : path;
	}
}
