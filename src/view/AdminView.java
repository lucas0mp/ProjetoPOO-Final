package view;

import java.util.Scanner;
import model.Administrador;

/**
 * AdminView (View / Visao)
 * * Esta classe e responsavel por toda a interacao de "tela" (console)
 * * com o Administrador.
 * * Ela nao contem logica de negocio (nao fala com o DAO).
 * * Ela apenas exibe menus, pede dados e os devolve ao Controller.
 * * Conceito de POO: **ENCAPSULAMENTO**.
 */
public class AdminView {

    // **ENCAPSULAMENTO**: O Scanner e um detalhe de implementacao
    // da View. Ele e 'private' para que nenhuma outra classe
    // (como o Controller) possa acessa-lo. A View cuida
    // de si mesma.
    private Scanner scanner;

    /**
     * Construtor.
     * Cria a instancia do Scanner para ler a entrada do console.
     */
    public AdminView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Exibe o menu principal do Admin e captura a opcao digitada.
     * @return O numero da opcao (1, 2, 3 ou 0).
     */
    public int exibirMenuAdmin() {
        System.out.println("\n--- Portal do Administrador ---");
        System.out.println("1. Gerenciar Pacientes");
        System.out.println("2. Gerenciar Medicos");
        System.out.println("3. Criar novo Administrador");
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opcao: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Retorna -1 se o usuario digitar algo que nao e um numero
            return -1;
        }
    }
    
    /**
     * Coleta os dados para um *novo* Administrador.
     * @return Um objeto Administrador (Model) preenchido com os dados.
     */
    public Administrador obterDadosAdmin() {
        Administrador a = new Administrador();
        System.out.println("--- Novo Administrador ---");
        System.out.print("Nome: ");
        a.setNome(scanner.nextLine());
        System.out.print("Usuario (login): ");
        a.setUsuario(scanner.nextLine());
        return a; // Retorna o objeto pronto para o Controller
    }
    
    /**
     * Metodo generico para pedir uma senha (reutilizado).
     * @return A senha digitada como String.
     */
    public String obterSenha() {
        System.out.print("Defina uma senha para o admin: ");
        return scanner.nextLine();
    }
    
    /**
     * Metodo generico para exibir qualquer mensagem para o usuario.
     * Usado pelos Controllers para dar feedback (ex: "Salvo com sucesso!").
     */
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}