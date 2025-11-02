package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Administrador;

public class AdminDAO {
    
    private Connection conexao;

    public AdminDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    public Administrador login(String usuario, String senha) {
        String sql = "SELECT * FROM administrador WHERE usuario = ? AND senha = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Administrador a = new Administrador();
                    a.setId_admin(rs.getInt("id_admin"));
                    a.setUsuario(rs.getString("usuario"));
                    a.setNome(rs.getString("nome"));
                    return a;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fazer login de admin:");
            e.printStackTrace();
        }
        return null; // Falha no login
    }
    
    // Admin pode criar outros admins
    public void salvar(Administrador admin, String senha) throws SQLException {
        String sql = "INSERT INTO administrador (usuario, senha, nome) VALUES (?, ?, ?)";

        // Removemos o try-catch interno.
        // A exceção (como 'usuário duplicado') será lançada para o Controller.
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, admin.getUsuario());
            stmt.setString(2, senha);
            stmt.setString(3, admin.getNome());
            stmt.executeUpdate();
        }
        // A mensagem "Salvo com sucesso!" foi movida para o Controller
    }
}