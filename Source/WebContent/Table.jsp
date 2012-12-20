<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
	<style type="text/css">
		input#chat {
            width: 410px
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
			Console.log("Error during WebSocket connection")
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
	</script>
</head>
<body>
	<p>
        <input type="text" placeholder="Tapez votre message et appuyez sur Entree" id="chat">
    </p>
	<div id="console-container">
        <div id="console"></div>
    </div>
</body>
</html>