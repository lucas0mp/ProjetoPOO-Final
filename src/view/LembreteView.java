package view;

import java.text.SimpleDateFormat;
import java.util.List;
import model.Lembrete;

/**
 * LembreteView (View / Visao)
 * * Esta classe e responsavel por uma unica tarefa: formatar e exibir
 * * uma lista de lembretes para o usuario (paciente).
 * * Ela nao tem logica de menus ou de entrada de dados, apenas de saida (exibicao).
 * * Conceito de POO: **ENCAPSULAMENTO**.
 */
public class LembreteView {
    
    // **ENCAPSULAMENTO**: O formatador e um detalhe de implementacao
    // interno da View. E 'private' para que so esta classe possa usa-lo.
    // Isso esconde a complexidade de formatacao de data/hora do Controller.
    private SimpleDateFormat formatadorDataHora = new SimpleDateFormat("dd/MM/yyyy 'as' HH:mm");

    /**
     * Recebe uma lista de objetos Lembrete (vinda do Controller) e
     * exibe-os de forma formatada no console.
     * @param lembretes A lista de lembretes vinda do LembreteDAO.
     */
    public void exibirListaLembretes(List<Lembrete> lembretes) {
        System.out.println("\n--- Seus Lembretes de Medicacao ---");
        
        // Verifica se a lista esta vazia
        if (lembretes.isEmpty()) {
            System.out.println("Nenhum lembrete encontrado.");
            return;
        }
        
        // Faz um loop por cada item (Lembrete) na lista
        for (Lembrete l : lembretes) {
            System.out.println("---------------------------------");
            // **ENCAPSULAMENTO (do Model)**: A View nao acessa os atributos
            // privados do Lembrete (l.nomeMedicamento), ela usa os metodos 
            // publicos (getters) para obter os dados (ex: l.getNomeMedicamento()).
            System.out.println("Medicamento: " + l.getNomeMedicamento());
            System.out.println("Dosagem: " + l.getDosagem());
            System.out.println("Horario Programado: " + formatadorDataHora.format(l.getHorario_programado()));
            System.out.println("Status: " + l.getStatus());
        }
        System.out.println("---------------------------------");
    }
}