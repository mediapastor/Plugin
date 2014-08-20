package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class Cell extends AdvancedObject {

	public Cell() {
		super();
		mSuper = "ITableCellView";
		mSuffix = "cell";
		mType = "Cell";
		mViewType = Constants.ViewType.CELL;
		mSuper = Config.getInstance()
				.getDefaultSupers().get(mSuffix);
	}
	
	@Override
	public String declare() {
		super.mHeaderTemplate = "default cell class";
		super.mHeaderTplPath = "src/com/template/cell_class_header.template";
		
		String srcCode = super.declare();
		StringBuilder tagBuilder = new StringBuilder()
			.append(ViewUtils.createElementTags(mLabelGroupInView, 100))
			.append(ViewUtils.createElementTags(mMenuGroupInView, 200))
			.append(ViewUtils.createElementTags(mMenuItemGroups, 300))
			.append(ViewUtils.createElementTags(mSpriteGroupInView, 400));
		srcCode = srcCode.replace("//{element_tags}", tagBuilder.toString());
		
		return srcCode;
	}

	@Override
	public String implement(boolean pInfunction) {
		super.mImplementingTemplate = "default cell class";
		super.mImplementingTplPath = "src/com/template/cell_class_implementing.template";
		return super.implement(pInfunction);
	}
	
	public String declareAndImplement() {
		StringBuilder builder = new StringBuilder(declare())
			.append(implement(false));
		return builder.toString();
	}
	
	@Override
	public String getName() {
		return "this";
	}

}