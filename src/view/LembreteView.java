package view;

import java.text.SimpleDateFormat;
import java.util.List;
import model.Lembrete;

public class LembreteView {
    
    private SimpleDateFormat formatadorDataHora = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");

    public void exibirListaLembretes(List<Lembrete> lembretes) {
        System.out.println("\n--- Seus Lembretes de Medicação ---");
        if (lembretes.isEmpty()) {
            System.out.println("Nenhum lembrete encontrado.");
            return;
        }
        
        for (Lembrete l : lembretes) {
            System.out.println("---------------------------------");
            System.out.println("Medicamento: " + l.getNomeMedicamento());
            System.out.println("Dosagem: " + l.getDosagem());
            System.out.println("Horário Programado: " + formatadorDataHora.format(l.getHorario_programado()));
            System.out.println("Status: " + l.getStatus());
        }
        System.out.println("---------------------------------");
    }
}