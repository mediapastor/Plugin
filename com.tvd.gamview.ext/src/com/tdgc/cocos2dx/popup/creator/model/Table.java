package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;

public class Table extends CommonObject {

	public Table() {
		super();
		this.mDeclareObjectName = "CCTableView";
		this.mSuffix = "Table";
		this.mType = ModelType.TABLE;
		this.mSuper = Config.getInstance().getDefautSuper("table");
		this.mAnchorPoint = new Point(0, 0);
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
//		String template = new FileUtils().fetchTemplate("TableView", 
//				"src/com/template/new_table.template");
		String template = new FileUtils().fetchTemplate("TableView", 
				"src/com/template/new_table.template", getProject());
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		template = template.replace("{var_name}", mName)
			.replace("{size_name}", mSizeName)
			.replace("{position_name}", mPositionName)
			.replace("{rows}", "" + mRows)
			.replace("{columns}", "" + mColumns)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{cell_position_name}", mCellPositionName)
			.replace("{cell_size_name}", mCellSizeName);
		
		builder.append(template);
		
		return builder.toString();
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		super.setPositionName(pPositionName);
		mSizeName = mPositionName.replace("POSITION", "SIZE");
		mCellPositionName = mPositionName.replace("TABLE_POSITION", "CELL_POSITION");
		mCellSizeName = mPositionName.replace("TABLE_POSITION", "CELL_SIZE");
	}
	
	@Override
	public String declarePositions() {
		StringBuilder builder = new StringBuilder(super.declarePositions());
		builder.append("\n\tCCPoint " + mCellPositionName + ";\n")
			.append("\tCCSize " + mSizeName + ";\n")
			.append("\tCCSize " + mCellSizeName + ";\n");
		
		return builder.toString();
	}
	
	@Override
	public String implementPositions() {
		declarePositions();
		StringBuilder builder = new StringBuilder();
		String cellPositionString = mCell.getPositionString();
		String cellSizeString = mCell.getSizeString();
		
//		String pointTemplate = new FileUtils().fetchTemplate("CCPoint", 
//				"src/com/template/new_point.template");
//		String sizeTemplate = new FileUtils().fetchTemplate("CCSizeMake", 
//				"src/com/template/new_size.template");
		String pointTemplate = new FileUtils().fetchTemplate("CCPoint", 
				"src/com/template/new_point.template", getProject());
		String sizeTemplate = new FileUtils().fetchTemplate("CCSizeMake", 
				"src/com/template/new_size.template", getProject());
		
		builder.append(pointTemplate.replace("{var_name}", mPositionName)
				.replace("{position}", mPositionString))
			.append("\t" + sizeTemplate.replace("{var_name}", mSizeName)
				.replace("{size}", mSizeString))
			.append("\t" + pointTemplate.replace("{var_name}", mCellPositionName)
				.replace("{position}", cellPositionString))
			.append("\t" + sizeTemplate.replace("{var_name}", mCellSizeName)
				.replace("{size}", cellSizeString));
		
		return builder.toString();
	}
	
	@Override
	public void setSize(String size) {
		this.mSizeString = size;
		String[] strs = mSizeString.split(",");
		float w = Float.parseFloat(strs[0].trim());
		float h = Float.parseFloat(strs[1].trim());
		this.mSize = new Size(w, h);
	}
	
	@Override
	public void setSize(Size size) {
		if(size != null && mCell != null) {
			mCell.setSize(size.getWidth(), (int)size.getHeight()/mRows);
		}
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
	
	public void addCell(Cell cell) {
		this.mCell = cell;
	}
	
	public Cell getCell() {
		return mCell;
	}
	
	public void setImage(Image img) {
		this.mImage = img;
	}
	
	public Image getImage() {
		return mImage;
	}
	
	protected Image mImage;
	protected String mCellSizeName;
	protected String mCellPositionName;
	protected String mSizeName;
	protected Cell mCell;
	protected int mRows;
	protected int mColumns;
	
}
