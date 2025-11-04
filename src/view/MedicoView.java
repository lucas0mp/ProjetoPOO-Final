package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import model.Medico;
import model.Prescricao;

/**
 * MedicoView (View / Visao)
 * * Esta classe e responsavel por toda a interacao de "tela" (console)
 * * que um Medico (logado) ou um Admin (gerenciando medicos) precisa.
 * * Ela nao tem logica de negocio (nao fala com o DAO).
 * * Ela apenas exibe menus, pede dados e os devolve ao Controller.
 * * Conceitos de POO: **ENCAPSULAMENTO**, **POLIMORFISMO**.
 */
public class MedicoView {

    // **ENCAPSULAMENTO**: O Scanner e o formatador de data sao
    // detalhes de implementacao da View. Eles sao 'private'
    // para que nenhuma outra classe (como o Controller)
    // possa acessa-los. A View esconde *como* ela le e formata os dados.
    private Scanner scanner;
    private SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Construtor.
     * Cria a instancia do Scanner para ler a entrada do console.
     */
    public MedicoView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Exibe o menu principal do Medico (logado).
     * Usado pelo MedicoController.
     * @return O numero da opcao (1, 2, 3, 4 ou 0).
     */
    public int exibirMenuMedico() {
        System.out.println("\n--- Portal do Medico ---");
        System.out.println("1. Criar novo Paciente");
        System.out.println("2. Listar meus Pacientes");
        System.out.println("3. Adicionar Prescricao");
        System.out.println("4. Adicionar Medicao");
        System.out.println("0. Sair (Logout)");
        System.out.print("Escolha uma opcao: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Exibe o menu de CRUD (Criar, Ler, Atualizar, Excluir)
     * para o Admin gerenciar medicos.
     * Usado pelo AdminController.
     * @return O numero da opcao (1, 2, 3, 4 ou 0).
     */
    public int exibirMenuAdminCRUD() {
        System.out.println("\n--- Gerenciamento de Medicos (Admin) ---");
        System.out.println("1. Criar Medico");
        System.out.println("2. Listar Medicos");
        System.out.println("3. Atualizar Medico");
        System.out.println("4. Excluir Medico");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opcao: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Coleta os dados para um *novo* Medico (ou para atualizar).
     * Usado pelo AdminController.
     * @return Um objeto Medico (Model) preenchido com os dados.
     */
    public Medico obterDadosMedico() {
        Medico m = new Medico();
        System.out.print("CRM: ");
        m.setCrm(scanner.nextLine());
        System.out.print("Nome: ");
        m.setNome(scanner.nextLine());
        System.out.print("Especialidade: ");
        m.setEspecialidade(scanner.nextLine());
        return m; // Retorna o objeto pronto para o Controller
    }
    
    /**
     * Metodo generico para pedir uma senha.
     * Usado pelo AdminController ao criar um medico.
     * @return A senha digitada como String.
     */
    public String obterSenha() {
        System.out.print("Defina uma senha para o medico: ");
        return scanner.nextLine();
    }
    
    /**
     * Metodo generico para pedir um ID (para atualizar ou excluir).
     * Usado pelo AdminController.
     * @param acao O texto a ser exibido (ex: "ATUALIZAR", "EXCLUIR").
     * @return O ID digitado pelo usuario.
     */
    public int obterIdMedico(String acao) {
        System.out.print("Digite o ID do medico que deseja " + acao + " (ou 0 para cancelar): ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido. Digite apenas numeros.");
            return -1;
        }
    }
    
    /**
     * Recebe uma lista de Medicos (vinda do Controller) e exibe no console.
     * Usado pelo AdminController.
     * @param medicos A lista de medicos vinda do MedicoDAO.
     */
    public void listarMedicos(List<Medico> medicos) {
        System.out.println("\n--- Lista de Medicos Cadastrados ---");
        if (medicos.isEmpty()) {
            System.out.println("Nenhum medico encontrado.");
            return;
        }
        
        // **POLIMORFISMO**: Ao imprimir 'm' (o objeto Medico),
        // o Java automaticamente chama o metodo 'm.toString()'.
        // Como nos sobrescrevemos (Override) o toString() na classe Medico,
        // ele imprime os dados formatados (ID, Nome, CRM) em vez
        // do endereco de memoria.
        for (Medico m : medicos) {
            System.out.println(m); // Chama o m.toString()
        }
    }
    
    /**
     * Coleta os dados para uma *nova* Prescricao.
     * Usado pelo MedicoController.
     * @return Um objeto Prescricao (Model) preenchido.
     */
    public Prescricao obterDadosPrescricao(int idMedico, int idPaciente) {
        Prescricao p = new Prescricao();
        
        // IDs que o Controller ja sabe
        p.setId_medico(idMedico);
        p.setId_paciente(idPaciente);
        
        System.out.print("ID do Medicamento (numero): ");
        p.setId_medicamento(Integer.parseInt(scanner.nextLine()));
        
        System.out.print("Data de Inicio (dd/MM/yyyy): ");
        try {
            // Usa o formatador privado para converter a String em Data
            p.setData_inicio(formatadorData.parse(scanner.nextLine()));
        } catch (ParseException e) {
            System.out.println("Data em formato invalido! Usando data de hoje.");
            p.setData_inicio(new java.util.Date()); // Usa data atual como fallback
        }
        
        System.out.print("Dosagem (ex: 500mg): ");
        p.setDosagem(scanner.nextLine());
        System.out.print("Frequencia (ex: 2x ao dia): ");
        p.setFrequencia(scanner.nextLine());
        System.out.print("Instrucoes Adicionais (opcional): ");
        p.setInstrucoes_adicionais(scanner.nextLine());
        
        return p; // Retorna o objeto pronto
    }
    
    /**
     * Coleta o horario para criar o Lembrete associado a prescricao.
     * Usado pelo MedicoController.
     * @return O horario no formato "HH:mm".
     */
    public String obterHorarioLembrete() {
        System.out.print("Qual o horario do Lembrete? (HH:mm): ");
        return scanner.nextLine();
    }
    
    /**
     * Metodo generico para exibir qualquer mensagem para o usuario.
     * (ex: "Salvo com sucesso!", "Erro!").
     */
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}