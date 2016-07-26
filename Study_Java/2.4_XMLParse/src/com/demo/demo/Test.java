package com.demo.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 测试dom解析：
 * 			1.将AndroidManifest中targetSdkVersion值改成20；
 * 			2.去掉入口Activity的intent-filter，放到ManiActivity上；
 * 			3.在ManiActivity中添加meta-data标签
 * @author HKW2962
 *
 */
public class Test {

	public static void main(String[] args) {
		try {
			Document doc = new SAXReader().read(new File("INI/AM.xml").getAbsoluteFile());
			Element manifest = doc.getRootElement();
			
			//将AndroidManifest中targetSdkVersion值改成20
			manifest.element("uses-sdk").attribute("targetSdkVersion").setText("20");
			
			//去掉入口Activity的intent-filter，放到ManiActivity上
			String aciton = null;
			String category = null;
			@SuppressWarnings("unchecked")
			List<Element> activityList = manifest.element("application").elements();
			for(Element e : activityList) {
				if(e.attribute("name").getText().equals("com.xgsdk.client.api.splash.XGSplashActivity")) {
					aciton = e.element("intent-filter").element("action").attribute("name").getValue();
					category = e.element("intent-filter").element("category").attribute("name").getValue();
					e.remove(e.element("intent-filter"));
				}
			}
			for(Element e : activityList) {
				@SuppressWarnings("unchecked")
				Iterator<Element> it = e.elementIterator();
				if(!it.hasNext()) {
					continue;
				}
				if(e.element("intent-filter").element("action").attribute("name").getValue().equals("xg.game.MAIN")) {
					e.element("intent-filter").element("action").attribute("name").setValue(aciton);
					e.element("intent-filter").element("category").attribute("name").setValue(category);
					//在ManiActivity中添加meta-data标签
					e.addElement("meta-data");
				}
			}
			//将修改后的xml输出
			try {
				OutputFormat of = OutputFormat.createCompactFormat();
				of.setEncoding("utf-8");
				XMLWriter xw = new XMLWriter(new FileOutputStream(new File("INI/AM2.xml"), true), of);
				xw.write(doc);
				xw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
}
