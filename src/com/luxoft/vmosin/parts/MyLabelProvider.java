package com.luxoft.vmosin.parts;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.luxoft.vmosin.entity.Person;
import com.luxoft.vmosin.utils.Const;

public class MyLabelProvider extends LabelProvider {

	private ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
	private Image imageGroup = ImageDescriptor.createFromURL(getClass().getResource(Const.IMAGE_GROUP))
			.createImage();
	private Image imageStudent = ImageDescriptor.createFromURL(getClass().getResource(Const.IMAGE_STUDENT)).createImage();

	@Override
	public Image getImage(Object element) {
		return (element instanceof Person) ? imageStudent : imageGroup;
	}

	@Override
	public void dispose() {
		super.dispose();
		resourceManager.dispose();
		if (imageStudent != null) {
			imageStudent.dispose();
		}
		if (imageGroup != null) {
			imageGroup.dispose();
		}
	}
}
