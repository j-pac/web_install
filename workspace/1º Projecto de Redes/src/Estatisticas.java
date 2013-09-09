import java.util.Arrays;

public class Estatisticas {

	private int errosOcorridos;
	private int errosNaoDetectados;
	private int bitsErradosTotais;
	private int correcoesCorrectas;
	private int verificacoesTotais;

	private int numero_bits_errados(int[] trama1, int[] trama2) {
		int bits_diferenca = 0;
		for (int i = 0; i < trama2.length; i++) {
			if (trama1[i] != trama2[i]) {
				bits_diferenca++;
			}
		}
		return bits_diferenca;
	}

	public void analisar_amostras(int[] trama_enviada, int[] trama_recebida,
			int[] trama_corrigida, boolean erro_detectado) {

		int diferenca_bits = numero_bits_errados(trama_enviada, trama_recebida);
		// Se as tramas enviada e recebida sao diferentes, ocorreu erro
		if (diferenca_bits > 0) {
			if (!erro_detectado) {
				errosNaoDetectados++;
			}
			errosOcorridos++;
			bitsErradosTotais += diferenca_bits;
			
			// se a tramas enviada e corrigida forem iguais, a correccao foi bem
			// sucedida
			if (trama_corrigida != null) {
				if (numero_bits_errados(trama_enviada, trama_corrigida) == 0) {
					correcoesCorrectas++;
				}
			}
		}
		verificacoesTotais++;
	}

	public double probabilidade_transmissao_sem_erros() {
		return (verificacoesTotais - errosOcorridos) / (double)verificacoesTotais;
	}

	public double prob_erros_nao_detectados_dado_ocorreram_erros() {
		if (errosOcorridos != 0) {
			return errosNaoDetectados/(double)errosOcorridos;
		}
		return 0;
	}

	public double prob_correccao_correcta_dado_ocorreram_erros() {
		if (errosOcorridos != 0) {
			return correcoesCorrectas/(double)errosOcorridos;
		}
		return 0;
	}

	public double valor_esperado_numero_bits_errados() {
		return bitsErradosTotais / (double)verificacoesTotais;
	}

	public void reset_estatisticas() {
		errosOcorridos = 0;
		errosNaoDetectados = 0;
		bitsErradosTotais = 0;
		correcoesCorrectas = 0;
		verificacoesTotais = 0;
	}
}
