package view;

import java.util.Scanner;
import model.Administrador;

public class AdminView {

    private Scanner scanner;

    public AdminView() {
        this.scanner = new Scanner(System.in);
    }

    public int exibirMenuAdmin() {
        System.out.println("\n--- Portal do Administrador ---");
        System.out.println("1. Gerenciar Pacientes (CRUD)");
        System.out.println("2. Gerenciar Médicos (CRUD)"); // TODO
        System.out.println("3. Criar novo Administrador"); // TODO
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public Administrador obterDadosAdmin() {
        Administrador a = new Administrador();
        System.out.println("--- Novo Administrador ---");
        System.out.print("Nome: ");
        a.setNome(scanner.nextLine());
        System.out.print("Usuário (login): ");
        a.setUsuario(scanner.nextLine());
        return a;
    }
    
    public String obterSenha() {
        System.out.print("Defina uma senha para o admin: ");
        return scanner.nextLine();
    }
    
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}