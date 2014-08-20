package com.tdgc.cocos2dx.popup.creator.utils;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;

public class ViewUtils {
	
	public static String implementGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(int i = groups.size() - 1 ; i >= 0 ; i--) {
			builder.append(groups.get(i).implement(false))
				.append("\n");
		}
		
		return builder.toString();
	}
	
	public static String declareGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.declare())
				.append("\n");
		}
		
		return builder.toString();
	}
	
	public static String declarePositionGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.declarePosition());
		}
		
		return builder.toString();
	}
	
	public static String implementPositionGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.implementPosition());
		}
		
		return builder.toString();
	}
	
	public static String createElementTags(List<ItemGroup> groups, int begin) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < groups.size() ; i++) {
			int tag = (i > 0) ? (groups.size() + i + 2) : (i + 1);
			builder.append(groups.get(i).createElementTags(tag));
		}
		
		return builder.toString();
	}
}
