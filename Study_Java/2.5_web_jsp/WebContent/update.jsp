<%@ page language="java" contentType="text/text; charset=utf-8" pageEncoding="UTF-8"
	import="net.sf.json.*"
	import="java.io.File"
	import="java.text.DecimalFormat"
	import="com.zhao.util.PropertiesUtil"%>
<%  //TODO 正确的方式，创建一个页面填写更新内容和上传包，如果成功则将包的信息和更新内容写入数据库，删除则删除数据库相应信息
	//这里直接从本地读取包的信息，并返回
	File file = new File("D:\\ProgramFiles\\android\\apache-tomcat-8.0.36\\webapps\\2.5_web_jsp\\MyAndroidTest.apk");
	JSONObject json = new JSONObject();
	if (file.exists()) {
		long size = file.length();
		String sizeStr = new DecimalFormat("#.00").format(size/1024/1024D);
		String path = application.getRealPath("/") + "META-INF/APPUpdateInfo.txt";
		String version = PropertiesUtil.getInstance().getValue("version", path);
		String content = PropertiesUtil.getInstance().getValue("updateContent", path);
		String updateDate = PropertiesUtil.getInstance().getValue("updateDate", path);
		try {
			json.put("code", 0);
			json.put("msg", "get update information success");
			json.put("updateVersion", version);
			json.put("updateContent", content);
			json.put("apkSize", sizeStr + " M");
			json.put("updateDate", updateDate);
			json.put("updateUrl", "http://10.20.72.68:8080/2.5_web_jsp/MyAndroidTest.apk");
		} catch (Exception e){}
		out.println(json.toString());
		path = null;
		version = null;
		content = null;
		System.gc();
	} else {
		try {
			json.put("code", -1);
			json.put("msg", "Update apk is not found");
		} catch (Exception e){}
		out.println(json.toString());
	}
%>