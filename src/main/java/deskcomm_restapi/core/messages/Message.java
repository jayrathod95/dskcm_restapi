package deskcomm_restapi.core.messages;

import deskcomm_restapi.dbconn.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class Message<T> {
    private String messageId;
    private String fromUuid;
    private String toUuid;
    private String body;
    private T ignore;

    public Message(String messageId, String fromUuid, String toUuid, String body) {
        this.messageId = messageId;
        this.fromUuid = fromUuid;
        this.toUuid = toUuid;
        this.body = body;

    }

    public boolean saveToDatabase() {

        try {
            Connection connection = DBConnection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(_uuid,data) VALUES(?,?) ");
            statement.setString(1, messageId);
            statement.setString(2, body);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            if (updateCount > 0) {
                PreparedStatement statement1 = connection.prepareStatement("INSERT INTO users_messages(message_id,`_from`,`_to`) VALUES(?,?,?)");
                statement1.setString(1, messageId);
                statement1.setString(2, fromUuid);
                statement1.setString(3, toUuid);
                statement1.executeUpdate();
                int updateCount1 = statement1.getUpdateCount();
                if (updateCount1 > 0) connection.commit();
                else connection.rollback();
            } else connection.rollback();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
