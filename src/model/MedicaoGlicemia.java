package model;

/**
 * Model MedicaoGlicemia (Modelo de Dados)
 * * Esta classe e uma "classe FILHA" (Subclasse) da classe Medicao.
 * * Ela demonstra os conceitos de HERANCA e POLIMORFISMO.
 */

// **HERANCA**: A palavra-chave 'extends' indica que MedicaoGlicemia
// herda todos os atributos (como id_paciente, observacoes) e metodos
// publicos/protegidos da classe PAI (Superclasse) 'Medicao'.
public class MedicaoGlicemia extends Medicao {

    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Estes sao os atributos *especificos* desta classe filha.
    // Eles sao privados e acessados por getters/setters.
    private double nivel_glicose;
    private String periodo;

    /**
     * **POLIMORFISMO** (Sobrescrita de Metodo):
     * * A classe PAI 'Medicao' definiu o metodo 'getTipoMedicao()' como 'abstract'.
     * * Esta classe filha (MedicaoGlicemia) e *obrigada* a fornecer uma
     * implementacao (um corpo) para esse metodo.
     * * A anotacao '@Override' indica que este metodo esta
     * substituindo (sobrescrevendo) o metodo da classe pai.
     * * Esta e a implementacao *especifica* para Glicemia.
     */
    @Override
    public String getTipoMedicao() {
        return "Glicemia";
    }
    
    // --- Getters e Setters ---
    // (Acesso encapsulado aos atributos privados)
    
    public double getNivel_glicose() {
        return nivel_glicose;
    }

    public void setNivel_glicose(double nivel_glicose) {
        this.nivel_glicose = nivel_glicose;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}