package model;

/**
 * Model Administrador (Modelo de Dados)
 * * Esta classe representa a entidade "Administrador" no sistema.
 * * Ela serve como um "molde" ou "planta" para criar objetos
 * que podem carregar os dados de um administrador (ID, usuario e nome).
 * * Esta classe foca fortemente no conceito de **ENCAPSULAMENTO**.
 */
public class Administrador {
    
    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Os atributos (dados) da classe sao definidos como 'private'.
    // Isso significa que eles nao podem ser acessados ou modificados diretamente
    // por nenhuma outra classe (como um Controller ou View). O acesso e
    // controlado pelos metodos publicos (Getters e Setters).
    private int id_admin;
    private String usuario;
    private String nome;

    // Getters e Setters
    public int getId_admin() {
        return id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}