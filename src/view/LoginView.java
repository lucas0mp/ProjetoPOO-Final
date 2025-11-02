package view;

import java.util.Scanner;

public class LoginView {
    
    private Scanner scanner;

    public LoginView() {
        this.scanner = new Scanner(System.in);
    }
    
    public int exibirMenuLogin() {
        System.out.println("==========================================");
        System.out.println("  ACOMPANHAMENTO DE DOENÇAS CRÔNICAS  ");
        System.out.println("==========================================");
        System.out.println("Selecione o tipo de usuário:");
        System.out.println("1 - Paciente");
        System.out.println("2 - Médico");
        System.out.println("3 - Administrador");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public String[] obterCredenciais(int tipoUsuario) {
        String[] credenciais = new String[2];
        if (tipoUsuario == 1) {
            System.out.print("Usuário (CPF): ");
        } else if (tipoUsuario == 2) {
            System.out.print("Usuário (CRM): ");
        } else {
            System.out.print("Usuário (Admin): ");
        }
        credenciais[0] = scanner.nextLine();
        
        System.out.print("Senha: ");
        credenciais[1] = scanner.nextLine();
        return credenciais;
    }
    
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}