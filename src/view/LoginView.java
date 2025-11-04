package view;

import java.util.Scanner;

/**
 * LoginView (View / Visao)
 * * Esta classe e responsavel pela "tela" (console) inicial do sistema.
 * * Ela exibe o menu de login e coleta as credenciais (usuario/senha).
 * * Nao possui logica de negocio (nao decide se o login e valido).
 * * Conceito de POO: **ENCAPSULAMENTO**.
 */
public class LoginView {
    
    // **ENCAPSULAMENTO**: O Scanner e um detalhe de implementacao
    // da View. Ele e 'private' para que o Controller
    // nao possa (nem deva) acessa-lo. A View esconde *como*
    // ela le os dados do usuario.
    private Scanner scanner;

    /**
     * Construtor.
     * Cria a instancia do Scanner para ler a entrada do console.
     */
    public LoginView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Exibe o menu principal de Login (Paciente, Medico, Admin)
     * e le a escolha do usuario.
     * @return O numero da opcao (1, 2, 3 ou 0).
     */
    public int exibirMenuLogin() {
        System.out.println("==========================================");
        System.out.println("  APLICACAO DE GERENCIAMENTO MEDICO  "); // Limpo
        System.out.println("==========================================");
        System.out.println("Selecione o tipo de usuário:");
        System.out.println("1 - Paciente");
        System.out.println("2 - Médico");
        System.out.println("3 - Administrador");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        try {
            // Le a linha inteira e converte para numero
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Retorna -1 se o usuario digitar texto (ex: "abc")
            return -1;
        }
    }
    
    /**
     * Metodo reutilizavel para pedir "usuario" (CPF/CRM/Login) e "senha".
     * @param tipoUsuario (1, 2, 3) define qual texto exibir (CPF, CRM, etc.)
     * @return Um array de String (String[]) onde [0] e o usuario e [1] e a senha.
     */
    public String[] obterCredenciais(int tipoUsuario) {
        String[] credenciais = new String[2];
        
        // Define o texto (label) correto para o tipo de login
        if (tipoUsuario == 1) {
            System.out.print("Usuário (CPF): ");
        } else if (tipoUsuario == 2) {
            System.out.print("Usuário (CRM): ");
        } else {
            System.out.print("Usuário (Admin): ");
        }
        credenciais[0] = scanner.nextLine(); // Pega o usuario
        
        System.out.print("Senha: ");
        credenciais[1] = scanner.nextLine(); // Pega a senha
        return credenciais; // Devolve as credenciais para o Controller
    }
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}