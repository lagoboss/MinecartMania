package com.afforess.minecartmaniacore.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class CoreSettingParser implements SettingParser{
	private static final double version = 1.0;
	
	public boolean isUpToDate(Document document) {
		try {
			NodeList list = document.getElementsByTagName("version");
			Double version = MinecartManiaConfigurationParser.toDouble(list.item(0).getChildNodes().item(0).getNodeValue(), 0);
			return version == CoreSettingParser.version;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean read(Document document) {
		Object value;
		NodeList list;
		String setting;
		
		try {
			//Parse Simple Settings First
			setting = "MinecartsKillMobs";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MinecartsClearRails";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 1);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "KeepMinecartsLoaded";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MinecartsReturnToOwner";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MaximumMinecartSpeedPercent";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 165);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "DefaultMinecartSpeedPercent";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 100);
			MinecartManiaWorld.getConfiguration().put(setting, value);
	
			//read arrays
			ControlBlockList.controlBlocks = new ArrayList<ControlBlock>();
			list = document.getElementsByTagName("ControlBlock");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node n = list.item(temp);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) n;
					ControlBlock cb = new ControlBlock();
					
					NodeList templist = element.getElementsByTagName("BlockType").item(0).getChildNodes();
					Node tempNode = (Node) templist.item(0);
					cb.setType(MinecartManiaConfigurationParser.toItem(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("SpeedMultiplier").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setMultiplier(MinecartManiaConfigurationParser.toDouble(tempNode != null ? tempNode.getNodeValue() : null, 1.0));
					
					templist = element.getElementsByTagName("Catch").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setCatcherBlock(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("LauncherSpeed").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setLauncherSpeed(MinecartManiaConfigurationParser.toDouble(tempNode != null ? tempNode.getNodeValue() : null, 0.0));
					
					templist = element.getElementsByTagName("Eject").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setEjectorBlock(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("Platform").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setPlatformBlock(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("Station").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setStationBlock(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("SpawnMinecart").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setSpawnMinecart(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("KillMinecart").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setKillMinecart(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("ReqRedstone").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setReqRedstone(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					templist = element.getElementsByTagName("RedstoneDisables").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setRedstoneDisables(MinecartManiaConfigurationParser.toBool(tempNode != null ? tempNode.getNodeValue() : null));
					
					ControlBlockList.controlBlocks.add(cb);
				}
			 }
			 
			list = document.getElementsByTagName("ItemAlias");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node n = list.item(temp);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) n;
					
					NodeList templist = element.getElementsByTagName("AliasName").item(0).getChildNodes();
					Node tempNode = (Node) templist.item(0);
					String key = tempNode != null ? tempNode.getNodeValue() : null;
					
					ArrayList<Item> values = new ArrayList<Item>();
					templist = element.getElementsByTagName("ItemType").item(0).getChildNodes();
					for (int i = 0; i < templist.getLength(); i++) {
						tempNode = (Node) templist.item(i);
						values.add(MinecartManiaConfigurationParser.toItem(tempNode != null ? tempNode.getNodeValue() : null));
					}
					
					ItemAliasList.aliases.put(key, values);
				}
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean write(File configFile) {
		try {
			JarFile jar = new JarFile(MinecartManiaCore.MinecartManiaCore);
			JarEntry entry = jar.getJarEntry("MinecartManiaConfiguration.xml");
			InputStream is = jar.getInputStream(entry);
			FileOutputStream os = new FileOutputStream(configFile);
			byte[] buf = new byte[(int)entry.getSize()];
			is.read(buf, 0, (int)entry.getSize());
			os.write(buf);
			os.close();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
}
