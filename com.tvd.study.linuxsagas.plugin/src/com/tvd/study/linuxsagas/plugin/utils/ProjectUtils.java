package com.tvd.study.linuxsagas.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ProjectUtils {

	public static IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		return projects;
	}
	
	public static IResource extractSelection(ISelection sel) {
	      if (!(sel instanceof IStructuredSelection))
	          return null;
	       IStructuredSelection ss = (IStructuredSelection) sel;
	       Object element = ss.getFirstElement();
	       if (element instanceof IResource)
	          return (IResource) element;
	       if (!(element instanceof IAdaptable))
	          return null;
	       IAdaptable adaptable = (IAdaptable)element;
	       Object adapter = adaptable.getAdapter(IResource.class);
	       System.out.println("ProjectUtils.extractSelection " + 
	    		   ((IResource)adapter).getProject().getName());
	       return (IResource) adapter;
	   }
	
	public static IResource extractResource(IEditorPart editor) {
	      IEditorInput input = editor.getEditorInput();
	      if (!(input instanceof IFileEditorInput)) {
	         return null;
	      }
	      System.out.println("ProjectUtils.extractResource " + 
	    		  ((IFileEditorInput)input).getFile().getProject().getName());
	      return ((IFileEditorInput)input).getFile();
	   }
	
	public static IProject getActiveProject() {
		IWorkbenchWindow workbenchWindow = null;
		IWorkbenchPage workbenchPage = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench != null) {
			workbenchWindow = workbench.getActiveWorkbenchWindow();
			IResource resource = extractSelection(workbenchWindow
						.getSelectionService()
						.getSelection());
			if(resource != null) {
				return resource.getProject();
			}
		}
		if(workbenchWindow != null) {
			workbenchPage = workbenchWindow.getActivePage();
		}
		if(workbenchPage != null) {
			IEditorPart editorPart = workbenchPage.getActiveEditor();
			if(editorPart != null) {
				IResource resource = extractResource(editorPart).getProject();
				if(resource != null) {
					return resource.getProject();
				}
			}
		}
		
		return null;
	}
	
	public static IProject getActiveSdkProject() {
		IProject project = getActiveProject();
		if(project != null) {
			if(findSdkProject(project.getName()) != null) {
				return project;
			}
		}
		
		return null;
	}
	
	public static List<IProject> getSdkProjects() {
		IProject[] projects = getProjects();
		List<IProject> result = new ArrayList<IProject>();
		for(IProject project : projects) {
			try {
				String persistentProperty = 
						project.getPersistentProperty(new QualifiedName("tvd", "author"));
				if(persistentProperty != null && persistentProperty.equals("tvd")) {
					result.add(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static List<String> getSdkProjectNames() {
		IProject[] projects = getProjects();
		List<String> result = new ArrayList<String>();
		for(IProject project : projects) {
			try {
				String persistentProperty = 
						project.getPersistentProperty(new QualifiedName("tvd", "author"));
				if(persistentProperty != null && persistentProperty.equals("tvd")) {
					result.add(project.getName());
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static IProject findSdkProject(String pName) {
		List<IProject> projects = getSdkProjects();
		IProject result = null;
		for(IProject project : projects) {
			if(project.getName().equals(pName)) {
				result = project;
			}
		}
		
		return result;
	}
}
