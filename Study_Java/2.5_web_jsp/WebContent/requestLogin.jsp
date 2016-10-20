<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="UTF-8"
	import="java.util.Date"
	import="java.sql.ResultSet"
	import="java.sql.Statement"
	import="java.sql.Connection"
	import="net.sf.json.*"
	import="java.io.PrintWriter"
	import="java.sql.DriverManager"%>
<% //请求登录
	String name = null;
	String pwd = null;

	// 1.对于get请求
	name = request.getParameter("name");
	pwd = request.getParameter("pwd");

	// 2.对于post请求


	// 判断账号密码是否正确
	boolean result = false;
	Class.forName("com.mysql.jdbc.Driver");
	String url="jdbc:mysql://localhost:3306/mytest";
	String user="zhaoyh";
	String password="123456";
	Connection conn = DriverManager.getConnection(url, user, password);
	Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery("select id from user where name='" + name + "' and pwd='" + pwd + "';");
	if(rs.next())
		result = true;
	JSONObject json = new JSONObject();
	try{
		json.put("result", result);
	}catch(Exception e){}
	// 返回结果数据
	out.println(json.toString());
	// 把数据写到tomcat
	System.out.println(json.toString());
%>