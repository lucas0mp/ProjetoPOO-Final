package principal;

import controller.LoginController;

/**
 * Classe Main
 * Ponto de partida de toda a aplicacao.
 */
public class Main {

    /**
     * Metodo principal (Entry Point) do Java.
     * Este e o primeiro metodo executado.
     */
    public static void main(String[] args) {
        
        // 1. Cria o controlador inicial (Login)
        LoginController loginController = new LoginController();
        
        // 2. **ABSTRACAO / ENCAPSULAMENTO**:
        // Delega toda a logica do programa para o LoginController.
        // A Main nao sabe (e nao precisa saber) o que acontece depois.
        loginController.iniciar();
    }
}