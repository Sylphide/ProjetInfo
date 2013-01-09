<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="object.Card" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<style type="text/css">
		#chat-container{
			float:left;
		}
		
		input#chat {
            width: 410px;
        }
		#console-container {
	        width: 400px;
	    }
	
        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }
        
        #playerList {
        	height:350px;
        	float:left;
        	border:1px solid black;
        }
        
        #table-container {
        	height:350px;
        	width:100%;
        	border:1px solid black;
        }
        
        #table {
        	text-align:center;
        	margin-top:100px;
        }
        
        .cardInHand {
        	border: 1px solid black;
        	margin:1px;
        	transition-property:border;
        	transition-duration:0s;
        	transition-timing-function:linear;
        	transition-delay:0s;
        }
        
        .cardOnTable {
        	margin:1px;
        	border: 1px solid black;
        }
        
        .cardInHand:hover {
        	border: 2px solid black;
        }
        
        #hand {
        	width:600px;
        	float:left;
        	border: 1px solid black;
            text-align:center;
        }
    </style>
	<script language="javascript" type="text/javascript">  
		var chatUri = "ws://localhost:8080/BeardMan/ChatWebSocket";  
		function ChatWebSocketInit() { 
			if(window.WebSocket) {
				chatWebSocket = new WebSocket(chatUri); 
				chatWebSocket.onopen = function(evt) { chatOnOpen(evt) };
				chatWebSocket.onclose = function(evt) { chatOnClose(evt) };
				chatWebSocket.onmessage = function(evt) { chatOnMessage(evt) }; 
				chatWebSocket.onerror = function(evt) { chatOnError(evt) };
				} 
			else {
		        alert('Votre navigateur ne supporte pas les webSocket!');
				}
			
		 	}
		function chatOnOpen(evt) { 
			document.getElementById('chat').onkeydown = function(event) {
                if (event.keyCode == 13) {
                	var message = document.getElementById('chat').value;
                    if (message != '') {
                    	chatWebSocket.send(message);
                        document.getElementById('chat').value = '';
                    	}
                	}
            	};
			}
		function chatOnClose(evt) { 
			Console.log("Connection close ...");
			}
		function chatOnMessage(evt) { 
			Console.log(evt.data);
			}
		function chatOnError(evt) {
			Console.log("Error during ChatWebSocket connection");
			}
		
		var controllerUri = "ws://localhost:8080/BeardMan/ControllerWebSocket";  
		function ControllerWebSocketInit() { 
			if(window.WebSocket) {
				controllerWebSocket = new WebSocket(controllerUri);
				controllerWebSocket.onopen = function(evt) { controllerOnOpen(evt) };
				controllerWebSocket.onclose = function(evt) { controllerOnClose(evt) };
				controllerWebSocket.onmessage = function(evt) { controllerOnMessage(evt) }; 
				controllerWebSocket.onerror = function(evt) { controllerOnError(evt) };
				} 
			else {
		        alert('Votre navigateur ne supporte pas les webSocket!');
				}
		 	}
		
		function controllerOnOpen(evt) { 
			}
		
		function controllerOnClose(evt) { 
			}
		
		function controllerOnMessage(evt) { 
			var hand=evt.data.split(";");
			if(hand[0]=="StartGame"){
				if(hand[1]=="true"){
					for(var i=2; i<hand.length-1; i++){
						var card=document.createElement('img');
						card.className="cardInHand";
						card.id=i;
						card.src="http://localhost:8080/BeardMan/Images/Cards/"+hand[i]+".jpg";
						card.onclick=function(){playCard(this.id);};
						document.getElementById("hand").appendChild(card);					
					}
					var tableContainer=document.getElementById("table-container");
					var startGameButton=document.getElementById("startgame");
					tableContainer.removeChild(startGameButton);
				}
				else{
					alert("Il n'y a pas assez de joueur a la table");
				}
				
			}
			
			}
		
		function controllerOnError(evt) {
			Console.log("Error during ControllerWebSocket connection");
			}
		
		
		var Console = {};
		Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 25) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });
		window.addEventListener("load", ChatWebSocketInit, false);
		window.addEventListener("load", ControllerWebSocketInit, false);
		
		function playCard(id) {
			var hand=document.getElementById("hand");
			var oldCard=document.getElementById(id);
			hand.removeChild(oldCard);
			var newImg=document.createElement('img');
			newImg.src=oldCard.src;
			newImg.id=id;
			newImg.className="cardOnTable";
			var table=document.getElementById("table");
			table.appendChild(newImg);
			}
		
		function StartGame(){
			controllerWebSocket.send("StartGame");
		}
	</script>
</head>
<body>
	<jsp:useBean id="player" class="javabean.Player" scope="session"/>
	<div style="width:100%;text-align:center;}">
		Table n°<%=player.getCurrentTable() %>
	</div>
	<div id="playerList">
		PlayerList
	</div>
	<div id="table-container">
		Table
		<div id="table">
			
		</div>
		<input type="button" id="startgame" value="Lancer la partie" onclick="StartGame()">
	</div>
	<div id="chat-container">
		<p>
	        <input type="text" placeholder="Tapez votre message et appuyez sur Entree" id="chat">
	    </p>
		<div id="console-container">
	        <div id="console"></div>
	    </div>
    </div>	
	<div id="hand">
	</div>
	
</body>
</html>