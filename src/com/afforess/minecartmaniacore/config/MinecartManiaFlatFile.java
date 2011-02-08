package com.afforess.minecartmaniacore.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MinecartManiaFlatFile {
	public static void createNewHeader(BufferedWriter output, String title, String subtitle, boolean firstHeader) throws IOException {
		final String linebreak = "-------------------------------";
		if (!firstHeader) {
			output.write("---------------");
			output.newLine();
			output.newLine();
			output.write(linebreak);
			output.newLine();
		}
		output.write(title);
		output.newLine();
		output.write(subtitle);
		output.newLine();
		output.write(linebreak);
		output.newLine();
		output.newLine();
	}
	
	public static void createNewSetting(BufferedWriter output, String name, String value, String description) throws IOException {
		final String linebreak = "---------------";
		output.append(linebreak);
		output.newLine();
		output.write("Setting: " + name);
		output.newLine();
		output.write("Value: " + value);
		output.newLine();
		output.write("Description:");

		ArrayList<String> desc = new ArrayList<String>();
		desc.add(0, "");
		final int maxLength = 80;
		String[] words = description.split(" ");
		int lineNumber = 0;
		for (int i = 0; i < words.length; i++) {
			if (desc.get(lineNumber).length() + words[i].length() < maxLength) {
				desc.set(lineNumber, desc.get(lineNumber) + " " + words[i]);
			}
			else {
				lineNumber++;
				desc.add(lineNumber, "             " + words[i]);
			}
		}
		for(String s : desc) {
			output.write(s);
			output.newLine();
		}
	}
	
	public static String getValueFromSetting(File input, String name, String defaultVal)  throws IOException{
		BufferedReader bufferedreader = new BufferedReader(new FileReader(input));
		for (String s = ""; (s = bufferedreader.readLine()) != null; )
		{
			if (s.indexOf(name) > -1)
			{
				//Next line
				s = bufferedreader.readLine();
				String val[] = s.split(":");
				bufferedreader.close();
				return val[1].trim();
			}
		}
		bufferedreader.close();
		return defaultVal.trim();
	}
	
	public static void updateSetting(File input, String setting, String desc, String value)  throws IOException{
		BufferedReader bufferedreader = new BufferedReader(new FileReader(input));
		BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(input));
		boolean found = false;
		for (String s = ""; (s = bufferedreader.readLine()) != null; )
		{
			bufferedwriter.write(s);
			if (s.indexOf(setting) > -1)
			{
				found = true;
				s = bufferedreader.readLine();
				bufferedwriter.write("Value: " + value);
			}
		}
		bufferedreader.close();
		
		if (!found) {
			createNewSetting(bufferedwriter, setting, value, desc);
		}
		
		bufferedwriter.close();
	}
	
	public static String getNumber(String s)
	{
		String n = "";
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (Character.isDigit(c) || c == '.' || c == '-')
				n += c;
		}
		return n;
	}

	public static void updateVersionHeader(File input, String header) throws IOException {
		BufferedReader bufferedreader = new BufferedReader(new FileReader(input));
		BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(input));
		boolean found = false;
		for (String s = ""; (s = bufferedreader.readLine()) != null; )
		{
			if (!found) {
				bufferedwriter.write(header);
				found = true;
			}
			else {
				bufferedwriter.write(s);
			}
		}
		bufferedreader.close();
		bufferedwriter.close();
	}
}
