package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;

public class Sprite extends CommonObject {
	
	public Sprite() {
		super();
		this.mSuffix = "Sprite";
		this.mDeclareObjectName = "CCSprite";
		this.mType = ModelType.SPRITE;
		this.mImage = null;
		this.mXmlTagName = Tag.SPRITE;
		this.mIsUnlocated = false;
	}
	
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder();
		builder.append("CCSprite* " + mName + ";");
		
		return builder.toString();
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		super.setPositionName(pPositionName);
	}

	@Override
	public String implement(boolean pInfunction) {
		StringBuilder builder = new StringBuilder("\n");
		
		String templateName = "CCSprite";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} else {

		}
		if(mIsUnlocated) {
			templateName += " unlocated";
		}
		
//		String template = new FileUtils().fetchTemplate(templateName, 
//				"src/com/template/new_sprite.template");
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_sprite.template", getProject());
		
		String imageName = "";
		if(mImage == null) {
			imageName = ((Sprite)mBackground).getImage().getId();
		} else {
			imageName = mImage.getId();
		}
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		
		template = template.replace("{var_name}", mName)
			.replace("{position_name}", mPositionName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{image_name}", imageName)
			.replace("{z-index}", mZIndex);
		builder.append(template);
		
		return builder.toString();
	}
	
	@Override
	public String implementPositions() {
		return super.implementPositions();
	}
	
	@Override
	public void setLocationInView(float x, float y) {
		super.setLocationInView(x, y);
	}
	
	public void setPosition(Point point) {
		if(mIsUnlocated) {
			mParent.setPosition(point);
		}
		super.setPosition(point);
	}
	
	@Override
	public void setSize(Size size) {
		super.setSize(size);
	}
	
	public void setImage(Image pImage) {
		this.mImage = pImage;
	}
	
	public Image getImage() {
		return this.mImage;
	}
	
	public void setUnlocated(boolean unlocated) {
		this.mIsUnlocated = unlocated;
	}
	
	@Override
	public CommonObject clone() {
		Sprite sprite = new Sprite();
		sprite.mImage = mImage;
		this.setAllPropertiesForObject(sprite);
		
		return sprite;
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " " + Attribute.VISIBLE + "=\"true\" ")
			.append(Attribute.COMMENT + "=\"\">");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.POSITION_NAME + " " + Attribute.VALUE 
					+ "=\"" + mXmlPositionName + "\" ");
		if(!mIsUnlocated) {
			builder.append("/>");
			builder.append("\n" + tab + "\t")
			.append("<" + Tag.ANCHORPOINT + " " + Attribute.VALUE 
					+ "=\"" + mAnchorPointString + "\" />");
			builder.append("\n" + tab + "\t")
			.append("<" + Tag.POSITION + " " + Attribute.VALUE 
					+ "=\"" + mPosition + "\" />");
			builder.append("\n" + tab + "\t")
			.append("<" + Tag.Z_INDEX + " " + Attribute.VALUE 
					+ "=\"" + mZIndex + "\" />");
		} else {
			builder.append(Attribute.UNLOCATED + "=\"true\" />");
		}
		
		builder.append("\n" + mImage.toXML());
		
		builder.append("\n")
			.append(super.toXML())
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
	@Override
	public void setParent(CommonObject parent) {
		super.setParent(parent);
		if(parent instanceof MenuItem) {
			mIsUnlocated = true;
		}
	}
	
	protected boolean mIsUnlocated;
	protected Image mImage;
}
