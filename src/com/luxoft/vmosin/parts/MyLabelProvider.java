package com.luxoft.vmosin.parts;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.luxoft.vmosin.entity.Person;

public class MyLabelProvider extends LabelProvider {

	private ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

	@Override
	public Image getImage(Object element) {
		return ImageDescriptor
				.createFromURL(getClass()
						.getResource((element instanceof Person) ? "/icons/home.png" : "/icons/open_in_app.png"))
				.createImage();
	}

	@Override
	public void dispose() {
		super.dispose();
		resourceManager.dispose();
	}
}
