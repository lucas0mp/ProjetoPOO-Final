package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import model.Paciente;

public class PacienteView {
    
    private Scanner scanner;
    private SimpleDateFormat formatadorData;

    public PacienteView() {
        this.scanner = new Scanner(System.in);
        this.formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    }

    // Menu para o PACIENTE LOGADO
    public int exibirMenuPaciente() {
        System.out.println("\n--- Portal do Paciente ---");
        System.out.println("1. Visualizar meus Lembretes de Medicação");
        System.out.println("2. Visualizar meu Histórico de Medições");
        // System.out.println("3. Adicionar medicação (Auto-Reporte)"); // Futura implementação
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opção: ");
        
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Menu para o ADMIN (CRUD)
    public int exibirMenuAdminCRUD() {
        System.out.println("\n--- CRUD Pacientes (Admin) ---");
        System.out.println("1. Cadastrar Novo Paciente");
        System.out.println("2. Listar Todos os Pacientes");
        System.out.println("3. Atualizar Paciente");
        System.out.println("4. Excluir Paciente");
        System.out.println("0. Voltar ao Menu Admin");
        System.out.print("Escolha uma opção: ");
        
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Método reutilizado por Admin e Médico
    public Paciente obterDadosPaciente(Paciente pacienteExistente) {
        Paciente p = (pacienteExistente == null) ? new Paciente() : pacienteExistente;
        
        System.out.println("Coletando dados do paciente...");
        System.out.print("Nome: ");
        p.setNome(scanner.nextLine());
        
        System.out.print("CPF (11 dígitos): ");
        p.setCpf(scanner.nextLine());

        System.out.print("Data Nascimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        try {
            p.setData_nascimento(formatadorData.parse(dataStr));
        } catch (ParseException e) {
            System.err.println("Formato de data inválido! Usando data atual.");
            p.setData_nascimento(new java.util.Date());
        }

        System.out.print("Telefone Celular: ");
        p.setTelefone_celular(scanner.nextLine());
        
        System.out.print("Email: ");
        p.setEmail(scanner.nextLine());

        return p;
    }

    public String obterSenha() {
        System.out.print("Defina uma senha para o usuário: ");
        return scanner.nextLine();
    }
    
    public void listarPacientes(List<Paciente> pacientes) {
        System.out.println("\n--- Lista de Pacientes ---");
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            for (Paciente p : pacientes) {
                System.out.println(p.toString()); // Use o toString() que você já tinha
            }
        }
    }
    
    public int obterIdPaciente(String acao) { // "ATUALIZAR" ou "EXCLUIR" ou "SELECIONAR"
        System.out.print("Digite o ID do paciente que deseja " + acao + ": ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ID inválido.");
            return -1;
        }
    }
    
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}