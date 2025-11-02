package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Importe este
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

/**
 * Classe DAO para a entidade Paciente.
 * Contém o CRUD e métodos de login/busca.
 * Versão corrigida: métodos de escrita (salvar, atualizar, excluir)
 * lançam SQLException para serem tratados pelo Controller.
 */
public class PacienteDAO {

    private Connection conexao;

    public PacienteDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }

    /**
     * Autentica um paciente pelo CPF e Senha.
     * Este método trata a própria exceção, pois é um método de leitura/verificação.
     * @return Objeto Paciente se o login for válido, null se inválido.
     */
    public Paciente login(String cpf, String senha) {
        String sql = "SELECT * FROM paciente WHERE cpf = ? AND senha = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId_paciente(rs.getInt("id_paciente"));
                    p.setNome(rs.getString("nome"));
                    p.setCpf(rs.getString("cpf"));
                    p.setData_nascimento(rs.getDate("data_nascimento"));
                    p.setTelefone_celular(rs.getString("telefone_celular"));
                    p.setEmail(rs.getString("email"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fazer login de paciente:");
            e.printStackTrace();
        }
        return null; // Falha no login
    }
    
    /**
     * Salva um novo paciente no banco (CREATE).
     * @return O ID do paciente recém-criado.
     * @throws SQLException Se ocorrer um erro (ex: CPF duplicado).
     */
    public int salvar(Paciente paciente, String senha) throws SQLException {
        
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone_celular, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
        int idGerado = -1;
        
        // O PreparedStatement agora pede as chaves geradas (RETURN_GENERATED_KEYS)
        // O try-with-resources gerencia o PreparedStatement.
        // A exceção (ex: CPF duplicado) será lançada para o Controller.
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, senha); 
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1); // Pega o ID gerado
                    }
                }
            }
        }
        return idGerado; // Retorna o ID
    }
    
    /**
     * Lista todos os pacientes (READ).
     * Este método trata a própria exceção, pois é um método de leitura.
     */
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente";
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
    
    /**
     * Lista todos os pacientes acompanhados por um médico específico (READ).
     */
    public List<Paciente> listarPorMedico(int idMedico) {
        String sql = "SELECT p.* FROM paciente p " +
                     "JOIN acompanha a ON p.id_paciente = a.id_paciente " +
                     "WHERE a.id_medico = ?";
        List<Paciente> pacientes = new ArrayList<>();
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pacientes por médico:");
            e.printStackTrace();
        }
        return pacientes;
    }
    
    /**
     * Atualiza os dados de um paciente (UPDATE).
     * @throws SQLException Se ocorrer um erro (ex: CPF duplicado).
     */
    public void atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, data_nascimento = ?, telefone_celular = ?, email = ? WHERE id_paciente = ?";
        
        // Removemos o catch interno para que o Controller possa tratar
        // o erro de 'CPF duplicado'
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            stmt.setInt(6, paciente.getId_paciente());
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Exclui um paciente do banco (DELETE).
     * @throws SQLException Se ocorrer um erro (ex: violação de FK, embora
     * corrigimos isso no SQL com ON DELETE CASCADE)
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        
        // Removemos o catch interno. A exceção será lançada para o Controller.
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            stmt.executeUpdate();
        }
    }
}