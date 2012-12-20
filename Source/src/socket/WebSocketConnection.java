package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javabean.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

public class WebSocketConnection extends WebSocketServlet{

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
		
		public InternalWebSocket(HttpServletRequest request){
			session=request.getSession();
		}
		
		
		@Override
        protected void onOpen(WsOutbound outbound) {
            connections.add(this);
            String message = String.format("* %s %s",((UserInfo)session.getAttribute("user")).getNickName(), "has joined.");
			broadcast(message);
        }
		
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			//this application does not expect binary data
			throw new UnsupportedOperationException(
			"Binary message not supported.");
		}
		 
		@Override
		protected void onTextMessage(CharBuffer message){
			String formatedMessage= String.format("%s: %s",((UserInfo)session.getAttribute("user")).getNickName(), message.toString());
			broadcast(formatedMessage);
		}
		 
		private void broadcast(String message){
			for (InternalWebSocket connection : connections) {
                try {
                    CharBuffer buffer = CharBuffer.wrap(message);
                    connection.getWsOutbound().writeTextMessage(buffer);
                    connection.getWsOutbound().flush();
                } catch (IOException ignore) {
                    // Ignore
                }
            }
		}
	}

}