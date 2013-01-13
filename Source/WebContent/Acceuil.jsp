<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Pragma" content="No-Cache" />
<meta http-equiv="Expires" content="-1" />
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
	<div style="text-align:center;width=100%;">
	<img src="http://localhost:8080/BeardMan/Images/Banner.jpg" alt="Banniere test" width=100% height=40%/>
	</div>
	<p style="color: Black ">
              Bienvenue sur la plateforme de e-Barbu BeardMan!
    </p>
     <div style="text_align:center;border: 5px solid;margin:10px;">
      Presentation:
      </div>
      <div style="text_align:center;border: 5px solid;margin:10px;">
      Charte:
      </div>
       <div style="text_align:center;border: 2px solid; float:left;">
       Si vous êtes nouveau et voulez vous inscrire, cliquez-ici :
       <form action="/BeardMan/Controller" method="post">
	        <input id="registrationButton" name="button" type="submit" value="Inscrivez-vous!">
	    </form>
      </div>
       <div style="text_align:center;border: 2px solid;float:left;">
       Si vous êtes déja inscrit, connectez-vous, cliquez-ici :
       <form action="/BeardMan/Controller" method="post">
		<input id="connectionButton" name="button" type="submit" value="Se connecter">
	   </form>
      </div>
</body>
</html>