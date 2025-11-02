package model;

import java.util.Date;

// [Source: 2]
public class Paciente {
    
    // Encapsulamento: Atributos privados
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