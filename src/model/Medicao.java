package model;

import java.util.Date;

/**
 * Model Medicao (Modelo de Dados)
 * * Esta e a classe "PAI" (Superclasse) para todos os tipos de medicao.
 * * Ela demonstra varios conceitos de POO:
 * *
 * * 1. **ABSTRACAO**: A classe e declarada como 'public abstract class'.
 * * Isso significa que voce nao pode criar um objeto "Medicao" diretamente
 * * (ex: 'new Medicao()' daria erro). Ela serve apenas como um modelo
 * * base para outras classes (as classes filhas).
 * *
 * * 2. **HERANCA**: Esta classe e projetada para ser a "Superclasse"
 * * (ou classe pai). As classes 'MedicaoGlicemia' e 'MedicaoPressao'
 * * vao "herdar" (extends) todos os atributos e metodos desta classe.
 * *
 * * 3. **ENCAPSULAMENTO**: Os atributos sao 'protected' (protegidos).
 * * Isso e um tipo de encapsulamento. Eles sao privados para o
 * * mundo exterior, mas publicos para as classes filhas que herdam
 * * desta (ex: 'MedicaoGlicemia' pode acessar 'id_paciente' diretamente).
 */
// ABSTRACAO: Classe abstrata que nao pode ser instanciada
// Define um modelo base para medicoes
public abstract class Medicao {
    
    // Atributos comuns que serao herdados pelas classes filhas
    protected int id_medicao;
    protected int id_paciente;
    protected Date data_hora;
    protected String observacoes;

    // --- Metodo Abstrato ---
        
    /**
     * **ABSTRACAO** e **POLIMORFISMO**:
     * * 1. (Abstracao): Este metodo e 'abstract'. Ele *define* um contrato
     * * (O QUE fazer), mas nao tem implementacao (COMO fazer).
     * * 2. (Polimorfismo): Ele *obriga* as classes filhas (como MedicaoGlicemia
     * * e MedicaoPressao) a implementar (override) este metodo.
     * * Cada filha vai implementa-lo de uma forma diferente (polimorfismo):
     * * - MedicaoGlicemia vai retornar "Glicemia".
     * * - MedicaoPressao vai retornar "Pressao Arterial".
     */
    // POLIMORFISMO: Metodo abstrato que sera implementado
    // de formas diferentes nas classes filhas.
    public abstract String getTipoMedicao();

    // --- CORRECAO: Getters e Setters Faltantes ---
    
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