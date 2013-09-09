package conversation;

public class Phrase {
	
	private String phrase;
	
	public Phrase(String words) {
		phrase = words;
		phrase += System.getProperty("line.separator");
	}

}
