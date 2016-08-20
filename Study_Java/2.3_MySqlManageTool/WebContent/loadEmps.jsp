<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@ page language="java" contentType="text/xml; charset=utf-8" pageEncoding="utf-8"%><?xml version="1.0" encoding="UTF-8"?>
<students>
<% //查询数据库  查出所有的员工信息 
	//把员工信息都输出成xml标签
	//JDBC 查询数据库 获取员工集合 
	//获取ResultSet    -->     List<Employee>
	//select employee_id, first_name, last_name, email, hire_date,salary from employees
	//1.获取Connection 
	Class.forName("com.mysql.jdbc.Driver");
	String url="jdbc:mysql://localhost:3306/hr";
	String user="root";
	String password="";
	Connection conn=DriverManager.getConnection(url, user, password);
	String sql="select employee_id, first_name, last_name, email, hire_date,salary from employees";
	Statement stmt=conn.createStatement();
	ResultSet res=stmt.executeQuery(sql);
	while(res.next()){
		int id=res.getInt(1);
		String firstName=res.getString(2);
		String lastName=res.getString(3);
		String email=res.getString(4);
		Date hireDate=res.getDate(5);
		Double salary=res.getDouble(6);
		//输出一个student子标签 
%>
		<student>
			<id><%=id%></employee_id>
			<name><%=name%></first_name>
			<birthday><%=birthday%></last_name>
			<sex><%=sex%></email>
			<tid><%=new SimpleDateFormat("yyyy-MM-dd").format(hiredate)%></hiredate>
			<goal><%=goal%></salary>
			<Email><%=salary%></salary>
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
