import java.sql.Array;

public class CodigoDeHamming extends TecnicaControloErros {

	private static final int DISTANCIA_DE_HAMMING = 3;

	public CodigoDeHamming() {
		setCorrecaoErros(true);
	}

	@Override
	public void definirEstruturaTramaAEnviar(int tamanho_mensagem) {
		definirTamanhoMensagem(tamanho_mensagem);
		definirTrama(tamanho_mensagem == 4 ? 7 : 15);
	}

	@Override
	public int[] geracaoTramaAEnviar(int[] dados_a_enviar) {
		// Marca na trama o local dos bits de paridade sinalizando-os com -1
		localizarBitsParidade();
		// Introduçao da mensagem por ordem na trama, nos locais nao marcados
		// para bits de paridade
		introduzirMensagemNaTrama(dados_a_enviar);
		// Calculo e introdução dos bits de paridade na trama
		introduzirBitsParidade();
		return getEstruturaTrama();
	}

	

	private void localizarBitsParidade() {
		int i = 0;

		while (Math.pow(2, i) <= getTotalLength()) {
			definirBit((int) (Math.pow(2, i) - 1), -1);
			i++;
		}
	}

	private void introduzirMensagemNaTrama(int[] dados_a_enviar) {
		int dados_index = 0;
		for (int i = 0; i < getTotalLength(); i++) {
			if (consultarBit(i) != -1) {
				definirBit(i, dados_a_enviar[dados_index]);
				dados_index++;
			}
		}
		assert (dados_index + 1 == getMessageLength()) : "--> Deu bronca: -O Algoritmo nao esta a funcionar correctamente!";
	}

	// Funçao que define os valores dos bits de paridade
	private void introduzirBitsParidade() {
		for (int i = 0; (int) Math.pow(2, i) < getTotalLength(); i++) {

			definirBit(
					(int) Math.pow(2, i) - 1,
					calcularBitParidade((int) Math.pow(2, i),
							getEstruturaTrama()));
		}
	}

	// Calcula bitparidade sendo dada a sua posicao
	private int calcularBitParidade(int mod2, int[] trama) {
		int resultado = 0;

		int j = 0;
		for (int i = mod2; i <= trama.length; i = j + mod2) {
			for (j = i; j < i + mod2; j++) {
				if (j != mod2) {
					resultado = XOR(resultado, trama[j - 1]);
				}
			}
		}
		return resultado;
	}

	// Correcçao de erros no codigo de Hamming, apenas é correctamente aplicado
	// caso haja apenas um erro cuja posicao é dada pela soma dos check bits
	@Override
	public int[] CorrecaoErrosNaTrama(int[] trama_recebida) {
		int[] trama_corrigida = new int[trama_recebida.length];
		System.arraycopy(trama_recebida, 0, trama_corrigida, 0, trama_recebida.length);

		// Se [check bits sum] = 0 nao houveram erros
		// se [check bits sum] for superior ao tamanho da trama, a correcçao nao está correcta 
		// em ambos os casos serao retornados a trama sem correcçao recebida
		if (check_bits_sum > 0 && check_bits_sum <= trama_recebida.length) {
			trama_corrigida[check_bits_sum - 1] = (trama_recebida[check_bits_sum - 1] == 0 ? 1 : 0);
		}
		return trama_corrigida;
	}

	@Override
	public String criterioDecisao() {
		return "Bits de verificação";
	}

	@Override
	public String toString() {
		return super.toString() + " Código de Hamming"
				+ System.getProperty("line.separator")
				+ "Tamanho da mensagem: " + getMessageLength() + " bits"
				+ System.getProperty("line.separator");
	}
}
