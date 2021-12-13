package com.luxoft.vmosin.parts;

import org.eclipse.jface.viewers.ITreeContentProvider;

import com.luxoft.vmosin.entity.PersonGroup;
import com.luxoft.vmosin.tmp.MyModel;

public class MyContentProvider implements ITreeContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement) {
		
		return ((PersonGroup)inputElement).getPersons();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	@Override
	public Object getParent(Object element) {
		if( element == null) {
			return null;
		}

		return ((MyModel)element).parent;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof PersonGroup);
	}
	
//	SharedImages ss = new SharedImages();
//	ss.

}
