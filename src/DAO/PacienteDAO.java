package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

// ABSTRAÇÃO: Essa classe abstrai todo o acesso a dados da tabela Paciente.
// O Controller não precisa saber SQL, apenas chamar os métodos.
public class PacienteDAO {

    private Connection conexao;

    public PacienteDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }

    // CREATE (Salvar)
    public void salvar(Paciente paciente) {
        // [Source: 2]
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone_celular, email) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            // Converte java.util.Date para java.sql.Date
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            
            stmt.executeUpdate();
            System.out.println("Paciente salvo com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar paciente:");
            e.printStackTrace();
        }
    }
    
    // READ (Listar Todos)
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente"; // [Source: 2]
        List<Paciente> pacientes = new ArrayList<>();
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId_paciente(rs.getInt("id_paciente"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setData_nascimento(rs.getDate("data_nascimento"));
                p.setTelefone_celular(rs.getString("telefone_celular"));
                p.setEmail(rs.getString("email"));
                pacientes.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes:");
            e.printStackTrace();
        }
        return pacientes;
    }
    
    // UPDATE (Atualizar)
    public void atualizar(Paciente paciente) {
        // [Source: 2]
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, data_nascimento = ?, telefone_celular = ?, email = ? WHERE id_paciente = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            stmt.setInt(6, paciente.getId_paciente());
            
            stmt.executeUpdate();
            System.out.println("Paciente atualizado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente:");
            e.printStackTrace();
        }
    }
    
    // DELETE (Excluir)
    public void excluir(int id) {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?"; // [Source: 2]
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            stmt.executeUpdate();
            System.out.println("Paciente excluído com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao excluir paciente:");
            e.printStackTrace();
        }
    }
}