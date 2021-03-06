public class BitDeParidade extends TecnicaControloErros {

	public BitDeParidade() {
		setCorrecaoErros(false);
	}
	
	@Override
	public void definirEstruturaTramaAEnviar(int tamanho_mensagem) {
		definirTamanhoMensagem(tamanho_mensagem);
		definirTrama(tamanho_mensagem + 1);
	}

	@Override
	public int[] geracaoTramaAEnviar(int[] dados_a_enviar) {

		// Colocação dos dados por ordem na Trama
		for (int i = 0; i < dados_a_enviar.length; i++) {
			definirBit(i, dados_a_enviar[i]);
		}

		// Verificaçao da paridade da mensagem e introduçao do bit de controlo
		if (ePar(dados_a_enviar)) {
			definirBit((dados_a_enviar.length), 0);
		} else {
			definirBit((dados_a_enviar.length), 1);
		}

		return getEstruturaTrama();
	}

	@Override
	public boolean deteccaoErros(int[] trama_recebida) {
		return !ePar(trama_recebida);
	}

	private boolean ePar(int[] trama_a_verificar) {
		int count = 0;
		for (int i = 0; i < trama_a_verificar.length; i++) {
			if (trama_a_verificar[i] == 1) {
				count++;
			}
		}
		return count % 2 == 0;
	}

	
	@Override
	public String criterioDecisao() {
		return "Paridade da trama";
	}
	
	@Override
	public String toString() {
		return super.toString() + " Bit de Paridade"
				+ System.getProperty("line.separator")
				+ "Tamanho da mensagem: " + getMessageLength() + " bits"
				+ System.getProperty("line.separator");
	}

}
