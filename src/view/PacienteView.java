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
        // Formato brasileiro de data
        this.formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    }

    public int exibirMenu() {
        System.out.println("\n--- CRUD Pacientes ---");
        System.out.println("1. Cadastrar Novo Paciente");
        System.out.println("2. Listar Todos os Pacientes");
        System.out.println("3. Atualizar Paciente");
        System.out.println("4. Excluir Paciente");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
        
        int opcao = -1;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Opção inválida. Digite apenas números.");
        }
        return opcao;
    }

    public Paciente obterDadosPaciente(Paciente pacienteExistente) {
        Paciente p = (pacienteExistente == null) ? new Paciente() : pacienteExistente;

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

    public void listarPacientes(List<Paciente> pacientes) {
        System.out.println("\n--- Lista de Pacientes ---");
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            for (Paciente p : pacientes) {
                System.out.println(p.toString());
            }
        }
    }
    
    public int obterIdPacienteParaAtualizar() {
        System.out.print("Digite o ID do paciente que deseja ATUALIZAR: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ID inválido.");
            return -1;
        }
    }
    
    public int obterIdPacienteParaExcluir() {
        System.out.print("Digite o ID do paciente que deseja EXCLUIR: ");
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