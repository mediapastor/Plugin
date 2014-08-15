package com.tdgc.cocos2dx.popup.creator.model;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup.Type;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;

public class Resources extends CommonObject {

	public Resources() {
		super();
	}
	
	public void addItemGroup(ItemGroup pItemGroup) {
		switch (pItemGroup.getType()) {
		case Type.MENU:
			addMenuGroup(pItemGroup);
			break;
			
		case Type.MENUITEM:
			addMenuItemGroup(pItemGroup);
			break;
			
		case Type.SPRITE:
			addSpriteGroup(pItemGroup);
			break;
			
		case Type.TABLE:
			addTableGroup(pItemGroup);
			break;
			
		case Type.LABLE:
			addLabelGroup(pItemGroup);
			break;
		
		default:
			break;
		}
	}
	
	public String declareExtendFunction() {
		return "void " + mName + "(CCNode* pParent);";
	}
	
	public String createExtendFunction() {
		
		//setparent
		String parentStr = "pParent";
		String parentType = "CCNode*";
		CommonObject parentArg = null;//new CommonObject();
		parentArg.setName(parentStr);
		
		this.setParent(mSpriteGroups, parentArg);
		this.setParent(mMenuItemGroups, parentArg);
		this.setParent(mMenuGroups, parentArg);
		this.setParent(mLabelGroups, parentArg);
		
		String superImplement = "";//super.implement(true);
		StringBuilder builder = new StringBuilder();
		String template = new FileUtils().fetchTemplate("normal",
				"src/com/template/new_function.template", getProject());
		String arguments = parentType + " " + parentStr;
		template = template.replace("{function_type}", "void")
				.replace("{function_name}", mName)
				.replace("//{arguments}", arguments)
				.replace("{tab}", "\t")
				.replace("{function_content}", superImplement.trim());
		
		builder.append(template);
		
		String result = builder.toString();
		
		return result;
	}
	@Override
	public CommonObject getBackground() {
		for(int i = 0 ; i < mSpriteGroups.size() ; i++) {
			for(int j = 0 ; j < mSpriteGroups.get(i).getItems().size() ; j++) {
				if(mSpriteGroups.get(i).getItems().get(j).isBackground()) {
					return mSpriteGroups.get(i).getItems().get(j);
				}
			}
		}
		
		return null;
	}
	
	public void setCurrentGroup(ItemGroup pCurrentGroup) {
		pCurrentGroup.setResources(this);
	}
	
	private void setParent(List<ItemGroup> pGroup, CommonObject pParent) {
		for(int i = 0 ; i < pGroup.size() ; i++) {
			for(int j = 0 ; j < pGroup.get(i).getItems().size() ; j++) {
				pGroup.get(i).getItems().get(j).setParent(pParent);
			}
		}
	}

	@Override
	public String declare() {
		return null;
	}

	@Override
	public String implement(boolean pInfunction) {
		return null;
	}
	
}
