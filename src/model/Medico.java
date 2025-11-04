package model;

/**
 * Model Medico (Modelo de Dados)
 * * Esta classe representa a entidade "Medico" no sistema.
 * * E uma classe POJO (Plain Old Java Object), usada para transportar dados
 * * (ID, CRM, nome, etc.) entre o DAO (banco) e os Controllers/Views.
 * * Conceitos de POO aplicados: ENCAPSULAMENTO e POLIMORFISMO.
 */
public class Medico {
    
    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Todos os atributos sao 'private'.
    // O acesso a eles (leitura e escrita) e controlado
    // atraves dos metodos publicos (getters e setters) abaixo.
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
    
    /**
     * **POLIMORFISMO** (Sobrescrita de Metodo):
     * * O metodo 'toString()' e herdado da classe PAI 'Object'.
     * * Estamos sobrescrevendo (@Override) este metodo para fornecer uma
     * * representacao em String que seja util para este objeto (Medico).
     * * Em vez de imprimir o endereco de memoria, ele imprime os dados do medico.
     * * (Usado na View, no metodo 'listarMedicos').
     */
    @Override
    public String toString() {
        return "ID: " + id_medico + 
               ", Nome: " + nome + 
               ", CRM: " + crm + 
               ", Especialidade: " + especialidade;
    }
}