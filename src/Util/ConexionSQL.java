package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
    
    // Fíjate bien que tenga "public static Connection"
    public static Connection getConexion() {
        Connection cn = null;
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=iestp_peru_japon;encrypt=true;trustServerCertificate=true;";
            cn = DriverManager.getConnection(url, "sa", "1234");
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return cn;
    }
}

