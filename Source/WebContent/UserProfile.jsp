<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="javabean.Lobby" %>
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
        #newTableButton {
        	float: right;
            margin-right : 50px;
            }
</style>
</head>
<body>
	<%ServletContext context=getServletConfig().getServletContext();%>
	<jsp:useBean id="user" class="javabean.UserInfo" scope="session"/>
	<p style="color: blue">
		Welcome <%= user.getNickName()%>!
	</p>
	<fieldset id="center">
	    <legend>Asseyez-vous</legend>
	    <%Lobby lobby=(Lobby)context.getAttribute("lobby");
	    if(lobby!=null){
	    	int nbTables=lobby.getNumberOfTables();
	    	for(int i=0; i<nbTables; i++)
	    	{%>
	    	 <form action="/BeardMan/Controller" method="post">
	    		<input name="tableId" type="hidden" value="<%=i%>">
	    		<input name="button" type="submit" value="Table n°<%=i%>">
	    	</form>
	    	<%}
	    }%>
	     <form action="/BeardMan/Controller" method="post">
	        <input id="newTableButton" name="button" type="submit" value="Creer une nouvelle table">
	    </form>
    </fieldset>
</body>
</html>