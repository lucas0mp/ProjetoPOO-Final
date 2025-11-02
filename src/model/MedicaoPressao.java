package model;

// HERANÇA: MedicaoPressao herda de Medicao 
public class MedicaoPressao extends Medicao {
    
    private double pressao_sistolica;
    private double pressao_diastolica;
    
    @Override
    public String getTipoMedicao() {
        // Implementação específica do Polimorfismo
        return "Pressão Arterial";
    }

    // Getters e Setters específicos
    public double getPressao_sistolica() {
        return pressao_sistolica;
    }

    public void setPressao_sistolica(double pressao_sistolica) {
        this.pressao_sistolica = pressao_sistolica;
    }
}