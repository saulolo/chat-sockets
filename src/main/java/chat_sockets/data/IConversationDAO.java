package chat_sockets.data;

import java.sql.SQLException;

public interface IConversationDAO {

    void recordConversationA(String message) throws SQLException;

    void recordConversationB(String message) throws SQLException;
}
