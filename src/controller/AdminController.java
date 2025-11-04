package controller;

import dao.AdminDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import java.sql.SQLException;
import java.util.List;
import model.Administrador;
import model.Medico;
import model.Paciente;
import view.AdminView;
import view.MedicoView;
import view.PacienteView;

/**
 * Controller para o Administrador.
 * Esta classe gerencia a lógica de negócios do portal do administrador,
 * coordenando as Views (para exibir menus) e os DAOs (para buscar/salvar dados).
 */
public class AdminController {

    // --- Atributos Principais ---
    
    // Armazena a instância do administrador que fez login
    private Administrador adminLogado;
    // A View principal deste controller (o menu do admin)
    private AdminView view;
    
    // DAOs e Views necessários para as operações deste controller
    private PacienteDAO pacienteDAO;
    private PacienteView pacienteView;
    private MedicoDAO medicoDAO;
    private MedicoView medicoView;
    private AdminDAO adminDAO;

    /**
     * Construtor do AdminController.
     * É chamado pelo LoginController quando um admin é autenticado.
     * * @param adminLogado O objeto Administrador que acabou de fazer login.
     */
    public AdminController(Administrador adminLogado) {
        // Guarda o admin logado para uso futuro
        this.adminLogado = adminLogado;
        
        // Inicializa (instancia) todas as classes auxiliares que este controller vai usar
        this.view = new AdminView();
        this.pacienteDAO = new PacienteDAO();
        this.pacienteView = new PacienteView();
        this.medicoDAO = new MedicoDAO();
        this.medicoView = new MedicoView();
        this.adminDAO = new AdminDAO();
    }

    /**
     * Método principal que inicia o loop do menu do administrador.
     * Fica em execução até que o admin escolha a opção '0' (Sair).
     */
    public void iniciar() {
        int opcao;
        do {
            // 1. Pede à View para exibir o menu e capturar a opção do usuário
            opcao = view.exibirMenuAdmin();
            
            // 2. Direciona a ação com base na opção escolhida
            switch (opcao) {
                case 1:
                    gerenciarPacientes(); // Chama o sub-menu de pacientes
                    break;
                case 2:
                    gerenciarMedicos(); // Chama o sub-menu de médicos
                    break;
                case 3:
                    criarAdmin(); // Chama a função de criar novo admin
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu principal (Logout)...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0); // Repete o menu enquanto a opção não for '0'
    }
    
    // --- GERENCIAR PACIENTES (Sub-menu) ---

    /**
     * Exibe o menu de CRUD (Create, Read, Update, Delete) para Pacientes.
     * Este é um loop de sub-menu.
     */
    private void gerenciarPacientes() {
        int opcao;
        do {
            // 1. Usa a PacienteView para mostrar o menu de CRUD de pacientes
            opcao = pacienteView.exibirMenuAdminCRUD(); 
            
            // 2. Direciona para a operação de CRUD específica
            switch (opcao) {
                case 1:
                    criarPaciente();
                    break;
                case 2:
                    listarPacientes();
                    break;
                case 3:
                    atualizarPaciente();
                    break;
                case 4:
                    excluirPaciente();
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu de Administrador...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // --- GERENCIAR MÉDICOS (Sub-menu) ---

    /**
     * Exibe o menu de CRUD (Create, Read, Update, Delete) para Médicos.
     */
    private void gerenciarMedicos() {
        int opcao;
        do {
            // 1. Usa a MedicoView para mostrar o menu de CRUD de médicos
            opcao = medicoView.exibirMenuAdminCRUD(); 
            
            switch (opcao) {
                case 1:
                    criarMedico();
                    break;
                case 2:
                    listarMedicos();
                    break;
                case 3:
                    atualizarMedico();
                    break;
                case 4:
                    excluirMedico();
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu de Administrador...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    /**
     * (Opção 3 do Menu Principal) Cria um novo Administrador.
     */
    private void criarAdmin() {
        view.exibirMensagem("--- Cadastro de Novo Administrador ---");
        
        // 1. Coleta os dados (usuário, nome) da AdminView
        Administrador a = view.obterDadosAdmin();
        String senha = view.obterSenha();
        
        try {
            // 2. Tenta salvar no banco via AdminDAO
            adminDAO.salvar(a, senha);
            view.exibirMensagem("Administrador salvo com sucesso!");
            
        } catch (SQLException e) {
            // 3. Tratamento de Erro: Verifica se o erro é de "usuário duplicado"
            // O código 1062 (MySQL) ou SQLState 23000 (Padrão SQL) indicam violação de chave única (UNIQUE)
             if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O usuário '" + a.getUsuario() + "' já está cadastrado.");
            } else {
                // Outro erro de banco
                view.exibirMensagem("\nErro de banco de dados ao salvar admin:");
                e.printStackTrace(); // Mostra o erro completo no console para debug
            }
        }
    }

    // --- MÉTODOS DO CRUD DE PACIENTE ---
    
    /**
     * (CRUD - Create) Cria um novo paciente.
     */
    private void criarPaciente() {
        view.exibirMensagem("--- Cadastro de Novo Paciente ---");
        
        // 1. Coleta dados (nome, cpf, etc) da PacienteView
        Paciente p = pacienteView.obterDadosPaciente(null); // 'null' indica que é um paciente novo
        String senha = pacienteView.obterSenha();

        try {
            // 2. Tenta salvar no banco via PacienteDAO
            pacienteDAO.salvar(p, senha); 
            view.exibirMensagem("Paciente salvo com sucesso!");
        } catch (SQLException e) {
            // 3. Tratamento de Erro: Verifica se é CPF duplicado
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                pacienteView.exibirMensagem("\nERRO: O CPF '" + p.getCpf() + "' já está cadastrado.");
            } else {
                pacienteView.exibirMensagem("\nErro de banco de dados ao salvar paciente:");
                e.printStackTrace();
            }
        }
    }

    /**
     * (CRUD - Read) Lista todos os pacientes cadastrados.
     */
    private void listarPacientes() {
        // 1. Busca a lista de pacientes no DAO
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        // 2. Entrega a lista para a View, que sabe como formatar e exibir
        pacienteView.listarPacientes(pacientes);
    }

    /**
     * (CRUD - Update) Atualiza um paciente existente.
     */
    private void atualizarPaciente() {
        view.exibirMensagem("--- Atualização de Paciente ---");
        
        // 1. Lista os pacientes para o admin saber qual ID ele quer editar
        listarPacientes();
        // 2. Pede à View o ID do paciente a ser atualizado
        int id = pacienteView.obterIdPaciente("ATUALIZAR");
        if (id == -1) return; // Se o ID for inválido (ex: digitou texto), cancela

        try {
            // 3. Pede à View os *novos* dados
            Paciente p = pacienteView.obterDadosPaciente(null); 
            // 4. Define o ID do paciente que será atualizado
            p.setId_paciente(id);
            // 5. Manda o DAO executar o UPDATE no banco
            pacienteDAO.atualizar(p);
            view.exibirMensagem("Paciente atualizado com sucesso!");
        } catch (Exception e) {
            // 6. Tratamento de Erro: Captura se o novo CPF já existe no banco
             if (e instanceof SQLException && (((SQLException)e).getErrorCode() == 1062 || ((SQLException)e).getSQLState().equals("23000"))) {
                pacienteView.exibirMensagem("\nERRO: Esse CPF já pertence a outro usuário.");
            } else {
                pacienteView.exibirMensagem("\nErro ao atualizar paciente: " + e.getMessage());
            }
        }
    }

    /**
     * (CRUD - Delete) Exclui um paciente.
     */
    private void excluirPaciente() {
        view.exibirMensagem("--- Exclusão de Paciente ---");
        
        // 1. Lista os pacientes
        listarPacientes();
        // 2. Pede à View o ID do paciente a ser excluído
        int id = pacienteView.obterIdPaciente("EXCLUIR");
        if (id == -1) return; // Cancela se ID inválido
        
        try {
            // 3. Manda o DAO executar o DELETE no banco
            pacienteDAO.excluir(id);
            view.exibirMensagem("Paciente excluído com sucesso!");
        } catch (SQLException e) {
            // 4. Tratamento de Erro: (Ex: Se o banco não permitir a exclusão)
            view.exibirMensagem("\nErro ao excluir paciente: " + e.getMessage());
        }
    }
    
    // --- MÉTODOS DO CRUD DE MÉDICO ---
    // A lógica abaixo é idêntica à do Paciente, mas usando MedicoView e MedicoDAO
    
    /**
     * (CRUD - Create) Cria um novo médico.
     */
    private void criarMedico() {
        view.exibirMensagem("--- Cadastro de Novo Médico ---");
        Medico m = medicoView.obterDadosMedico(); // Pede dados (Nome, CRM)
        String senha = medicoView.obterSenha(); // Pede a senha
        
        try {
            medicoDAO.salvar(m, senha); // Tenta salvar
            view.exibirMensagem("Médico salvo com sucesso!");
        } catch (SQLException e) {
            // Trata erro de CRM duplicado
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O CRM '" + m.getCrm() + "' já está cadastrado.");
            } else {
                view.exibirMensagem("\nErro de banco de dados ao salvar médico:");
                e.printStackTrace();
            }
        }
    }

    /**
     * (CRUD - Read) Lista todos os médicos.
     */
    private void listarMedicos() {
        List<Medico> medicos = medicoDAO.listarTodos(); // Busca
        medicoView.listarMedicos(medicos); // Exibe
    }

    /**
     * (CRUD - Update) Atualiza um médico.
     */
    private void atualizarMedico() {
        view.exibirMensagem("--- Atualização de Médico ---");
        listarMedicos(); // Mostra a lista
        int id = medicoView.obterIdMedico("ATUALIZAR"); // Pede o ID
        if (id == -1) return;

        try {
            Medico m = medicoView.obterDadosMedico(); // Pede novos dados
            m.setId_medico(id); // Define o ID
            medicoDAO.atualizar(m); // Tenta atualizar
            view.exibirMensagem("Médico atualizado com sucesso!");
        } catch (Exception e) {
            // Trata erro de CRM duplicado
            if (e instanceof SQLException && (((SQLException)e).getErrorCode() == 1062 || ((SQLException)e).getSQLState().equals("23000"))) {
                view.exibirMensagem("\nERRO: Esse CRM já pertence a outro médico.");
            } else {
                view.exibirMensagem("\nErro ao atualizar médico: " + e.getMessage());
            }
        }
    }

    /**
     * (CRUD - Delete) Exclui um médico.
     */
    private void excluirMedico() {
        view.exibirMensagem("--- Exclusão de Médico ---");
        listarMedicos(); // Mostra a lista
        int id = medicoView.obterIdMedico("EXCLUIR"); // Pede o ID
        if (id == -1) return;
        
        try {
            medicoDAO.excluir(id); // Tenta excluir
            view.exibirMensagem("Médico excluído com sucesso!");
        } catch (SQLException e) {
            view.exibirMensagem("\nErro ao excluir médico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}