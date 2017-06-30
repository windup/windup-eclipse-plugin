package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {

	public TreeContentProvider() {
		super();
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getChildren(Object element) {
		return (Object[])element;
	}

	@Override
	public Object[] getElements(Object element) {
		return (Object[])element;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(java.lang.Object element) {
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public boolean isDeleted(Object element) {
		return false;
	}
}