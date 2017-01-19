<%@page import="java.text.SimpleDateFormat"%><%@page import="java.util.Date"%><%@page import="java.sql.ResultSet"%><%@page import="java.sql.Statement"%><%@page import="java.sql.Connection"%><%@page import="com.zhao.demo.DataBase"%><%@ page language="java" contentType="text/text; charset=utf-8" pageEncoding="utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<students>
// 测试获取请求的数据
<method><%=request.getMethod()%></method>
<requesturl><%=request.getRequestURI()%></requesturl>
<protocol><%=request.getProtocol()%></protocol>
<servletPath><%=request.getServletPath()%></servletPath>
<serverName><%=request.getServerName()%></serverName>
<serverPort><%=request.getServerPort()%></serverPort>
<remoteAddr><%=request.getRemoteAddr()%></remoteAddr>
<remoteHost><%=request.getRemoteHost()%></remoteHost>
<% //查询学生数据，这里用java写好的数据库工具类来获取Connection
	Connection conn=DataBase.getInstance().getConnection(application.getRealPath("/") + "META-INF/SQLConnet.txt");
	String sql="select id, name, birthday, sex, tid, goal, Email from student";
	Statement stmt=conn.createStatement();
	ResultSet res=stmt.executeQuery(sql);
	while(res.next()){
		String id=res.getString(1);
		String name=res.getString(2);
		Date birthday=res.getDate(3);
		String sex=res.getString(4);
		String tid=res.getString(5);
		int goal=res.getInt(6);
		String email=res.getString(7);
		//输出一个student子标签 
%>
		<student>
			<id><%=id%></id>
			<name><%=name%></name>
			<birthday><%=new SimpleDateFormat("yyyy-MM-dd").format(birthday)%></birthday>
			<sex><%=sex%></sex>
			<tid><%=tid%></tid>
			<goal><%=goal%></goal>
			<email><%=email%></email>
		</student>
<%	
	}
%>
	
</students>

<%
	//释放资源 
	res.close();
	stmt.close();
	conn.close();
%>
