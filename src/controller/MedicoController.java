package controller;

// Imports para Models
import model.Lembrete;
import model.MedicaoGlicemia;
import model.MedicaoPressao;
import model.Medico;
import model.Paciente;
import model.Prescricao;

// Imports para DAOs
import dao.AcompanhaDAO;
import dao.LembreteDAO;
import dao.MedicaoDAO;
import dao.PacienteDAO;
import dao.PrescricaoDAO;

// Imports para Views
import view.MedicaoView;
import view.MedicoView;
import view.PacienteView;

// Imports do Java
import java.sql.SQLException;
import java.sql.Statement; // Importante para o DAO de Paciente
import java.util.Calendar;
import java.util.List;


public class MedicoController {
    
    // --- Atributos do Controller ---
    
    private Medico medicoLogado;
    private MedicoView view;
    
    // Views e DAOs necessários para as operações do Médico
    private PacienteView pacienteView;
    private PacienteDAO pacienteDAO;
    private MedicaoView medicaoView;
    private MedicaoDAO medicaoDAO;
    private PrescricaoDAO prescricaoDAO;
    private LembreteDAO lembreteDAO;
    private AcompanhaDAO acompanhaDAO; // Para atribuição automática

    /**
     * Construtor do Controller. Recebe o médico que fez login
     * e inicializa todas as classes de DAO e View necessárias.
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
        this.acompanhaDAO = new AcompanhaDAO(); // Inicializa o novo DAO
    }
    
    /**
     * Ponto de entrada e loop principal do menu do Médico.
     */
    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuMedico();
            
            // Switch com a lógica corrigida
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
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    /**
     * Case 1: Criar novo Paciente.
     * Inclui o tratamento de erro para CPF duplicado e
     * a atribuição automática do paciente ao médico logado.
     */
    private void criarPaciente() {
        view.exibirMensagem("--- Cadastro de Novo Paciente ---");
        Paciente p = pacienteView.obterDadosPaciente(null);
        String senha = pacienteView.obterSenha();

        try {
            // --- PARTE 1: SALVAR O PACIENTE ---
            // Tenta salvar o paciente e pegar o ID gerado (requer o PacienteDAO modificado)
            int novoIdPaciente = pacienteDAO.salvar(p, senha);
            
            if (novoIdPaciente == -1) {
                // Falha se o DAO não retornar um ID válido
                throw new SQLException("Falha ao criar paciente, ID não foi gerado.");
            }
            
            view.exibirMensagem("Paciente salvo com sucesso! (ID: " + novoIdPaciente + ")");
            
            // --- PARTE 2: ATRIBUIR PACIENTE AO MÉDICO (AUTOMÁTICO) ---
            try {
                int idMedicoLogado = medicoLogado.getId_medico();
                acompanhaDAO.salvar(idMedicoLogado, novoIdPaciente);
                view.exibirMensagem("Paciente '" + p.getNome() + "' foi atribuído automaticamente a você.");
                
            } catch (SQLException eAcompanha) {
                // Se der erro na atribuição (ex: já existe), só avisa
                view.exibirMensagem("\nAVISO: Paciente salvo, mas falha ao atribuir a você (erro: " + eAcompanha.getMessage() + ")");
            }

        } catch (SQLException e) {
            // --- TRATAMENTO DE ERRO (ex: CPF DUPLICADO) ---
            // Código 1062 = Duplicate entry (MySQL)
            // SQLState 23000 = Integrity Constraint Violation (Padrão SQL)
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O CPF '" + p.getCpf() + "' já pertence a outra pessoa. Tente novamente.");
            } else {
                // Outro erro inesperado
                view.exibirMensagem("\nErro inesperado de banco de dados ao salvar paciente:");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Case 2: Listar Pacientes.
     * Lista apenas os pacientes vinculados a este médico na tabela 'acompanha'.
     */
    private void listarMeusPacientes() {
        view.exibirMensagem("--- Meus Pacientes ---");
        // Usa o método do DAO para listar apenas os pacientes
        // que este médico acompanha
        List<Paciente> pacientes = pacienteDAO.listarPorMedico(medicoLogado.getId_medico());
        pacienteView.listarPacientes(pacientes);
    }
    
    /**
     * Case 3: Adicionar Prescrição e Lembrete.
     * Guia o médico pela criação de uma prescrição e um lembrete associado.
     */
    private void adicionarPrescricao() {
        view.exibirMensagem("--- Adicionar Prescrição ---");
        
        // 1. Listar e escolher o paciente
        listarMeusPacientes();
        int idPaciente = pacienteView.obterIdPaciente("SELECIONAR");
        if (idPaciente == -1) {
            view.exibirMensagem("Operação cancelada.");
            return;
        }
        
        // 2. Obter dados da prescrição
        Prescricao p = view.obterDadosPrescricao(medicoLogado.getId_medico(), idPaciente);
        
        // 3. Salvar prescrição e obter o ID gerado
        int idPrescricao = prescricaoDAO.salvar(p);
        if (idPrescricao == -1) {
            view.exibirMensagem("Falha ao salvar a prescrição.");
            return;
        }
        
        // 4. Obter dados do lembrete (horário)
        String horaMinuto = view.obterHorarioLembrete(); // Espera "HH:mm"
        
        // 5. Criar e salvar o objeto Lembrete
        try {
            Lembrete lembrete = new Lembrete();
            lembrete.setId_prescricao(idPrescricao);
            
            // Define a data/hora do lembrete (simplificado para hoje)
            String[] partes = horaMinuto.split(":");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partes[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(partes[1]));
            cal.set(Calendar.SECOND, 0);
            
            lembrete.setHorario_programado(cal.getTime());
            lembrete.setStatus("Pendente"); // Status padrão
            
            lembreteDAO.salvar(lembrete);
            view.exibirMensagem("Prescrição e Lembrete salvos com sucesso!");
            
        } catch (Exception e) {
            // Captura erros de formato de hora (ex: "abc" em vez de "08:00")
            view.exibirMensagem("Erro ao criar data do lembrete (use formato HH:mm): " + e.getMessage());
        }
    }

    /**
     * Case 4: Adicionar Medição.
     * Permite ao médico registrar uma medição de Glicemia ou Pressão
     * para um paciente.
     */
    private void adicionarMedicao() {
        view.exibirMensagem("--- Adicionar Medição ---");
        
        // 1. Listar e escolher o paciente
        listarMeusPacientes();
        int idPaciente = pacienteView.obterIdPaciente("SELECIONAR");
        if (idPaciente == -1) {
            view.exibirMensagem("Operação cancelada.");
            return;
        }
        
        // 2. Escolher tipo de medição (Glicemia ou Pressão)
        int tipoMedicao = medicaoView.exibirMenuTipoMedicao();
        
        switch(tipoMedicao) {
            case 1: // Glicemia
                try {
                    MedicaoGlicemia mg = medicaoView.obterDadosGlicemia(idPaciente);
                    if (medicaoDAO.salvarGlicemia(mg)) {
                        view.exibirMensagem("Medição de Glicemia salva com sucesso!");
                    } else {
                        view.exibirMensagem("Erro ao salvar medição de Glicemia.");
                    }
                } catch (NumberFormatException e) {
                    view.exibirMensagem("Erro: Nível de glicose deve ser um número.");
                }
                break;
            case 2: // Pressão
                try {
                    MedicaoPressao mp = medicaoView.obterDadosPressao(idPaciente);
                    if (medicaoDAO.salvarPressao(mp)) {
                        view.exibirMensagem("Medição de Pressão salva com sucesso!");
                    } else {
                        view.exibirMensagem("Erro ao salvar medição de Pressão.");
                    }
                } catch (NumberFormatException e) {
                    view.exibirMensagem("Erro: Pressão deve ser um número (use ponto, ex: 12.0).");
                }
                break;
            case 0:
                view.exibirMensagem("Operação cancelada.");
                break;
            default:
                view.exibirMensagem("Tipo de medição inválido.");
        }
    }
}