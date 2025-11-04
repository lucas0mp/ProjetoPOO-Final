package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Medico;

/**
 * MedicoDAO (Data Access Object)
 * Classe responsavel por todas as operacoes de banco de dados
 * relacionadas a tabela 'medico'.
 */
public class MedicoDAO {
    
    // **ENCAPSULAMENTO**: A conexao com o banco e privada.
    // Nenhuma classe externa pode acessa-la diretamente.
    private Connection conexao;

    /**
     * Construtor.
     * Pega a conexao unica (Singleton) da classe ConexaoMySQL.
     */
    public MedicoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Metodo de Login (Leitura - SELECT).
     * Usado pelo LoginController para autenticar um medico.
     * * @param crm O CRM digitado.
     * @param senha A senha digitada.
     * @return Um objeto Medico preenchido se o login for valido, ou 'null' se falhar.
     */
    public Medico login(String crm, String senha) {
        // 1. Define o comando SQL
        String sql = "SELECT * FROM medico WHERE crm = ? AND senha = ?";
        
        // 2. "try-with-resources" (garante que stmt e rs sejam fechados)
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?"
            stmt.setString(1, crm);
            stmt.setString(2, senha);
            
            // 4. Executa a consulta
            try (ResultSet rs = stmt.executeQuery()) {
                
                // 5. Se encontrou um resultado
                if (rs.next()) {
                    // Cria e preenche o objeto Medico
                    Medico m = new Medico();
                    m.setId_medico(rs.getInt("id_medico"));
                    m.setNome(rs.getString("nome"));
                    m.setCrm(rs.getString("crm"));
                    m.setEspecialidade(rs.getString("especialidade"));
                    return m; // Retorna o medico encontrado
                }
            }
        } catch (SQLException e) {
            // Trata erros de leitura (SELECT) internamente
            System.err.println("Erro ao fazer login de medico:");
            e.printStackTrace();
        }
        // 6. Se nao encontrou ou deu erro, retorna nulo
        return null;
    }
    
    /**
     * Metodo Salvar (Escrita - INSERT).
     * Usado pelo AdminController para criar um novo medico.
     * * @param medico O objeto Medico com os dados (nome, crm, especialidade).
     * @param senha A senha para o novo medico.
     * @throws SQLException Lanca a excecao para o Controller (AdminController)
     * para que ele possa tratar erros (ex: CRM duplicado).
     */
    public void salvar(Medico medico, String senha) throws SQLException {
        // 1. Define o comando SQL
        String sql = "INSERT INTO medico (crm, nome, especialidade, senha) VALUES (?, ?, ?, ?)";
        
        // 2. "try-with-resources"
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?" pelos dados do objeto
            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getNome());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, senha);
            
            // 4. Executa o INSERT
            stmt.executeUpdate();
        }
        // 5. Nao ha 'catch', o erro (ex: CRM duplicado) e lancado para o Controller
    }

    // --- NOVOS METODOS CRUD (Usados pelo AdminController) ---
    
    /**
     * (CRUD - Read) Lista todos os medicos.
     * Usado pelo AdminController.
     * @return Uma Lista de objetos Medico.
     */
    public List<Medico> listarTodos() {
        String sql = "SELECT * FROM medico";
        List<Medico> medicos = new ArrayList<>();
        
        // "try-with-resources" (Statement e ResultSet)
        try (Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Loop para ler cada linha retornada pelo banco
            while (rs.next()) {
                Medico m = new Medico();
                m.setId_medico(rs.getInt("id_medico"));
                m.setNome(rs.getString("nome"));
                m.setCrm(rs.getString("crm"));
                m.setEspecialidade(rs.getString("especialidade"));
                medicos.add(m); // Adiciona o medico a lista
            }

        } catch (SQLException e) {
            // Trata erros de leitura internamente
            System.err.println("Erro ao listar medicos:");
            e.printStackTrace();
        }
        return medicos; // Retorna a lista (pode estar vazia)
    }
    
    /**
     * (CRUD - Update) Atualiza os dados de um medico.
     * Usado pelo AdminController.
     * * @param medico O objeto Medico com o ID e os *novos* dados.
     * @throws SQLException Lanca a excecao para o Controller tratar
     * (ex: se o novo CRM ja existir em outro registro).
     */
    public void atualizar(Medico medico) throws SQLException {
        // 1. Define o SQL (Nao atualiza a senha, apenas dados cadastrais)
        String sql = "UPDATE medico SET crm = ?, nome = ?, especialidade = ? WHERE id_medico = ?";
        
        // 2. "try-with-resources"
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?"
            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getNome());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setInt(4, medico.getId_medico()); // O ID e usado no WHERE
            
            // 4. Executa o UPDATE
            stmt.executeUpdate();
        }
        // 5. Erros (ex: CRM duplicado) sao lancados para o Controller
    }
    
    /**
     * (CRUD - Delete) Exclui um medico.
     * Usado pelo AdminController.
     * * @param id O ID do medico a ser excluido.
     * @throws SQLException Lanca a excecao para o Controller tratar
     * (ex: se o medico nao puder ser excluido por ter FK em outras tabelas,
     * embora o SQL use ON DELETE CASCADE para evitar isso).
     */
    public void excluir(int id) throws SQLException {
        // 1. Define o SQL
        String sql = "DELETE FROM medico WHERE id_medico = ?";
        
        // 2. "try-with-resources"
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui o "?"
            stmt.setInt(1, id);
            
            // 4. Executa o DELETE
            stmt.executeUpdate();
        }
        // 5. Erros sao lancados para o Controller
    }
}