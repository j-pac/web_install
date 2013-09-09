import java.util.ArrayList;
import java.util.List;


public class Livro {
	
	private static final int MAX_PHRASES = 100;
	
	private List<String> book = new ArrayList<String>();	
	
	public synchronized void addPhrase(String phrase) {
		if(book.size() != MAX_PHRASES)
			book.add(phrase);
		
	}
	
	public boolean isCompletelyFilled() {
		return book.size() == MAX_PHRASES;
	}
	
	@Override
	public String toString() {
		String str = "+ Conteúdo do Livro: " + System.getProperty("line.separator");
		
		for (String phrase : book) {
			str += phrase + System.getProperty("line.separator");
		}
		return str;
	}

}
