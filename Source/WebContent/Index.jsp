<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BeardMan</title>
	<style type="text/css">
		html,body{
		height:100%;
		width:100%;
		}
		#center {
        	height : 150px;
            width : 500px;
            margin : auto;
            }
        .formLabel {
        	float : left;
            width : 150px;
            margin-left : 50px;
            }
        #connectionButton {
        	float: right;
            margin-right : 50px;
            }  
        #registrationButton {
        	float: left;
            margin-left : 50px;
            }   
    </style>
</head>
<body>
	<p style="color: blue ">
              Bienvenue sur la plateforme de e-Barbu BeardMan!
    </p>
      
    <ul style="color: blue">
		<%if(request.getAttribute("sender")!=null && request.getAttribute("sender").equals("registration")) { %>
             <li>Votre inscription s'est déroulée avec succès!</li>
     	<% }%>
	</ul>
	
	<ul style="color: red">
		<% if(request.getAttribute("sender")!=null && request.getAttribute("sender").equals("index")) { %>
             <li>La combinaison Pseudo/Mot de passe est incorrect</li>
     	<% }%>
	</ul>
	<fieldset id="center">
	    <legend>Connection</legend>
	    <form action="/BeardMan/Controller" method="post">
	        <p> 
	        	<span class="formLabel">Login : </span>
	        	<input type="text" name="name" value =""> 
	        </p>
	        <p> 
	        	<span class="formLabel">Password :</span> 
	        	<input type="password" name="password" value=""> 
	        </p>
	        <input id="connectionButton" name="button" type="submit" value="Se connecter">
	        <input id="registrationButton" name="button" type="submit" value="Inscrivez-vous!">
	    </form>
    </fieldset>
</body>
</html>