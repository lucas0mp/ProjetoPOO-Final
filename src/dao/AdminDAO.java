package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Administrador;

/**
 * AdminDAO (Data Access Object)
 * Classe responsavel por todas as operacoes de banco de dados
 * relacionadas a tabela 'administrador'.
 * Ela "esconde" a logica SQL dos Controllers (Principio do Encapsulamento).
 */
public class AdminDAO {
    
    // **ENCAPSULAMENTO**: O atributo 'conexao' e privado.
    // A classe gerencia sua propria conexao, que e obtida no construtor.
    // Nenhuma classe externa pode acessar ou modificar esta conexao diretamente.
    private Connection conexao;

    /**
     * Construtor do AdminDAO.
     * Assim que um AdminDAO e criado (la no Controller),
     * ele imediatamente pega a conexao ativa gerenciada pela ConexaoMySQL.
     */
    public AdminDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Metodo de Login (Leitura - SELECT).
     * Verifica se um usuario e senha correspondem a um registro no banco.
     * * @param usuario O 'usuario' (login) digitado.
     * @param senha A 'senha' digitada.
     * @return Um objeto Administrador preenchido se o login for valido, ou 'null' se falhar.
     */
    public Administrador login(String usuario, String senha) {
        // 1. Define o comando SQL
        String sql = "SELECT * FROM administrador WHERE usuario = ? AND senha = ?";
        
        // 2. Usa "try-with-resources" (garante que stmt e rs sejam fechados)
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?" pelos valores recebidos
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            
            // 4. Executa a consulta (query)
            try (ResultSet rs = stmt.executeQuery()) {
                
                // 5. Verifica se o banco de dados retornou alguma linha
                if (rs.next()) {
                    // SUCESSO: Cria um objeto Administrador com os dados do banco
                    Administrador a = new Administrador();
                    a.setId_admin(rs.getInt("id_admin"));
                    a.setUsuario(rs.getString("usuario"));
                    a.setNome(rs.getString("nome"));
                    return a; // Retorna o objeto preenchido
                }
            }
        } catch (SQLException e) {
            // Erros de leitura (login) sao tratados aqui mesmo.
            // O Controller nao precisa saber *porque* falhou, apenas *que* falhou.
            System.err.println("Erro ao fazer login de admin:");
            e.printStackTrace();
        }
        // FALHA: Se nao encontrou (rs.next() foi false) ou se deu erro (catch)
        return null; 
    }
    
    /**
     * Metodo Salvar (Escrita - INSERT).
     * Cria um novo administrador no banco.
     * * @param admin O objeto Administrador com os dados (nome, usuario).
     * @param senha A senha para o novo admin.
     * @throws SQLException Este metodo *lanca* (throws) a excecao SQL.
     * Isso e intencional (parte do design MVC): o DAO nao trata o erro,
     * ele o envia para o *Controller*, que decidira o que fazer (ex: mostrar
     * uma mensagem de "usuario duplicado" para a View).
     */
    public void salvar(Administrador admin, String senha) throws SQLException {
        // 1. Define o comando SQL
        String sql = "INSERT INTO administrador (usuario, senha, nome) VALUES (?, ?, ?)";

        // 2. "try-with-resources" para o PreparedStatement
        // **ENCAPSULAMENTO**: A complexidade do SQL (como funciona o INSERT,
        // PreparedStatement, etc.) esta "escondida" aqui. O Controller so
        // precisa chamar o metodo publico 'salvar()'.
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?" pelos dados do objeto Administrador
            stmt.setString(1, admin.getUsuario()); // Pega o usuario do objeto
            stmt.setString(2, senha);
            stmt.setString(3, admin.getNome()); // Pega o nome do objeto
            
            // 4. Executa o comando INSERT
            stmt.executeUpdate();
        }
        // 5. Sem 'catch': O 'throws SQLException' na assinatura do metodo
        // garante que o erro seja "jogado" para quem chamou (o AdminController).
    }
}