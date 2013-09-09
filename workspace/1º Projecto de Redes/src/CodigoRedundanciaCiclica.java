public class CodigoRedundanciaCiclica extends TecnicaControloErros {

	public static int[] POLINOMIO_GERADOR = { 1, 0, 1, 1, 1 };

	public CodigoRedundanciaCiclica() {
		setCorrecaoErros(false);
	}

	@Override
	public void definirEstruturaTramaAEnviar(int tamanho_mensagem) {
		definirTamanhoMensagem(tamanho_mensagem);
		definirTrama(tamanho_mensagem + (POLINOMIO_GERADOR.length - 1));
	}

	@Override
	public int[] geracaoTramaAEnviar(int[] dados_a_enviar) {

		int[] msg_deslocada = new int[dados_a_enviar.length
				+ (POLINOMIO_GERADOR.length - 1)];

		System.arraycopy(dados_a_enviar, 0, msg_deslocada, 0,
				dados_a_enviar.length);

		// Calculo de FCS
		int[] fcs = calculoFCS(msg_deslocada);

		System.arraycopy(fcs, 0, msg_deslocada, msg_deslocada.length
				- (POLINOMIO_GERADOR.length - 1),
				(POLINOMIO_GERADOR.length - 1));

		return msg_deslocada;
	}

	@Override
	public boolean deteccaoErros(int[] trama_recebida) {
		int[] resto = calculoFCS(trama_recebida);

		for (int i = 0; i < resto.length; i++) {
			if (resto[i] == 1) {
				return true;
			}
		}
		return false;
	}

	private int[] calculoFCS(int[] mensagem) {
		int[] portas_logicas = localizacaoPortasLogicas();
		int[] registo_deslocamento = new int[POLINOMIO_GERADOR.length - 1];

		for (int i = 0; i < mensagem.length; i++) {
			registo_deslocamento = deslocamentoDoRegisto(registo_deslocamento,
					mensagem[i], portas_logicas);
		}

		return registo_deslocamento;
	}

	// As posiçoes das portas logicas ficarao marcadas com -1
	private int[] localizacaoPortasLogicas() {
		int[] portas = new int[POLINOMIO_GERADOR.length - 1];

		for (int i = 1; i < POLINOMIO_GERADOR.length; i++) {
			if (POLINOMIO_GERADOR[i] == 1) {
				portas[i - 1] = -1;
			}
		}
		return portas;
	}

	private int[] deslocamentoDoRegisto(int[] registo_deslocamento, int input,
			int[] posicoes_XOR) {
		assert registo_deslocamento.length == posicoes_XOR.length;

		int[] registo_deslocado = new int[registo_deslocamento.length];

		int Cn = registo_deslocamento[0];

		for (int i = 0; i < registo_deslocamento.length - 1; i++) {
			// Se houver porta logica na posicao o valor do registo resultara
			// dessa funçao, caso contrario, apenas havera deslocaçao
			if (posicoes_XOR[i] == -1) {
				registo_deslocado[i] = XOR(Cn, registo_deslocamento[i + 1]);
			} else {
				registo_deslocado[i] = registo_deslocamento[i + 1];
			}
		}

		if (posicoes_XOR[posicoes_XOR.length - 1] == -1) {
			registo_deslocado[posicoes_XOR.length - 1] = XOR(Cn, input);
		} else {
			registo_deslocado[posicoes_XOR.length - 1] = input;
		}
		return registo_deslocado;
	}

	@Override
	public String criterioDecisao() {
		return "FCS";
	}

	@Override
	public String toString() {
		return super.toString() + " Código de Redundância Cíclica (CRC)"
				+ System.getProperty("line.separator")
				+ "Tamanho da mensagem: " + getMessageLength() + " bits"
				+ System.getProperty("line.separator");

	}

}
