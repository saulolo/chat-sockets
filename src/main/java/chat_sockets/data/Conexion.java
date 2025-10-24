package chat_sockets.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    //variable para guardar el estado de la conección.
    private static Connection connection;

    //Variable para crear una sola instancia
    private static Conexion instancia;

    //Variables de acceso a la BD
    private static final String URL = "jdbc:postgresql://localhost:5432/bd_chat_lta";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";

    //Metodo para conectarme a la BD
    public Connection getConnection() {
        try {
            // Cargar explícitamente el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            //1. Crear conección
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;

    }

    //Metod para cerrar la conección
    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e);
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    //Pátron singleton
    public static Conexion getInstance() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
}
