package com.afforess.minecartmaniacore.config;

public interface SettingList {
	
	public String getTagHeading();
	
	public String getTagName();
	
	public int getNumTags();
	
	public String getTag(int tag);
	
	public boolean isRepeatingTag(int tag);
	
	public String getTagComment(int tag);
	
	public boolean isItemTag(int tag);
	
	public int getNumElements();
	
	public Object getElementForTag(int tag, int element);
	
	public void setElementForTag(int tag, int element, Object value);
}
