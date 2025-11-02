package model;

import java.util.Date;

public class Lembrete {

    // Campos da tabela 'lembrete' 
    private int id_lembrete;
    private int id_prescricao;
    private Date horario_programado;
    private String status;
    
    // Campos extras (para exibir dados do JOIN)
    private String nomeMedicamento;
    private String dosagem;

    // Getters e Setters
    public int getId_lembrete() {
        return id_lembrete;
    }

    public void setId_lembrete(int id_lembrete) {
        this.id_lembrete = id_lembrete;
    }

    public int getId_prescricao() {
        return id_prescricao;
    }

    public void setId_prescricao(int id_prescricao) {
        this.id_prescricao = id_prescricao;
    }

    public Date getHorario_programado() {
        return horario_programado;
    }

    public void setHorario_programado(Date horario_programado) {
        this.horario_programado = horario_programado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters/Setters dos campos extras
    public String getNomeMedicamento() {
        return nomeMedicamento;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }
}