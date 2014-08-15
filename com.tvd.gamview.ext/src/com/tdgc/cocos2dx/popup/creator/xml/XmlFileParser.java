package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.Collections; 

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.log.Log;
import com.tdgc.cocos2dx.popup.creator.model.Image;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.model.Label;
import com.tdgc.cocos2dx.popup.creator.model.Menu;
import com.tdgc.cocos2dx.popup.creator.model.MenuItem;
import com.tdgc.cocos2dx.popup.creator.model.Resources;
import com.tdgc.cocos2dx.popup.creator.model.Sprite;
import com.tdgc.cocos2dx.popup.creator.model.Table;
import com.tdgc.cocos2dx.popup.creator.model.View;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Parameter;

public class XmlFileParser extends DefaultHandler {
	
	@Override
	public void startDocument() throws SAXException {
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		try {
		if(qName.equals(Tag.VIEW)) {
			mView = new View();
			mView.setClassName(getAttributeValue(Attribute.CLASS_NAME, atts));
			mView.setPrefix(getAttributeValue(Attribute.PREFIX, atts));
			Config.getInstance().setPrefix(mView.getPrefix());
			mView.setSuper(getAttributeValue(Attribute.SUPER, atts));
			mView.setSuffix(getAttributeValue(Attribute.TYPE, atts));
			mView.setType(getAttributeValue(Attribute.TYPE, atts));
			mView.setComment(getAttributeValue(Attribute.COMMENT, atts));
			mCurrentObject = mView;
		} else if(qName.equals(Tag.SPRITE)
				|| qName.equals(Tag.TABLE)
				|| qName.equals(Tag.MENU)
				|| qName.equals(Tag.MENUITEM)
				|| qName.equals(Tag.LABEL)
				|| qName.equals(Tag.RESOURCES)) {
			CommonObject parent = mCurrentObject;
			if(qName.equals(Tag.SPRITE)) {
				mCurrentObject = new Sprite();
				mCurrentObject.setIsBackground(
						getBoolean(getAttributeValue(Attribute.BACKGROUND, atts)));
				mCurrentObject.setComment(
						getAttributeValue(Attribute.COMMENT, atts));
			}
			else if(qName.equals(Tag.TABLE)) {
				mCurrentObject = new Table();
				Table table = (Table)mCurrentObject;
				table.setRows(getNumber(getAttributeValue(Attribute.ROWS, atts)));
				table.setColumns(getNumber(getAttributeValue(Attribute.COLUMNS, atts)));
				table.setSize(getAttributeValue(Attribute.SIZE, atts));
			}
			else if(qName.equals(Tag.MENU)) {
				mCurrentObject = new Menu();
			}
			else if(qName.equals(Tag.MENUITEM)) {
				mCurrentObject = new MenuItem();
			}
			else if(qName.equals(Tag.LABEL)) {
				Label label = new Label();
				mView.addLabel(label);
				mCurrentObject = label;
			} 
			else if(qName.equals(Tag.RESOURCES)) {
				mCurrentObject = new Resources();
				mCurrentResources = (Resources)mCurrentObject;
				mCurrentResources.setName(getAttributeValue(Attribute.NAME, atts));
				mCurrentResources.setComment(getAttributeValue(Attribute.COMMENT, atts));
				mCurrentResources.setCurrentGroup(mCurrentGroup);
				mView.addResource(mCurrentResources);
			}
			mCurrentObject.setParent(parent);
		}
		
		else if(qName.equals(Tag.FONT_SIZE)) {
			Label label = (Label)mCurrentObject;
			label.setFontSizeString(getAttributeValue(Attribute.S_VALUE, atts));
		}
		
		else if(qName.equals(Tag.IMAGE)) {
			mCurrentImage = new Image();
			mCurrentImage.setId(getAttributeValue(Attribute.ID, atts));
			mCurrentImage.setRealPath(getAttributeValue(Attribute.REAL_PATH, atts));
			mCurrentImage.setIsBackground(
					getBoolean(getAttributeValue(Attribute.BACKGROUND, atts)));
			mCurrentImage.setParent(mCurrentObject);
		}
		else if(qName.equals(Tag.SPRITES)
				|| qName.equals(Tag.TABLES)
				|| qName.equals(Tag.MENUS)
				|| qName.equals(Tag.MENUITEMS)
				|| qName.equals(Tag.LABELS)) {
			ItemGroup beforGroup = mCurrentGroup;
			if(qName.equals(Tag.TABLES)) {
				mCurrentGroup = new ItemGroup(ItemGroup.Type.TABLE);
			}
			else if(qName.equals(Tag.SPRITES)) {
				mCurrentGroup = new ItemGroup(ItemGroup.Type.SPRITE);
			} 
			else if(qName.equals(Tag.MENUS)) {
				mCurrentGroup = new ItemGroup(ItemGroup.Type.MENU);
			}
			else if(qName.equals(Tag.MENUITEMS)) {
				mCurrentGroup = new ItemGroup(ItemGroup.Type.MENUITEM);
			} 
			else if(qName.equals(Tag.LABELS)) {
				mCurrentGroup = new ItemGroup(ItemGroup.Type.LABLE);
			}
			mCurrentGroup.setIsArray(getBoolean(
					getAttributeValue(Attribute.ARRAY, atts)));
			mCurrentGroup.setContainer(mCurrentObject);
			mCurrentGroup.setBlockComment(
					getAttributeValue(Attribute.COMMENT, atts));
			mCurrentGroup.setBeforeGroup(beforGroup);
		} 
		else if(qName.equals(Tag.POSITION_NAME)) {
			mCurrentObject.setIsUnlocated(
					getBoolean(getAttributeValue(Attribute.UNLOCATED, atts)));
		}
		else if(qName.equals(Tag.PARAMETER)) {
			mCurrentParameter = new Parameter();
			mCurrentParameter.setType(getAttributeValue(Attribute.TYPE, atts));
			mCurrentParameter.setKind(getAttributeValue(Attribute.KIND, atts));
		}
		else if(qName.equals(Tag.XIBCONTAINER_PATH)) {
			boolean used = getBoolean(getAttributeValue(Attribute.USED, atts));
			if(used) {
				mView.setInterfaceBuilder(Constants.XIB);
			}
		}
		else if(qName.equals(Tag.SCREENCONTAINER_PATH)) {
			boolean used = getBoolean(getAttributeValue(Attribute.USED, atts));
			if(used) {
				mView.setInterfaceBuilder(Constants.SCREEN);
			}
		}
		else if(qName.equals(Tag.ANDROIDCONTAINER_PATH)) {
			boolean used = getBoolean(getAttributeValue(Attribute.USED, atts));
			if(used) {
				mView.setInterfaceBuilder(Constants.ANDROID);
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
		
	@Override
	public void endDocument() throws SAXException {
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, 
			String qName) throws SAXException {
		try {
		if(qName.equals(Tag.DEFINE_PATH)) {
			mView.setDefinePath(mCurrentText);
		}
		else if(qName.equals(Tag.PARAMETERS_PATH)) {
			mView.setParametersPath(mCurrentText);
		}
		else if(qName.equals(Tag.IMAGES_INPUTPATH)) {
			mView.setImagesInputPath(mCurrentText);
		}
		else if(qName.equals(Tag.IMAGES_PATH)) {
			mView.setImagesPath(mCurrentText);
		}
		else if(qName.equals(Tag.XIBCONTAINER_PATH)) {
			mView.setXibContainerPath(mCurrentText);
		}
		else if(qName.equals(Tag.SCREENCONTAINER_PATH)) {
			mView.setScreenContainerPath(mCurrentText);
		}
		else if(qName.equals(Tag.ANDROIDCONTAINER_PATH)) {
			mView.setAndroidContainerPath(mCurrentText);
		}
		else if(qName.equals(Tag.CLASS_PATH)) {
			mView.setClassPath(mCurrentText);
		}
		else if(qName.equals(Tag.IMAGE)) {
			mCurrentImage.setPhonyPath(mCurrentText);
			mView.addImage(mCurrentImage);
			
			if(mCurrentObject instanceof Sprite) {
				((Sprite)mCurrentObject).setImage(mCurrentImage);
			}
		} 
		else if(qName.equals(Tag.POSITION_NAME)) {
			mCurrentObject.setPositionName(mCurrentText);
		} 
		else if(qName.equals(Tag.ANCHORPOINT)) {
			mCurrentObject.setAnchorPoint(mCurrentText);
		} 
		else if(qName.equals(Tag.POSITION)) {
			mCurrentObject.setPosition(mCurrentText);
		} 
		else if(qName.equals(Tag.TEXT)) {
			((Label)mCurrentObject).setText(mCurrentText);
		}
		else if(qName.equals(Tag.FONT)) {
			((Label)mCurrentObject).setFont(mCurrentText);
		}
		else if(qName.equals(Tag.FONT_SIZE)) {
			((Label)mCurrentObject).setFontSizeFloat(mCurrentText);
		}
		else if(qName.equals(Tag.Z_INDEX)) {
			mCurrentObject.setZIndex(mCurrentText);
		}
		else if(qName.equals(Tag.PARAMETER)) {
			mCurrentParameter.setName(mCurrentText);
			mView.addParameter(mCurrentParameter);
		}
		else if(qName.equals(Tag.SPRITES)
				|| qName.equals(Tag.TABLES)
				|| qName.equals(Tag.MENUS)
				|| qName.equals(Tag.MENUITEMS)
				|| qName.equals(Tag.LABELS)) {
			mCurrentGroup.pushBack();
			if(qName.equals(Tag.SPRITES)) {
				mView.pushBackSpriteGroup(mCurrentGroup);
			} 
			else if(qName.equals(Tag.TABLES)) {
				mView.pushBackTableGroup(mCurrentGroup);
			} 
			else if(qName.equals(Tag.MENUS)) {
				mView.pushBackMenuGroup(mCurrentGroup);
			} 
			else if(qName.equals(Tag.MENUITEMS)) {
				mView.pushBackMenuItemGroup(mCurrentGroup);
			} 
			else if(qName.equals(Tag.LABELS)) {
				mView.pushBackLabelGroup(mCurrentGroup);
			}
			mCurrentGroup = mCurrentGroup.getBeforeGroup();
		}
		else if(qName.equals(Tag.SPRITE)
				|| qName.equals(Tag.TABLE)
				|| qName.equals(Tag.MENUITEM)
				|| qName.equals(Tag.MENU)
				|| qName.equals(Tag.LABEL)
				|| qName.equals(Tag.RESOURCES)) {
			boolean valid = true;
			if(qName.equals(Tag.SPRITE)) {
				Sprite sprite = (Sprite)mCurrentObject;
				if(sprite.getImage() != null
						&& sprite.getParent() instanceof Sprite) {
					Sprite parentSprite = ((Sprite)sprite.getParent());
					if(parentSprite.getImage() == null) {
						parentSprite.setImage(sprite.getImage());
						valid = false;
					}
				}
			}
			else if(qName.equals(Tag.MENUITEM)) {
				mView.addMenuItemTag(((MenuItem)mCurrentObject).getTagName());
			} 
			if(!qName.equals(Tag.RESOURCES) && valid) {
				mCurrentGroup.addItem(mCurrentObject);
			}
			mCurrentObject = mCurrentGroup.getContainer();
		} 
		else if(qName.equals(Tag.VIEW)) {
			Collections.sort(mView.getImages());
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
		String text = new String(ch, start, length).trim();
		if(text.length() == 0) {
			return;
		}
		mCurrentText = text.trim();
	}
	
	private String getAttributeValue(String attName, Attributes atts) {
		String result = null;
		for(int i = 0 ; i < atts.getLength() ; i++) {
			String thisAtt = atts.getQName(i);
			if(attName.equals(thisAtt)) {
				result = atts.getValue(i);
				return result.trim();
			}
		}
		return result;
	}
	
	private int getNumber(String value) {
		int result = 0;
		try {
			result = Integer.parseInt(value);
		} catch(NumberFormatException e) {
			Log.e(e);
		} catch (NullPointerException e) {
			Log.e(e);
		}
		return result;
		
	}
	
	private boolean getBoolean(String pValue) {
		boolean result = false;
		try {
			result = Boolean.parseBoolean(pValue);
		} catch(NullPointerException e) {
			Log.e(e);
		} catch (NumberFormatException e) {
			Log.e(e);
		}
		
		return result;
	}
	
	public View getView() {
		return this.mView;
	}
	
	private Resources mCurrentResources;
	private CommonObject mCurrentObject;
	private ItemGroup mCurrentGroup;
//	private ItemGroup mResourceGroup;
	private String mCurrentText;
    private View mView;
    private Image mCurrentImage;
    private Parameter mCurrentParameter;
}
