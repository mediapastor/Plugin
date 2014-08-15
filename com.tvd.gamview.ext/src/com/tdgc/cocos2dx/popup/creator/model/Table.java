package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;

public class Table extends CommonObject {

	public Table() {
		super();
		this.mDeclareObjectName = "CCTableView";
		this.mSuffix = "Table";
		this.mType = ModelType.TABLE;
	}
	
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder();
		builder.append("CCTableView* " + mName + ";");
		return builder.toString();
	}
	
	@Override
	public String implement(boolean pInfunction) {
		StringBuilder builder = new StringBuilder("\n");
		String template = new FileUtils().fetchTemplate("TableView", 
				"src/com/template/new_table.template", getProject());
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		template = template.replace("{var_name}", mName)
			.replace("position_name", mPositionName)
			.replace("{anchor_point_name}", mAnchorPointString)
			.replace("{rows}", "" + mRows)
			.replace("{columns}", "" + mColumns)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{size}", mSizeString);
		
		if(mSpriteGroups.get(0).getNodesArrayName() != null) {
			template = template.replace("{nodes_array}", 
					mSpriteGroups.get(0).getNodesArrayName());
		}
		builder.append(template);
		
		return builder.toString();
	}
	
	public void setRows(int pRows) {
		this.mRows = pRows;
	}
	
	public void setColumns(int pColumns) {
		this.mColumns = pColumns;
	}
	
	public int getRows() {
		return this.mRows;
	}
	
	public int getColumns() {
		return this.mColumns;
	}
	
	protected int mRows;
	protected int mColumns;
	
}
