package com.tdgc.cocos2dx.popup.creator.model.basic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.tdgc.cocos2dx.popup.creator.constants.Strings;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class CommonObject {
	
	public CommonObject() {
		mMenusGroups = new ArrayList<ItemGroup>();
		mSpritesGroups = new ArrayList<ItemGroup>();
		mLabelsGroups = new ArrayList<ItemGroup>();
		mMenuItemsGroups = new ArrayList<ItemGroup>();
		mTablesGroups = new ArrayList<ItemGroup>();
		
		mSuper = "PopUpLayer";
		mZIndex = "0";
		mAnchorPoint = null;
	}
	
	public String declare() {
		StringBuilder builder = new StringBuilder("\n");
		builder.append(declareObjects(mMenuItemsGroups))
			.append(declareObjects(mSpritesGroups))
			.append(declareObjects(mTablesGroups))
			.append(declareObjects(mMenusGroups))
			.append(declareObjects(mLabelsGroups));
		
		return builder.toString();
	}
	
	public String implement(boolean pInfunction) {
		StringBuilder builder = new StringBuilder("\n");
		builder.append(implementObjects(mSpritesGroups, pInfunction))
			.append(implementObjects(mTablesGroups, pInfunction))
			.append(implementObjects(mMenusGroups, pInfunction))
			.append(implementObjects(mMenuItemsGroups, pInfunction))
			.append(implementObjects(mLabelsGroups, pInfunction));
		
		return builder.toString();
	}
	
	public String declarePositions() {
		StringBuilder builder = new StringBuilder();
		if(mPositionName != null 
				&& mPositionString != null
				&& !mPositionString.equals(Strings.DEFAULT)) {
			builder.append("\tCCPoint " + mPositionName + ";");
		}
		builder.append("\n");
		builder.append(declarePosition(mMenuItemsGroups))
			.append(declarePosition(mSpritesGroups))
			.append(declarePosition(mTablesGroups))
			.append(declarePosition(mMenusGroups))
			.append(declarePosition(mLabelsGroups));
		
		return builder.toString();
	}
	
	public String implementPositions() {
		
		String template = new FileUtils().fetchTemplate("MakePointCommon", 
				"src/com/template/new_point.template", getProject());
		StringBuilder builder = new StringBuilder("\n");
		
		if(mPositionString != null 
				&& mPositionName != null
				&& !mPositionString.equals(Strings.DEFAULT)) {
			template = template.replaceAll("\\{var_name}", mPositionName)
					.replaceAll("\\{position}", mPositionString);
			builder.append("\t" + template);
		}
		
		builder.append(implementPosition(mMenuItemsGroups))
			.append(implementPosition(mSpritesGroups))
			.append(implementPosition(mTablesGroups))
			.append(implementPosition(mMenusGroups))
			.append(implementPosition(mLabelsGroups));
		
		return builder.toString();
	}
	
	public void setPositionName(String pPositionName) {
		mName = StringUtils.convertToNormalProperty(pPositionName + "_" + mSuffix);
		if(pPositionName.trim().equals(Strings.DEFAULT)
				|| this.mIsBackground) {
			mPositionName = null;
			return;
		}
		if(pPositionName.contains(Tag.POSITION)) {
			pPositionName = pPositionName.replace(Tag.POSITION, "place");
		}
		this.mPositionName = pPositionName.toUpperCase() + "_" + mSuffix.toUpperCase();
		this.mPositionName = Config.getInstance().getPrefix().toUpperCase() + "_" + mPositionName 
				+ "_POSITION";
	}
	
	public String toAnchorPointString() {
		String result = "";
		if(mAnchorPoint != null) {
			result = mName + "->setAnchorPoint(" + mAnchorPoint + ");";
		}
		return result;
	}
	
	public String getInfunctionName() {
		return mName.substring(0, 2).toLowerCase().charAt(1)
				+ mName.substring(2);
	}
	
	public void replacePositionName(String pNewPosName) {
		this.mPositionName = pNewPosName;
	}
	
	public void setName(String pName) {
		this.mName = pName;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public String getPositionName() {
		return this.mPositionName;
	}
	
	public void setPrefix(String pPrefix) {
		this.mPrefix = pPrefix;
	}
	
	public String getPrefix() {
		return this.mPrefix;
	}
	
	public void setSuffix(String pSuffix) {
		this.mSuffix = pSuffix;
	}
	
	public String getSuffix() {
		return this.mSuffix;
	}
	
	public void setAnchorPoint(Point pPoint) {
		this.mAnchorPoint = pPoint;
	}
	
	public void setAnchorPoint(float pX, float pY) {
		this.mAnchorPoint  = new Point(pX, pY);
	}
	
	
	public void addMenuItemGroups(ItemGroup pMenuItemGroups) {
		this.mMenuItemsGroups.add(pMenuItemGroups);
	}
	
	public void addMenuGroups(ItemGroup pMenuGroups) {
		this.mMenusGroups.add(pMenuGroups);
	}
	
	public void addSpriteGroups(ItemGroup pSpriteGroups) {
		this.mSpritesGroups.add(pSpriteGroups);
	}
	
	public void addLabelGroups(ItemGroup pLabelGroups) {
		this.mLabelsGroups.add(pLabelGroups);
	}
	
	public void addTableGroups(ItemGroup pTableGroups) {
		this.mTablesGroups.add(pTableGroups);
	}
	
	public void setComment(String pComment) {
		this.mComment = pComment;
	}
	
	public String getComment() {
		return this.mComment;
	}
	
	public void setSuper(String pSuper) {
		this.mSuper = pSuper;
	}
	
	public String getSuper() {
		return this.mSuper;
	}
	
	public void setType(String pType) {
		this.mType = pType;
	}
	
	public String getType() {
		return this.mType;
	}
	
	public void setPosition(Point pPosition) {
		this.mPosition = pPosition;
	}
	
	public void setPosition(float pX, float pY) {
		this.mPosition = new Point(pX, pY);
	}
	
	public void setPosition(String pPosition) {
		this.mPositionString = pPosition;
	}
	
	public Point getPosition() {
		return this.mPosition;
	}
	
	public String getPositionString() {
		return this.mPositionString;
	}
	
	public void setAnchorPoint(String pAnchorPoint) {
		this.mAnchorPointString = pAnchorPoint;
	}
	
	public String getAnchorPointString() {
		return this.mAnchorPointString;
	}
	
	public String getDeclareObjectName() {
		return this.mDeclareObjectName;
	}
	
	public void setParent(CommonObject pParent) {
		this.mParent = pParent;
	}
	
	public CommonObject getParent() {
		return this.mParent;
	}
	
	public void setSize(String pSize) {
		this.mSizeString = pSize;
	}
	
	public String getSizeString() {
		return this.mSizeString;
	}
	
	public CommonObject getBackground() {
		return this.mBackground;
	}
	
	public void setBackground(CommonObject pBackground)	{
		if(this.mBackground == null) {
			this.mBackground = pBackground;
		}
	}
	
	public void setIsBackground(boolean pIsBackground) {
		this.mIsBackground = pIsBackground;
	}
	
	public boolean isBackground() {
		return this.mIsBackground;
	}
	
	public void setIsUnlocated(boolean pIsUnlocated) {
		this.mIsUnlocated = pIsUnlocated;
	}
	
	public void setZIndex(String pZIndex) {
		this.mZIndex = pZIndex;
	}
	
	public String getZIndex() {
		return this.mZIndex;
	}
	
	protected String declareObjects(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).declare())
				.append("\n");
		}
		
		return builder.toString();
	}
	
	protected String implementObjects(List<ItemGroup> pObjs, boolean pInfunction) {
		StringBuilder builder = new StringBuilder("\n");
		if(mComment != null && !mComment.equals("")) {
			builder.append("\t//" + mComment + "\n");
		}
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append(pObjs.get(i).implement(pInfunction))
				.append("\n");
		}
		
		return builder.toString();
	}
	
	protected String declarePosition(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).declarePosition());
		}
		return builder.toString();
	}
	
	protected String implementPosition(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).implementPosition());
		}
		return builder.toString();
	}
	
	protected String creatExtendFunctionsByGroup(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append(pObjs.get(i).createExtendFunction())
				.append("\n");
		}
		return builder.toString();
	}
	
	protected void setPrefixForItemsGroup(List<ItemGroup> pObjs) {
		for(int i = 0 ; i < pObjs.size() ; i++) {
			pObjs.get(i).setPrefix(mPrefix);
		}
	}
	
	public void setProject(IProject project) {
		this.mProject = project;
	}
	
	public IProject getProject() {
		IProject project = mProject;
		CommonObject parent = mParent;
		if(mProject == null && parent != null) {
			project = parent.mProject;
			parent = parent.getParent();
		}
		
		return project;
	}
	
	protected List<ItemGroup> mMenuItemsGroups;
	protected List<ItemGroup> mLabelsGroups;
	protected List<ItemGroup> mSpritesGroups;
	protected List<ItemGroup> mMenusGroups;
	protected List<ItemGroup> mTablesGroups;
	
	protected String mPrefix;
	protected String mSuffix;
	protected String mPositionName;
	protected String mName;
	protected Point mAnchorPoint;
	protected String mAnchorPointString;
	protected String mSuper;
	protected String mComment;
	protected String mType;
	protected String mPositionString;
	protected Point mPosition;
	protected String mSizeString;
	protected CommonObject mBackground;
	
	protected CommonObject mParent;
	
	protected String mDeclareObjectName;
	
	protected boolean mIsBackground;
	protected boolean mIsUnlocated;
	protected String mZIndex;
	
	protected IProject mProject;
}
