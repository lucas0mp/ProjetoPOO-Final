package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO para gerenciar a tabela de relacionamento 'acompanha' 
 */
public class AcompanhaDAO {
    
    private Connection conexao;

    public AcompanhaDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Cria a ligação entre um médico e um paciente
     * @param idMedico ID do médico
     * @param idPaciente ID do paciente
     * @throws SQLException 
     */
    public void salvar(int idMedico, int idPaciente) throws SQLException {
        String sql = "INSERT INTO acompanha (id_medico, id_paciente) VALUES (?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            stmt.setInt(2, idPaciente);
            stmt.executeUpdate();
        }
        // Se der erro (ex: ligação já existe), a SQLException será lançada
    }
}