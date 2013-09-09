public class CanalRuidoso {

	private double peb;

	public void definirProbabilidadeErro(double peb) {
		this.peb = peb;
	}
	
	// Metodo responsavel pela geração do padrao de erros - E(peb)
	public int[] geracaoPadraoErros(int n_bits) {
		int[] padrao_erros = new int[n_bits];

		for (int i = 0; i < n_bits; i++) {
			if (geraErro()) {
				padrao_erros[i] = 1;
			}
		}
		return padrao_erros;
	}

	// Metodo responsavel pela geraçao da trama a receber pelo receptor - R(T, E)
	public int[] geracaoTramaReceber(int[] trama_enviada, int[] padrao_erros) {
		int[] trama_receber = new int[trama_enviada.length];

		for (int i = 0; i < trama_enviada.length; i++) {
			if (padrao_erros[i] == 1) {
				trama_receber[i] = Math.abs(trama_enviada[i] - 1);
			} else {
				trama_receber[i] = trama_enviada[i];
			}
		}
		return trama_receber;
	}

	private boolean geraErro() {
		return Math.random() <= peb;
	}
}
