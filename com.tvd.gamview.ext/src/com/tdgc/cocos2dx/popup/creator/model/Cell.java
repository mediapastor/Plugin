package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
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
			.append(ViewUtils.createElementTags(mMenuItemGroupInView, 300))
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
	
	@Override
	public String implementPositions() {
		for(int i = 0 ; i < mSpriteGroupInView.size() ; i++) {
			ItemGroup itemGroup = mSpriteGroupInView.get(i);
			if(!itemGroup.isArray()) {
				continue;
			}
			itemGroup.mLength = ((Table)getParent()).mColumns;
			if(!itemGroup.mValidArray) {
				itemGroup.checkArray();
			}
			for(int j = 1 ; j < itemGroup.mLength ; j++) {
				CommonObject itemAt0 = itemGroup.getItems().get(0);
				float x = itemAt0.getPosition().getX() 
						+ (int)(j*getSize().getWidth()/itemGroup.mLength);
				float y = itemAt0.getPosition().getY();
				itemGroup.getItems().get(j).setPosition(new Point(x, y).toString());;
			}
		}
		return super.implementPositions();
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
