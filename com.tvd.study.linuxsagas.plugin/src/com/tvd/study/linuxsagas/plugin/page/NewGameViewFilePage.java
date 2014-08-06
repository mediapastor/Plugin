package com.tvd.study.linuxsagas.plugin.page;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tvd.gameview.plugin.model.DirectorySelectionField;
import com.tvd.gameview.plugin.model.FileSelectionField;
import com.tvd.study.linuxsagas.plugin.utils.ProjectUtils;

public class NewGameViewFilePage extends WizardPage {

	public NewGameViewFilePage(String pageName,
			IStructuredSelection selection) {
		super(pageName);
		this.setPageComplete(false);
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		
		this.initializeDialogUnits(parent);
		
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        this.createDefineGroup(composite);
        this.createSeparator(composite);
        
        this.createImageGroup(composite);
        this.createSeparator(composite);
        
        this.createScreenContainerGroup(composite);
        this.createSeparator(composite);
        
        this.createClassContainerGroup(composite);
        this.createSeparator(composite);
        
        this.createProjectsGroup(composite);
        
		this.setErrorMessage(null);
		this.setMessage(null);
		this.setControl(composite);
		
		this.validatePage();
	}
	
	
	protected boolean validatePage() {
		return false;
	}
	private void createDefineGroup(final Composite parent) {
		this.mDefineSelectionField = new FileSelectionField.Builder(parent)
				.setLabelText("Define path:")
				.build();
		this.mParamsSelectionField = new FileSelectionField.Builder(parent)
			.setLabelText("Parameters path:")
			.build();
		this.mDefineSelectionField.getText().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
			}
		});
		this.mParamsSelectionField.getText().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
			}
		});
	}
	
	private void createImageGroup(final Composite parent) {
		this.mImageInputPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Images input path:")
			.build();
		this.mImageOutputPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Images output path:")
			.build();
		
	}
	
	private void createScreenContainerGroup(final Composite parent) {
		this.mXibContainerPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Xib container path: ")
			.build();
		this.mScreenContainerPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Screen container path: ")
			.build();
	}
	
	private void createClassContainerGroup(final Composite parent) {
		this.mClassPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Class path")
			.build();
	}
	
	private void createProjectsGroup(Composite parent) {
        String tooltip = "The Android Project where the new resource file will be created.";
        Label label = new Label(parent, SWT.NONE);
        label.setText("Project: ");
        label.setToolTipText(tooltip);
        
        mRootElementCombo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        mRootElementCombo.setEnabled(false);
        mRootElementCombo.select(0);
        mRootElementCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mRootElementCombo.setToolTipText("The root element to create in the XML file.");
        updateProjectsGroup();
        
        emptyCell(parent);
        
        createFileNameField(parent);
        
    }
	
	private void createFileNameField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
        label.setText("File");
        label.setToolTipText("The name of view");
        
        mFileNameTextField = new Text(parent, SWT.BORDER);
        mFileNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mFileNameTextField.setToolTipText("The name of the resource file to create.");
        mFileNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validatePage();
            }
        });
        
        emptyCell(parent);
        
    }
	
	private void updateProjectsGroup() {
        // reset all the values in the combo
        mRootElementCombo.removeAll();
        
        // get the list of roots. The list can be empty but not null.
        List<String> roots = ProjectUtils.getSdkProjectNames();
            	
        // enable the combo if there's more than one choice
        mRootElementCombo.setEnabled(roots != null && roots.size() > 0);
        mRootElementCombo.add("--- Choose Project ---");
            
        for (String root : roots) {
        	mRootElementCombo.add(root);
        }
            
        int index = 0; // default is to select the first one
        String defaultRoot = "";//type.getDefaultRoot();
        if (defaultRoot != null) {
        	index = roots.indexOf(defaultRoot);
        }
        mRootElementCombo.select(index < 0 ? 0 : index);
    }
	
	private void createSeparator(Composite composite) {
		Label rootSeparator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd = new GridData(GridData.GRAB_HORIZONTAL);
        gd.horizontalAlignment = SWT.FILL;
        gd.horizontalSpan = 3;
        rootSeparator.setLayoutData(gd);
	}
	
	
    private void emptyCell(Composite parent) {
        new Label(parent, SWT.NONE);
    }
	
	private DirectorySelectionField mClassPathField;
	
	private DirectorySelectionField mXibContainerPathField;
	private DirectorySelectionField mScreenContainerPathField;
	
	private DirectorySelectionField mImageInputPathField;
	private DirectorySelectionField mImageOutputPathField;
	
	private FileSelectionField mDefineSelectionField;
	private FileSelectionField mParamsSelectionField;
	

	public DirectorySelectionField getClassPathField() {
		return mClassPathField;
	}

	public DirectorySelectionField getXibContainerPathField() {
		return mXibContainerPathField;
	}

	public DirectorySelectionField getScreenContainerPathField() {
		return mScreenContainerPathField;
	}

	public DirectorySelectionField getImageInputPathField() {
		return mImageInputPathField;
	}

	public DirectorySelectionField getImageOutputPathField() {
		return mImageOutputPathField;
	}

	public FileSelectionField getDefineSelectionField() {
		return mDefineSelectionField;
	}

	public FileSelectionField getParamsSelectionField() {
		return mParamsSelectionField;
	}

	public Combo getRootElementCombo() {
		return mRootElementCombo;
	}

	public Text getFileNameTextField() {
		return mFileNameTextField;
	}

	private Combo mRootElementCombo;
	private Text mFileNameTextField;
}

