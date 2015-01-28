/**
 * Copyright (c) 2014 Dung Ta Van . All rights reserved.
 * 
 * This file is part of com.tvd.gameview.ext.
 * com.tvd.gameview.ext is free eclipse plug-in: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * com.tvd.gameview.ext is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with com.tvd.gameview.ext.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tvd.cocos2dx.popup.creator.model;

import com.tvd.cocos2dx.popup.creator.global.Config;
import com.tvd.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tvd.cocos2dx.popup.creator.utils.StringUtils;
import com.tvd.cocos2dx.popup.creator.utils.ViewUtils;

public class AdvancedMenuItem extends AdvancedObject {
	public AdvancedMenuItem() {
		super();
		mTemplateFile = "menuitem.template";
		mTemplateName = "CCMenuItemSprite";
		mSuper = "CCMenuItemSprite";
	}
	
	@Override
	public void update() {
		super.update();
		mClassName = StringUtils.convertToClassName(
				mBasicObject.getXmlPositionName() + "_menuItem",
				Config.getInstance().getDefaultViewSuffix());
		if(mPrefix == null) {
			setPrefix("menuItem_" + mBasicObject.getXmlPositionName());
		}
	}
	
	@Override
	public String implement(boolean infunction) {
		ViewUtils.unlockAddingGroupToView(mBasicObject);
		
		MenuItem basicMenuItem = (MenuItem)mBasicObject;
		String result = super.implement(infunction)
				.replace("{normal_sprite}", basicMenuItem.getSpriteName("Normal"))
				.replace("{selected_sprite}", basicMenuItem.getSpriteName("Active"));
		String disableSpriteName = basicMenuItem.getSpriteName("Disable");
		if(!disableSpriteName.equals("")) {
			result = result.replace("//{disable_sprite}", disableSpriteName);
		}
		
		return result;
	}
}
