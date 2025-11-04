package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import model.Paciente;

/**
 * PacienteView (View / Visao)
 * * Esta classe e responsavel por toda a interacao de "tela" (console)
 * * relacionada a Pacientes.
 * * Ela e usada por três controllers:
 * * 1. PacienteController: Para exibir o menu do paciente (logado).
 * * 2. AdminController: Para exibir o menu CRUD e coletar dados.
 * * 3. MedicoController: Para coletar dados de um novo paciente.
 * * Conceitos de POO: **ENCAPSULAMENTO**, **POLIMORFISMO**.
 */
public class PacienteView {
    
    // **ENCAPSULAMENTO**: O Scanner e o formatador sao
    // detalhes de implementacao da View. Sao 'private'
    // para que nenhuma outra classe (como o Controller)
    // possa acessa-los. A View esconde *como* ela le e formata os dados.
    private Scanner scanner;
    private SimpleDateFormat formatadorData;

    /**
     * Construtor.
     * Cria a instancia do Scanner para ler a entrada do console.
     */
    public PacienteView() {
        this.scanner = new Scanner(System.in);
        this.formatadorData = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Exibe o menu principal do Paciente (logado).
     * Usado pelo PacienteController.
     * @return O numero da opcao (1, 2 ou 0).
     */
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

    /**
     * Exibe o menu de CRUD (Criar, Ler, Atualizar, Excluir)
     * para o Admin gerenciar pacientes.
     * Usado pelo AdminController.
     * @return O numero da opcao (1, 2, 3, 4 ou 0).
     */
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

    /**
     * Coleta os dados para um *novo* Paciente (ou para atualizar).
     * Usado pelo AdminController e MedicoController.
     * @param pacienteExistente (Se for null, cria um novo Paciente).
     * @return Um objeto Paciente (Model) preenchido com os dados.
     */
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
            p.setData_nascimento(new java.util.Date()); // Data atual como fallback
        }

        System.out.print("Telefone Celular: ");
        p.setTelefone_celular(scanner.nextLine());
        
        System.out.print("Email: ");
        p.setEmail(scanner.nextLine());

        return p; // Retorna o objeto pronto para o Controller
    }

    /**
     * Metodo generico para pedir uma senha.
     * Usado pelo AdminController e MedicoController ao criar um paciente.
     * @return A senha digitada como String.
     */
    public String obterSenha() {
        System.out.print("Defina uma senha para o usuário: ");
        return scanner.nextLine();
    }
    
    /**
     * Recebe uma lista de Pacientes (vinda do Controller) e exibe no console.
     * Usado pelo AdminController e MedicoController.
     * @param pacientes A lista de pacientes vinda do PacienteDAO.
     */
    public void listarPacientes(List<Paciente> pacientes) {
        System.out.println("\n--- Lista de Pacientes ---");
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            // **POLIMORFISMO**: Ao imprimir 'p' (o objeto Paciente),
            // o Java automaticamente chama o metodo 'p.toString()'.
            // Como sobrescrevemos o toString() na classe Paciente,
            // ele imprime os dados formatados (ID, Nome, CPF)
            for (Paciente p : pacientes) {
                System.out.println(p.toString()); // Chama o p.toString()
            }
        }
    }
    
    /**
     * Metodo generico para pedir um ID (para atualizar, excluir ou selecionar).
     * Usado pelo AdminController e MedicoController.
     * @param acao O texto a ser exibido (ex: "ATUALIZAR", "EXCLUIR").
     * @return O ID digitado pelo usuario.
     */
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