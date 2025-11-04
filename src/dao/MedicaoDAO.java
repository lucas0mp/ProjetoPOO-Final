package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.MedicaoGlicemia;
import model.MedicaoPressao;  

/**
 * MedicaoDAO (Data Access Object)
 * Classe responsavel pelas operacoes de banco de dados
 * relacionadas as medicoes.
 * * Esta classe lida com a **HERANCA** definida nos Models
 * (Medicao -> MedicaoGlicemia e Medicao -> MedicaoPressao).
 * Ela salva dados em duas tabelas (a tabela PAI 'medicao' e a
 * tabela FILHA 'medicao_glicemia' ou 'medicao_pressao')
 * usando transacoes SQL.
 */
public class MedicaoDAO {

    // **ENCAPSULAMENTO**: A conexao e privada.
    private Connection conexao;

    /**
     * Construtor.
     * Pega a conexao unica (Singleton) da classe ConexaoMySQL.
     */
    public MedicaoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Metodo auxiliar (helper) privado.
     * **ENCAPSULAMENTO**: Este metodo esconde a logica de inserir
     * na tabela "PAI" ('medicao'). So pode ser chamado de dentro desta classe.
     * * @param idPaciente O ID do paciente.
     * @param observacoes As observacoes gerais.
     * @return O ID (chave primaria) da medicao base que acabou de ser criada.
     * @throws SQLException Se o INSERT falhar.
     */
    private int salvarMedicaoBase(int idPaciente, String observacoes) throws SQLException {
        String sql = "INSERT INTO medicao (id_paciente, data_hora, observacoes) VALUES (?, ?, ?)";
        int idGerado = -1;
        
        // "try-with-resources"
        // Statement.RETURN_GENERATED_KEYS e usado para conseguir pegar o ID que o banco criou
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idPaciente);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Pega data/hora atual
            stmt.setString(3, observacoes);
            
            int affectedRows = stmt.executeUpdate();
            
            // Pega o ID gerado
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                    }
                }
            }
        }
        return idGerado;
    }
    
    /**
     * Salva uma medicao de Glicemia.
     * Este metodo usa uma TRANSACAO SQL, pois precisa inserir em duas tabelas
     * (medicao E medicao_glicemia). Se uma falhar, a outra e desfeita (rollback).
     * * @param med O objeto MedicaoGlicemia (que contem dados da classe PAI e FILHA).
     * @return true se a transacao foi bem-sucedida, false se falhou.
     */
    public boolean salvarGlicemia(MedicaoGlicemia med) {
        try {
            // 1. Inicia a Transacao
            conexao.setAutoCommit(false);
            
            // 2. Salva na tabela PAI ('medicao') e pega o ID gerado
            int idBase = salvarMedicaoBase(med.getId_paciente(), med.getObservacoes());
            
            if (idBase == -1) {
                throw new SQLException("Falha ao criar medicao base.");
            }
            
            // 3. Salva na tabela FILHA ('medicao_glicemia') usando o ID da base
            String sqlGlicemia = "INSERT INTO medicao_glicemia (id_medicao, nivel_glicose, periodo) VALUES (?, ?, ?)";
            try (PreparedStatement stmtGlic = conexao.prepareStatement(sqlGlicemia)) {
                stmtGlic.setInt(1, idBase);
                stmtGlic.setDouble(2, med.getNivel_glicose());
                stmtGlic.setString(3, med.getPeriodo());
                stmtGlic.executeUpdate();
            }
            
            // 4. Se chegou ate aqui sem erros, confirma a transacao
            conexao.commit();
            return true;
            
        } catch (SQLException e) {
            // 5. Se deu erro em qualquer passo, desfaz tudo (Rollback)
            System.err.println("Erro ao salvar medicao de glicemia:");
            e.printStackTrace();
            try {
                conexao.rollback(); // Desfaz as alteracoes
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // 6. Devolve o controle de commit para a conexao
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Salva uma medicao de Pressao.
     * A logica de TRANSACAO e a mesma do 'salvarGlicemia'.
     * * @param med O objeto MedicaoPressao.
     * @return true se sucesso, false se falhar.
     */
    public boolean salvarPressao(MedicaoPressao med) {
        try {
            // 1. Inicia transacao
            conexao.setAutoCommit(false);
            
            // 2. Salva na tabela PAI ('medicao') e pega ID
            int idBase = salvarMedicaoBase(med.getId_paciente(), med.getObservacoes());
            
            if (idBase == -1) {
                throw new SQLException("Falha ao criar medicao base.");
            }
            
            // 3. Salva na tabela FILHA ('medicao_pressao')
            String sqlPressao = "INSERT INTO medicao_pressao (id_medicao, pressao_sistolica, pressao_diastolica) VALUES (?, ?, ?)";
            try (PreparedStatement stmtPres = conexao.prepareStatement(sqlPressao)) {
                stmtPres.setInt(1, idBase);
                stmtPres.setDouble(2, med.getPressao_sistolica());
                stmtPres.setDouble(3, med.getPressao_diastolica());
                stmtPres.executeUpdate();
            }
            
            // 4. Confirma transacao
            conexao.commit();
            return true;
            
        } catch (SQLException e) {
            // 5. Desfaz (Rollback) em caso de erro
            System.err.println("Erro ao salvar medicao de pressao:");
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            // 6. Reseta conexao
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Metodo de Leitura (SELECT) - Usado pelo PacienteController.
     * Lista o historico de medicoes (ambos os tipos) de um paciente.
     * * @param idPaciente O ID do paciente.
     * @return Uma lista de Strings ja formatadas para exibicao.
     */
    public List<String> listarMedicoesPorPaciente(int idPaciente) {
        List<String> medicoesFormatadas = new ArrayList<>();
        
        // 1. Query complexa para lidar com a **HERANCA** no banco.
        // Usa LEFT JOIN para juntar a tabela PAI (medicao) com AMBAS as tabelas
        // FILHAS (glicemia e pressao) de uma so vez.
        String sql = "SELECT m.data_hora, m.observacoes, " +
                     "mg.nivel_glicose, mg.periodo, " +
                     "mp.pressao_sistolica, mp.pressao_diastolica " +
                     "FROM medicao m " +
                     "LEFT JOIN medicao_glicemia mg ON m.id_medicao = mg.id_medicao " +
                     "LEFT JOIN medicao_pressao mp ON m.id_medicao = mp.id_medicao " +
                     "WHERE m.id_paciente = ? " +
                     "ORDER BY m.data_hora DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            try (ResultSet rs = stmt.executeQuery()) {
                
                // 2. Loop pelos resultados
                while (rs.next()) {
                    String tipo = "Tipo nao identificado";
                    String valor = "";
                    
                    // 3. Logica para descobrir o TIPO da medicao
                    double glicose = rs.getDouble("nivel_glicose");
                    if (!rs.wasNull()) { 
                        // Se 'nivel_glicose' nao for nulo, e Glicemia
                        tipo = "Glicemia";
                        valor = glicose + " mg/dL (Periodo: " + rs.getString("periodo") + ")";
                    } else {
                        double sistolica = rs.getDouble("pressao_sistolica");
                        if (!rs.wasNull()) { 
                            // Se 'pressao_sistolica' nao for nulo, e Pressao
                            tipo = "Pressao Arterial";
                            valor = sistolica + " x " + rs.getDouble("pressao_diastolica");
                        }
                    }
                    
                    // 4. Formata a string para a View
                    String formatada = String.format("Data: %s | Tipo: %s | Valor: %s | Obs: %s",
                            rs.getTimestamp("data_hora").toString(),
                            tipo,
                            valor,
                            rs.getString("observacoes"));
                    medicoesFormatadas.add(formatada);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar medicoes:");
            e.printStackTrace();
        }
        // 5. Retorna a lista de strings formatadas
        return medicoesFormatadas;
    }
}