package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;

public class Menu extends CommonObject {
	public Menu() {
		super();
		this.mSuffix = "Menu";
		this.mDeclareObjectName = "CCMenu";
		this.mType = ModelType.MENU;
	}
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mMenuItemGroups.size() ; i++) {
			builder.append(mMenuItemGroups.get(i).declare());
		}
		builder.append("CCMenu* " + mName + "; ");
		
		return builder.toString();
	}

	@Override
	public String implement(boolean pInfunction) {
		
		if(pInfunction && isBackground()) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		
		String templateName = "CCMenu";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} else {
			if(mParent != null && mParent.getType().equals(ModelType.TABLE)) {
				templateName = "CCMenu non-add";
			}
		}
		
//		String template = new FileUtils().fetchTemplate(templateName, 
//				"src/com/template/new_menu.template");
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_menu.template", getProject());
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName);
		builder.append(template);
		
		return builder.toString();
	}
}
