<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="UTF-8"
	import="java.util.Date"
	import="java.sql.ResultSet"
	import="java.sql.Statement"
	import="java.sql.Connection"
	import="net.sf.json.*"
	import="java.sql.DriverManager"%>
<%  //获取请求类别
	String requestThing = request.getParameter("request");
	// 链接数据库
	Class.forName("com.mysql.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3306/mytest";
	String user = "zhaoyh";
	String password = "123456";
	Connection conn = DriverManager.getConnection(url, user, password);
	Statement stmt = conn.createStatement();
	if (requestThing.equals("login")) { // 如果是登录请求
		boolean loginResult = false;
		// 不管是get请求还是post请求，都能获取到参数
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		// 判断账号密码是否正确
		ResultSet rs = stmt.executeQuery("select id from user where name='" + name + "' and pwd='" + pwd + "';");
		String msg = null;
		if (rs.next()) {
			loginResult = true;
			msg = "right";
		} else {
			msg = "the account or password is error";
		}
		JSONObject json = new JSONObject();
		try {
			json.put("result", loginResult);
			json.put("msg", msg);
		} catch (Exception e){}
		// 返回结果数据
		out.println(json.toString());
		// 把数据写到tomcat
		System.out.println(json.toString());
		rs.close();
	} else if (requestThing.equals("register")) { // 如果是注册请求
		int registerResult = -1;
		// 不管是get请求还是post请求，都能获取到参数
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		JSONObject json = new JSONObject();
		if (name == null || pwd == null) {
			json.put("exception", "name or pwd is null");
			json.put("result", -1);
		} else {
			// 将注册的账号密码写入数据库
			String sql = "insert into user(name, pwd) values ('" + name + "', '" + pwd + "');";
			try {
				registerResult = stmt.executeUpdate(sql);
			} catch (Exception e) {
				json.put("exception", e.getMessage());
			}
			json.put("result", registerResult);
		}
		// 返回结果数据
		out.println(json.toString());
		// 把数据写到tomcat
		System.out.println(json.toString());
	}
	// 释放资源
	stmt.close();
	conn.close();
%>