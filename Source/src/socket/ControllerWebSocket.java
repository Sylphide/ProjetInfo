package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javabean.Lobby;
import javabean.Player;
import javabean.Table;
import javabean.UserInfo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import object.Card;
import object.Card.Rank;
import object.Card.Suit;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class ControllerWebSocket extends WebSocketServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Set<InternalWebSocket> connections =
            new CopyOnWriteArraySet<InternalWebSocket>();
	

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return new InternalWebSocket(request);
	}
	
	private final class InternalWebSocket extends MessageInbound {
		
		private HttpSession session;
		private int currentTable;
		private int playerId;
		
		public InternalWebSocket(HttpServletRequest request){
			session=request.getSession();
		}
		
		public int getCurrentTable()
		{
			return currentTable;
		}
		
		public void setPlayerId(int value){
			playerId=value;
		}
		
		@Override
        protected void onOpen(WsOutbound outbound) {
			currentTable=((UserInfo)session.getAttribute("user")).getCurrentTable();
            connections.add(this);
        }
		
		@Override
        protected void onClose(int status) {
			connections.remove(this);
			if(currentTable!=-1){
				ServletContext context = getServletConfig().getServletContext();
	            Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				table.removePlayer(playerId);
				int newPlayerId=0;
				for(InternalWebSocket connection : connections){
					if(connection.getCurrentTable()==this.currentTable){
						connection.setPlayerId(newPlayerId);
						newPlayerId++;
					}
					if(table.getNumberOfPlayer()==4){
						CharBuffer buffer = CharBuffer.wrap("CreateTable;"+currentTable+";");
                		try {
							connection.getWsOutbound().writeTextMessage(buffer);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				if(table.isGameStarted() || table.getNumberOfPlayer()==0){
					if(table.getNumberOfPlayer()==0)
						lobby.removeTable(currentTable);
					if(table.isGameStarted())
						table.setStarted(false);
					for (InternalWebSocket connection : connections) {
						try {
							if(connection.getCurrentTable()==this.currentTable)
		                	{
	                			CharBuffer buffer = CharBuffer.wrap("PlayerLeft;");
		                		connection.getWsOutbound().writeTextMessage(buffer);
		                	}
							if(table.getNumberOfPlayer()==0){
								System.out.println("RemoveTable;"+currentTable);
								CharBuffer buffer = CharBuffer.wrap("RemoveTable;"+currentTable);
		                		connection.getWsOutbound().writeTextMessage(buffer);
							}
						} catch (IOException ignore) {
		                    // Ignore
		                }
		            }
				}
			}
        }
		
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			//this application does not expect binary data
			throw new UnsupportedOperationException(
			"Binary message not supported.");
		}
		 
		@Override
		protected void onTextMessage(CharBuffer message){
			String[] formatedMessage=message.toString().split(";");
			ServletContext context = getServletConfig().getServletContext();
			String response;
			if(formatedMessage[0].equals("CreateTable"))
			{	
				Lobby lobby;
				response="CreateTable;";
				if(context.getAttribute("lobby")!=null)
				{
					lobby=(Lobby)context.getAttribute("lobby");
					response+=String.valueOf(lobby.getCurrentTableIdCount()+1)+";";
				}
				else
					response+="0;";
				if(context.getAttribute("lobby")!=null)
				{
					lobby=(Lobby)context.getAttribute("lobby");
					for(Integer j=0; j<lobby.getNumberOfTables();j++)
	    			{
	    				if(lobby.getTable(j).isGameStarted()==false){
	    					response+="true;";
	    				}
	    				else
	    					response+="false;";
	    			}
				}
				for (InternalWebSocket connection : connections) {
	                try {
	                		System.out.println(response);
                			CharBuffer buffer = CharBuffer.wrap(response);
	                		connection.getWsOutbound().writeTextMessage(buffer);
	                } catch (IOException ignore) {
	                    // Ignore
	                }
	            }
		        return;
			}
			else if(formatedMessage[0].equals("JoinGame")){
	            Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				playerId=table.getNumberOfPlayer()-1;
				if(table.isFull()){
					for (InternalWebSocket connection : connections) {
		                try {
			                	System.out.println("RemoveTable;"+currentTable);
								CharBuffer buffer = CharBuffer.wrap("RemoveTable;"+currentTable);
			            		connection.getWsOutbound().writeTextMessage(buffer);
		                } catch (IOException ignore) {
		                    // Ignore
		                }
		            }
			        return;
				}
			}
			else if(formatedMessage[0].equals("StartGame"))
			{
				
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				for (InternalWebSocket connection : connections) {
					
	        		try {
	        			response="round;"+table.getRound()+";";
	        			System.out.println(response);
	        			CharBuffer buffer = CharBuffer.wrap(response);
						connection.getWsOutbound().writeTextMessage(buffer);
	        			}	        			
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				for (InternalWebSocket connection : connections) {
					
	        		try {
	        			if(connection.getCurrentTable()==this.currentTable)
	                	{
	        				response="id;"+table.getPlayerByID(table.getCurrentPlayer())+";";
	                		CharBuffer buffer = CharBuffer.wrap(response);
							connection.getWsOutbound().writeTextMessage(buffer);
	        			
							System.out.println(response);
	                	}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				response="Deal;";
				
				if(table.getNumberOfPlayer()<3 || table.getNumberOfPlayer()>5){
					try {
						response+="false;";
						CharBuffer buffer = CharBuffer.wrap(response);
						this.getWsOutbound().writeTextMessage(buffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				else{
					response+="true;";
					table.startGame();
					int playerIndex=0;
					for (InternalWebSocket connection : connections) {
		                try {
		                	CharBuffer buffer = CharBuffer.wrap("RemoveTable;"+currentTable+";");
	                		connection.getWsOutbound().writeTextMessage(buffer);
		                	if(connection.getCurrentTable()==this.currentTable)
		                	{
		                		ArrayList<Card> hand=table.getPlayerHand(playerIndex);
		                		response+=String.valueOf(playerIndex)+";";
		                		for(int i=0; i<hand.size(); i++)
		                			response+=hand.get(i).getRank()+"_"+hand.get(i).getSuit()+";";
		                		System.out.println(response);
		                		buffer = CharBuffer.wrap(response);
		                		connection.getWsOutbound().writeTextMessage(buffer);
		                		response="Deal;true;";
		                		
		                		playerIndex++;
		                	}
		                } catch (IOException ignore) {
		                    // Ignore
		                }
		            }
			        return;
				}
			}
			else if(formatedMessage[0].equals("NextTurn"))
			{
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				int playerIndex=0;
				
				for (InternalWebSocket connection : connections) {
					
	        		try {
	        			response="round;"+table.getRound()+";";
	        			System.out.println(response);
	        			CharBuffer buffer = CharBuffer.wrap(response);
						connection.getWsOutbound().writeTextMessage(buffer);
	        			}	        			
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				for (InternalWebSocket connection : connections) {
					
	        		try {
	        			response="points;";
	        			for(int i=0 ; i< table.getPlayersName().size();i++){
	                		response+= table.getPlayersName().get(i)+";";
	                		System.out.println(response);
	                	}
	        		
	        			ArrayList<Player> Players= table.getPlayers();
	        			for(int j = 0; j< Players.size();j++){
	        				response+= Players.get(j).getPoints()+";";
	        				
	        			}
	        			System.out.println("POINTS"+response);
	        			CharBuffer buffer = CharBuffer.wrap(response);
						connection.getWsOutbound().writeTextMessage(buffer);
	        			}	        			
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				response="Deal;true;";
				
				for (InternalWebSocket connection : connections) {
	                try {
	                	if(connection.getCurrentTable()==this.currentTable)
	                	{
	                		ArrayList<Card> hand=table.getPlayerHand(playerIndex);
	                		response+=String.valueOf(playerIndex)+";";
	                		for(int i=0; i<hand.size(); i++)
	                			response+=hand.get(i).getRank()+"_"+hand.get(i).getSuit()+";";
	                		System.out.println(response);
	                		CharBuffer buffer = CharBuffer.wrap(response);
	                		connection.getWsOutbound().writeTextMessage(buffer);
	                		response="Deal;true;";
	                		playerIndex++;
	                	}
	                } catch (IOException ignore) {
	                    // Ignore
	                }
	            }
		        return;
			}
			else if (formatedMessage[0].equals("GetPlayer"))
			{
				response="GetPlayer;";
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				
				for(int i=0 ; i< table.getPlayersName().size();i++){
            		response+= table.getPlayersName().get(i)+";";
            		System.out.println(response);
            	}
				for (InternalWebSocket connection : connections) {
	                try {
	                	if(connection.getCurrentTable()==this.currentTable)
	                	{
	                
	                	CharBuffer buffer = CharBuffer.wrap(response);
                		connection.getWsOutbound().writeTextMessage(buffer);
	                	}
	                } catch (IOException ignore) {
	                    // Ignore
	                }
	            }
		        return;
			}
			else if(formatedMessage[0].equals("PlayCard"))
			{
				
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				int playerId=Integer.parseInt(formatedMessage[1]);
				System.out.println("PLAYER ID ="+ playerId);
				
				for (InternalWebSocket connection : connections) {
					
	        		try {
	        			response="round;"+table.getRound()+";";
	        			System.out.println(response);
	        			CharBuffer buffer = CharBuffer.wrap(response);
						connection.getWsOutbound().writeTextMessage(buffer);
	        			}	        			
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
//				for (InternalWebSocket connection : connections) {
//					
//	        		try {
//	        			if(playerId+1 < table.getNumberOfPlayer()){
//	        			response="id;"+table.getPlayerByID(playerId+1)+";";
//	        			CharBuffer buffer = CharBuffer.wrap(response);
//						connection.getWsOutbound().writeTextMessage(buffer);
//	        			}else{
//	        				
//	        			}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				
				String cardName=formatedMessage[2];
				String[] cardValues=cardName.split("_");
				Card card=new Card(Rank.valueOf(cardValues[0]),Suit.valueOf(cardValues[1]));
				int nextRankId=card.getRank().ordinal()+1;
				if(table.playCard(playerId, card))
				{
					int playerIndex=0;
					for (InternalWebSocket connection : connections) {
		                try {
		                	if(connection.getCurrentTable()==this.currentTable)
		                	{
		                		response="id;"+table.getPlayerByID(table.getCurrentPlayer())+";";
		                		CharBuffer buffer = CharBuffer.wrap(response);
								connection.getWsOutbound().writeTextMessage(buffer);
		                		
		                		if(table.isBoardStarting()){
		                			System.out.println("Clear");
		                			buffer=CharBuffer.wrap("ClearBoard;");
		                			connection.getWsOutbound().writeTextMessage(buffer);
		                		}
		                		
		                		if(playerIndex==playerId)
		                			response="PlayCard;true;true;"+cardName+";";               	
		                		else
		                			response="PlayCard;true;false;"+cardName+";";
		                		 
		                		
		                		if(table.isEndTurn()){
		                			response+="true;";
		                		}
		                		else{
		                			response+="false;";
		                		
		                		}
		                		System.out.println(response);
		                		
		                		buffer = CharBuffer.wrap(response);
		                		connection.getWsOutbound().writeTextMessage(buffer);	                			
		                		playerIndex++;
		                		
		                		response="AddBoard;"+cardName+";";
		                		if(table.isRoundReussite()){
		                			response+="true;";
		                			if(table.isAppend())
		                				response+="true;";
		                			else{
		                				card.setRank(Rank.values()[nextRankId]);
		                				response+=card.getRank()+"_"+card.getSuit()+";";
		                			}
		                		}
		                		else
		                			response+="false;";
		                		
		                		System.out.println(response);
	                			buffer=CharBuffer.wrap(response);
	                			connection.getWsOutbound().writeTextMessage(buffer);
		                	}
		                	
		                } catch (IOException ignore) {
		                    // Ignore
		                }
		            }
			        return;
				}
				else
				{
					try{
						response="PlayCard;false;";
	            		CharBuffer buffer = CharBuffer.wrap(response);
	            		this.getWsOutbound().writeTextMessage(buffer);
	                }
	                catch (IOException ignore) {
	                    // Ignore
	                }
				}
					
			}
		}
	}

}
