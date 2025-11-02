package view;

import java.util.List;
import java.util.Scanner;
import model.MedicaoGlicemia;
import model.MedicaoPressao;

public class MedicaoView {
    
    private Scanner scanner;

    public MedicaoView() {
        this.scanner = new Scanner(System.in);
    }
    
    public int exibirMenuTipoMedicao() {
        System.out.println("--- Adicionar Medição ---");
        System.out.println("Qual tipo de medição deseja registrar?");
        System.out.println("1. Glicemia");
        System.out.println("2. Pressão Arterial");
        System.out.println("0. Cancelar");
        System.out.print("Opção: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public MedicaoGlicemia obterDadosGlicemia(int idPaciente) {
        MedicaoGlicemia med = new MedicaoGlicemia();
        med.setId_paciente(idPaciente);
        
        System.out.print("Nível de Glicose (mg/dL): ");
        med.setNivel_glicose(Double.parseDouble(scanner.nextLine()));
        System.out.print("Período (Ex: Jejum, Pós-prandial): ");
        med.setPeriodo(scanner.nextLine());
        System.out.print("Observações: ");
        med.setObservacoes(scanner.nextLine());
        
        return med;
    }
    
    public MedicaoPressao obterDadosPressao(int idPaciente) {
        MedicaoPressao med = new MedicaoPressao();
        med.setId_paciente(idPaciente);
        
        System.out.print("Pressão Sistólica (Ex: 12.0): ");
        med.setPressao_sistolica(Double.parseDouble(scanner.nextLine()));
        System.out.print("Pressão Diastólica (Ex: 8.0): ");
        med.setPressao_diastolica(Double.parseDouble(scanner.nextLine()));
        System.out.print("Observações: ");
        med.setObservacoes(scanner.nextLine());
        
        return med;
    }
    
    public void listarMedicoes(List<String> medicoes) {
        System.out.println("\n--- Histórico de Medições ---");
        if (medicoes.isEmpty()) {
            System.out.println("Nenhuma medição registrada.");
            return;
        }
        for (String s : medicoes) {
            System.out.println(s);
        }
        System.out.println("---------------------------------");
    }
    
    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}