package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Lembrete;
import model.Prescricao; 
import model.Medicamento; 

/**
 * LembreteDAO (Data Access Object)
 * Classe responsavel pelas operacoes de banco de dados
 * relacionadas a tabela 'lembrete'.
 */
public class LembreteDAO {
    
    // **ENCAPSULAMENTO**: O atributo 'conexao' e privado.
    // A classe gerencia sua propria conexao, obtida no construtor.
    private Connection conexao;

    /**
     * Construtor.
     * Pega a conexao unica (Singleton) da classe ConexaoMySQL.
     */
    public LembreteDAO() {
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Metodo de Leitura (SELECT) - Usado pelo PacienteController.
     * Lista todos os lembretes de um paciente especifico.
     * * @param idPaciente O ID do paciente logado.
     * @return Uma Lista de objetos Lembrete preenchidos.
     */
    public List<Lembrete> listarPorPaciente(int idPaciente) {
        List<Lembrete> lembretes = new ArrayList<>();
        
        // 1. Logica SQL complexa (JOIN)
        // Este e o metodo mais complexo deste DAO.
        // Um lembrete so tem o ID da prescricao.
        // Para mostrar o *nome do remedio*, precisamos:
        //    lembrete -> junta com prescricao (pelo id_prescricao)
        //    prescricao -> junta com medicamento (pelo id_medicamento)
        //    E filtra tudo pelo id_paciente.
        String sql = "SELECT l.id_lembrete, l.horario_programado, l.status, " +
                     "p.dosagem, p.frequencia, m.nome_comercial " +
                     "FROM lembrete l " +
                     "JOIN prescricao p ON l.id_prescricao = p.id_prescricao " +
                     "JOIN medicamento m ON p.id_medicamento = m.id_medicamento " +
                     "WHERE p.id_paciente = ? " +
                     "ORDER BY l.horario_programado ASC";
        
        // 2. "try-with-resources" para gerenciar o PreparedStatement e ResultSet
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // Substitui o "?" pelo ID do paciente
            stmt.setInt(1, idPaciente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                // 3. Loop pelos resultados
                while(rs.next()) {
                    // Cria um objeto Lembrete
                    Lembrete lembrete = new Lembrete();
                    
                    // Preenche com dados da tabela 'lembrete'
                    lembrete.setId_lembrete(rs.getInt("id_lembrete"));
                    lembrete.setHorario_programado(rs.getTimestamp("horario_programado"));
                    lembrete.setStatus(rs.getString("status"));
                    
                    // **ENCAPSULAMENTO**: O objeto Lembrete (Model) foi modificado
                    // para carregar dados extras que nao sao da sua tabela
                    // (nome_comercial, dosagem). Isso facilita o transporte
                    // dos dados do JOIN de volta para a View.
                    lembrete.setNomeMedicamento(rs.getString("nome_comercial"));
                    lembrete.setDosagem(rs.getString("dosagem"));
                    
                    lembretes.add(lembrete);
                }
            }
        } catch (SQLException e) {
            // Trata erros de leitura (SELECT) aqui mesmo
            System.err.println("Erro ao listar lembretes por paciente:");
            e.printStackTrace();
        }
        // 4. Retorna a lista (vazia ou preenchida)
        return lembretes;
    }
    
    /**
     * Metodo de Escrita (INSERT) - Usado pelo MedicoController.
     * Salva um novo lembrete no banco.
     * * @param lembrete O objeto Lembrete com os dados (id_prescricao, horario).
     */
    public void salvar(Lembrete lembrete) {
        // SQL simples de insercao
        String sql = "INSERT INTO lembrete (id_prescricao, horario_programado, status) VALUES (?, ?, ?)";
        
        // "try-with-resources"
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // Substitui os "?" pelos dados do objeto
            stmt.setInt(1, lembrete.getId_prescricao());
            // Converte java.util.Date para java.sql.Timestamp
            stmt.setTimestamp(2, new java.sql.Timestamp(lembrete.getHorario_programado().getTime()));
            stmt.setString(3, "Pendente"); // Define o status padrao
            
            // Executa o INSERT
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            // Trata erros de escrita (INSERT) aqui mesmo
            System.err.println("Erro ao salvar lembrete:");
            e.printStackTrace();
        }
        // Este metodo (diferente do AdminDAO) nao lanca (throws) a excecao,
        // ele a trata internamente. E uma escolha de design.
    }
}