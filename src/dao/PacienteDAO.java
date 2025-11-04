package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

/**
 * PacienteDAO (Data Access Object)
 * Classe responsavel por todas as operacoes de banco de dados
 * relacionadas a tabela 'paciente'.
 * Ela abstrai a logica SQL dos Controllers.
 */
public class PacienteDAO {

    // **ENCAPSULAMENTO**: O atributo 'conexao' e privado.
    // A classe gerencia sua propria conexao; nenhuma classe externa
    // pode acessa-la diretamente.
    private Connection conexao;

    /**
     * Construtor.
     * Pega a conexao unica (Singleton) da classe ConexaoMySQL.
     */
    public PacienteDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }

    /**
     * Autentica um paciente pelo CPF e Senha (Leitura - SELECT).
     * Usado pelo LoginController.
     * * @return Objeto Paciente se o login for valido, null se invalido.
     */
    public Paciente login(String cpf, String senha) {
        String sql = "SELECT * FROM paciente WHERE cpf = ? AND senha = ?";
        
        // "try-with-resources" (garante que stmt e rs sejam fechados)
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Se encontrou, preenche o objeto Paciente
                    Paciente p = new Paciente();
                    p.setId_paciente(rs.getInt("id_paciente"));
                    p.setNome(rs.getString("nome"));
                    p.setCpf(rs.getString("cpf"));
                    p.setData_nascimento(rs.getDate("data_nascimento"));
                    p.setTelefone_celular(rs.getString("telefone_celular"));
                    p.setEmail(rs.getString("email"));
                    return p; // Retorna o paciente
                }
            }
        } catch (SQLException e) {
            // Trata erros de login (leitura) internamente
            System.err.println("Erro ao fazer login de paciente:");
            e.printStackTrace();
        }
        return null; // Falha (nao encontrou ou deu erro)
    }
    
    /**
     * Salva um novo paciente no banco (Escrita - INSERT).
     * Usado pelo AdminController e MedicoController.
     * * @return O ID (chave primaria) do paciente recem-criado.
     * @throws SQLException Lanca a excecao para o Controller tratar
     * (ex: CPF duplicado, que viola a constraint UNIQUE).
     */
    public int salvar(Paciente paciente, String senha) throws SQLException {
        
        String sql = "INSERT INTO paciente (nome, cpf, data_nascimento, telefone_celular, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
        int idGerado = -1;
        
        // **ENCAPSULAMENTO**: A logica complexa de como um "paciente" vira um
        // comando INSERT, como os dados sao preparados (PreparedStatement)
        // e como o ID gerado e retornado (RETURN_GENERATED_KEYS),
        // esta totalmente escondida (encapsulada) dentro deste metodo.
        // O Controller apenas chama "dao.salvar()".
        
        // Pede ao PreparedStatement para retornar as chaves geradas (o ID)
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, senha); 
            
            int affectedRows = stmt.executeUpdate();
            
            // Se o INSERT funcionou (linhas afetadas > 0), pega o ID
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1); // Pega o ID gerado
                    }
                }
            }
        }
        // Nao ha 'catch', o erro (ex: CPF duplicado) e "jogado" para o Controller
        return idGerado; // Retorna o ID
    }
    
    /**
     * Lista todos os pacientes (Leitura - SELECT).
     * Usado pelo AdminController.
     */
    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();
        
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                // Para cada linha, cria um objeto Paciente e preenche
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
        return pacientes; // Retorna a lista (vazia ou cheia)
    }
    
    /**
     * Lista pacientes de um medico especifico (Leitura - SELECT com JOIN).
     * Usado pelo MedicoController.
     * * @param idMedico O ID do medico logado.
     */
    public List<Paciente> listarPorMedico(int idMedico) {
        // SQL que junta 'paciente' com a tabela 'acompanha'
        String sql = "SELECT p.* FROM paciente p " +
                     "JOIN acompanha a ON p.id_paciente = a.id_paciente " +
                     "WHERE a.id_medico = ?";
        List<Paciente> pacientes = new ArrayList<>();
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Preenche o objeto Paciente
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
            System.err.println("Erro ao listar pacientes por medico:");
            e.printStackTrace();
        }
        return pacientes;
    }
    
    /**
     * Atualiza os dados de um paciente (Escrita - UPDATE).
     * Usado pelo AdminController.
     * * @throws SQLException Lanca a excecao para o Controller tratar (ex: CPF duplicado).
     */
    public void atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, data_nascimento = ?, telefone_celular = ?, email = ? WHERE id_paciente = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            // Define os novos dados
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setDate(3, new java.sql.Date(paciente.getData_nascimento().getTime()));
            stmt.setString(4, paciente.getTelefone_celular());
            stmt.setString(5, paciente.getEmail());
            // Define quem sera atualizado (clausula WHERE)
            stmt.setInt(6, paciente.getId_paciente());
            
            stmt.executeUpdate();
        }
        // Erros (ex: CPF duplicado) sao lancados para o Controller
    }
    
    /**
     * Exclui um paciente do banco (Escrita - DELETE).
     * Usado pelo AdminController.
     * * @throws SQLException Lanca a excecao se o DELETE falhar.
     */
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id); // Define o ID de quem sera excluido
            stmt.executeUpdate();
        }
        // Erros sao lancados para o Controller
    }
}