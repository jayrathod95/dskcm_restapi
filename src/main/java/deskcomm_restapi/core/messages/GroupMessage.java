package deskcomm_restapi.core.messages;

import deskcomm_restapi.dbconn.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static deskcomm_restapi.core.Keys.*;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class GroupMessage extends Message {

    public GroupMessage(String messageId, String fromUuid, String toUuid, String body) {
        super(messageId, fromUuid, toUuid, body);
    }

    public GroupMessage(JSONObject jsonObject) {
        super(jsonObject.getString(MESSAGE_ID), jsonObject.getString(MESSAGE_FROM), jsonObject.getString(MESSAGE_TO), jsonObject.getString(MESSAGE_BODY));
    }

    @Override
    public boolean saveToDatabase() {
        try {
            Connection connection = DbConnection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(_uuid,data) VALUES(?,?) ");
            statement.setString(1, messageId);
            statement.setString(2, body);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            if (updateCount > 0) {
                PreparedStatement statement1 = connection.prepareStatement("INSERT INTO groups_messages(message_id,`_from`,`group_id`) VALUES(?,?,?)");
                statement1.setString(1, messageId);
                statement1.setString(2, fromUuid);
                statement1.setString(3, toUuid);
                statement1.executeUpdate();
                int updateCount1 = statement1.getUpdateCount();
                if (updateCount1 > 0) {
                    connection.commit();
                    statement1.close();
                    connection.close();
                } else {
                    connection.rollback();
                    statement1.close();
                    connection.close();
                }
            } else {
                connection.rollback();
                connection.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
                .put(MESSAGE_ID, messageId)
                .put(MESSAGE_FROM, fromUuid)
                .put(MESSAGE_TO, toUuid)
                .put(MESSAGE_BODY, body);
    }


    @Override
    public void dispatch() {

    }

}
