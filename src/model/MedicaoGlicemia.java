package model;

// HERANÇA: MedicaoGlicemia herda de Medicao 
public class MedicaoGlicemia extends Medicao {

    private double nivel_glicose;
    private String periodo;

    @Override
    public String getTipoMedicao() {
        // Implementação específica do Polimorfismo
        return "Glicemia";
    }
    
    // Getters e Setters específicos
    public double getNivel_glicose() {
        return nivel_glicose;
    }

    public void setNivel_glicose(double nivel_glicose) {
        this.nivel_glicose = nivel_glicose;
    }
}