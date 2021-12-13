package com.luxoft.vmosin.parts;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.luxoft.vmosin.entity.Person;

public class MyLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		return ImageDescriptor
				.createFromURL(getClass()
						.getResource((element instanceof Person) ? "/icons/home.png" : "/icons/open_in_app.png"))
				.createImage();
	}

}
