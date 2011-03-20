package com.afforess.minecartmaniacore.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.ItemUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class MinecartManiaConfigurationParser {
	
	public static void read(String filename, String directory, Setting[] settings) {
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File config = new File(directory, filename);
		if (!config.exists()) {
			try {
				config.createNewFile();
				write(config, settings);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			read(config, settings);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void read(File config, Setting[] settings) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 Document doc = dBuilder.parse(config);
		 doc.getDocumentElement().normalize();
		 
		 for (Setting s : settings) {
			if (s.getValue() instanceof SettingList) {
				List<String[]> list = getTagsFromNode(doc, ((SettingList)s.getValue()));
				for (int i = 0; i < list.size(); i++) {
					for (int j = 0; j < list.get(i).length; j++) {
						((SettingList)s.getValue()).setElementForTag(j, i, parseString(list.get(i)[j], ((SettingList)s.getValue()).isItemTag(j)));
					}
				}
			}
			else {
				NodeList list = doc.getElementsByTagName(s.getName().replaceAll(" ", ""));
				Object value = parseString(list.item(0).getChildNodes().item(0).getNodeValue(), false);
				MinecartManiaWorld.getConfiguration().put(s.getName(), value);
			}
		 }
		
	}

	private static Object parseString(String value, boolean item) {
		if (value == null) {
			return null;
		}
		if (value.equals("null")) {
			return null;
		}
		if (value.equals("true")) {
			return Boolean.TRUE;
		}
		if (value.equals("false")) {
			return Boolean.FALSE;
		}
		if (item) {
			Item i = ItemUtils.getFirstItemStringToMaterial(value);
			if (i != null) {
				return i;
			}
		}
		if (!value.contains(".") && !StringUtils.getNumber(value).isEmpty()) {
			try {
				return Integer.parseInt(StringUtils.getNumber(value));
			}
			catch(Exception e) {}
		}
		if (!StringUtils.getNumber(value).isEmpty()) {
			try {
				return Double.parseDouble(StringUtils.getNumber(value));
			}
			catch(Exception e) {}
		}
		return value;
	}

	private static void write(File config, Setting[] settings) throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		//root elements
		Document doc = docBuilder.newDocument();
		doc.setXmlStandalone(true);
		
		Element rootElement = doc.createElement("MinecartManiaConfiguration");
		doc.appendChild(rootElement);
		
		for (Setting s : settings) {
			if (s.getValue() instanceof SettingList) {
				writeSettingElement((SettingList)s.getValue(), doc, rootElement);
			}
			else {
				Element element = doc.createElement(s.getName().replaceAll(" ", ""));
				element.appendChild(doc.createTextNode(s.getValue().toString()));
				rootElement.appendChild(element);
				Comment comment = doc.createComment(s.getDescription());
				rootElement.insertBefore(comment, element);
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(config.toURI().getPath());
		transformer.transform(source, result);
	}

	private static void writeSettingElement(SettingList list, Document doc, Element rootElement) {
		//heading elements
		Element heading = doc.createElement(list.getTagHeading());
		rootElement.appendChild(heading);
		boolean first = true;
		for (int i = 0; i < list.getNumElements(); i++) {
			Element element = doc.createElement(list.getTagName());
			int tag = 0;
			while(tag < list.getNumTags() || (list.isRepeatingTag(tag) && list.getElementForTag(i, tag) != null)) {
				Element e = doc.createElement(list.getTag(tag));
				if (list.getElementForTag(i, tag) != null) {
					e.appendChild(doc.createTextNode(list.getElementForTag(i, tag).toString()));
				}
				if (!list.isRepeatingTag(tag) || list.getElementForTag(i, tag) != null) {
					element.appendChild(e);
				}
				
				if (first && list.getTagComment(tag) != null) {
					Comment comment = doc.createComment(list.getTagComment(tag));
					element.insertBefore(comment, e);
				}
				tag++;
			}
			first = false;
			heading.appendChild(element);
		}
	}

	public static List<String[]> getTagsFromNode(Document doc, SettingList list) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<String[]> tagList = new ArrayList<String[]>();
		 NodeList nl = doc.getElementsByTagName(list.getTagName());
		 for (int temp = 0; temp < nl.getLength(); temp++) {
			Node n = nl.item(temp);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) n;
				
				ArrayList<String> values = new ArrayList<String>();
				int duplicate = 0;
				String last = "";
				String value = getTagValue(list.getTag(0), element, duplicate);
				int loop = 0;
				while ((value != null && list.isRepeatingTag(loop)) || (loop < list.getNumTags())) {
					if (last.equals(list.getTag(loop))) duplicate++;
					else duplicate = 0;
					last = list.getTag(loop);
					
					values.add(value);
					loop++;
					value = getTagValue(list.getTag(loop), element, duplicate);
				}
				String[] s = new String[values.size()];
				tagList.add(values.toArray(s));
			}
		 }
		 return tagList;
	}
			 
	private static String getTagValue(String tag, Element element, int index){
		if (tag != null && element != null && index < element.getElementsByTagName(tag).getLength()) {
			NodeList list = element.getElementsByTagName(tag).item(index).getChildNodes();
			Node value = (Node) list.item(0);
			return value != null ? value.getNodeValue() : null; 
		}
		return null;
	}
}
