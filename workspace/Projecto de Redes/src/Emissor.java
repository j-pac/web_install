import java.util.LinkedList;
import java.util.Queue;




public class Emissor {

	private TecnicaControloErros tecnica_controlo_erros;

	public void definirTecnicaControlo(TecnicaControloErros tecnica_controlo) {
		tecnica_controlo_erros = tecnica_controlo;
	}

	// Metodo responsavel pela geraçao da mensagem a transmitir - MS
	public int[] gerarDados(int n_bits) {
		int[] dados = new int[n_bits];
		
		for (int i = 0; i < n_bits; i++) {
			dados[i] = gerarBitAleatorio();
		}

		return dados;
	}

	// Metodo responsavel pela geraçao da trama a transmitir, utiliza algoritmo
	// de acordo com a tecnica definida - T(M)
	public int[] gerarTrama(int[] dados) {
		// if(geradorTrama == null) throw new IllegalS)

		return tecnica_controlo_erros.geracaoTramaAEnviar(dados);
	}

	private int gerarBitAleatorio() {
		if (Math.random() <= 0.5) {
			return 1;
		} else {
			return 0;
		}
	}

}
