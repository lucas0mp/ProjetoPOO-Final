package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
    
    // ATENÇÃO: Altere "root" e "sua_senha" para seu usuário e senha do MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/poo";
    private static final String USUARIO = "root";
    private static final String SENHA = "Lucas8mp%"; // <-- MUDE AQUI

    private static Connection conexao = null;

    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                // Carrega o driver JDBC (Necessário ter o JAR no projeto)
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Obtém a conexão
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL não encontrado! Adicione o JAR.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados:");
            e.printStackTrace();
        }
        return conexao;
    }
}