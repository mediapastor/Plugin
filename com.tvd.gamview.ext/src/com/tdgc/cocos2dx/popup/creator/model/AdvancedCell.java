package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;

public class AdvancedCell extends AdvancedObject {

	public AdvancedCell() {
		super();
		mSuper = Config.getInstance().getDefaultSuper(mSuffix);
		
		mTemplateName = "ITableCellView";
		mTemplateFile = "cell.template";
		mIsGenerateClass = false;
	}
	
	@Override
	public String implement(boolean pInfunction) {
		return super.implement(pInfunction);
	}
	
	@Override
	public String declarePositions() {
		return super.declarePositions();
	}
	
	@Override
	public String implementPositions() {
		return super.implementPositions();
	}
	public String declareAndImplement() {
		if(mBasicObject.isGenerateClass()) {
			return "";
		}
		StringBuilder builder = new StringBuilder(declare())
			.append(implement(false));
		return builder.toString();
	}
	
	@Override
	public void update() {
		if(mBasicObject.isGenerateClass()) {
			mTemplateName = "ITableCellView full";
		}
	}
	
	@Override
	public void setAdvancedParent(AdvancedObject parent) {
		if(parent != this) {
			super.setAdvancedParent(parent);
		}
	}
	
}
