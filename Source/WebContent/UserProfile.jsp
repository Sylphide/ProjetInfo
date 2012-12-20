<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Votre Profil</title>
<style type="text/css">
		html,body{
		height:100%;
		width:100%;
		}
		#center {
        	height : 220px;
            width : 500px;
            margin : auto;
            }
</style>
</head>
<body>
	<jsp:useBean id="user" class="javabean.UserInfo" scope="session"/>
	<p style="color: blue">
		Welcome <%= user.getNickName()%>!
	</p>
	<fieldset id="center">
	    <legend>Asseyez-vous</legend>
	    <form action="/BeardMan/Controller" method="post">
	        <input name="button" type="submit" value="Table">
	        <input name="button" type="submit" value="Table2">
	    </form>
    </fieldset>
</body>
</html>