package controller;

import dao.AdminDAO;
import dao.MedicoDAO;
import dao.PacienteDAO;
import model.Administrador;
import model.Medico;
import model.Paciente;
import view.LoginView;

/**
 * LoginController (Controlador de Login)
 * Esta e a classe principal que controla o fluxo de autenticacao.
 * Ela atua como o "portao de entrada" da aplicacao.
 */
public class LoginController {

    // O LoginController so precisa conhecer sua propria View
    private LoginView view;

    /**
     * Construtor do LoginController.
     * E chamado pela classe Main.java assim que o programa inicia.
     */
    public LoginController() {
        // Inicializa (cria o objeto) da LoginView para que possa ser usada
        this.view = new LoginView();
    }

    /**
     * Ponto de entrada e loop principal do menu de Login.
     * Fica ativo ate o usuario escolher a opcao 0 (Sair).
     */
    public void iniciar() {
        int tipoUsuario;
        do {
            // 1. Chama a View para exibir o menu (Paciente, Medico, Admin, Sair) e pegar a opcao
            tipoUsuario = view.exibirMenuLogin();
            
            // 2. Processa a escolha do usuario
            switch (tipoUsuario) {
                case 1:
                    autenticarPaciente(); // Chama o metodo de login do paciente
                    break;
                case 2:
                    autenticarMedico(); // Chama o metodo de login do medico
                    break;
                case 3:
                    autenticarAdmin(); // Chama o metodo de login do admin
                    break;
                case 0:
                    view.exibirMensagem("Saindo do sistema..."); // Encerra o programa
                    break;
                default:
                    view.exibirMensagem("Opção inválida."); // Loop repete
            }
        } while (tipoUsuario != 0); // Continua rodando ate o usuario digitar 0
    }

    /**
     * Logica de autenticacao para o Paciente.
     */
    private void autenticarPaciente() {
        // 1. Pede a View o CPF e a Senha (o '1' indica que e tipo Paciente)
        String[] creds = view.obterCredenciais(1); // creds[0] = CPF, creds[1] = Senha
        
        // 2. Prepara o DAO para consultar a tabela 'paciente'
        PacienteDAO dao = new PacienteDAO();
        
        // 3. Tenta fazer o login usando os dados fornecidos
        Paciente p = dao.login(creds[0], creds[1]);

        // 4. Verifica o resultado
        if (p != null) {
            // SUCESSO: O DAO retornou um objeto Paciente valido
            view.exibirMensagem("Login de Paciente bem-sucedido! Bem-vindo(a), " + p.getNome());
            
            // 5. TRANSFERENCIA DE CONTROLE:
            // Cria o PacienteController, entregando os dados do paciente logado
            PacienteController pc = new PacienteController(p);
            // Inicia o loop do menu do paciente. O LoginController fica em espera.
            pc.iniciar();
        } else {
            // FALHA: O DAO retornou 'null'
            view.exibirMensagem("CPF ou senha inválidos.");
        }
    }

    /**
     * Logica de autenticacao para o Medico. (Mesma logica do Paciente)
     */
    private void autenticarMedico() {
        // 1. Pede a View o CRM e a Senha (tipo 2)
        String[] creds = view.obterCredenciais(2); 
        
        // 2. Prepara o DAO para consultar a tabela 'medico'
        MedicoDAO dao = new MedicoDAO();
        
        // 3. Tenta fazer o login
        Medico m = dao.login(creds[0], creds[1]); 

        if (m != null) {
            // SUCESSO
            view.exibirMensagem("Login de Médico bem-sucedido! Bem-vindo(a), " + m.getNome());
            
            // 5. TRANSFERENCIA DE CONTROLE: Inicia o MedicoController
            MedicoController mc = new MedicoController(m);
            mc.iniciar();
        } else {
            // FALHA
            view.exibirMensagem("CRM ou senha inválidos.");
        }
    }

    /**
     * Logica de autenticacao para o Administrador. (Mesma logica)
     */
    private void autenticarAdmin() {
        // 1. Pede a View o Usuario e a Senha (tipo 3)
        String[] creds = view.obterCredenciais(3);
        
        // 2. Prepara o DAO para consultar a tabela 'administrador'
        AdminDAO dao = new AdminDAO();
        
        // 3. Tenta fazer o login
        Administrador a = dao.login(creds[0], creds[1]); 

        if (a != null) {
            // SUCESSO
            view.exibirMensagem("Login de Administrador bem-sucedido! Bem-vindo(a), " + a.getNome());
            
            // 5. TRANSFERENCIA DE CONTROLE: Inicia o AdminController
            AdminController ac = new AdminController(a);
            ac.iniciar();
        } else {
            // FALHA
            view.exibirMensagem("Usuário ou senha inválidos.");
        }
    }
}