import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainClass implements ActionListener {

	private static final String[] TIPO_VERSAO = { "Versão de demonstração",
			"Versão de simulação" };
	private static final String[][] OPCOES = {
			{ "Bit de Paridade", "CRC G23(x)", "Código de Hamming" },
			{ "4 bits", "11 bits" }, { "Opção A", "Opção B" } };
	private static final int[] TAMANHO_MENSAGEM = { 4, 11 };

	private static final double[] PROBABILIDADES_ERRO_SIMULACAO = {
			Math.pow(10, -6), Math.pow(10, -5), Math.pow(10, -4),
			Math.pow(10, -3), Math.pow(10, -2), Math.pow(10, -1),
			(1 / (double) 8), (1 / (double) 4), (1 / (double) 2) };

	private JFrame janela;
	private Container contentor;

	private Versao versao_a_correr;
	private Emissor emissor;
	private CanalRuidoso canal;
	private Receptor receptor;

	public static void main(String[] args) {
		new MainClass().init();
	}

	// ---------------------- Método responsavel pelo inicio da execuçao do
	// Programa ----------------------------------
	public void init() {
		// Criaçao dos actores da arquitectura
		construirArquitectura();

		// Instanciação da classe Versao
		versao_a_correr = new Versao();

		// Apresentaçao dialogo para escolha do Modo de funcionamento
		iniciarOpcoes();
	}

	private void construirArquitectura() {
		canal = new CanalRuidoso();
		emissor = new Emissor();
		receptor = new Receptor();
	}

	public void iniciarOpcoes() {
		janela = new JFrame("Trabalho Prático I: Detecção e correcção de erros");
		janela.setVisible(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// janela.setResizable(false);
		janela.setSize(500, 100);
		janela.setLocation(400, 150);

		contentor = janela.getContentPane();

		showOptionPanel(TIPO_VERSAO, "Escolha a versão a executar: ");

	}

	public void showOptionPanel(String[] options, String label) {
		contentor.removeAll();
		JPanel painel_global = new JPanel();
		painel_global.setLayout(new BorderLayout());

		JPanel painel_botoes = new JPanel();

		for (int i = 0; i < options.length; i++) {
			JButton botao = new JButton(options[i]);
			botao.addActionListener(this);
			painel_botoes.add(botao);
		}

		painel_global.add(new JLabel(label, JLabel.CENTER), BorderLayout.NORTH);
		painel_global.add(painel_botoes, BorderLayout.CENTER);
		contentor.add(painel_global);
		contentor.repaint();
		contentor.validate();
	}

	private class Versao {

		private static final int N_ITERACOES_SIMULACAO = 10000000;

		private FicheiroDeResultados ficheiro;
		private TecnicaControloErros tecnica_a_correr;
		private int tamanho_mensagem_actual;

		private int[] mensagem;
		private int[] trama_a_enviar;
		private int[] padrao_erros;
		private int[] mensagem_a_receber;

		public void definirMensagem(int[] mensagem) {
			this.mensagem = mensagem;
		}

		public void definirPadraoErros(int[] padrao_erros) {
			this.padrao_erros = padrao_erros;
		}

		public void definirTecnicaCorrente(TecnicaControloErros tecnica) {
			tecnica_a_correr = tecnica;
			emissor.definirTecnicaControlo(tecnica);
			receptor.definirTecnicaDeteccao(tecnica);
		}

		public void definirTamanhoMensagemCorrente(int tamanho_mensagem) {
			tamanho_mensagem_actual = tamanho_mensagem;
			tecnica_a_correr.definirEstruturaTramaAEnviar(tamanho_mensagem);
		}

		public TecnicaControloErros consultarTecnica() {
			return tecnica_a_correr;
		}

		// ----------------------------------------- VERSAO DE DEMONSTRAÇAO
		// -------------------------------------------
		public void correrVersaoDemonstracao(char opcao) {

			ficheiro = new FicheiroDeResultados("Versão de Demonstração.txt");

			try {
				ficheiro.abrirFicheiro();

				ficheiro.escreverLinha(tecnica_a_correr.toString());

				// O EMISSOR GERA A MENSAGEM A ENVIAR, NO CASO DA OPÇAO B
				if (opcao == 'B') {
					mensagem = emissor.gerarDados(tamanho_mensagem_actual);
				}

				ficheiro.escreverLinha("Mensagem: " + Arrays.toString(mensagem));

				// O EMISSOR GERA A TRAMA CONFORME A TECNICA A UTILIZAR
				trama_a_enviar = emissor.gerarTrama(mensagem);

				ficheiro.escreverLinha("Trama: "
						+ Arrays.toString(trama_a_enviar));

				// O CANAL DEFINE UM PADRAO DE ERROS EM FUNÇAO DO PADRAO DE
				// ERROS
				// RECEBIDO, NO CASO DA OPÇAO B
				if (opcao == 'B') {
					padrao_erros = canal
							.geracaoPadraoErros(trama_a_enviar.length);
				}

				ficheiro.escreverLinha("Padrão de erros: "
						+ Arrays.toString(padrao_erros));

				// A MENSAGEM ORIGINAL É ALTERADA CONFORME OS ERROS QUE
				// OCORRERAM,
				// FORMANDO A MENSAGEM A RECEBER
				mensagem_a_receber = canal.geracaoTramaReceber(trama_a_enviar,
						padrao_erros);

				ficheiro.escreverLinha("Mensagem recebida: "
						+ Arrays.toString(mensagem_a_receber));

				// O RECEPTOR PROCEDE Á VERIFICAÇAO DE ERROS
				boolean deuErro = receptor.detectarErros(mensagem_a_receber);

				ficheiro.escreverLinha("Erros detectados: "
						+ (deuErro ? "Sim" : "Não"));
				ficheiro.escreverLinha("Critério de detecção de erros: "
						+ tecnica_a_correr.criterioDecisao());

				if (tecnica_a_correr.permiteCorrecaoErros() && deuErro) {
					int[] erros_corrigidos = tecnica_a_correr
							.CorrecaoErrosNaTrama(mensagem_a_receber);
					ficheiro.escreverLinha("Trama Corrigida: "
							+ Arrays.toString(erros_corrigidos));
				}

			} finally {
				ficheiro.fecharFicheiro();
			}

			System.exit(0);

		}

		// ----------------------------------------- VERSAO DE SIMULAÇÃO
		// -------------------------------------------
		public void correrVersaoSimulacao() {

			ficheiro = new FicheiroDeResultados("Versão de Simulação.csv");
			Estatisticas modulo_estatisticas = new Estatisticas();

			try {

				ficheiro.abrirFicheiro();

				ficheiro.escreverLinha("Tecnica utilizada,tamanho mensagem(m),Peb,P(sem erros),E[bits errados],P(nd | erros),P(cc | erros)");

				// Para cada tecnica aplicavel
				for (int tecnica_index = 0; tecnica_index < OPCOES[0].length; tecnica_index++) {

					definirTecnicaCorrente(instanciarTecnicaPorString(OPCOES[0][tecnica_index]));

					// Por cada tamanho de mensagem
					for (int length_index = 0; length_index < TAMANHO_MENSAGEM.length; length_index++) {

						definirTamanhoMensagemCorrente(TAMANHO_MENSAGEM[length_index]);

						// Em cada probabilidade de erro de bit
						for (int peb_index = 0; peb_index < PROBABILIDADES_ERRO_SIMULACAO.length; peb_index++) {

							canal.definirProbabilidadeErro(PROBABILIDADES_ERRO_SIMULACAO[peb_index]);

							// Perfaz as iteraçoes de simulaçao
							for (int i = 0; i < N_ITERACOES_SIMULACAO; i++) {

								mensagem = emissor
										.gerarDados(tamanho_mensagem_actual);

								trama_a_enviar = emissor.gerarTrama(mensagem);

								padrao_erros = canal
										.geracaoPadraoErros(trama_a_enviar.length);

								mensagem_a_receber = canal.geracaoTramaReceber(
										trama_a_enviar, padrao_erros);

								// Detecçao de erros
								boolean erros_detectados = receptor
										.detectarErros(mensagem_a_receber);

								// Tentativa de correcçao de erros - [so é
								// invocada com o codigo de hamming]
								int[] trama_corrigida = null;
								if (erros_detectados
										&& tecnica_a_correr
												.permiteCorrecaoErros()) {
									trama_corrigida = tecnica_a_correr
											.CorrecaoErrosNaTrama(mensagem_a_receber);
								}

								// contagem das amostras obtidas
								modulo_estatisticas.analisar_amostras(
										trama_a_enviar, mensagem_a_receber,
										trama_corrigida, erros_detectados);

							}
							ficheiro.escreverLinha(OPCOES[0][tecnica_index]
									+ ","
									+ TAMANHO_MENSAGEM[length_index]
									+ ","
									+ PROBABILIDADES_ERRO_SIMULACAO[peb_index]
									+ ","
									+ modulo_estatisticas
											.probabilidade_transmissao_sem_erros()
									+ ","
									+ modulo_estatisticas
											.valor_esperado_numero_bits_errados()
									+ ","
									+ modulo_estatisticas
											.prob_erros_nao_detectados_dado_ocorreram_erros()
									+ ","
									+ modulo_estatisticas
											.prob_correccao_correcta_dado_ocorreram_erros());
							modulo_estatisticas.reset_estatisticas();
						}
					}
				}
			} finally {
				ficheiro.fecharFicheiro();
			}

			System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals(TIPO_VERSAO[0])) {
			showOptionPanel(OPCOES[0], "Escolha a técnica a demonstrar: ");
		} else if (e.getActionCommand().equals(TIPO_VERSAO[1])) {

			janela.setVisible(false);
			versao_a_correr.correrVersaoSimulacao();

		} else if (e.getActionCommand().equals(OPCOES[0][0])) {
			versao_a_correr.definirTecnicaCorrente(new BitDeParidade());
			showOptionPanel(OPCOES[1], "Escolha o tamanho da mensagem: ");
		} else if (e.getActionCommand().equals(OPCOES[0][1])) {
			versao_a_correr
					.definirTecnicaCorrente(new CodigoRedundanciaCiclica());

			showOptionPanel(OPCOES[1], "Escolha o tamanho da mensagem: ");
		} else if (e.getActionCommand().equals(OPCOES[0][2])) {
			versao_a_correr.definirTecnicaCorrente(new CodigoDeHamming());
			showOptionPanel(OPCOES[1], "Escolha o tamanho da mensagem: ");

		} else if (e.getActionCommand().equals(OPCOES[1][0])) {
			versao_a_correr.definirTamanhoMensagemCorrente(TAMANHO_MENSAGEM[0]);
			showOptionPanel(OPCOES[2], "Escolha uma das seguintes opções: ");
		} else if (e.getActionCommand().equals(OPCOES[1][1])) {
			versao_a_correr.definirTamanhoMensagemCorrente(TAMANHO_MENSAGEM[1]);
			showOptionPanel(OPCOES[2], "Escolha uma das seguintes opções: ");

		} else if (e.getActionCommand().equals(OPCOES[2][0])) {
			// Opcao A : O utilizador introduz os bits de dados(M) e o padrao de
			// erros(E)
			boolean dados_aceitaveis = false;
			while (!dados_aceitaveis) {
				String dados = JOptionPane.showInputDialog(janela,
						"Introduza os "
								+ versao_a_correr.consultarTecnica()
										.getMessageLength()
								+ " bits da Mensagem a enviar:");
				String padrao_erros = JOptionPane.showInputDialog(janela,
						"Introduza os "
								+ versao_a_correr.consultarTecnica()
										.getTotalLength()
								+ " bits do padrão de erro:");
				if (dados.length() == versao_a_correr.consultarTecnica()
						.getMessageLength()
						&& padrao_erros.length() == versao_a_correr
								.consultarTecnica().getTotalLength()) {
					// FALTA VERIFICAR QUE SAO TODOS OS BITS 0 OU 1
					versao_a_correr
							.definirMensagem(convertStringToBitsArray(dados));
					versao_a_correr
							.definirPadraoErros(convertStringToBitsArray(padrao_erros));
					dados_aceitaveis = true;

					// DAQUI COMEÇA A CORRER A VERSAO DE DEMONSTRAÇAO
					versao_a_correr.correrVersaoDemonstracao('A');
				}
			}
			janela.setVisible(false);

		} else if (e.getActionCommand().equals(OPCOES[2][1])) {
			// Opcao B : O utilizador introduz a peb --> define a parametro do
			// canal
			boolean dados_aceitaveis = false;
			while (!dados_aceitaveis) {
				String peb_str = JOptionPane.showInputDialog(janela,
						"Introduza a probabilidade de Erro: ");
				if (peb_str.contains("0.")) {
					double peb = Double.parseDouble(peb_str);
					canal.definirProbabilidadeErro(peb);
					dados_aceitaveis = true;

					// DAQUI COMEÇA A CORRER A VERSAO DE DEMONSTRAÇAO
					versao_a_correr.correrVersaoDemonstracao('B');
				}
			}
			janela.setVisible(false);

		}
	}

	private int[] convertStringToBitsArray(String str) {
		int[] bits_array = new int[str.length()];

		for (int i = 0; i < str.length(); i++) {
			bits_array[i] = Character.digit(str.charAt(i), 10);
		}
		return bits_array;
	}

	private TecnicaControloErros instanciarTecnicaPorString(String nome_tecnica) {
		if (nome_tecnica == OPCOES[0][0]) {
			return new BitDeParidade();
		}
		if (nome_tecnica.equals(OPCOES[0][1])) {
			return new CodigoRedundanciaCiclica();
		} else {
			return new CodigoDeHamming();
		}
	}
}
