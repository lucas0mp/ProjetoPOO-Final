package principal;

import controller.PacienteController;

public class Main {

    public static void main(String[] args) {
        // Cria o controlador
        PacienteController controller = new PacienteController();
        
        // Inicia a aplicação
        controller.iniciar();
    }
}