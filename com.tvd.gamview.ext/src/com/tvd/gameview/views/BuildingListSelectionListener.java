package com.tvd.gameview.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class BuildingListSelectionListener implements ISelectionListener {
	
	public BuildingListSelectionListener(Viewer viewer, IWorkbenchPart part) {
		this.mViewer = viewer;
		this.mPart = part;
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection sel) {
//		System.out.println("BuildingListSelectionListener::selectionChanged::sel instanceof = " + sel.getClass().getName());
		if(part != this.mPart && sel instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection)sel).getFirstElement();
			Object current = ((IStructuredSelection)mViewer.getSelection())
					.getFirstElement();
//			System.out.println("BuildingListSelectionListener::selectionChanged::selected instanceof = " + ((selected != null)?selected.getClass().getName():"null"));
			if(selected != current && selected instanceof BuildingListElement) {
				mViewer.setSelection(sel);
				if(mViewer instanceof StructuredViewer) {
					((StructuredViewer)mViewer).reveal(selected);
				}
			}
		}
	}
	
	private Viewer mViewer;
	private IWorkbenchPart mPart;
	
}
