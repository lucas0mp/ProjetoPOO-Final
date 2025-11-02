package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Prescricao;

public class PrescricaoDAO {
    
    private Connection conexao;

    public PrescricaoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    // Salva uma prescrição e retorna o ID dela
    public int salvar(Prescricao prescricao) {
        // 
        String sql = "INSERT INTO prescricao (id_medico, id_paciente, id_medicamento, data_inicio, dosagem, frequencia, instrucoes_adicionais) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int idGerado = -1;
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, prescricao.getId_medico());
            stmt.setInt(2, prescricao.getId_paciente());
            stmt.setInt(3, prescricao.getId_medicamento());
            stmt.setDate(4, new java.sql.Date(prescricao.getData_inicio().getTime()));
            stmt.setString(5, prescricao.getDosagem());
            stmt.setString(6, prescricao.getFrequencia());
            stmt.setString(7, prescricao.getInstrucoes_adicionais());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1); // Retorna o ID
                    }
                }
            }
            System.out.println("Prescrição salva com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar prescrição:");
            e.printStackTrace();
        }
        return idGerado; // Retorna o ID da prescrição criada
    }
}