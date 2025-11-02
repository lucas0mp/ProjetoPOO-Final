package principal;

import controller.LoginController;

public class Main {

    public static void main(String[] args) {
        // Ponto de entrada da aplicação agora é o Login
        LoginController loginController = new LoginController();
        loginController.iniciar();
    }
}