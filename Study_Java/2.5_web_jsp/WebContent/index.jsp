<%@page language="java" 
	contentType="text/html; charset=utf-8"
	import="java.text.* , java.util.*"
    pageEncoding="utf-8"%>
<%
for(int i=0; i<100; i++){
	//out.write("Hello World  "+i);
	String result="hello world"+i;
	String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
%>
	<%=now %>
<%
}
%>
