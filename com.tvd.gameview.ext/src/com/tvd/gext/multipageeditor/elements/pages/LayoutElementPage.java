package com.tvd.gext.multipageeditor.elements.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.tvd.cocos2dx.popup.creator.constants.Constants;
import com.tvd.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tvd.gext.multipageeditor.pages.LayoutDetailsPage;

public class LayoutElementPage extends LayoutCommonPage {
	
	public LayoutElementPage(LayoutDetailsPage page) {
		mFormPage = page;
	}

	@Override
	public void createContents(Composite parent) {
		super.createContents(parent);
		FormToolkit toolkit = mForm.getToolkit();
		
		Composite client = mClient;
		
		mNameText = createText(toolkit, client, 
				commontext("position_name"), "");
		mAnchorpointText = createText(toolkit, client, 
				commontext("anchorpoint"), "");
		mPositionText = createText(toolkit, client, 
				commontext("position"), "");
		mPositionTypeCombo = createCombo(toolkit, client, 
				commontext("position_type"), Constants.ABSOLUTE);
		mZIndexText = createText(toolkit, client, 
				commontext("z_index"), "0");
		
	}
	
	@Override
	protected void update(Object model) {
		if(model == null) {
			return ;
		}
		CommonObject obj = (CommonObject)model;
		mElement = obj;
		
		String positionName = obj.getXmlPositionName();
		String anchorpoint = obj.getAnchorPointString();
		String position = obj.getPositionString();
		String positionType = obj.getPositionType();
		String zIndex = obj.getZIndex();
		mNameText.setText(positionName);
		mAnchorpointText.setText(anchorpoint);
		mPositionText.setText(position);
		mZIndexText.setText(zIndex);
		
		int index = positionType.equals(Constants.RELATIVE)
				? 1 : 0;
		mPositionTypeCombo.select(index);
	}
	
	protected Text createText(FormToolkit toolkit, Composite parent, 
			String label, String value) {
		return super.createText(toolkit, parent, 
				label, value, 
				mModifyListener);
	}
	
	protected Combo createCombo(FormToolkit toolkit, Composite parent, 
			String label, String positionType) {
		toolkit.createLabel(parent, label);
		final Combo combo = new Combo(parent, SWT.DROP_DOWN);
		combo.setItems(new String[] {
				Constants.ABSOLUTE,
				Constants.RELATIVE
		});
		combo.select(0);
		combo.addSelectionListener(mSelectionListener);
		
		return combo;
	}
	
	protected ModifyListener mModifyListener = new ModifyListener() {
		
		@Override
		public void modifyText(ModifyEvent event) {
			Text text = (Text)event.getSource();
			
			if(!text.isFocusControl()) {
				return;
			}
			
			if(text == mNameText) {
				mElement.setPositionName(text.getText());
			}
			else if(text == mAnchorpointText) {
				mElement.setAnchorPoint(text.getText());
			}
			else if(text == mPositionText) {
				mElement.setPosition(text.getText());
			}
			else if(text == mZIndexText) {
				mElement.setZIndex(text.getText());
			}
			
			mFormPage.setDirty(true);
		}
	};
	
	protected SelectionListener mSelectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			Combo combo = (Combo)event.getSource();
			if(!combo.isFocusControl()) {
				return;
			}
			mElement.setPositionType(combo.getText());
			mFormPage.setDirty(true);
		}
	};
	
	protected Text mNameText;
	protected Text mAnchorpointText;
	protected Text mPositionText;
	protected Combo mPositionTypeCombo;
	protected Text mMarginLeftText;
	protected Text mMarginTopText;
	protected Text mMarginRightText;
	protected Text mMarginBottomText;
	protected Text mZIndexText;
	
	protected CommonObject mElement; 
	protected LayoutDetailsPage mFormPage;
	
}