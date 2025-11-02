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

public class AdminController {

    private Administrador adminLogado;
    private AdminView view;
    
    private PacienteDAO pacienteDAO;
    private PacienteView pacienteView;
    private MedicoDAO medicoDAO;
    private MedicoView medicoView;
    private AdminDAO adminDAO;

    public AdminController(Administrador adminLogado) {
        this.adminLogado = adminLogado;
        this.view = new AdminView();
        
        this.pacienteDAO = new PacienteDAO();
        this.pacienteView = new PacienteView();
        this.medicoDAO = new MedicoDAO();
        this.medicoView = new MedicoView();
        this.adminDAO = new AdminDAO();
    }

    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuAdmin();
            
            switch (opcao) {
                case 1:
                    gerenciarPacientes();
                    break;
                case 2:
                    gerenciarMedicos(); // <-- ESTA É A FUNÇÃO ATUALIZADA
                    break;
                case 3:
                    criarAdmin();
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu principal (Logout)...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    // --- GERENCIAR PACIENTES (Sem alteração) ---

    private void gerenciarPacientes() {
        int opcao;
        do {
            opcao = pacienteView.exibirMenuAdminCRUD(); 
            
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

    // --- GERENCIAR MÉDICOS (Totalmente refatorado) ---

    /**
     * Case 2: Abre o sub-menu de CRUD de Médicos
     */
    private void gerenciarMedicos() {
        int opcao;
        do {
            // 1. Chama o novo menu da MedicoView
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
     * Case 3: Cria um novo Administrador (Sem alteração)
     */
    private void criarAdmin() {
        view.exibirMensagem("--- Cadastro de Novo Administrador ---");
        Administrador a = view.obterDadosAdmin();
        String senha = view.obterSenha();
        
        try {
            adminDAO.salvar(a, senha);
            view.exibirMensagem("Administrador salvo com sucesso!");
        } catch (SQLException e) {
             if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O usuário '" + a.getUsuario() + "' já está cadastrado.");
            } else {
                view.exibirMensagem("\nErro de banco de dados ao salvar admin:");
                e.printStackTrace();
            }
        }
    }

    // --- MÉTODOS DO CRUD DE PACIENTE (Sem alteração) ---
    
    private void criarPaciente() {
        view.exibirMensagem("--- Cadastro de Novo Paciente ---");
        Paciente p = pacienteView.obterDadosPaciente(null);
        String senha = pacienteView.obterSenha();

        try {
            pacienteDAO.salvar(p, senha); 
            view.exibirMensagem("Paciente salvo com sucesso!");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                pacienteView.exibirMensagem("\nERRO: O CPF '" + p.getCpf() + "' já está cadastrado.");
            } else {
                pacienteView.exibirMensagem("\nErro de banco de dados ao salvar paciente:");
                e.printStackTrace();
            }
        }
    }

    private void listarPacientes() {
        List<Paciente> pacientes = pacienteDAO.listarTodos();
        pacienteView.listarPacientes(pacientes);
    }

    private void atualizarPaciente() {
        view.exibirMensagem("--- Atualização de Paciente ---");
        listarPacientes();
        int id = pacienteView.obterIdPaciente("ATUALIZAR");
        if (id == -1) return;

        // NOTA: O 'atualizar' do PacienteDAO também precisa do 'throws SQLException'
        // e de um try-catch aqui para 'Duplicate entry' se o CPF for mudado
        // para um já existente.
        try {
            Paciente p = pacienteView.obterDadosPaciente(null); 
            p.setId_paciente(id);
            pacienteDAO.atualizar(p);
            view.exibirMensagem("Paciente atualizado com sucesso!");
        } catch (Exception e) {
             if (e instanceof SQLException && (((SQLException)e).getErrorCode() == 1062 || ((SQLException)e).getSQLState().equals("23000"))) {
                pacienteView.exibirMensagem("\nERRO: Esse CPF já pertence a outro usuário.");
            } else {
                pacienteView.exibirMensagem("\nErro ao atualizar paciente: " + e.getMessage());
            }
        }
    }

    private void excluirPaciente() {
        view.exibirMensagem("--- Exclusão de Paciente ---");
        listarPacientes();
        int id = pacienteView.obterIdPaciente("EXCLUIR");
        if (id == -1) return;
        
        try {
            pacienteDAO.excluir(id);
            view.exibirMensagem("Paciente excluído com sucesso!");
        } catch (SQLException e) {
            view.exibirMensagem("\nErro ao excluir paciente: " + e.getMessage());
        }
    }
    
    // --- NOVOS MÉTODOS DO CRUD DE MÉDICO ---
    
    private void criarMedico() {
        view.exibirMensagem("--- Cadastro de Novo Médico ---");
        Medico m = medicoView.obterDadosMedico();
        String senha = medicoView.obterSenha();
        
        try {
            medicoDAO.salvar(m, senha);
            view.exibirMensagem("Médico salvo com sucesso!");
        } catch (SQLException e) {
            // Trata erro de CRM duplicado [cite: 3]
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                view.exibirMensagem("\nERRO: O CRM '" + m.getCrm() + "' já está cadastrado.");
            } else {
                view.exibirMensagem("\nErro de banco de dados ao salvar médico:");
                e.printStackTrace();
            }
        }
    }

    private void listarMedicos() {
        List<Medico> medicos = medicoDAO.listarTodos();
        medicoView.listarMedicos(medicos);
    }

    private void atualizarMedico() {
        view.exibirMensagem("--- Atualização de Médico ---");
        listarMedicos();
        int id = medicoView.obterIdMedico("ATUALIZAR");
        if (id == -1) return;

        try {
            Medico m = medicoView.obterDadosMedico(); // Pede novos dados
            m.setId_medico(id); // Seta o ID para o UPDATE
            medicoDAO.atualizar(m);
            view.exibirMensagem("Médico atualizado com sucesso!");
        } catch (Exception e) {
            // Trata erro de CRM duplicado [cite: 3]
            if (e instanceof SQLException && (((SQLException)e).getErrorCode() == 1062 || ((SQLException)e).getSQLState().equals("23000"))) {
                view.exibirMensagem("\nERRO: Esse CRM já pertence a outro médico.");
            } else {
                view.exibirMensagem("\nErro ao atualizar médico: " + e.getMessage());
            }
        }
    }

    private void excluirMedico() {
        view.exibirMensagem("--- Exclusão de Médico ---");
        listarMedicos();
        int id = medicoView.obterIdMedico("EXCLUIR");
        if (id == -1) return;
        
        try {
            medicoDAO.excluir(id);
            view.exibirMensagem("Médico excluído com sucesso!");
        } catch (SQLException e) {
            view.exibirMensagem("\nErro ao excluir médico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}