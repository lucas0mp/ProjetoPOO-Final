package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Prescricao;

/**
 * PrescricaoDAO (Data Access Object)
 * Classe responsavel por todas as operacoes de banco de dados
 * relacionadas a tabela 'prescricao'.
 * E usada principalmente pelo MedicoController.
 */
public class PrescricaoDAO {
    
    // **ENCAPSULAMENTO**: A conexao com o banco e privada.
    // A classe gerencia sua propria conexao.
    private Connection conexao;

    /**
     * Construtor.
     * Pega a conexao unica (Singleton) da classe ConexaoMySQL.
     */
    public PrescricaoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Salva uma nova prescricao no banco (Escrita - INSERT).
     * Usado pelo MedicoController.
     * * @param prescricao O objeto Prescricao com todos os dados preenchidos.
     * @return O ID (chave primaria) da prescricao que acabou de ser criada.
     * Retorna -1 se a criacao falhar.
     */
    public int salvar(Prescricao prescricao) {
        // 1. Define o comando SQL
        String sql = "INSERT INTO prescricao (id_medico, id_paciente, id_medicamento, data_inicio, dosagem, frequencia, instrucoes_adicionais) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int idGerado = -1; // Valor padrao em caso de falha
        
        // 2. "try-with-resources"
        // Pede ao PreparedStatement para retornar o ID gerado (Statement.RETURN_GENERATED_KEYS)
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // 3. Substitui os "?" pelos dados do objeto 'prescricao'
            // **ENCAPSULAMENTO**: O DAO puxa os dados de dentro do objeto
            // 'prescricao' usando seus metodos publicos (getters).
            stmt.setInt(1, prescricao.getId_medico());
            stmt.setInt(2, prescricao.getId_paciente());
            stmt.setInt(3, prescricao.getId_medicamento());
            // Converte java.util.Date (do Model) para java.sql.Date (do JDBC)
            stmt.setDate(4, new java.sql.Date(prescricao.getData_inicio().getTime()));
            stmt.setString(5, prescricao.getDosagem());
            stmt.setString(6, prescricao.getFrequencia());
            stmt.setString(7, prescricao.getInstrucoes_adicionais());
            
            // 4. Executa o INSERT
            int affectedRows = stmt.executeUpdate();
            
            // 5. Se o INSERT deu certo, busca o ID gerado
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1); // Pega o ID
                    }
                }
            }
            System.out.println("Prescricao salva com sucesso!");
            
        } catch (SQLException e) {
            // 6. Trata erros de INSERT internamente
            System.err.println("Erro ao salvar prescricao:");
            e.printStackTrace();
        }
        
        // 7. Retorna o ID gerado (ou -1 se deu erro)
        return idGerado;
    }
}