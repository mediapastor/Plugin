package com.tdgc.cocos2dx.popup.creator.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.file.ImageFileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;
import com.tdgc.cocos2dx.popup.creator.utils.XmlContentUtils;
import com.tdgc.cocos2dx.popup.creator.xml.XibFetcher;

public class View extends AdvancedObject {

	public View() {
		super();
		this.mViewType = Constants.ViewType.VIEW;
		this.mSuffix = "popup";
		this.mName = Config.getInstance().getDefaultParentPopup();
		this.mImages = new ArrayList<Image>();
		this.mResources = new ArrayList<Resources>();
		this.mScreenContainerPath = 
				Config.getInstance().getScreenContainerPath();
		this.mBackgroundImage = new Image("common_background", 
				"pop_common_bg.png", 
				"pop_common_bg.png",
				true, this);
		this.mImages.add(mBackgroundImage);
		this.mLabels = new ArrayList<Label>();
		this.mPrefix = "";
		this.mSuper = Config.getInstance().getDefautSuper(mSuffix);

	}
	
	@Override
	public String declare() {
		if(mTableGroupInView.size() > 0) {
			super.mHeaderTemplate = "extend table";
		}
		String srcCode = super.declare()
				.replace("{background_id}", mBackgroundImage.getId());
		
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			srcCode = srcCode.replace("{super_table_name}", 
					mTableGroupInView.get(0).getItems().get(0).getSuper());
			srcCode = srcCode.replace("{super_cell_name}", cell.getSuper())
					.replace("{table_view_name}", Config.getInstance().getTableViewName());
		}
		return srcCode;
	}
	
	@Override
	public String implement(boolean pInfunction) {
		super.mImplementingTemplate = "implement";
		if(mTableGroupInView.size() > 0) {
			super.mImplementingTemplate = "extend table";
		}
		String srcCode = super.implement(pInfunction);
		
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			srcCode = srcCode.replace("//{extend_class}", 
					cell.declareAndImplement())
					.replace("{super_cell_name}", cell.getSuper())
					.replace("{table_view_name}", Config.getInstance().getTableViewName())
					.replace("{cell_class_name}", cell.getClassName()); 
		}
		return srcCode;
	}
	
	@Override
	public String declarePositions() {
		
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, Constants.POSITION_DECLARING_CMM);
		builder.append(super.declarePositions());
		
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
				.getItems().get(0))).getCell();
			builder.append(cell.declarePositions());
		}
		
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		return StringUtils.standardizeCode(builder.toString());
	}
	
	@Override
	public String implementPositions() {
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, Constants.POSITION_IMPLEMENTING_CMM);
		builder.append(super.implementPositions());
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			builder.append(cell.implementPositions());
		}
			
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return StringUtils.standardizeCode(builder.toString());
	}
	
	public String implementPositions(String device) {
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, 
				Constants.POSITION_IMPLEMENTING_CMM + "(" + device + ")");
		builder.append(super.implementPositions());
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			builder.append(cell.implementPositions());
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE + "(" + device + ")");
		
		return builder.toString().trim();
	}
	
	public String declareImageIds() {
		StringBuilder builder = new StringBuilder();
		createHeaderCommentTemplate(builder, Constants.IMAGEIDS_DECLARING_CMM);
		for(int i = 0 ; i < mImages.size(); i++) {
			builder.append("\tstring " + mImages.get(i).getId().trim() + ";")
				.append("\n");
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return builder.toString().trim();
	}
	
	public String implementImageIds() {
		StringBuilder builder = new StringBuilder();
		createHeaderCommentTemplate(builder, 
				Constants.IMAGEIDS_IMPLEMENTING_CMM);
		
		for(int i = 0 ; i < mImages.size(); i++) {
			builder.append("\t" + mImages.get(i).getId())
				.append("\t\t= ")
				.append("\"" + mImages.get(i).getRealPath() + "\";")
				.append("\n");
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return builder.toString().trim();
	}
	
	public void export() {
		this.exportDeclaringImageIds();
		this.exportImplementedImageIds();
		this.exportDeclaringPositions();
		this.exportImplementedPositions();
		this.exportDirectory();
		this.exportHeaderCode();
		this.exportImplementedCode();
	}
	
	public void exportImages() {
		ImageFileUtils imageFileUtils = new ImageFileUtils();
		imageFileUtils.writeImagesFromTo(mImagesInputPath, 
				mImagesPath);
	}
	
//	public void exportXibTemplate(String pDevice, boolean override) {
	public void exportXibTemplate(String pDevice, IProject pProject, boolean override) {
			FileUtils fileUtils = new FileUtils();
//			String path = "resources/xib/" 
//					+ pDevice + "/template.xib";
			IFile file = pProject.getFile("resources/xib/" 
					+ pDevice + "/template.xib");
			
			//String xibContent = fileUtils.readFromFile(path);
			String xibContent = fileUtils.readFromFile(file);
			
			StringBuilder builder = new StringBuilder();
			builder.append(createImageViewsTag())
				.append("\n")
				.append(createLabelsTagForXib());
			xibContent = xibContent.replace("<!--{imageviews_tag}-->", builder.toString())
				.replace("<!--{images_tag}-->", createImagesTag());
			
			final String xibFilePath = mXibContainerPath + "/" 
					+ "/" + mClassName + ".xib";
			fileUtils.setContent(xibContent).writeToFile(xibFilePath, override);
							
	}
	
//	public void exportScreenTemplate(String pDevice) {
	public void exportScreenTemplate(String pDevice, IProject pProject) {
		FileUtils fileUtils = new FileUtils();
//		String path = "resources/screen/" 
//				+ pDevice + "/template.screen";
//		String screenContent = fileUtils.readFromFile(path);
		IFile file = pProject.getFile("resources/screen/" 
				+ pDevice + "/template.screen");
		String screenContent = fileUtils.readFromFile(file);
		screenContent = screenContent.replace("<!--{widgets}-->", createWidgetsTag());
		final String screenPath = mScreenContainerPath
				+ "/" + mClassName + ".screen";
		fileUtils.setContent(screenContent).writeToFile(screenPath, true);
//		ScreenFetcher screenFetcher = new ScreenFetcher(mImages);
//		screenFetcher.fetchData(screenPath);
//		String fileContent = fileUtils.readFromFile(mXmlFilePath);
//		for(int i = 0 ; i < mImages.size() ; i++) {
//			fileContent = XmlContentUtils.replaceSpritePosition(
//				fileContent, mImages.get(i));
//		}
// 		fileUtils.setContent(fileContent).replaceContent(mXmlFilePath);
		
	}

	public void addImage(Image pImage) {
		if(pImage.getParent() instanceof View) {
			mBackgroundImage = pImage;
			this.mImages.get(0).replaceWithAnother(pImage);
		} else {
			this.mImages.add(pImage);
		}
	}
	
	@Override
	public void setPrefix(String pPrefix) {
		if(pPrefix == null) {
			System.out.println("TVD-DEBUG: prefix is null");
			return;
		}
		this.mPrefix = pPrefix;
		String directoryName = pPrefix.substring(pPrefix.indexOf('_') + 1);
		this.mDirectoryName = StringUtils.convertToClassName(directoryName, "");
	}

	private void createHeaderCommentTemplate(StringBuilder pBuilder,
			String pComment) {
		pBuilder.append("\n\t// " + mClassName + " " + pComment)
		.append("\n\t// by " + System.getProperty("user.name"))
		.append(" " + new Date().toString())
		.append("\n");
	}
	
	public void exportDeclaringImageIds() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mDefinePath + ".h");
		String replaceContent = this.declareImageIds();
		
		//delete old source code
		String beginStr = "// " + mClassName + " " 
				+ Constants.IMAGEIDS_DECLARING_CMM;
		content = fileUtils.deleteSourceCode(content,
				beginStr, Constants.DONT_DELETE_THIS_LINE);
		
		//append content
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		
		fileUtils.setContent(content)
			.writeToFile(mDefinePath + ".h", false);
	}
	
	public void exportImplementedImageIds() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mDefinePath + ".cpp");
		String replaceContent = this.implementImageIds();
		
		//delete old source code
		String beginStr = "// " + mClassName + " " 
				+ Constants.IMAGEIDS_IMPLEMENTING_CMM;
		content = fileUtils.deleteSourceCode(content,
				beginStr, Constants.DONT_DELETE_THIS_LINE);
		
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mDefinePath + ".cpp", false);
	}
	
	public void exportDeclaringPositions() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mParametersPath + ".h");
		String replaceContent = this.declarePositions();
		
		//delete old source code
		String beginStr = "// " + mClassName + " " 
				+ Constants.POSITION_DECLARING_CMM;
		content = fileUtils.deleteSourceCode(content,
				beginStr, Constants.DONT_DELETE_THIS_LINE);
		
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mParametersPath + ".h", false);
	}
	
	public void exportImplementedPositions() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(
				mParametersPath + ".cpp");
		String replaceContent = this.implementPositions();
		
		//delete old source code
		String beginStr = "// " + mClassName + " " 
				+ Constants.IMAGEIDS_IMPLEMENTING_CMM;
		content = fileUtils.deleteSourceCode(content,
				beginStr, Constants.DONT_DELETE_THIS_LINE);
		
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mParametersPath + ".cpp", false);
	}
	
	public void exportImplementedPositions(String device) {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(
				mParametersPath + ".cpp");
		String replaceContent = this.implementPositions(device);
		
		//delete old source code
		String beginStr = "// " + mClassName + " " 
				+ Constants.POSITION_IMPLEMENTING_CMM + "(" + device + ")";
		content = fileUtils.deleteSourceCode(content,
				beginStr, Constants.DONT_DELETE_THIS_LINE + "(" + device + ")");
		
		content = content.replace(Constants.DONT_DELETE_THIS_LINE + "(" + device + ")", 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mParametersPath + ".cpp", false);
	}
	
	public void exportHeaderCode() {
		String content = declare();
		new FileUtils().setContent(content)
			.writeToFile(mClassPath + "/"
				+ mDirectoryName + "/" + mClassName + ".h", false);
				
	}
	
	public void exportImplementedCode() {
		String content = implement(false);
		new FileUtils().setContent(content)
			.writeToFile(mClassPath + "/"
				+ mDirectoryName + "/" + mClassName + ".cpp", false);
	}
	
	public void refreshXMLFile() throws CoreException {
		System.out.println("Interface Builder = " + mInterfaceBuilder);
		
		//if use XCode - Interface Builder
		if(mInterfaceBuilder.equals(Constants.XIB)) {
			final String xibFilePath = mXibContainerPath + "/" 
					+ "/" + mClassName + ".xib";
			
			//check exist
			File file = new File(xibFilePath);
			if(!file.exists()) {
				return; //if not exist
			}
			
			this.setImagesIdsForXib();
			this.setLabelsIdForXib();
			
			XibFetcher xibFetcher = new XibFetcher(mImages, mLabels);
			xibFetcher.fetchData(xibFilePath);
			FileUtils fileUtils = new FileUtils();
			
//			String fileContent = fileUtils.readFromFile(mXmlFilePath);
			String fileContent = fileUtils.readFromFile(mXmlFile);
			for(int i = 0 ; i < mImages.size() ; i++) {
				mImages.get(i).alignFollowParrent();
				fileContent = XmlContentUtils.replaceSpritePosition(
					fileContent, mImages.get(i));
			}
			for(int i = 0 ; i < mLabels.size() ; i++) {
				System.out.println("label text = " + mLabels.get(i).getText());
				mLabels.get(i).alignFollowParrent();
				fileContent = XmlContentUtils.replaceSpritePosition(
						fileContent, mLabels.get(i));
			}
			
//			fileUtils.setContent(fileContent).writeToFile(mXmlFilePath, false);
			ByteArrayInputStream inputStream = 
	 				new ByteArrayInputStream(fileContent.getBytes());
	 		mXmlFile.setContents(inputStream, true, false, null);
		}
	}
	
	private void exportDirectory() {
		new FileUtils().createDirectory(mClassPath, 
				mDirectoryName);
	}
	
	private String createImageViewsTag() {
		StringBuilder builder = new StringBuilder("\n");
		setImagesIdsForXib();
		for(int i = 0 ; i < mImages.size() ; i++) {
			try {
				builder.append(mImages.get(i).createImageViewTag(mImagesPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
	}
	
	private void setImagesIdsForXib() {
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath() != null && 
					mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			String id = "img-png-" + StringUtils.generateString(i);
			mImages.get(i).setImageViewId(id);
		}
	}
	
	private String createImagesTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath() != null 
					&& mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			builder.append(mImages.get(i).createImageTag());
		}
		
		return builder.toString();
	}
	
	private String createWidgetsTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			String id = (i + 1) + "";
			try {
				builder.append(mImages.get(i).createWidgetsTag(id, mImagesPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		
	}
	
	public void setDefinePath(String mDefinePath) {
		this.mDefinePath = mDefinePath;
	}

	public void setParametersPath(String mParametersPath) {
		this.mParametersPath = mParametersPath;
	}

	public void setImagesInputPath(String mImagesInputPath) {
		this.mImagesInputPath = mImagesInputPath;
	}

	public void setImagesPath(String mImagesPath) {
		this.mImagesPath = mImagesPath;
	}

	public void setXibContainerPath(String mXibContainerPath) {
		this.mXibContainerPath = mXibContainerPath;
	}

	public void setClassPath(String mClassPath) {
		this.mClassPath = mClassPath;
	}

	public void setDirectoryName(String mDirectoryName) {
		this.mDirectoryName = mDirectoryName;
	}
	
	public void setXmlFilePath(String pXmlFilePath) {
		this.mXmlFilePath = pXmlFilePath;
	}
	
	public String getScreenContainerPath() {
		return this.mScreenContainerPath;
	}
	
	public List<Image> getImages() {
		return this.mImages;
	}
	
	public void setScreenContainerPath(String pScreenContainerPath) {
		this.mScreenContainerPath = pScreenContainerPath;
	}
	
	public void addResource(Resources pResources) {
		this.mResources.add(pResources);
	}
	
	public String getXibContainerPath() {
		return mXibContainerPath;
	}
	
	public void setAndroidContainerPath(String path) {
		this.mAndroidContainerPath = path;
	}
	
	public String getAndroidContainerPath() {
		return this.mAndroidContainerPath;
	}
	
	public void setInterfaceBuilder(String ib) {
		this.mInterfaceBuilder = ib;
	}
	
	public String getInterfaceBuilder() {
		return mInterfaceBuilder;
	}
	
	//for label management
	private void setLabelsIdForXib() {
		for(int i = 0 ; i < mLabels.size() ; i++) {
			mLabels.get(i).setLabelViewId(
					"TVD-LB-" + StringUtils.generateString(i));
		}
 	}
	public String createLabelsTagForXib() {
		setLabelsIdForXib();
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mLabels.size() ; i++) {
			builder.append(mLabels.get(i).createLabelTagForXib())
				.append("\n");
		}
		
		return builder.toString();
	}
	public void addLabel(Label label) {
		this.mLabels.add(label);
	}
	
	public List<Label> getLabels() {
		return this.mLabels;
	}
	
	public void setXmlFile(IFile xmlFile) {
		this.mXmlFile = xmlFile;
	}
	
	public IFile getXmlFile() {
		return this.mXmlFile;
	}
	
	private IFile mXmlFile;
	private String mDefinePath;
	private String mParametersPath;
	private String mImagesInputPath;
	private String mImagesPath;
	private String mXibContainerPath;
	private String mClassPath;
	private String mAndroidContainerPath;
	
	private String mInterfaceBuilder;
	
	private String mDirectoryName;
	
	@SuppressWarnings("unused")
	private String mXmlFilePath;
	
	private Image mBackgroundImage;
	private List<Image> mImages;
	private List<Label> mLabels;
	private List<Resources> mResources;
	private String mScreenContainerPath;
	
}