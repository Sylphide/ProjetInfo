<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Inscription</title>
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
        .formLabel {
        	float : left;
            width : 150px;
            margin-left : 50px;
            }
        #subscriptionButton {
        	float: right;
            margin-right : 50px;
            }
        #cancelButton {
        	float: left;
            margin-left : 50px;
            } 
    </style>
</head>
<body>
	<p style="color: blue ">
              Bienvenue sur la plateforme de e-Barbu BeardMan!
    </p>
    
	<ul style="color: red">
		<% ArrayList<String> errors=(ArrayList<String>)request.getAttribute("errors");
		if(errors!=null && !errors.isEmpty()) { 
			for(int i=0; i<errors.size();i++) {%>
             <li><%=errors.get(i)%></li>
     		<% }
     	}%>
	</ul>
	<fieldset id="center">
	    <legend>Inscription</legend>
	    <form action="/BeardMan/Controller" method="post">
	        <p> 
	        	<span class="formLabel">Prenom : </span>
	        	<input type="text" name="firstName" value =""> 
	        </p>
	        <p> 
	        	<span class="formLabel">Nom :</span> 
	        	<input type="text" name="lastName" value=""> 
	        </p>
	        <p> 
	        	<span class="formLabel">Pseudo :</span> 
	        	<input type="text" name="nickName" value=""> 
	        </p>
	        <p> 
	        	<span class="formLabel">Mot de passe :</span> 
	        	<input type="text" name="password" value=""> 
	        </p>
	        <input id="subscriptionButton" name="button" type="submit" value="Souscrire">
	        <input id="cancelButton" name="button" type="submit" value="Retour">
	    </form>
    </fieldset>
</body>
</html>