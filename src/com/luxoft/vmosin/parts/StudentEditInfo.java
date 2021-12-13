package com.luxoft.vmosin.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class StudentEditInfo {
	
	Text fieldName;
	Canvas photo;
	Text fieldGroup;
	Text fieldAddress;
	Text fieldCity;
	Text fieldResult;
	
	
	@Inject
	public MPart part;

	@PostConstruct
	public void postConstruct(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginWidth = 30;
		gridLayout.marginHeight = 30;
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 20;
		parent.setLayout(gridLayout);
		
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("Name:");
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 2;
	    fieldName = new Text(parent, SWT.LEFT | SWT.BORDER);
		fieldName.setLayoutData(gridData);
		photo = new Canvas(parent, SWT.BORDER);
		gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
	    gridData.widthHint = 60;
	    gridData.heightHint = 60;
	    gridData.horizontalSpan = 3;
	    gridData.verticalSpan = 5;
	    photo.setLayoutData(gridData);
	    photo.addPaintListener(new PaintListener() 
	    {
	      public void paintControl(final PaintEvent event) 
	      {
//	        if (dogImage != null) 
//	        {
//	          event.gc.drawImage(dogImage, 0, 0);
//	        }
	      }
	    });
	    label = new Label(parent, SWT.RIGHT);
	    label.setText("Group:");
	    gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 2;
	    fieldGroup = new Text(parent, SWT.LEFT | SWT.BORDER);
	    fieldGroup.setLayoutData(gridData);
	    label = new Label(parent, SWT.RIGHT);
	    label.setText("Address:");
	    gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 2;
	    fieldAddress = new Text(parent, SWT.LEFT | SWT.BORDER);
	    fieldAddress.setLayoutData(gridData);
	    label = new Label(parent, SWT.RIGHT);
	    label.setText("City:");
	    gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 2;
	    fieldCity = new Text(parent, SWT.LEFT | SWT.BORDER);
	    fieldCity.setLayoutData(gridData);
	    label = new Label(parent, SWT.RIGHT);
	    label.setText("Result:");
	    gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 2;
	    fieldResult = new Text(parent, SWT.LEFT | SWT.BORDER);
	    fieldResult.setLayoutData(gridData);
	    gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
	    gridData.horizontalSpan = 6;
	    Button button = new Button(parent, SWT.PUSH);
	    button.setLayoutData(gridData);
	    button.setText("Save");
	}
	
	
	@PreDestroy
	public void preDestroy() {
		
	}
	
	
	@Focus
	public void onFocus() {
		
	}
	
	
	@Persist
	public void save() {
		
	}
	
}
