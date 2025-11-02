package model;

import java.util.Date;

// ABSTRAÇÃO: Classe abstrata que não pode ser instanciada
// Define um modelo base para medições
public abstract class Medicao {
    
    // 
    protected int id_medicao;
    protected int id_paciente;
    protected Date data_hora;
    protected String observacoes;

    // POLIMORFISMO: Método abstrato que será implementado
    // de formas diferentes nas classes filhas.
    public abstract String getTipoMedicao();

    // --- CORREÇÃO: Getters e Setters Faltantes ---
    
    public int getId_medicao() {
        return id_medicao;
    }

    public void setId_medicao(int id_medicao) {
        this.id_medicao = id_medicao;
    }

    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public Date getData_hora() {
        return data_hora;
    }

    public void setData_hora(Date data_hora) {
        this.data_hora = data_hora;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}