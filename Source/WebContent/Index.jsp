<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="No-Cache" />
<meta http-equiv="Expires" content="-1" />
<title>Interface de Connexion</title>
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
	<div style="text-align:center;width=100%;">
	<img src="http://localhost:8080/BeardMan/Images/Banner.jpg" alt="Banniere test" width=100% height=40%/>
	</div>
	<p style="color: black ">
            <b>Veuillez entrer votre pseudo et mot de passe puis cliquer sur "Se connecter" afin d'acc�der au lobby: </b>
    </p>
      
    <ul style="color: blue">
		<%if(request.getAttribute("sender")!=null && request.getAttribute("sender").equals("registration")) { %>
             <li>Votre inscription s'est d�roul�e avec succ�s!</li>
     	<% }%>
	</ul>
	
	<ul style="color: red">
		<% if(request.getAttribute("errors")!=null) { %>
             <li><%=request.getAttribute("errors")%></li>
     	<% }%>
	</ul>
	<fieldset id="center">
	    <legend>Connection</legend>
	    <form action="/BeardMan/Controller" method="post">
	        <p> 
	        	<span class="formLabel">Login : </span>
	        	<input type="text" name="nickName" value =""> 
	        </p>
	        <p> 
	        	<span class="formLabel">Password :</span> 
	        	<input type="password" name="password" value=""> 
	        </p>
	        <input id="connectionButton" name="button" type="submit" value="Se connecter">
	        <input id="registrationButton" name="button" type="submit" value="Inscrivez-vous!">
	         <input id="cancelButton" name="button" type="submit" value="Retour">
	    </form>
    </fieldset>
</body>
</html>