package com.tvd.gameview.ext.pages;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.tvd.gameview.ext.utils.MessageUtils;

public class GameViewMainPage extends WizardNewProjectCreationPage {

	public GameViewMainPage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite composite = (Composite)getControl();
		Group group = new Group(composite, SWT.NONE);
		group.setText(MessageUtils.getString("game_project"));
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(
		         GridData.HORIZONTAL_ALIGN_FILL));
		
		mGameProjectLocation = new DirectoryFieldEditor("location", 
				"Location", group);
		mGameProjectLocation.setFocus();
		mGenerateTemplate = new Button(group, SWT.CHECK);
		mGenerateTemplate.setText("Create template");
		mGenerateTemplate.setSelection(true);
		mGenerateTemplate.setEnabled(false);
		
		mGameProjectLocation.getTextControl(group).addModifyListener(
				new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent e) {
						if(validatePage()) {
							GameViewMainPage.this.setPageComplete(true);
							GameViewMainPage.this.setErrorMessage(null);
						}
					}
		});
	}
	
	@Override
	protected boolean validatePage() {
		if(mGameProjectLocation != null) {
			String projectLocation = mGameProjectLocation.getStringValue();
			if(projectLocation == null || projectLocation.equals("")) {
				this.setErrorMessage("Invalid game template location");
				return false;
			}
		} 
		return super.validatePage();
	}
	
	public DirectoryFieldEditor getGameProjectLocation() {
		return this.mGameProjectLocation;
	}
	
	private DirectoryFieldEditor mGameProjectLocation;
	private Button mGenerateTemplate;
}
