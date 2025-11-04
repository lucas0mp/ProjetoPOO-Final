package view;

import java.util.List;
import java.util.Scanner;
import model.MedicaoGlicemia;
import model.MedicaoPressao;

/**
 * MedicaoView (View / Visao)
 * * Esta classe e responsavel por toda a interacao de "tela" (console)
 * * relacionada ao registro e visualizacao de medicoes.
 * * Ela e usada por dois controllers:
 * * 1. MedicoController: Para *coletar dados* (obterDadosGlicemia/Pressao).
 * * 2. PacienteController: Para *exibir dados* (listarMedicoes).
 * * Conceito de POO: **ENCAPSULAMENTO**.
 */
public class MedicaoView {
    
    // **ENCAPSULAMENTO**: O Scanner e um detalhe de implementacao
    // da View. E 'private' para que o Controller
    // nao possa acessa-lo.
    private Scanner scanner;
    
    /**
     * Construtor.
     * Cria a instancia do Scanner para ler a entrada do console.
     */
    public MedicaoView() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Usado pelo MedicoController para saber qual tipo de medicao registrar.
     * @return 1 (Glicemia), 2 (Pressao) ou 0 (Cancelar).
     */
    public int exibirMenuTipoMedicao() {
        System.out.println("--- Tipo de Medicao ---");
        System.out.println("1. Glicemia");
        System.out.println("2. Pressao Arterial");
        System.out.println("0. Cancelar");
        System.out.print("Escolha uma opcao: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Coleta os dados especificos de Glicemia.
     * @param idPaciente O ID do paciente (recebido do Controller).
     * @return Um objeto MedicaoGlicemia (Model) preenchido.
     * @throws NumberFormatException Se o usuario digitar texto em vez de numero.
     */
    public MedicaoGlicemia obterDadosGlicemia(int idPaciente) throws NumberFormatException {
        // **HERANCA (Consumo)**: A View cria uma instancia da classe FILHA.
        MedicaoGlicemia mg = new MedicaoGlicemia();
        
        // Define atributos herdados da classe PAI (Medicao)
        mg.setId_paciente(idPaciente); 
        
        System.out.print("Nivel de Glicose (mg/dL): ");
        // Define atributos especificos da classe FILHA
        mg.setNivel_glicose(Double.parseDouble(scanner.nextLine()));
        
        System.out.print("Periodo (ex: Jejum, Pos-prandial): ");
        mg.setPeriodo(scanner.nextLine());
        
        System.out.print("Observacoes (opcional): ");
        // Define outro atributo herdado da classe PAI
        mg.setObservacoes(scanner.nextLine());
        
        return mg; // Devolve o objeto preenchido
    }
    
    /**
     * Coleta os dados especificos de Pressao Arterial.
     * @param idPaciente O ID do paciente (recebido do Controller).
     * @return Um objeto MedicaoPressao (Model) preenchido.
     * @throws NumberFormatException Se o usuario digitar texto em vez de numero.
     */
    public MedicaoPressao obterDadosPressao(int idPaciente) throws NumberFormatException {
        // **HERANCA (Consumo)**: A View cria a outra classe FILHA.
        MedicaoPressao mp = new MedicaoPressao();
        
        // Define atributos herdados da classe PAI (Medicao)
        mp.setId_paciente(idPaciente); 
        
        System.out.print("Pressao Sistolica (ex: 12.0): ");
        // Define atributos especificos da classe FILHA
        mp.setPressao_sistolica(Double.parseDouble(scanner.nextLine()));
        
        System.out.print("Pressao Diastolica (ex: 8.0): ");
        mp.setPressao_diastolica(Double.parseDouble(scanner.nextLine()));
        
        System.out.print("Observacoes (opcional): ");
        // Define outro atributo herdado da classe PAI
        mp.setObservacoes(scanner.nextLine());
        
        return mp; // Devolve o objeto preenchido
    }
    
    /**
     * Usado pelo PacienteController para exibir o historico.
     * @param medicoes Uma lista de Strings (ja formatadas pelo MedicaoDAO).
     */
    public void listarMedicoes(List<String> medicoes) {
        System.out.println("\n--- Seu Historico de Medicoes ---");
        if (medicoes.isEmpty()) {
            System.out.println("Nenhuma medicao registrada.");
            return;
        }
        
        // A View apenas faz o loop e imprime as strings
        // A logica de formatacao complexa esta no DAO
        for (String medicaoFormatada : medicoes) {
            System.out.println(medicaoFormatada);
        }
    }
}