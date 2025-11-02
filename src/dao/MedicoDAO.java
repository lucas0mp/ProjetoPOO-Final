package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Medico;

public class MedicoDAO {
    private Connection conexao;

    public MedicoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    // MÉTODO DE LOGIN (Existente)
    public Medico login(String crm, String senha) {
        String sql = "SELECT * FROM medico WHERE crm = ? AND senha = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, crm);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Medico m = new Medico();
                    m.setId_medico(rs.getInt("id_medico"));
                    m.setNome(rs.getString("nome"));
                    m.setCrm(rs.getString("crm"));
                    m.setEspecialidade(rs.getString("especialidade"));
                    return m;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fazer login de médico:");
            e.printStackTrace();
        }
        return null;
    }
    
    // MÉTODO SALVAR (CREATE)
    public void salvar(Medico medico, String senha) throws SQLException {
        String sql = "INSERT INTO medico (crm, nome, especialidade, senha) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getNome());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, senha);
            
            stmt.executeUpdate();
        }
    }

    // --- NOVOS MÉTODOS CRUD ---
    
    // READ (Listar Todos)
    public List<Medico> listarTodos() {
        String sql = "SELECT * FROM medico";
        List<Medico> medicos = new ArrayList<>();
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Medico m = new Medico();
                m.setId_medico(rs.getInt("id_medico"));
                m.setNome(rs.getString("nome"));
                m.setCrm(rs.getString("crm"));
                m.setEspecialidade(rs.getString("especialidade"));
                medicos.add(m);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar médicos:");
            e.printStackTrace();
        }
        return medicos;
    }
    
    // UPDATE (Atualizar)
    // (Obs: Este método não atualiza a senha, apenas dados cadastrais)
    public void atualizar(Medico medico) throws SQLException {
        String sql = "UPDATE medico SET crm = ?, nome = ?, especialidade = ? WHERE id_medico = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getNome());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setInt(4, medico.getId_medico());
            
            stmt.executeUpdate();
        }
    }
    
    // DELETE (Excluir)
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM medico WHERE id_medico = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}