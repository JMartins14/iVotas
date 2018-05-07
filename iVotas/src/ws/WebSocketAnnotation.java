package ws;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketAnnotation {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private String username;
    private Session session;
    private static final Set<WebSocketAnnotation> users = new CopyOnWriteArraySet<>();

    public WebSocketAnnotation() {
        username = "User" + sequence.getAndIncrement();
        
    }

    @OnOpen
    public void start(Session session) {
    	
        this.session = session;
        users.add(this);
    }

    @OnClose
    public void end() {
    	users.remove(this);
    	String obj = "{'GET_USERS':null}";
    	receiveMessage(obj);
    	
    	// clean up once the WebSocket connection is closed
    }

    @OnMessage
    public void receiveMessage(String message) {

			if(!message.equals("")) {
			// one should never trust the client, and sensitive HTML
	        // characters should be replaced with &lt; &gt; &quot; &amp;
	    	try {
	    		
				JSONObject obj = new JSONObject(message);
				
				if(obj.has("ADD_USER")) {
					addUsers((String)obj.get("ADD_USER"));
				}
				if(obj.has("GET_USERS")) {
					getUsers();
				}
				if(obj.has("DEL_USER")) {
					removeUser((String)obj.get("DEL_USER"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//String upperCaseMessage = message.toUpperCase();
	    	//sendMessage("[" + username + "] " + upperCaseMessage);
			}
    }
    
    private void removeUser(String string) {
    	for(WebSocketAnnotation w : users) {
			if(w.username.equals(string)) {
				users.remove(w);
				String a="{'GET_USERS':null}";
				this.receiveMessage(a);
				break;
			}
		}
    	
	}

	@OnError
    public void handleError(Throwable t) {
    	t.printStackTrace();
    }
    private void sendToAdmin(String text) {
    	for(WebSocketAnnotation w : users) {
    		if(w.username.equals("admin")) {
    			try {
					w.session.getBasicRemote().sendText(text);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    private void sendMessage(String text) {
    	// uses *this* object's session to call sendText()
    	try {
    		
    		for(WebSocketAnnotation w : users) {
    			w.session.getBasicRemote().sendText(text);
    		}
    		
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    }
    private void addUsers(String username) {
    	if(users.contains(this)) {
			users.remove(this);
			this.username = username;		
			users.add(this);
			String a="{'GET_USERS':null}";
			this.receiveMessage(a);
		}
    }
    private void getUsers() {
    	String s = "";
		for(WebSocketAnnotation w : users) {
			if(users.size()>1 )
				s = s.concat(w.username +'|');
			else
				s = s.concat(w.username);
		}
		sendToAdmin(s);
    }
}
