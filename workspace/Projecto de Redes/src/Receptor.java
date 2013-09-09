



public class Receptor {

	public TecnicaControloErros tecnica_deteccao;
	
	public void definirTecnicaDeteccao(TecnicaControloErros tecnica_deteccao) {
		this.tecnica_deteccao = tecnica_deteccao;
	}
	
	// Metodo responsavel pela detecçao de erros na trama recebida R
	public boolean detectarErros(int[] trama_recebida) {
		return tecnica_deteccao.deteccaoErros(trama_recebida);
	}
}
