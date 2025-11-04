package model;

/**
 * Model MedicaoPressao (Modelo de Dados)
 * * Esta classe e a outra "classe FILHA" (Subclasse) da classe Medicao.
 * * Assim como 'MedicaoGlicemia', ela demonstra HERANCA e POLIMORFISMO.
 */

// **HERANCA**: A palavra-chave 'extends' indica que MedicaoPressao
// herda todos os atributos (como id_paciente, observacoes) e metodos
// publicos/protegidos da classe PAI (Superclasse) 'Medicao'.
public class MedicaoPressao extends Medicao {
    
    // --- Atributos ---
    
    // **ENCAPSULAMENTO**: Estes sao os atributos *especificos* desta classe filha.
    // Eles sao privados e acessados por getters/setters.
    private double pressao_sistolica;
    private double pressao_diastolica;
    
    /**
     * **POLIMORFISMO** (Sobrescrita de Metodo):
     * * A classe PAI 'Medicao' definiu o metodo 'getTipoMedicao()' como 'abstract'.
     * * Esta classe filha (MedicaoPressao) tambem e *obrigada* a fornecer
     * * uma implementacao para esse metodo.
     * * A anotacao '@Override' indica que este metodo esta
     * * substituindo (sobrescrevendo) o metodo da classe pai.
     * * Esta e a implementacao *especifica* para Pressao Arterial.
     */
    @Override
    public String getTipoMedicao() {
        // Implementacao especifica do Polimorfismo
        return "Pressao Arterial";
    }

    // --- CORRECAO: Getters e Setters Faltantes ---
    // (Acesso encapsulado aos atributos privados)

    public double getPressao_sistolica() {
        return pressao_sistolica;
    }

    public void setPressao_sistolica(double pressao_sistolica) {
        this.pressao_sistolica = pressao_sistolica;
    }

    public double getPressao_diastolica() {
        return pressao_diastolica;
    }

    public void setPressao_diastolica(double pressao_diastolica) {
        this.pressao_diastolica = pressao_diastolica;
    }
}