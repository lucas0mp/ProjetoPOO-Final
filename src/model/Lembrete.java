package model;

import java.util.Date;

/**
 * Model Lembrete (Modelo de Dados)
 * * Esta classe representa a entidade "Lembrete" do banco de dados.
 * * E uma classe POJO (Plain Old Java Object), usada para transportar dados
 * entre o DAO (banco) e as Views (telas).
 * * O principal conceito de POO aplicado aqui e o **ENCAPSULAMENTO**.
 */
public class Lembrete {

    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Todos os atributos sao 'private'.
    // O acesso a eles (leitura e escrita) e controlado
    // atraves dos metodos publicos (getters e setters) abaixo.

    // Campos que existem diretamente na tabela 'lembrete' 
    private int id_lembrete;
    private int id_prescricao; // Chave estrangeira que liga o lembrete a uma prescricao
    private Date horario_programado;
    private String status;
    
    // **ABSTRACAO (de dados)**: Estes campos NAO existem na tabela 'lembrete'.
    // Eles sao adicionados a este Model para facilitar o transporte de dados
    // vindos de um JOIN (la no LembreteDAO). O DAO busca o nome do medicamento
    // e a dosagem (de outras tabelas) e "abstrai" eles para dentro deste
    // unico objeto, simplificando a vida do Controller e da View.
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