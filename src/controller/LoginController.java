package controller;

import dao.AdminDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import model.Administrador;
import model.Medico;
import model.Paciente;
import view.LoginView;

public class LoginController {

    private LoginView view;

    public LoginController() {
        this.view = new LoginView();
    }

    public void iniciar() {
        int tipoUsuario;
        do {
            tipoUsuario = view.exibirMenuLogin();
            switch (tipoUsuario) {
                case 1:
                    autenticarPaciente();
                    break;
                case 2:
                    autenticarMedico();
                    break;
                case 3:
                    autenticarAdmin();
                    break;
                case 0:
                    view.exibirMensagem("Saindo do sistema...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        } while (tipoUsuario != 0);
    }

    private void autenticarPaciente() {
        String[] creds = view.obterCredenciais(1); // 1 = Paciente
        PacienteDAO dao = new PacienteDAO();
        Paciente p = dao.login(creds[0], creds[1]); // creds[0] = CPF, creds[1] = Senha

        if (p != null) {
            view.exibirMensagem("Login de Paciente bem-sucedido! Bem-vindo(a), " + p.getNome());
            // Inicia o controller do paciente, passando o paciente logado
            PacienteController pc = new PacienteController(p);
            pc.iniciar();
        } else {
            view.exibirMensagem("CPF ou senha inválidos.");
        }
    }

    private void autenticarMedico() {
        String[] creds = view.obterCredenciais(2); // 2 = Medico
        MedicoDAO dao = new MedicoDAO();
        Medico m = dao.login(creds[0], creds[1]); // creds[0] = CRM, creds[1] = Senha

        if (m != null) {
            view.exibirMensagem("Login de Médico bem-sucedido! Bem-vindo(a), " + m.getNome());
            MedicoController mc = new MedicoController(m);
            mc.iniciar();
        } else {
            view.exibirMensagem("CRM ou senha inválidos.");
        }
    }

    private void autenticarAdmin() {
        String[] creds = view.obterCredenciais(3); // 3 = Admin
        AdminDAO dao = new AdminDAO();
        Administrador a = dao.login(creds[0], creds[1]); // creds[0] = Usuario, creds[1] = Senha

        if (a != null) {
            view.exibirMensagem("Login de Administrador bem-sucedido! Bem-vindo(a), " + a.getNome());
            AdminController ac = new AdminController(a);
            ac.iniciar();
        } else {
            view.exibirMensagem("Usuário ou senha inválidos.");
        }
    }
}