<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="No-Cache" />
<meta http-equiv="Expires" content="-1" />
<title>Bienvenu sur la plateforme du BeardMan en Ligne!</title>
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
        #textLabel100{
        	text_align:center;
        	float:left;
        	margin:1px;
        	border:2px solid;
            width:100%;
        	
       }
        #textLabel50{
        	text_align:center;
        	float:left;
        	margin:1px;
        	border:2px solid;
        	
        	
       }
    </style>
</head>
<body>
	<div style="text-align:center;width=100%; margin:5px; border:1px solid;">
	<img src="http://localhost:8080/BeardMan/Images/Banner.jpg" alt="Banniere test" width=100% height=40%/>
	</div>
    <div id="textLabel100">
      Presentation:
      </div>
     <div id="textLabel100">
      Charte:
      </div>
       <div id="textLabel50">
       Si vous êtes nouveau et voulez vous inscrire, cliquez-ici :
       <form action="/BeardMan/Controller" method="post">
	        <input id="registrationButton" name="button" type="submit" value="Inscrivez-vous!">
	    </form>
      </div>
      <div id="textLabel50">
       Si vous êtes déja inscrit, connectez-vous, cliquez-ici :
       <form action="/BeardMan/Controller" method="post">
		<input id="connectionButton" name="button" type="submit" value="Se connecter!">
	   </form>
      </div>
</body>
</html>