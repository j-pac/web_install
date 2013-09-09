package session;
import java.util.LinkedList;
import java.util.List;

import conversation.Conversation;

import user.User;


public class Session {

	private User session_user;
	private List<Conversation> open_conversations = new LinkedList<Conversation>();
	
	public void openSession(User user) {
		session_user = user;
	}
	
	public void startConversation() {
		open_conversations.add(new Conversation());
	}
	
	
}
