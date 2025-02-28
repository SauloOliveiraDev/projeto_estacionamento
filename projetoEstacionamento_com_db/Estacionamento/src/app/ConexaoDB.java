//Equipe: Saulo de Oliveira, Igor Vasconcelos, Leonardo Santos, Paulo Medeiros, Ravi Mariane
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    private static final String URL = "jdbc:mysql://localhost:3306/estacionamento_saulo";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

