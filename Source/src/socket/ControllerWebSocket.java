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
			String formatedMessage=message.toString();
			ServletContext context = getServletConfig().getServletContext();
			System.out.println(formatedMessage);
			if(formatedMessage.equals("CreateTable"))
			{	
				Lobby lobby;
				String response="";
				if(context.getAttribute("lobby")!=null)
				{
					lobby=(Lobby)context.getAttribute("lobby");
					response=String.valueOf(lobby.getNumberOfTables());
				}
				else
					response="0";
				
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
			else if(formatedMessage.equals("StartGame"))
			{
				String response="";
				Lobby lobby=(Lobby)context.getAttribute("lobby");
				Table table=lobby.getTable(currentTable);
				table.startGame();
				int playerIndex=0;
				for (InternalWebSocket connection : connections) {
	                try {
	                	if(connection.getCurrentTable()==this.currentTable)
	                	{
	                		ArrayList<Card> hand=table.getPlayerHand(playerIndex);
	                		for(int i=0; i<hand.size(); i++)
	                			response+=hand.get(i).getRank()+"_"+hand.get(i).getSuit()+";";
	                		System.out.println(response);
	                		CharBuffer buffer = CharBuffer.wrap(response);
	                		connection.getWsOutbound().writeTextMessage(buffer);
	                		response="";
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
