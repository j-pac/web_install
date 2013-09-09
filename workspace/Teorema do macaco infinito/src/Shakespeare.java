import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Shakespeare extends Thread {

	private static final String DESIRABLE_PHRASE = "macacos";

	private List<String> taked_strings = new ArrayList<String>(3);
	private MesaDeShakespeare mesa;
	private Livro livro;
	private List<Macaco> macacos;

	public Shakespeare(MesaDeShakespeare mesa, List<Macaco> macacos) {
		this.mesa = mesa;
		this.macacos = macacos;

		livro = new Livro();
	}

	@Override
	public void run() {
		// completa o livro
		while (!livro.isCompletelyFilled()) {

			taked_strings = mesa.takeStrings();
			testPhrases();
			taked_strings.clear();
		}

		// quando acaba mete os macacos na jaula
		putMonkeysInCage();
		System.out.println("* Monkeys are now in the cage! Waiting for them to finish!");

		// espera que os macacos terminem
		try {
			join();
		} catch (InterruptedException e) {
			System.out
					.println("--> Shakespeare interrompido enquanto esperava que os macacos acabassem!");
		}

		// imprime o livro para a consola
		livro.toString();

	}

	private void testPhrases() {

		Iterator<String> iterator = taked_strings.iterator();
		
		while (iterator.hasNext()) {
			String string = iterator.next();
			
			if (string.contains(DESIRABLE_PHRASE)) {
				livro.addPhrase(string);
				System.out.println("--> Foi colocada no Livro a frase: "
						+ string);
			}
			
//			// teste
//			if(string.contains(" ")) {
//				System.out.println(string);
//			}
		}
	}

	private void putMonkeysInCage() {
		for (Macaco macaco : macacos) {
			macaco.interrupt();
		}
	}

}
