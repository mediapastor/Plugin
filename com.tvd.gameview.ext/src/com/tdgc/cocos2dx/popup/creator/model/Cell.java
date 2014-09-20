package com.tdgc.cocos2dx.popup.creator.model;

import java.util.ArrayList;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Parameter;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Cell extends CommonObject {
	public Cell() {
		super();
		mSuper = "ITableCellView";
		mSuffix = "cell";
		mType = "cell";
		mViewType = Constants.ViewType.CELL;
		mXmlTagName = Tag.CELL;
		mIsAddToGroup = true;
		setAdvancedObject(new AdvancedCell());
	}
	
	public AdvancedObject createAdvancedObject() {
		return mAdvancedObject;
	}
	
	@Override
	public String declare() {
		return "";
	}

	@Override
	public String implement(boolean pInfunction) {
		return "non-template";
	}

	@Override
	public CommonObject clone() {
		Cell cell = new Cell();
		this.setAllPropertiesForObject(cell);
		
		
		return cell;
	}
	
	@Override
	public void setImage(Image image) {
		this.mImage = image;
		this.mImage.setTabCount(mTabCount + 1);
	}
	
	@Override
	public void update() {
		mIsAddToGroup = false;
		mImage.setExists(true);
		checkColumnArray();
	}
	
	@Override
	public void setParent(CommonObject parent) {
		super.setParent(parent);
		((Table)parent).addCell(this);
	}
	
	@Override
	public String getName() {
		return "this";
	}
	
	private void checkColumnArray() {
		List<List<ItemGroup>> groups = new ArrayList<List<ItemGroup>>();
		groups.add(mLabelGroups);
		groups.add(mSpriteGroups);
		groups.add(mMenuGroups);
		groups.add(mMenuItemGroups);
		groups.add(mTableGroups);
		
		Table table = (Table)getParent();
		
		for(int k = 0 ; k < groups.size() ; k++) {
			for(int i = 0 ; i < groups.get(k).size() ; i++) {
				ItemGroup itemGroup = groups.get(k).get(i);
				if(!itemGroup.isArray()
						|| itemGroup.getArrayLength() != table.getColumns()) {
					continue;
				}
				if(!itemGroup.mValidArray) {
					itemGroup.checkArray();
				}
				if(itemGroup != null) {
					int length = itemGroup.getArrayLength();
					CommonObject itemAt0 = itemGroup.getItems().get(0);
					float widthAt0 = itemAt0.getSize().getWidth();
					float width = this.getSize().getWidth()
							- 2*(itemAt0.getLocationInView().getX()
							- this.getLocationInView().getX());
					float margin = (width - length*widthAt0)/(length - 1);
					for(int j = 1 ; j < length; j++) {
						float x = (int)(itemAt0.getPosition().getX() 
								+ (j*widthAt0 + j*margin));
						float y = itemAt0.getPosition().getY();
						CommonObject item = itemGroup.getItems().get(j);
						item.setPosition(new Point(x, y));
						item.locationInViewWithPosition();
					}
				}
			}
		}
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		StringBuilder generateClassString = new StringBuilder();
		StringBuilder exportedAtt = new StringBuilder();
		StringBuilder parameterTags = new StringBuilder();
		if(mIsGenerateClass) {
			List<Parameter> params = mAdvancedObject.getParameters();
			for(int i = 0 ; i < params.size() ; i++) {
				parameterTags.append("\n" + tab)
					.append(params.get(i).toXML());
			}
			generateClassString.append("\n\t\t" + tab)
				.append(Attribute.GENERATE_CLASS + "=\"true\" ");
			exportedAtt.append(Attribute.EXPORTED)
				.append("=\"" + mAdvancedObject.isExported() + "\"");
		}
		//set attribute
		builder.append("<" + mXmlTagName + " ")
			.append(Attribute.CLASS_NAME + "=\"" + mAdvancedObject.getClassName() + "\" ")
			.append(Attribute.PREFIX + "=\"" + mAdvancedObject.getPrefix() + "\"")
			.append("\n" + tab + "\t\t" + Attribute.SUPER + "=\"" + mSuper + "\" ")
			.append(Attribute.VISIBLE + "=\"true\" ")
			.append(generateClassString)
			.append(exportedAtt)
			.append("\n" + tab + "\t\t" + Attribute.COMMENT + "=\"\">");
		
		//add elements
		builder.append(parameterTags);
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.POSITION + " " + Attribute.VALUE 
				+ "=\"" + mPosition + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.SIZE + " " + Attribute.VALUE 
				+ "=\"" + mSize + "\" />");
		
		builder.append("\n" + mImage.toXML());
		
		builder.append("\n")
			.append(super.toXML())
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
}
