package model;

public class Medico {
    
    private int id_medico;
    private String crm;
    private String nome;
    private String especialidade;

    // Getters e Setters (existentes)
    public int getId_medico() {
        return id_medico;
    }

    public void setId_medico(int id_medico) {
        this.id_medico = id_medico;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
    
    // --- NOVO MÉTODO toString() ---
    @Override
    public String toString() {
        return "ID: " + id_medico + 
               ", Nome: " + nome + 
               ", CRM: " + crm + 
               ", Especialidade: " + especialidade;
    }
}