<%@ page language="java" contentType="text/html; charset=GB18030"
         pageEncoding="GB18030"%>
<%@ page import="java.util.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>������֤</title>
</head>
<body>
<%
    request.setCharacterEncoding("GB18030");
    String name = request.getParameter("userName");

    if(name.equals("lk")) {

%>
<jsp:forward page="afterLogin.jsp">
    <jsp:param name="userName" value="<%=name%>"/>
</jsp:forward>
<%
}
else {
%>
<jsp:forward page="index.jsp"/>
<%
    }
%>
</body>
</html>