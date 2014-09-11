package com.tdgc.cocos2dx.popup.creator.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.constants.Strings;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.file.ImageFileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.xml.XibFetcher;

public class View extends AdvancedObject implements IContainer {

	public View() {
		super();
		this.mViewType = Constants.ViewType.VIEW;
		this.mSuffix = "popup";
		this.mType = "popup";
		this.mImages = new ArrayList<Image>();
		this.mScreenContainerPath = 
				Config.getInstance().getScreenContainerPath();
		this.mLabels = new ArrayList<Label>();
		this.mPrefix = "";
		this.mXmlTagName = Tag.VIEW;
		this.mPositionTemplateName = 
				Config.getInstance().getDefaultTemplateName("position");
		this.mPositionTemplateFile = "point.template";
		this.mSizeTemplateName = 
				Config.getInstance().getDefaultTemplateName("size");
		this.mSizeTemplateFile = "size.template";
		this.mLocationInView = new Point(0, 0);
		this.mIsExitable = true;
	}
	
	@Override
	public String declare() {
		String srcCode = super.declare();
		if(mBackgroundImage != null 
				&& mBackgroundImage.getId() != null) {
			srcCode = srcCode.replace("{background_id}", mBackgroundImage.getId());
		}
		
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			srcCode = srcCode.replace("{super_table_name}", 
					mTableGroupInView.get(0).getItems().get(0).getSuper());
			srcCode = srcCode.replace("{super_cell_name}", cell.getSuper());
		}
		String exitNormalImgId = Strings.DEFAULT_IMAGE_ID;
		String exitActiveImgId = Strings.DEFAULT_IMAGE_ID;
		String exitPositionName = Strings.DEFAULT_POSIONTION_NAME;
		if(mIsExitable 
				&& mExitResource != null
				&& mExitResource.getImages().size() > 1
				&& mExitSprite != null) {
			exitNormalImgId = mExitResource.getImage(0).getId();
			exitActiveImgId = mExitResource.getImage(1).getId();
			exitPositionName = mExitSprite.getPositionName();
		}
		srcCode = srcCode.replace("{exit_normal_id}", exitNormalImgId)
				.replace("{exit_active_id}", exitActiveImgId)
				.replace("{exit_position_name}", exitPositionName);
		
		return srcCode;
	}
	
	@Override
	public String implement(boolean pInfunction) {
		String srcCode = super.implement(pInfunction);
		
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			srcCode = srcCode.replace("//{extend_class}", 
					cell.declareAndImplement())
					.replace("{super_cell_name}", cell.getSuper())
					.replace("{cell_class_name}", cell.getClassName()); 
		}
		String exitNormalImgId = Strings.DEFAULT_IMAGE_ID;
		String exitActiveImgId = Strings.DEFAULT_IMAGE_ID;
		String exitPositionName = Strings.DEFAULT_POSIONTION_NAME;
		if(mIsExitable 
				&& mExitResource != null
				&& mExitResource.getImages().size() > 1
				&& mExitSprite != null) {
			exitNormalImgId = mExitResource.getImage(0).getId();
			exitActiveImgId = mExitResource.getImage(1).getId();
			exitPositionName = mExitSprite.getPositionName();
		}
		srcCode = srcCode.replace("{exit_normal_id}", exitNormalImgId)
				.replace("{exit_active_id}", exitActiveImgId)
				.replace("{exit_position_name}", exitPositionName);
		
		return srcCode;
	}
	
	@Override
	public String declarePositions() {
		
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, Constants.POSITION_DECLARING_CMM);
		builder.append(super.declarePositions());
		if(mExitSprite != null) {
			builder.append("\t" + mExitSprite.declarePositions());
		}
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
				.getItems().get(0))).getCell();
			builder.append(cell.declarePositions());
		}
		
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		return StringUtils.standardizeCode(
				builder.toString()).trim();
	}
	
	@Override
	public String implementPositions() {
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, Constants.POSITION_IMPLEMENTING_CMM);
		builder.append(super.implementPositions());
		if(mExitSprite != null) {
			builder.append("\t" + mExitSprite.implementPositions());
		}
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			builder.append(cell.implementPositions());
		}
			
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return StringUtils.standardizeCode(
				builder.toString()).trim();
	}
	
	public String implementPositions(String device) {
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, 
				Constants.POSITION_IMPLEMENTING_CMM + "(" + device + ")");
		builder.append(super.implementPositions());
		if(mExitSprite != null) {
			builder.append("\t" + mExitSprite.implementPositions());
		}
		if(mTableGroupInView.size() > 0) {
			Cell cell = ((Table)(mTableGroupInView.get(0)
					.getItems().get(0))).getCell();
			builder.append(cell.implementPositions());
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE + "(" + device + ")");
		
		return StringUtils.standardizeCode(
				builder.toString().trim());
	}
	
	public String declareImageIds() {
		StringBuilder builder = new StringBuilder();
		createHeaderCommentTemplate(builder, Constants.IMAGEIDS_DECLARING_CMM);
		String template = new FileUtils().fetchTemplate(
				"Image identifiers declaring", 
				"src/com/template/id.template", getProject()).trim();
		List<Image> images = new ArrayList<Image>(mImages);
		if(mResource != null) {
			images.addAll(mResource.getImages());
		}
		for(int i = 0 ; i < images.size(); i++) {
			if(images.get(i) == null || images.get(i).isExists()) {
				continue;
			}
			builder.append(template.replace("{id}", images.get(i).getId().trim())
						.replace("{tab}", "\t"))
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
		
		String template = new FileUtils().fetchTemplate(
				"Image identifiers implementing", 
				"src/com/template/id.template", getProject()).trim();
		List<Image> images = new ArrayList<Image>(mImages);
		if(mResource != null) {
			images.addAll(mResource.getImages());
		}
		for(int i = 0 ; i < images.size(); i++) {
			if(images.get(i) == null || images.get(i).isExists()
					|| images.get(i).getRealPath() == null) {
				continue;
			}
			builder.append(template.replace("{id}", images.get(i).getId())
						.replace("{value}", images.get(i).getRealPath())
						.replace("{tab}", "\t"))
					.append("\n");
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return builder.toString().trim();
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
			
//			String xibContent = fileUtils.readFromFile(path);
			String xibContent = fileUtils.readFromFile(file);
			
			StringBuilder builder = new StringBuilder();
			builder.append(createImageViewsTag())
				.append("\n")
				.append(createLabelsTagForXib());
			xibContent = xibContent.replace("<!--{imageviews_tag}-->", builder.toString())
				.replace("<!--{images_tag}-->", createImagesTag());
			
			final String xibFilePath = mXibContainerPath + "/" 
					+ "/" + mClassName + ".xib";
//			System.out.println(xibContent);
			
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
		pImage.setResource(false);
		this.mImages.add(pImage);
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
	
//	public void refreshXMLFile() {
	public void refreshXMLFile() throws CoreException {
		System.out.println("Interface Builder = " + mInterfaceBuilder);
		
		//if use XCode - Interface Builder
		if(mInterfaceBuilder.equals(Constants.XIB)) {
			final String xibFilePath = mXibContainerPath + "/" 
					+ "/" + mClassName + ".xib";
			
			//check exist
			File file = new File(xibFilePath);
			if(!file.exists()) {
				System.err.println("ERROR::refreshXMLFile " + file + " not exists");
				return; //if not exist
			}
			
			this.setImagesIdsForXib();
			this.setLabelsIdForXib();
			
			XibFetcher xibFetcher = new XibFetcher(mImages, mLabels);
			xibFetcher.fetchData(xibFilePath);
//			FileUtils fileUtils = new FileUtils();
			
//			String fileContent = fileUtils.readFromFile(mXmlFile);
			for(int i = 0 ; i < mImages.size() ; i++) {
				mImages.get(i).alignFollowParrent();
			}
			for(int i = 0 ; i < mLabels.size() ; i++) {
				mLabels.get(i).alignFollowParrent();
			}
			String xmlContent = this.toXML();
			
//			fileUtils.setContent(xmlContent).writeToFile(mXmlFilePath, false);
			ByteArrayInputStream inputStream = 
	 				new ByteArrayInputStream(xmlContent.getBytes());
	 		mXmlFile.setContents(inputStream, true, false, null);
		}
	}
	
	private String createImageViewsTag() {
		StringBuilder builder = new StringBuilder("\n");
		setImagesIdsForXib();
		for(int i = 0 ; i < mImages.size() ; i++) {
			try {
				if(mImages.get(i) == null || !mImages.get(i).isAddToInterfaceBuilder()) {
					System.err.println("ERROR::setImagesIdsForXib something is wrong! " 
							+ mImages.get(i));
					continue;
				}
				builder.append(mImages.get(i).createImageViewTag(mImagesPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
	}
	
	private void setImagesIdsForXib() {
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i) == null || !mImages.get(i).isAddToInterfaceBuilder()) {
				System.err.println("ERROR::setImagesIdsForXib something is wrong! " 
						+ mImages.get(i));
				continue;
			}
			String id = "img-png-" + StringUtils.generateString(i);
			mImages.get(i).setImageViewId(id);
		}
	}
	
	private String createImagesTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i) == null || !mImages.get(i).isAddToInterfaceBuilder()) {
				System.err.println("ERROR::setImagesIdsForXib something is wrong! " 
						+ mImages.get(i));
				continue;
			}
			builder.append(mImages.get(i).createImageTag());
		}
		
		return builder.toString();
	}
	
	private String createWidgetsTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i) == null || !mImages.get(i).isAddToInterfaceBuilder()) {
				System.err.println("ERROR::setImagesIdsForXib something is wrong! " 
						+ mImages.get(i));
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
	public String getName() {
		if(mBackgroundName == null || mBackgroundName.equals(Strings.DEFAULT)) {
			mBackgroundName = Config.getInstance().getDefaultBackgroundOnSupers(mType);
		}
		return mBackgroundName;
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		super.setPositionName(pPositionName);
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
	
	public void addResource(Resource pResource) {
		pResource.setTabCount(getTabCount() + 1);
		this.mResource = pResource;
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
	
	public void setBackgroundImage(Image img) {
		img.setParent(this);
		if(mBackgroundImage == null) {
			mBackgroundImage = img;
			mImages.add(mBackgroundImage);
		} else {
			mBackgroundImage.replaceWithAnother(img);
		}
	}
	
	@Override
	public void setType(String type) {
		super.setType(type);
		if(type != null) {
			Config config = Config.getInstance();
			this.mSuper = config.getDefautSuper(mType);
			this.mBackgroundImage = config.getDefaultBackgroundImage(
					type);
			this.mDefaultBackgroundImage = mBackgroundImage;
			if(mBackgroundImage != null) {
				this.mBackgroundImage.setIsBackground(true);
				this.mBackgroundImage.setExists(true);
				this.mBackgroundImage.setParent(this);
				this.mImages.add(mBackgroundImage);
			}
			this.mExitResource = config.getDefaultExitResource(
					type);
			if(mExitResource != null 
					&& mExitResource.getImages().size() > 0
					&& mExitResource.getImage(0) != null 
					&& mIsExitable) {
				Image exitImage = mExitResource.getImage(0);
				exitImage.setExists(true);
				mExitSprite = new Sprite(exitImage);
				mExitSprite.setParent(this);
				mExitSprite.setPositionName(mPrefix, 
						mExitResource.getImage(0).getId());
				this.mImages.add(exitImage);
			} else {
				mExitResource = null;
			}
		}
	}
	
	public void setExitable(boolean exitable) {
		this.mIsExitable = exitable;
	}
	
	public void setExitImageLocationInView(String location) {
		if(location != null && mExitResource != null) {
			mExitResource.getImage(0)
				.setLocationInInterfaceBuilder(Point.parsePoint(location));
		}
	}
	
	public void setExitSpritePosition(String position) {
		if(position != null && mExitSprite != null) {
			Point p = Point.parsePoint(position);
			mExitSprite.setPosition(p);
		}
	}
	
	public void setXmlFile(IFile xmlFile) {
		this.mXmlFile = xmlFile;
	}
	
	public IFile getXmlFile() {
		return this.mXmlFile;
	}
	
	@Override
	public void setContainerParent(IContainer parent) {}

	@Override
	public IContainer getContainerParent() {
		return null;
	}
	
	@Override
	public void update() {
		if(mExitResource == null) {
			mIsExitable = false;
		}
		if(mBackgroundImage == mDefaultBackgroundImage) {
			mBackgroundImage.setExists(true);
		}
		mBackgroundImage.setXMLTagName(Tag.BACKGROUND_IMAGE);
		Collections.sort(getImages());
	}
	
	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" " +
				"encoding=\"UTF-8\" standalone=\"no\"?>\n");
		
		//append class-name attribute
		builder.append("<view " + Attribute.CLASS_NAME + "=\"" + mClassName + "\" ")
			.append(Attribute.PREFIX + "=\"" + mPrefix + "\" ")
			.append(Attribute.TYPE + "=\"" + mType + "\" ")
			.append(Attribute.EXITABLE + "=\"" + mIsExitable + "\" ")
			.append("\n\t\t" + Attribute.SUPER + "=\"" + mSuper + "\" ")
			.append(Attribute.BACKGROUND_NAME + "=\"" + mBackgroundName + "\" ")
			.append(Attribute.SIZE + "=\"" + mSize + "\" ")
			.append("\n\t\t" + Attribute.TEMPLATE_NAME + "=\"" + mTemplateName + "\"")
			.append("\n\t\t" + Attribute.COMMENT + "=\"" + mComment + "\"")
			.append("\n\t\txmlns=\"http://www.tvd.com/tools\"")
			.append("\n\t\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")
			.append("\n\t\txsi:schemaLocation=\"http://www.tvd.com/tools ../xsd/view_2_0.xsd\">")
			.append("\n");
		//position
		builder.append("\t<" + Tag.POSITION_NAME + " " + Attribute.VALUE + "=\"")
			.append(mXmlPositionName + "\" />\n");
		builder.append("\t<" + Tag.POSITION + " " + Attribute.VALUE + "=\"default\" />\n");
		
		//define group
		builder.append("\t<" + Tag.DEFINE_PATH + " " + Attribute.VALUE + "=\"")
			.append(mDefinePath + "\" />\n");
		builder.append("\t<" + Tag.PARAMETERS_PATH + " " + Attribute.VALUE + "=\"")
			.append(mParametersPath + "\" />\n");
		
		//image group
		builder.append("\t<" + Tag.IMAGES_INPUTPATH + " " + Attribute.VALUE + "=\"")
			.append(mImagesInputPath + "\" />\n");
		builder.append("\t<" + Tag.IMAGES_PATH + " " + Attribute.VALUE + "=\"")
			.append(mImagesPath + "\" />\n");
		
		//screen container group
		builder.append("\t<" + Tag.XIBCONTAINER_PATH + " " + Attribute.VALUE + "=\"")
			.append(mXibContainerPath + "\" ");
		if(mInterfaceBuilder.equals(Constants.XIB)) {
			builder.append(Attribute.USED + "=\"true" + "\" ");
		}
		builder.append("/>\n");
		builder.append("\t<" + Tag.SCREENCONTAINER_PATH + " " + Attribute.VALUE + "=\"")
			.append(mScreenContainerPath + "\" ");
		if(mInterfaceBuilder.equals(Constants.SCREEN)) {
			builder.append(Attribute.USED + "=\"true" + "\" ");
		}
		builder.append("/>\n");
		builder.append("\t<" + Tag.ANDROIDCONTAINER_PATH + " " + Attribute.VALUE + "=\"")
			.append(mAndroidContainerPath + "\" ");
		if(mInterfaceBuilder.equals(Constants.ANDROID)) {
			builder.append(Attribute.USED + "=\"true" + "\" ");
		}
		builder.append("/>\n");
		
		//class group
		builder.append("\t<" + Tag.CLASS_PATH + " " + Attribute.VALUE + "=\"")
			.append(mClassPath + "\" />\n");
		if(mBackgroundImage != null) {
			builder.append(mBackgroundImage.toXML() + "\n");
		}
		if(mIsExitable) {
			Image exitImg = mExitResource.getImage(0);
			builder.append("\t<" + Tag.EXIT_IMAGE + " " + Attribute.POSITION + "=\"")
			.append(exitImg.getPosition() + "\" ")
			.append(Attribute.LOCATION_IN_INTERFACEBUILDER + "=\"")
			.append(exitImg.getLocationInInterfaceBuilder() + "\" />\n");
		}
		
		builder.append("\n")
		.append(super.toXML());
	
		if(mResource != null) {
			builder.append(mResource.toXML())
				.append("\n");
		}
		builder.append("</" + mXmlTagName + ">");
	
		builder.append("\n");
		
		return builder.toString();
	}
	
	private IFile mXmlFile;
	private String mDefinePath;
	private String mParametersPath;
	private String mImagesInputPath;
	private String mImagesPath;
	private String mXibContainerPath;
	private String mAndroidContainerPath;
	private String mScreenContainerPath;
	private String mInterfaceBuilder;
	
	@SuppressWarnings("unused")
	private String mXmlFilePath;
	
	private Image mBackgroundImage;
	private Image mDefaultBackgroundImage;
	private List<Image> mImages;
	private List<Label> mLabels;
	private Resource mResource;
	private Resource mExitResource;
	private Sprite mExitSprite;
	
	private boolean mIsExitable;
}