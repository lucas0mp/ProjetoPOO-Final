package model;

/**
 * Model Medicamento (Modelo de Dados)
 * * Esta classe representa a entidade "Medicamento" do banco de dados.
 * * E uma classe POJO (Plain Old Java Object), usada para transportar dados
 * (como o ID do remedio, nome comercial e principio ativo)
 * entre o DAO e os Controllers/Views.
 * * O principal conceito de POO aplicado aqui e o **ENCAPSULAMENTO**.
 */
public class Medicamento {

    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Todos os atributos sao 'private'.
    // O acesso a eles (leitura e escrita) e controlado
    // atraves dos metodos publicos (getters e setters) abaixo.
    private int id_medicamento;
    private String nome_comercial;
    private String principio_ativo;

    // Getters e Setters
    public int getId_medicamento() {
        return id_medicamento;
    }

    public void setId_medicamento(int id_medicamento) {
        this.id_medicamento = id_medicamento;
    }

    public String getNome_comercial() {
        return nome_comercial;
    }

    public void setNome_comercial(String nome_comercial) {
        this.nome_comercial = nome_comercial;
    }

    public String getPrincipio_ativo() {
        return principio_ativo;
    }

    public void setPrincipio_ativo(String principio_ativo) {
        this.principio_ativo = principio_ativo;
    }
}