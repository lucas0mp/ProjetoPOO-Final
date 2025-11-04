package controller;

import dao.LembreteDAO;
import dao.MedicaoDAO;
import java.util.List;
import model.Lembrete;
import model.Paciente;
import view.LembreteView;
import view.MedicaoView;
import view.PacienteView;

/**
 * Controller para o Paciente.
 * Gerencia a logica de negocios do portal do paciente.
 * E iniciado pelo LoginController quando um paciente e autenticado.
 */
public class PacienteController {
    
    // --- Atributos ---
    // **ENCAPSULAMENTO**: Todos os atributos sao 'private'.
    // O acesso a eles e controlado pelo proprio controlador.
    
    // Guarda a instancia do paciente que fez login
    private Paciente pacienteLogado;
    
    // Views que este controlador utiliza para interagir com o usuario
    private PacienteView view;
    private LembreteView lembreteView;
    private MedicaoView medicaoView; // View para medicoes
    
    /**
     * Construtor. Chamado pelo LoginController.
     * @param pacienteLogado O objeto Paciente que foi autenticado.
     */
    public PacienteController(Paciente pacienteLogado) {
        // Armazena o paciente que fez login
        this.pacienteLogado = pacienteLogado;
        
        // Inicializa (cria as instancias) das Views que serao usadas
        this.view = new PacienteView();
        this.lembreteView = new LembreteView();
        this.medicaoView = new MedicaoView(); // Instancia a view de medicao
    }
    
    /**
     * Ponto de entrada e loop principal do menu do Paciente.
     * Fica em execucao ate o paciente escolher '0' (Sair).
     */
    public void iniciar() {
        int opcao;
        do {
            // 1. Pede a PacienteView para exibir o menu e pegar a opcao
            opcao = view.exibirMenuPaciente(); 
            
            // 2. Direciona a acao com base na escolha
            switch (opcao) {
                case 1:
                    visualizarMeusLembretes();
                    break;
                case 2:
                    visualizarMinhasMedicoes(); // Funcao para ver o historico
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu principal...");
                    break;
                default:
                    view.exibirMensagem("Opcao invalida!");
            }
        } while (opcao != 0); // Repete o menu enquanto a opcao nao for 0
    }
    
    /**
     * Case 1: Busca e exibe os lembretes de medicacao do paciente.
     */
    private void visualizarMeusLembretes() {
        // 1. Cria o DAO
        LembreteDAO dao = new LembreteDAO();
        
        // 2. Busca no banco os lembretes *apenas* deste paciente.
        // **ENCAPSULAMENTO**: Usa o ID do 'pacienteLogado' (guardado de forma privada)
        // para filtrar a busca no banco de dados.
        List<Lembrete> lembretes = dao.listarPorPaciente(pacienteLogado.getId_paciente());
        
        // 3. Entrega a lista de lembretes para a LembreteView formatar e exibir
        lembreteView.exibirListaLembretes(lembretes);
    }
    
    /**
     * Case 2: Busca e exibe o historico de medicoes (Glicemia e Pressao).
     */
    private void visualizarMinhasMedicoes() {
        // 1. Cria o DAO
        MedicaoDAO dao = new MedicaoDAO();
        
        // 2. Busca no banco as medicoes *apenas* deste paciente
        List<String> medicoes = dao.listarMedicoesPorPaciente(pacienteLogado.getId_paciente());
        
        // 3. Entrega a lista (ja formatada pelo DAO) para a MedicaoView exibir
        medicaoView.listarMedicoes(medicoes);
    }
}