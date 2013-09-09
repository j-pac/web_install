public abstract class TecnicaControloErros {

	private int m;
	private int n;
	private int[] estrutura_trama;

	private boolean corrige_erros;

	public abstract int[] geracaoTramaAEnviar(int[] dados_a_enviar);

	// Retorna true caso hajam erros
	public abstract boolean deteccaoErrosReal(int[] trama_recebida);

	public abstract boolean deteccaoErrosSimulacao(int[] trama_enviada,
			int[] trama_recebida);

	public abstract void definirEstruturaTramaAEnviar(int tamanho_mensagem);

	public abstract String criterioDecisao();

	public void definirTamanhoMensagem(int m) {
		this.m = m;
	}

	public void setCorrecaoErros(boolean corrige_erros) {
		this.corrige_erros = corrige_erros;
	}

	public void definirTrama(int n) {
		this.n = n;
		estrutura_trama = new int[n];
	}

	public int[] getEstruturaTrama() {
		return estrutura_trama;
	}

	public int getMessageLength() {
		return m;
	}

	public int getTotalLength() {
		return n;
	}

	public void definirBit(int posicao, int valor) {
		estrutura_trama[posicao] = valor;
	}

	public int consultarBit(int posicao) {
		return estrutura_trama[posicao];
	}

	// OPERADOR LOGICO XOR
	public int XOR(int bit1, int bit2) {
		return ((bit1 + bit2) == 1 ? 1 : 0);
	}

	public boolean correccaoErrosSimulacao(int[] trama_enviada,
			int[] trama_recebida) {
		return false;
	}

	public int[] CorrecaoErrosNaTrama(int[] trama_recebida) {
		return null;
	}

	@Override
	public String toString() {
		return "Técnica de controlo de erros utilizada: ";
	}
}
