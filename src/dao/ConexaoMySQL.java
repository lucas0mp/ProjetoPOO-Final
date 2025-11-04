package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConexaoMySQL (Classe de Conexao)
 * * Esta classe e responsavel por uma UNICA coisa: criar e gerenciar
 * a conexao com o banco de dados MySQL.
 * * Ela implementa o padrao de projeto "Singleton" (ou uma variacao dele).
 * Isso garante que toda a aplicacao (todos os DAOs) use a MESMA
 * instancia de conexao, economizando recursos.
 */
public class ConexaoMySQL {
    
    // --- Atributos Estaticos (static) ---
    // 'static' significa que esses atributos pertencem a CLASSE,
    // e nao a uma instancia especifica.
    
    // **ENCAPSULAMENTO**: As credenciais de conexao sao 'private static final'.
    // 'private': Nenhuma outra classe pode ve-las.
    // 'static': Sao compartilhadas por toda a aplicacao.
    // 'final': Nao podem ser alteradas depois de definidas.
    private static final String URL = "jdbc:mysql://localhost:3306/poo";
    private static final String USUARIO = "root";
    private static final String SENHA = "Lucas8mp%"; // <-- MUDE AQUI

    // **ENCAPSULAMENTO**: O objeto de conexao e 'private static'.
    // Sendo 'static', ele e unico e compartilhado por todos os DAOs.
    // Sendo 'private', ninguem pode acessa-lo diretamente,
    // exceto atraves do metodo publico getConexao().
    private static Connection conexao = null;

    /**
     * Metodo publico e estatico para obter a conexao.
     * E o "portao de entrada" global para o banco de dados.
     * * @return A instancia unica (Singleton) da conexao com o banco.
     */
    public static Connection getConexao() {
        try {
            // 1. Verifica se a conexao ainda nao foi criada (null)
            //    ou se foi fechada anteriormente.
            if (conexao == null || conexao.isClosed()) {
                
                // 2. Carrega o Driver JDBC do MySQL (necessario ter o .JAR no projeto)
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // 3. Tenta estabelecer uma nova conexao usando as credenciais
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (ClassNotFoundException e) {
            // Erro: O JAR do driver (mysql-connector) nao foi encontrado no projeto.
            System.err.println("Driver MySQL nao encontrado! Adicione o JAR.");
            e.printStackTrace();
        } catch (SQLException e) {
            // Erro: Falha ao conectar (ex: senha errada, BD offline, URL errada)
            System.err.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
        
        // 4. Retorna a conexao (seja a que acabou de ser criada ou a que ja existia)
        return conexao;
    }
}