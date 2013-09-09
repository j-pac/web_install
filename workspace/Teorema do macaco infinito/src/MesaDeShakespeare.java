import java.util.ArrayList;
import java.util.List;

public class MesaDeShakespeare {

	private static final int MAX_PHRASES_ON_TABLE = 3;
	
	private List<String> frases_na_mesa = new ArrayList<String>(3);

	// Neste metodo a excepçao é enviada para o macaco para poder terminar o seu run()
	public synchronized void putString(String str) throws InterruptedException {
		while (frases_na_mesa.size() == MAX_PHRASES_ON_TABLE) {
				wait();
		}
		
		frases_na_mesa.add(str);
		
		assert frases_na_mesa.size() <= 3 : "Erro: Foram colocadas mais que 3 frases na mesa!";
		
		if(frases_na_mesa.size() == 3) {
			notifyAll();
		}
	}
	
	public synchronized List<String> takeStrings() {
		while(frases_na_mesa.size() < MAX_PHRASES_ON_TABLE) {
			try {
				System.out.println("shakespeare vai entrar em Wait()!");
				wait();
				System.out.println("shakespeare vai sair do Wait()!");

			} catch (InterruptedException e) {}
		}
		
		List<String> frases = frases_na_mesa;
		
		assert frases.size() == MAX_PHRASES_ON_TABLE : "Erro: Está a ser devolvida uma lista de Strings com tamanho inferior a 3!";
		
		frases_na_mesa.clear();
		notifyAll();
		
		return frases;
	}

}
