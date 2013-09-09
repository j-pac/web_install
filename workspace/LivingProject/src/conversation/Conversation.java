package conversation;

import java.util.ArrayList;
import java.util.List;


//A conversa é um recurso partilhado entre os utilizadores, 
//vai precisar de sincronizaçao se assim ficar

public class Conversation {
	
	private List<Phrase> conversation = new ArrayList<Phrase>();
	private WritingState writing_state = null;
	
	public void addPhrase(Phrase phrase) {
		conversation.add(phrase);
	}
	
}