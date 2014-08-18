package com.tdgc.cocos2dx.popup.creator.xml;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XmlFileBuilder {
	
	public XmlFileBuilder(String pImagesPath) {
		this.mImagesPath = pImagesPath;
	}
	
	public XmlFileBuilder(String pImagePath, String pInterfaceBuilder) {
		this(pImagePath);
		this.mInterfaceBuilder = pInterfaceBuilder;
	}
	
	public String buildFor(String pDevice) {
		mXmlCreator = new XmlCreator(this.mImagesPath, pDevice,
				mInterfaceBuilder);
		Document doc = mXmlCreator.parseFilePaths();
		
		this.mFileOutputPath = "resources/xml/" + pDevice 
				+ "/" + mXmlCreator.getOutputFileName();
		this.mFileOutputName = mXmlCreator.getOutputFileName();
		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", 4);
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
				 
			// Output to console for testing
//			StreamResult result = null;
//			result = new StreamResult(new FileWriter(
//					new File(this.mFileOutputPath)));
			transformer.transform(source, result);
			
			return writer.getBuffer().toString();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getOutputFilePath() {
		return this.mFileOutputPath;
	}
	
	public String getOutputFileName() {
		return this.mFileOutputName;
	}
	
	private XmlCreator mXmlCreator;
	private String mFileOutputPath;
	private String mImagesPath;
	private String mFileOutputName;
	private String mInterfaceBuilder;
	
	
	public static void main(String[] args) {
		String inputPath = "/Users/apple/Downloads";
		String inputDirectory ="cza_avatars_popup";
		XmlFileBuilder builder = new XmlFileBuilder(inputPath + "/" + inputDirectory, "Xib");
		System.out.println(builder.buildFor("demohd"));
	}
}
