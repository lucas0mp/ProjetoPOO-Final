package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * AcompanhaDAO (Data Access Object)
 * * Esta classe e responsavel por uma unica coisa: gerenciar a tabela
 * de relacionamento 'acompanha'.
 * Ela faz a "ponte" entre a logica da aplicacao (Controller) e a
 * tabela 'acompanha' no banco de dados.
 */
public class AcompanhaDAO {
    
    // **ENCAPSULAMENTO**: O atributo 'conexao' e privado.
    // A classe gerencia seu proprio estado de conexao; nenhum objeto externo
    // pode modifica-lo diretamente.
    private Connection conexao;

    /**
     * Construtor do AcompanhaDAO.
     * Quando um AcompanhaDAO e criado (ex: la no MedicoController),
     * ele imediatamente pega uma conexao ativa do pool de conexoes.
     */
    public AcompanhaDAO() {
        // Pega a conexao singleton gerenciada pela classe ConexaoMySQL
        this.conexao = ConexaoMySQL.getConexao();
    }
    
    /**
     * Cria a ligacao (associacao) entre um medico e um paciente.
     * Este e o unico metodo desta classe. Ele executa um INSERT na tabela 'acompanha'.
     * * @param idMedico ID do medico (chave estrangeira)
     * @param idPaciente ID do paciente (chave estrangeira)
     * @throws SQLException Lanca uma excecao se o banco der um erro (ex: se a 
     * associacao ja existir, violando a Chave Primaria composta).
     */
    public void salvar(int idMedico, int idPaciente) throws SQLException {
        // 1. Define o comando SQL
        String sql = "INSERT INTO acompanha (id_medico, id_paciente) VALUES (?, ?)";
        
        // 2. Usa "try-with-resources" para garantir que o PreparedStatement (stmt)
        // seja fechado automaticamente, mesmo se ocorrer um erro.
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            // 3. Substitui os "?" pelos valores recebidos
            stmt.setInt(1, idMedico);
            stmt.setInt(2, idPaciente);
            
            // 4. Executa o comando INSERT no banco
            stmt.executeUpdate();
        }
        // Nao ha 'catch' aqui. A excecao (throws SQLException) e 
        // propositalmente lancada para cima, para que o Controller (MedicoController)
        // possa trata-la (ex: avisando o usuario sobre a falha).
    }
}