package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;

public class Label extends CommonObject {
	
	public Label() {
		super();
		this.mSuffix = "Label";
		this.mDeclareObjectName = "CCLabelTTF";
		this.mType = ModelType.LABEL;
	}
	
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder(super.declare());
		builder.append("\tCCLabelTTF* " + mName + ";");
		
		return builder.toString();
	}
	
	@Override
	public String implement(boolean pInfunction) {
		
		if(pInfunction && isBackground()) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		builder.append(super.implement(pInfunction));
		
		String templateName = "CCLabelTTF";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} else {
			if(mParent != null && mParent.getType().equals(ModelType.TABLE)) {
				templateName = "CCMenu non-add";
			}
		}
		
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_label.template", getProject());
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{anchorpoint}", mAnchorPointString)
			.replace("{position_name}", mPositionName)
			.replace("{label_text}", mText)
			.replace("{label_font}", mFont)
			.replace("{label_font_size}", mFontSize)
			.replace("{z-index}", mZIndex);
		builder.append(template);
		
		return builder.toString();
	}

	public void setText(String pText) {
		this.mText = pText;
	}
	
	public void setFont(String pFont) {
		this.mFont = pFont;
	}
	
	public String getText() {
		return this.mText;
	}
	
	public String getFont() {
		return this.mFont;
	}
	
	public float getSizeFloat() {
		return this.mSizeFloat;
	}
	
	public String getSizeString() {
		return this.mSizeString;
	}
	
	public void setShadow(boolean pIsShadow) {
		this.mIsShadow = pIsShadow;
	}
	
	public void setDimension(Size pDimension) {
		this.mDimension = pDimension;
	}
	
	public void setFontSize(String pFontSize) {
		this.mFontSize = pFontSize;
	}
	protected Size mDimension;
	protected boolean mIsShadow;
	protected String mText;
	protected String mFont;
	protected String mFontSize;
	protected float mSizeFloat;
}
