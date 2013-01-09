package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javabean.Lobby;
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
		
		public InternalWebSocket(HttpServletRequest request){
			session=request.getSession();
		}
		
		public int getCurrentTable()
		{
			return currentTable;
		}
		
		@Override
        protected void onOpen(WsOutbound outbound) {
			currentTable=((UserInfo)session.getAttribute("user")).getCurrentTable();
            connections.add(this);
        }
		
		@Override
        protected void onClose(int status) {
			connections.remove(this);
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
			if(formatedMessage[0].equals("CreateTable"))
			{	
				Lobby lobby;
				String response="CreateTable;";
				if(context.getAttribute("lobby")!=null)
				{
					lobby=(Lobby)context.getAttribute("lobby");
					response+=String.valueOf(lobby.getNumberOfTables())+";";
				}
				else
					response+="0;";
				
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
			else if(formatedMessage[0].equals("StartGame"))
			{
				String response="StartGame;";
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
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
		                	if(connection.getCurrentTable()==this.currentTable)
		                	{
		                		ArrayList<Card> hand=table.getPlayerHand(playerIndex);
		                		response+=String.valueOf(playerIndex)+";";
		                		for(int i=0; i<hand.size(); i++)
		                			response+=hand.get(i).getRank()+"_"+hand.get(i).getSuit()+";";
		                		System.out.println(response);
		                		CharBuffer buffer = CharBuffer.wrap(response);
		                		connection.getWsOutbound().writeTextMessage(buffer);
		                		response="StartGame;true;";
		                		playerIndex++;
		                	}
		                } catch (IOException ignore) {
		                    // Ignore
		                }
		            }
			        return;
				}
			}
			else if(formatedMessage[0].equals("PlayCard"))
			{
				String response="PlayCard;";
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				
				int playerId=Integer.parseInt(formatedMessage[1]);
				String cardName=formatedMessage[2];
				
				response+=formatedMessage[1]+";"+cardName+";";
				
				String[] cardValues=cardName.split("_");
				Card card=new Card(Rank.valueOf(cardValues[0]),Suit.valueOf(cardValues[1]));
				table.getPlayerHand(playerId).remove(card);
				int playerIndex=0;
				for (InternalWebSocket connection : connections) {
	                try {
	                	if(connection.getCurrentTable()==this.currentTable)
	                	{
//	                		ArrayList<Card> hand=table.getPlayerHand(playerIndex);
	                		response+=String.valueOf(playerIndex)+";";
	                		System.out.println(response);
	                		CharBuffer buffer = CharBuffer.wrap(response);
	                		connection.getWsOutbound().writeTextMessage(buffer);
	                		response="PlayCard;"+formatedMessage[1]+";"+cardName+";";
	                		playerIndex++;
	                	}
	                } catch (IOException ignore) {
	                    // Ignore
	                }
	            }
		        return;
			}
		}
	}

}
