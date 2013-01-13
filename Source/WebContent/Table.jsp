<%@page import="quicktime.app.players.Playable"%>
<%@page import="java.lang.reflect.Array"%>
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
			controllerWebSocket.send("JoinGame;");	
		}
		
		function controllerOnClose(evt) { 
		}
		
		function controllerOnMessage(evt) { 
			var response=evt.data.split(";");
			if(response[0]=="Deal"){
				//StartGame;Success;PlayerId;PlayerHand
				if(response[1]=="true"){
					for(var i=3; i<response.length-1; i++){
						var card=document.createElement('img');
						card.className="cardInHand";
						card.id=response[i];
						card.src="http://localhost:8080/BeardMan/Images/Cards/"+response[i]+".jpg";
						card.onclick=function(){playCard(response[2],this.id);};
						document.getElementById("hand").appendChild(card);					
					}
					var tableContainer=document.getElementById("table-container");
					var startGameButton=document.getElementById("startgame");
					if(startGameButton)
						tableContainer.removeChild(startGameButton);
					var nextTurnButton=document.getElementById("nextTurn");
					if(nextTurnButton)
						tableContainer.removeChild(nextTurnButton);
				}
				else{
					alert("Il n'y a pas assez de joueur a la table");
				}
			}
			else if(response[0] == "GetPlayer"){
				var text = "Players List";
				for(var i=1; i<response.length; i++){
					text += response[i];
				}
				document.getElementById("playerList").innerHTML= text;
			}
			else if(response[0]=="PlayCard"){
				//PlayCard;Success;CurrentPlayer;cardName;isEndTurn
				if(response[1]=="true"){
					var hand=document.getElementById("hand");
					if(response[2]=="true"){
						var oldCard=document.getElementById(response[3]);
						hand.removeChild(oldCard);
					}
					if(response[4]=="true"){
						var nextTurnButton=document.createElement('input');
						nextTurnButton.value="Tour suivant";
						nextTurnButton.type="button";
						nextTurnButton.id="nextTurn";
						nextTurnButton.onclick=function(){nextTurn();};
						var tableContainer=document.getElementById("table-container");
						tableContainer.appendChild(nextTurnButton);
					}
				}
				else
					alert("vous ne pouvez pas jouer cette carte/ce n'est pas votre tour");
				
			}
			else if(response[0]=="AddBoard"){
				//AddBoard;CardName;isReussite;isAppend/nextCard;
				
				
				var newImg=document.createElement('img');
				newImg.src="http://localhost:8080/BeardMan/Images/Cards/"+response[1]+".jpg";;
				newImg.id=response[1];
				newImg.className="cardOnTable";
				if(response[2]=="true"){
					var cardParameter=response[1].split("_");
					var divSuit=document.getElementById(cardParameter[1]);
					if(response[3]=="true")
						divSuit.appendChild(newImg);
					else{
						var nextCard=document.getElementById(response[3]);
						divSuit.insertBefore(newImg,nextCard);
					}
						
				}
				else{
					var table=document.getElementById("regularRounds");
					table.appendChild(newImg);
				}
				
			}
			else if(response[0]=="ClearBoard"){
				var table=document.getElementById("regularRounds");
				while(table.firstChild){
					table.removeChild(table.firstChild);
					};
			}
			else if(response[0]=="PlayerLeft"){
				alert("Un des joueurs a quitte la partie. La partie est maintenant terminee");
				var exitForm=document.getElementById("exitTable");
				exitForm.submit();
			}
			
		}
		
		function nextTurn(){
			controllerWebSocket.send("NextTurn");
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
		
		function playCard(playerId,cardName) {
			controllerWebSocket.send("PlayCard;"+playerId+";"+cardName+";");
		}
		
		function StartGame(){
			controllerWebSocket.send("StartGame");
		}
		
		function GetPlayer(){
			controllerWebSocket.send("GetPlayer");
		}
	</script>
</head>
<body >
	<jsp:useBean id="player" class="javabean.Player" scope="session"/>
	<jsp:useBean id="table" class="javabean.Table" scope="session" />
    <% ArrayList<String>playersName = table.getPlayersName(); %>  
    <% int i = 0; %>
	<div style="width:100%;text-align:center;}">
		Table n°<%=player.getCurrentTable() %>
	</div>
	<div id="playerList">
		<input type="button" id="getPlayer" value="Get Player" onclick="GetPlayer()">
	</div>	
	<div id="table-container">
		Table
		<div id="table">
			<div id="regularRounds">
			</div>
			<div id="reussite">
				<div id="HEARTS">
				</div>
				<div id="CLUBS">
				</div>
				<div id="DIAMONDS">
				</div>
				<div id="SPADES">
				</div>
			</div>
		</div>
		<input type="button" id="startgame" value="Lancer la partie" onclick="StartGame()">
		<form action="/BeardMan/Controller" id="exitTable" method="post">
			<input type="hidden" name="hiddenExitTable" value="ExitTable">
			<input type="submit" name="button" value="Quitter la table">
		</form>
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