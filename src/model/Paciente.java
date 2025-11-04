package model;

import java.util.Date;

/**
 * Model Paciente (Modelo de Dados)
 * * Esta classe representa a entidade "Paciente" no sistema.
 * * E uma classe POJO (Plain Old Java Object), usada para transportar dados
 * * (ID, nome, CPF, etc.) entre o DAO (banco) e os Controllers/Views.
 * * Conceitos de POO aplicados: **ENCAPSULAMENTO** e **POLIMORFISMO**.
 * * (Nao ha Heranca customizada ou Abstracao nesta classe especifica).
 */
public class Paciente {
    
    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Todos os atributos (dados) da classe sao 'private'.
    // Isso protege os dados de acesso direto ou modificacao
    // por outras classes. O acesso e controlado pelos metodos
    // publicos (getters e setters) abaixo.
    private int id_paciente;
    private String nome;
    private String cpf;
    private Date data_nascimento;
    private String telefone_celular;
    private String email;
    
    // Construtor vazio
    public Paciente() {
    }

    // Getters e Setters para acessar os atributos
    public int getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(int id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getTelefone_celular() {
        return telefone_celular;
    }

    public void setTelefone_celular(String telefone_celular) {
        this.telefone_celular = telefone_celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * **POLIMORFISMO** (Sobrescrita de Metodo):
     * * O metodo 'toString()' e universal (herdado da classe 'Object').
     * * Aqui, estamos sobrescrevendo (@Override) o comportamento padrao
     * * para fornecer uma representacao em String util para o Paciente.
     * * (Usado na PacienteView, no metodo 'listarPacientes').
     */
    @Override
    public String toString() {
        return "ID: " + id_paciente + 
               ", Nome: " + nome + 
               ", CPF: " + cpf + 
               ", Data Nasc: " + data_nascimento +
               ", Celular: " + telefone_celular +
               ", Email: " + email;
    }
}