package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.MedicaoGlicemia; // Já criado
import model.MedicaoPressao;  // Já criado

public class MedicaoDAO {

    private Connection conexao;

    public MedicaoDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    // Salva a medição base [cite: 11] e retorna o ID
    private int salvarMedicaoBase(int idPaciente, String observacoes) throws SQLException {
        String sql = "INSERT INTO medicao (id_paciente, data_hora, observacoes) VALUES (?, ?, ?)";
        int idGerado = -1;
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, idPaciente);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Pega data/hora atual
            stmt.setString(3, observacoes);
            
            int affectedRows = stmt.executeUpdate();
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
    
    // Salva medição de Glicemia [cite: 12]
    public boolean salvarGlicemia(MedicaoGlicemia med) {
        try {
            // Usa transação para garantir que ambos inserts funcionem
            conexao.setAutoCommit(false);
            
            // 1. Salva na tabela base 'medicao'
            int idBase = salvarMedicaoBase(med.getId_paciente(), med.getObservacoes());
            
            if (idBase == -1) {
                throw new SQLException("Falha ao criar medição base.");
            }
            
            // 2. Salva na tabela específica 'medicao_glicemia'
            String sqlGlicemia = "INSERT INTO medicao_glicemia (id_medicao, nivel_glicose, periodo) VALUES (?, ?, ?)";
            try (PreparedStatement stmtGlic = conexao.prepareStatement(sqlGlicemia)) {
                stmtGlic.setInt(1, idBase);
                stmtGlic.setDouble(2, med.getNivel_glicose());
                stmtGlic.setString(3, med.getPeriodo());
                stmtGlic.executeUpdate();
            }
            
            conexao.commit(); // Confirma a transação
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar medição de glicemia:");
            e.printStackTrace();
            try {
                conexao.rollback(); // Desfaz em caso de erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Salva medição de Pressão [cite: 13]
    public boolean salvarPressao(MedicaoPressao med) {
        try {
            conexao.setAutoCommit(false);
            
            // 1. Salva na tabela base 'medicao'
            int idBase = salvarMedicaoBase(med.getId_paciente(), med.getObservacoes());
            
            if (idBase == -1) {
                throw new SQLException("Falha ao criar medição base.");
            }
            
            // 2. Salva na tabela específica 'medicao_pressao'
            String sqlPressao = "INSERT INTO medicao_pressao (id_medicao, pressao_sistolica, pressao_diastolica) VALUES (?, ?, ?)";
            try (PreparedStatement stmtPres = conexao.prepareStatement(sqlPressao)) {
                stmtPres.setInt(1, idBase);
                stmtPres.setDouble(2, med.getPressao_sistolica());
                stmtPres.setDouble(3, med.getPressao_diastolica());
                stmtPres.executeUpdate();
            }
            
            conexao.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar medição de pressão:");
            e.printStackTrace();
            try {
                conexao.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conexao.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método para o Paciente visualizar
    public List<String> listarMedicoesPorPaciente(int idPaciente) {
        List<String> medicoesFormatadas = new ArrayList<>();
        // Query que junta a medição base com as duas filhas (glicemia e pressão)
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
                while (rs.next()) {
                    String tipo = "Tipo não identificado";
                    String valor = "";
                    
                    double glicose = rs.getDouble("nivel_glicose");
                    if (!rs.wasNull()) { // Se não for nulo, é Glicemia
                        tipo = "Glicemia";
                        valor = glicose + " mg/dL (Período: " + rs.getString("periodo") + ")";
                    } else {
                        double sistolica = rs.getDouble("pressao_sistolica");
                        if (!rs.wasNull()) { // Se não for nulo, é Pressão
                            tipo = "Pressão Arterial";
                            valor = sistolica + " x " + rs.getDouble("pressao_diastolica");
                        }
                    }
                    
                    String formatada = String.format("Data: %s | Tipo: %s | Valor: %s | Obs: %s",
                            rs.getTimestamp("data_hora").toString(),
                            tipo,
                            valor,
                            rs.getString("observacoes"));
                    medicoesFormatadas.add(formatada);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar medições:");
            e.printStackTrace();
        }
        return medicoesFormatadas;
    }
}