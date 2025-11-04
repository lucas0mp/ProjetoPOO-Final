package controller;

import model.Lembrete;
import model.MedicaoGlicemia;
import model.MedicaoPressao;
import model.Medico;
import model.Paciente;
import model.Prescricao;
import dao.AcompanhaDAO;
import dao.LembreteDAO;
import dao.MedicaoDAO;
import dao.PacienteDAO;
import dao.PrescricaoDAO;
import view.MedicaoView;
import view.MedicoView;
import view.PacienteView;
// Imports do Java
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;


/**
 * Controller para o Medico.
 * Gerencia a logica de negocios do portal do medico.
 * E iniciado pelo LoginController quando um medico faz login.
 */
public class MedicoController {
    
    // --- Atributos do Controller ---
    
    // Guarda o medico que fez login
    private Medico medicoLogado;
    // Views e DAOs necessarios para as operacoes
    private MedicoView view;
    private PacienteView pacienteView;
    private PacienteDAO pacienteDAO;
    private MedicaoView medicaoView;
    private MedicaoDAO medicaoDAO;
    private PrescricaoDAO prescricaoDAO;
    private LembreteDAO lembreteDAO;
    private AcompanhaDAO acompanhaDAO; // Para associacao automatica medico-paciente

    /**
     * Construtor. Recebe o medico que fez login
     * e inicializa todas as classes de DAO e View necessarias.
     */
    public MedicoController(Medico medicoLogado) {
        this.medicoLogado = medicoLogado;
        
        // Inicializa as Views
        this.view = new MedicoView();
        this.pacienteView = new PacienteView();
        this.medicaoView = new MedicaoView();
        
        // Inicializa os DAOs
        this.pacienteDAO = new PacienteDAO();
        this.medicaoDAO = new MedicaoDAO();
        this.prescricaoDAO = new PrescricaoDAO();
        this.lembreteDAO = new LembreteDAO();
        this.acompanhaDAO = new AcompanhaDAO(); 
    }
    
    /**
     * Ponto de entrada e loop principal do menu do Medico.
     */
    public void iniciar() {
        int opcao;
        do {
            // 1. Exibe o menu principal do medico e pega a opcao
            opcao = view.exibirMenuMedico();
            
            // 2. Direciona a acao
            switch (opcao) {
                case 1:
                    criarPaciente();
                    break;
                case 2:
                    listarMeusPacientes();
                    break;
                case 3:
                    adicionarPrescricao();
                    break;
                case 4:
                    adicionarMedicao();
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu principal (Logout)...");
                    break;
                default:
                    view.exibirMensagem("Opcao invalida!");
            }
        } while (opcao != 0); // Repete ate o medico escolher 0 (Sair)
    }
    
    /**
     * Case 1: Criar novo Paciente.
     * Logica principal:
     * 1. Salva o paciente.
     * 2. Associa automaticamente o paciente ao medico logado (tabela 'acompanha').
     */
    private void criarPaciente() {
        view.exibirMensagem("--- Cadastro de Novo Paciente ---");
        // 1. Coleta dados do paciente (nome, cpf, etc) e senha
        Paciente p = pacienteView.obterDadosPaciente(null);
        String senha = pacienteView.obterSenha();

        try {
            // --- PARTE 1: SALVAR O PACIENTE ---
            // Tenta salvar o paciente e pegar o ID gerado pelo banco
            int novoIdPaciente = pacienteDAO.salvar(p, senha);
            
            if (novoIdPaciente == -1) {
                throw new SQLException("Falha ao criar paciente, ID nao foi gerado.");
            }
            view.exibirMensagem("Paciente salvo com sucesso! (ID: " + novoIdPaciente + ")");
            
            // --- PARTE 2: LOGICA DE ASSOCIACAO AUTOMATICA ---
            // Vincula o paciente recem-criado ao medico que esta logado
            try {
                int idMedicoLogado = medicoLogado.getId_medico();
                acompanhaDAO.salvar(idMedicoLogado, novoIdPaciente);
                view.exibirMensagem("Paciente '" + p.getNome() + "' foi atribuido automaticamente a voce.");
                
            } catch (SQLException eAcompanha) {
                // Se der erro aqui (ex: ja existe), apenas avisa, pois o paciente foi salvo
                view.exibirMensagem("\nAVISO: Paciente salvo, mas falha ao atribuir a voce (erro: " + eAcompanha.getMessage() + ")");
            }

        } catch (SQLException e) {
            // --- TRATAMENTO DE ERRO (ex: CPF DUPLICADO) ---
            // Codigo 1062 = Erro de entrada duplicada (UNIQUE) no MySQL
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O CPF '" + p.getCpf() + "' ja pertence a outra pessoa. Tente novamente.");
            } else {
                view.exibirMensagem("\nErro inesperado de banco de dados ao salvar paciente:");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Case 2: Listar Pacientes.
     * Lista APENAS os pacientes vinculados a este medico.
     */
    private void listarMeusPacientes() {
        view.exibirMensagem("--- Meus Pacientes ---");
        // Chama o metodo do DAO que busca pacientes por ID do medico
        List<Paciente> pacientes = pacienteDAO.listarPorMedico(medicoLogado.getId_medico());
        // Envia a lista para a View exibir
        pacienteView.listarPacientes(pacientes);
    }
    
    /**
     * Case 3: Adicionar Prescricao e Lembrete.
     * Fluxo: 1. Escolhe Paciente -> 2. Cria Prescricao -> 3. Cria Lembrete
     */
    private void adicionarPrescricao() {
        view.exibirMensagem("--- Adicionar Prescricao ---");
        
        // 1. Lista e seleciona o paciente
        listarMeusPacientes();
        int idPaciente = pacienteView.obterIdPaciente("SELECIONAR");
        if (idPaciente == -1) {
            view.exibirMensagem("Operacao cancelada.");
            return;
        }
        
        // 2. Coleta dados da prescricao
        Prescricao p = view.obterDadosPrescricao(medicoLogado.getId_medico(), idPaciente);
        
        // 3. Salva a prescricao e pega o ID gerado
        int idPrescricao = prescricaoDAO.salvar(p);
        if (idPrescricao == -1) {
            view.exibirMensagem("Falha ao salvar a prescricao.");
            return;
        }
        
        // 4. Coleta o horario para o lembrete
        String horaMinuto = view.obterHorarioLembrete(); // Espera "HH:mm"
        
        // 5. Cria e salva o objeto Lembrete
        try {
            Lembrete lembrete = new Lembrete();
            lembrete.setId_prescricao(idPrescricao); // Vincula o lembrete a prescricao
            
            // Converte a string "HH:mm" em um objeto Date
            String[] partes = horaMinuto.split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partes[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(partes[1]));
            cal.set(Calendar.SECOND, 0);
            
            lembrete.setHorario_programado(cal.getTime());
            lembrete.setStatus("Pendente"); // Status padrao
            
            // Salva o lembrete no banco
            lembreteDAO.salvar(lembrete);
            view.exibirMensagem("Prescricao e Lembrete salvos com sucesso!");
            
        } catch (Exception e) {
            // Trata erro de formato de hora (ex: "abc" em vez de "08:00")
            view.exibirMensagem("Erro ao criar data do lembrete (use formato HH:mm): " + e.getMessage());
        }
    }

    /**
     * Case 4: Adicionar Medicao.
     * Permite ao medico registrar Glicemia ou Pressao para um paciente.
     */
    private void adicionarMedicao() {
        view.exibirMensagem("--- Adicionar Medicao ---");
        
        // 1. Lista e seleciona o paciente
        listarMeusPacientes();
        int idPaciente = pacienteView.obterIdPaciente("SELECIONAR");
        if (idPaciente == -1) {
            view.exibirMensagem("Operacao cancelada.");
            return;
        }
        
        // 2. Escolher tipo de medicao (Glicemia ou Pressao)
        int tipoMedicao = medicaoView.exibirMenuTipoMedicao();
        
        switch(tipoMedicao) {
            case 1: // Glicemia
                try {
                    // Pede os dados de glicemia
                    MedicaoGlicemia mg = medicaoView.obterDadosGlicemia(idPaciente);
                    // Salva no banco (DAO cuida da transacao em duas tabelas)
                    if (medicaoDAO.salvarGlicemia(mg)) {
                        view.exibirMensagem("Medicao de Glicemia salva com sucesso!");
                    } else {
                        view.exibirMensagem("Erro ao salvar medicao de Glicemia.");
                    }
                } catch (NumberFormatException e) {
                    // Trata erro se o usuario digitar texto no lugar de numero
                    view.exibirMensagem("Erro: Nivel de glicose deve ser um numero.");
                }
                break;
            case 2: // Pressao
                try {
                    // Pede os dados de pressao
                    MedicaoPressao mp = medicaoView.obterDadosPressao(idPaciente);
                    // Salva no banco
                    if (medicaoDAO.salvarPressao(mp)) {
                        view.exibirMensagem("Medicao de Pressao salva com sucesso!");
                    } else {
                        view.exibirMensagem("Erro ao salvar medicao de Pressao.");
                    }
                } catch (NumberFormatException e) {
                    view.exibirMensagem("Erro: Pressao deve ser um numero (use ponto, ex: 12.0).");
                }
                break;
            case 0:
                view.exibirMensagem("Operacao cancelada.");
                break;
            default:
                view.exibirMensagem("Tipo de medicao invalido.");
        }
    }
}