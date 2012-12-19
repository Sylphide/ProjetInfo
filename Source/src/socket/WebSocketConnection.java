package socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class WebSocketConnection extends WebSocketServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return new InternalWebSocket();
	}
	
	private final class InternalWebSocket extends MessageInbound {
		 
		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			//this application does not expect binary data
			throw new UnsupportedOperationException(
			"Binary message not supported.");
		}
		 
		@Override
		protected void onTextMessage(CharBuffer message) throws IOException {
			System.out.println("Reception message cote serveur");
			System.out.println("Envoie message au client");
			broadcast("Envoie message au client");
		}
		 
		private void broadcast(String message) throws IOException {
			this.getWsOutbound().writeTextMessage(CharBuffer.wrap(message));
		}
	}

}
