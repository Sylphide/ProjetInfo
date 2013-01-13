<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="javabean.Lobby" %>
<%@ page import="java.util.ArrayList" %>
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
<script language="javascript" type="text/javascript">  
		var wsUri = "ws://localhost:8080/BeardMan/ControllerWebSocket";  
		function WebSocketInit() { 
			if(window.WebSocket) {
				webSocket = new WebSocket(wsUri); 
				webSocket.onopen = function(evt) { onOpen(evt) };
				webSocket.onclose = function(evt) { onClose(evt) };
				webSocket.onmessage = function(evt) { onMessage(evt) }; 
				webSocket.onerror = function(evt) { onError(evt) };
				} 
			else {
		        alert('Votre navigateur ne supporte pas les webSocket!');
				}
			
		 	}
		function onOpen(evt) { 
			}
		function onClose(evt) { 
			}
		function onMessage(evt) {
			var response=evt.data.split(";");
			if(response[0]=="CreateTable"){
				var newForm=document.createElement('form');
				newForm.action="/BeardMan/Controller";
				newForm.method="post";
				newForm.id=response[1];
				
				var newHidden=document.createElement('input');
				newHidden.name="tableId";
				newHidden.type="hidden";
				newHidden.value=response[1];
				newForm.appendChild(newHidden);
				
				var newButton=document.createElement('input');
				newButton.name="button";
				newButton.type="submit";
				newButton.value="Table n°"+response[1];
				newForm.appendChild(newButton);
				
				document.getElementById("tables").appendChild(newForm);
			}
			else if(response[0]=="RemoveTable"){
				var oldForm=document.getElementById(response[1]);
				var tables=document.getElementById("tables");
				tables.removeChild(oldForm);
			}
		}
		
		function onError(evt) {
			alert("Error during WebSocket connection");
			}
		
		function createTable(){
			webSocket.send("CreateTable");
		}
		
		window.addEventListener("load", WebSocketInit, false);
</script>
</head>
<body>
	<%ServletContext context=getServletConfig().getServletContext();%>
	<jsp:useBean id="user" class="javabean.UserInfo" scope="session"/>
	<div style="color: blue">
		Welcome <%= user.getNickName()%>!
    <form action="/BeardMan/Controller" method="post">
		<input id="Disconnect" name="button" type="submit" value="Disconnect">
	</form>
	</div>
	<fieldset id="center">
	    <legend>Asseyez-vous</legend>
	    <div id="tables">
	    <%Lobby lobby=(Lobby)context.getAttribute("lobby");
	    if(lobby!=null){
	    	ArrayList<Integer> realTablesIds=lobby.getRealTablesIds();
	    	int exitTable=-1;
	    	if(request.getAttribute("exitTable")!=null && request.getAttribute("removeTable")!=null)
	    		exitTable=Integer.parseInt(request.getAttribute("exitTable").toString());
	    	for(Integer i : realTablesIds)
	    	{
	    		if(i!=exitTable){%>
	    	 <form action="/BeardMan/Controller" id="<%=i%>" method="post">
	    		<input name="tableId" type="hidden" value="<%=i%>">
	    		<input name="button" type="submit" value="Table n°<%=i%>">
	    		
	    	</form>
	    		<%}
	    	}
	    }%>
	    </div>
	     <form action="/BeardMan/Controller" method="post">
	        <input id="newTableButton" name="button" type="submit" value="Creer une nouvelle table" onclick="createTable()">
	    </form>
    </fieldset>
</body>
</html>