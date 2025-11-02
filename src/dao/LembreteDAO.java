package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Lembrete; // Você precisa criar esta classe Model
import model.Prescricao; // Você precisa criar esta classe Model
import model.Medicamento; // Você precisa criar esta classe Model

public class LembreteDAO {
    
    private Connection conexao;

    public LembreteDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    // Método para o PacienteController
    public List<Lembrete> listarPorPaciente(int idPaciente) {
        List<Lembrete> lembretes = new ArrayList<>();
        
        // SQL complexo que junta 4 tabelas para buscar as informações
        String sql = "SELECT l.id_lembrete, l.horario_programado, l.status, " +
                     "p.dosagem, p.frequencia, m.nome_comercial " +
                     "FROM lembrete l " +
                     "JOIN prescricao p ON l.id_prescricao = p.id_prescricao " +
                     "JOIN medicamento m ON p.id_medicamento = m.id_medicamento " +
                     "WHERE p.id_paciente = ? " +
                     "ORDER BY l.horario_programado ASC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    Lembrete lembrete = new Lembrete();
                    lembrete.setId_lembrete(rs.getInt("id_lembrete"));
                    lembrete.setHorario_programado(rs.getTimestamp("horario_programado"));
                    lembrete.setStatus(rs.getString("status"));
                    
                    // Adicionamos dados das outras tabelas no objeto Lembrete
                    // (Modifique seu Model 'Lembrete' para ter esses campos extras)
                    lembrete.setNomeMedicamento(rs.getString("nome_comercial"));
                    lembrete.setDosagem(rs.getString("dosagem"));
                    
                    lembretes.add(lembrete);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar lembretes por paciente:");
            e.printStackTrace();
        }
        return lembretes;
    }
    
    // Método para o MedicoController
    public void salvar(Lembrete lembrete) {
        String sql = "INSERT INTO lembrete (id_prescricao, horario_programado, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, lembrete.getId_prescricao());
            stmt.setTimestamp(2, new java.sql.Timestamp(lembrete.getHorario_programado().getTime()));
            stmt.setString(3, "Pendente");
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar lembrete:");
            e.printStackTrace();
        }
    }
}