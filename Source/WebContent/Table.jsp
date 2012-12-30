<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
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
		var wsUri = "ws://localhost:8080/BeardMan/WebSocketConnection";  
		function WebSocketInit() { 
			if(window.WebSocket) {
				websocket = new WebSocket(wsUri); 
				websocket.onopen = function(evt) { onOpen(evt) };
				websocket.onclose = function(evt) { onClose(evt) };
				websocket.onmessage = function(evt) { onMessage(evt) }; 
				websocket.onerror = function(evt) { onError(evt) };
				} 
			else {
		        alert('Votre navigateur ne supporte pas les webSocket!');
				}
			
		 	}
		function onOpen(evt) { 
			document.getElementById('chat').onkeydown = function(event) {
                if (event.keyCode == 13) {
                	var message = document.getElementById('chat').value;
                    if (message != '') {
                        websocket.send(message);
                        document.getElementById('chat').value = '';
                    	}
                	}
            	};
			}
		function onClose(evt) { 
			Console.log("Connection close ...");
			}
		function onMessage(evt) { 
			Console.log(evt.data);
			}
		function onError(evt) {
			Console.log("Error during WebSocket connection");
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
		window.addEventListener("load", WebSocketInit, false);
		
		function playCard(id) {
			var hand=document.getElementById("hand");
			var oldCard=document.getElementById(id);
			hand.removeChild(oldCard);
			var newImg=document.createElement('img');
			newImg.src="http://localhost:8080/BeardMan/Images/Cards/"+id+".jpg";
			newImg.id=id;
			newImg.className="cardOnTable";
			var table=document.getElementById("table");
			table.appendChild(newImg);
			}
	</script>
</head>
<body>
	<div id="playerList">
		PlayerList
	</div>
	<div id="table-container">
		Table
		<div id="table">
		
		</div>
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
		Hand
		<%ArrayList<String> hand=new ArrayList<String>();
		hand.add("AsCoeur");
		hand.add("DameCarreau");
		hand.add("SeptPique"); 
		for(int i=0; i<hand.size(); i++){%>
		<img class="cardInHand" id="<%=hand.get(i)%>" src="http://localhost:8080/BeardMan/Images/Cards/<%=hand.get(i)%>.jpg" onclick="playCard(this.id)">
		<%}%>
	</div>
	
</body>
</html>