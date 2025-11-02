package model;

import java.util.Date;

// ABSTRAÇÃO: Classe abstrata que não pode ser instanciada
// Define um modelo base para medições 
public abstract class Medicao {
    
    protected int id_medicao;
    protected int id_paciente;
    protected Date data_hora;
    protected String observacoes;

    // POLIMORFISMO: Método abstrato que será implementado
    // de formas diferentes nas classes filhas.
    public abstract String getTipoMedicao();

    // Getters e Setters (pode adicionar)
}