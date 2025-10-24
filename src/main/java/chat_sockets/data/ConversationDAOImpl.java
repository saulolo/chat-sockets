package chat_sockets.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConversationDAOImpl implements IConversationDAO {
    Conexion instancePostgresql = Conexion.getInstance();

    @Override
    public void recordConversationA(String message) throws SQLException {
        recordMessage("Cliente A", message);
    }

    @Override
    public void recordConversationB(String message) throws SQLException {
        recordMessage("Cliente B", message);
    }

    void recordMessage(String sender, String message) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = instancePostgresql.getConnection();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO conversations (sender, message) VALUES (?, ?)");
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar conversación: " + e.getMessage());
            throw e;
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }
}
