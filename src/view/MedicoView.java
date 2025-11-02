package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import model.Medico;
import model.Prescricao;

public class MedicoView {

    private Scanner scanner;
    private SimpleDateFormat formatadorData;

    public MedicoView() {
        this.scanner = new Scanner(System.in);
        this.formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    }

    // Menu para o MÉDICO LOGADO (Existente)
    public int exibirMenuMedico() {
        System.out.println("\n--- Portal do Médico ---");
        System.out.println("1. Criar novo Paciente");
        System.out.println("2. Listar meus Pacientes");
        System.out.println("3. Criar Prescrição (e Lembrete) para Paciente");
        System.out.println("4. Adicionar Medição para Paciente");
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    // --- NOVOS MÉTODOS PARA O ADMIN ---

    /**
     * Menu para o ADMIN (CRUD)
     */
    public int exibirMenuAdminCRUD() {
        System.out.println("\n--- CRUD Médicos (Admin) ---");
        System.out.println("1. Cadastrar Novo Médico");
        System.out.println("2. Listar Todos os Médicos");
        System.out.println("3. Atualizar Médico");
        System.out.println("4. Excluir Médico");
        System.out.println("0. Voltar ao Menu Admin");
        System.out.print("Escolha uma opção: ");
        
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lista os médicos (usado pelo Admin)
     */
    public void listarMedicos(List<Medico> medicos) {
        System.out.println("\n--- Lista de Médicos ---");
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
        } else {
            for (Medico m : medicos) {
                System.out.println(m.toString()); // Usa o toString() que adicionamos
            }
        }
    }

    /**
     * Pede o ID do médico para uma ação
     */
    public int obterIdMedico(String acao) { // "ATUALIZAR" ou "EXCLUIR"
        System.out.print("Digite o ID do médico que deseja " + acao + ": ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("ID inválido.");
            return -1;
        }
    }

    // --- MÉTODOS EXISTENTES (reutilizados) ---
    
    public Medico obterDadosMedico() {
        Medico m = new Medico();
        System.out.println("Coletando dados do médico...");
        System.out.print("Nome: ");
        m.setNome(scanner.nextLine());
        System.out.print("CRM (Ex: 12345-SP): ");
        m.setCrm(scanner.nextLine());
        System.out.print("Especialidade: ");
        m.setEspecialidade(scanner.nextLine());
        return m;
    }
    
    public Prescricao obterDadosPrescricao(int idMedico, int idPaciente) {
        Prescricao p = new Prescricao();
        p.setId_medico(idMedico);
        p.setId_paciente(idPaciente);
        
        System.out.println("--- Nova Prescrição ---");
        System.out.print("ID do Medicamento (Ex: 1 para Losartana, 2 para Metformina): ");
        p.setId_medicamento(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Data de Início (dd/MM/yyyy): ");
        try {
            p.setData_inicio(formatadorData.parse(scanner.nextLine()));
        } catch (ParseException e) {
            p.setData_inicio(new java.util.Date());
        }
        
        System.out.print("Dosagem (Ex: 1 comprimido): ");
        p.setDosagem(scanner.nextLine());
        
        System.out.print("Frequência (Ex: 2 vezes ao dia): ");
        p.setFrequencia(scanner.nextLine());
        
        System.out.print("Instruções Adicionais: ");
        p.setInstrucoes_adicionais(scanner.nextLine());
        
        return p;
    }
    
    public String obterHorarioLembrete() {
        System.out.println("--- Novo Lembrete ---");
        System.out.print("Informe o horário (HH:mm) para o primeiro lembrete diário: ");
        return scanner.nextLine(); 
    }

    public String obterSenha() {
        System.out.print("Defina uma senha para o usuário: ");
        return scanner.nextLine();
    }
    
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}