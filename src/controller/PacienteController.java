package controller;

import dao.LembreteDAO;
import dao.MedicaoDAO; // Importe
import java.util.List;
import model.Lembrete;
import model.Paciente;
import view.LembreteView;
import view.MedicaoView; // Importe
import view.PacienteView;

public class PacienteController {
    
    private Paciente pacienteLogado;
    private PacienteView view;
    private LembreteView lembreteView;
    private MedicaoView medicaoView; // Adicione
    
    public PacienteController(Paciente pacienteLogado) {
        this.pacienteLogado = pacienteLogado;
        this.view = new PacienteView();
        this.lembreteView = new LembreteView();
        this.medicaoView = new MedicaoView(); // Instancie
    }
    
    public void iniciar() {
        int opcao;
        do {
            opcao = view.exibirMenuPaciente(); 
            
            switch (opcao) {
                case 1:
                    visualizarMeusLembretes();
                    break;
                case 2:
                    visualizarMinhasMedicoes(); // Nova função
                    break;
                case 0:
                    view.exibirMensagem("Voltando ao menu principal...");
                    break;
                default:
                    view.exibirMensagem("Opção inválida!");
            }
        } while (opcao != 0);
    }
    
    private void visualizarMeusLembretes() {
        LembreteDAO dao = new LembreteDAO();
        List<Lembrete> lembretes = dao.listarPorPaciente(pacienteLogado.getId_paciente());
        lembreteView.exibirListaLembretes(lembretes);
    }
    
    // FUNÇÃO PARA PACIENTE VER MEDIÇÕES
    private void visualizarMinhasMedicoes() {
        MedicaoDAO dao = new MedicaoDAO();
        List<String> medicoes = dao.listarMedicoesPorPaciente(pacienteLogado.getId_paciente());
        medicaoView.listarMedicoes(medicoes);
    }
}