package com.afforess.bukkit.minecartmaniacore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.plugin.PluginDescriptionFile;

import com.afforess.bukkit.minecartmaniacore.config.MinecartManiaFlatFile;
import com.afforess.bukkit.minecartmaniacore.config.Setting;
import com.afforess.bukkit.minecartmaniacore.config.SettingList;


public class Configuration {
	/**
	 ** Initializes Minecart Mania Core configuration values
	 ** 
	 **/
	public static void loadConfiguration(PluginDescriptionFile desc, Setting config[]) {
		readFile(desc, config);
	}

	private static void readFile(PluginDescriptionFile desc, Setting config[]) {	

		File directory = new File(MinecartManiaCore.dataDirectory);
		if (!directory.exists())
			directory.mkdir();
		String input = MinecartManiaCore.dataDirectory + File.separator;
		input += StringUtils.removeWhitespace(desc.getName());
		input += "Settings.txt";
		File options = new File(input);
		if (!options.exists() || invalidFile(options, desc))
		{
			WriteFile(options, desc, config);
		}
		/*else if (invalidFile(options)) {
			updateFile(options);
		}*/
		ReadFile(options, desc, config);
	}

	private static boolean invalidFile(File file, PluginDescriptionFile desc) {
		try {
			BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
			for (String s = ""; (s = bufferedreader.readLine()) != null; ) {
				if (s.indexOf(desc.getVersion()) > -1) {
					return false;
				}
			}
			bufferedreader.close();
		}
		catch (IOException exception)
		{
			return true;
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	private static void updateFile(File options) {
		try {
			MinecartManiaFlatFile.updateVersionHeader(options, MinecartManiaCore.description.getName() + " " + MinecartManiaCore.description.getVersion());
			for (int i = 0; i < SettingList.config.length; i++) {
				MinecartManiaFlatFile.updateSetting(
						options,
						SettingList.config[i].getName(),
						SettingList.config[i].getDescription(),
						//Attempt to read value, otherwise use default
						MinecartManiaFlatFile.getValueFromSetting(options, SettingList.config[i].getName(), SettingList.config[i].getValue().toString()));
			}
		} catch (IOException e) {
			MinecartManiaCore.log.severe("Failed to update Minecart Mania settings!");
			e.printStackTrace();
		}
	}
	
	private static void WriteFile(File file, PluginDescriptionFile desc, Setting[] config)
	{
		try {
			file.createNewFile();
			BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file));
			
			MinecartManiaFlatFile.createNewHeader(
					bufferedwriter,
					desc.getName() + " " + desc.getVersion(),
					desc.getMain() + " Config Settings",
					true);
			
			for (int i = 0; i < config.length; i++) {
				MinecartManiaFlatFile.createNewSetting(
						bufferedwriter,
						config[i].getName(),
						config[i].getValue().toString(),
						config[i].getDescription());
			}
			bufferedwriter.close();
		}
		catch (Exception exception)
		{
			MinecartManiaCore.log.severe("Failed to write " + desc.getName() +" settings!");
			exception.printStackTrace();
		}
	}

	private static void ReadFile(File file, PluginDescriptionFile desc, Setting[] config)
	{
		try {
			for (int i = 0; i < config.length; i++) {
				String value = MinecartManiaFlatFile.getValueFromSetting(
						file,
						config[i].getName(),
						config[i].getValue().toString());
				//Attempt to parse the value as boolean
				if (value.contains("true")) {
					MinecartManiaWorld.setConfigurationValue(config[i].getName(),
							Boolean.TRUE);
				}
				else if (value.contains("false")) {
					MinecartManiaWorld.setConfigurationValue(config[i].getName(),
							Boolean.TRUE);
				}
				//Attempt to parse the value as a double or integer
				else if (!StringUtils.getNumber(value).isEmpty()) {
					Double d = Double.valueOf(StringUtils.getNumber(value));
					if (d.intValue() == d) {
						MinecartManiaWorld.setConfigurationValue(config[i].getName(),
								new Integer(d.intValue()));
					}
					else {
						MinecartManiaWorld.setConfigurationValue(config[i].getName(),
								d);
					}
				}
				//Fallback on string
				else {
					MinecartManiaWorld.setConfigurationValue(config[i].getName(),
							value);
				}
			}
		}
		catch (Exception exception)
		{
			MinecartManiaCore.log.severe("Failed to read " + desc.getName() +" settings!");
			exception.printStackTrace();
		}
	}


}
